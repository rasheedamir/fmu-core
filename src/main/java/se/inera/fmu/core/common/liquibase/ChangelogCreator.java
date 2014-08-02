package se.inera.fmu.core.common.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.ObjectQuotingStrategy;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.ext.hibernate.database.connection.HibernateConnection;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.serializer.core.xml.XMLChangeLogSerializer;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.*;
import java.nio.file.FileSystems;
import java.util.*;

/**
 * Created by Rasheed on 7/31/14.
 *
 * Compare the Hibernate Entity JPA and the current database.
 * If changes have been done, a new db-changelog-[SEQUENCE].xml file will be generated and the database will be updated.
 *
 * @author Rasheed
 */
@Component
@ConditionalOnClass(Liquibase.class)
public class ChangelogCreator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String MASTER_FILE = "src/main/resources/db/changelog-master.xml";
    public static final String CHANGELOG_FOLDER = "src/main/resources/db/changelog/";
    public static final String RELATIVE_CHANGELOG_FOLDER = "classpath:db/changelog/";

    @Inject
    private ConfigurableApplicationContext applicationContext;

    private CompareControl compareControl;

    public void recreate() {
        initCompareControl();

        log.debug("Reloading JPA & Liquibase classes");
        Database hibernateDatabase = null;
        Database sourceDatabase = null;

        try {
            final String packagesToScan = applicationContext.getEnvironment().getProperty("reload.package.domain");

            // Build source datasource
            DataSource dataSource = applicationContext.getBean(DataSource.class);
            sourceDatabase = getSourceDatabase(dataSource);
            sourceDatabase.setObjectQuotingStrategy(ObjectQuotingStrategy.QUOTE_ALL_OBJECTS);

            // Build hibernate datasource - used as a reference
            hibernateDatabase = new FmuHibernateSpringDatabase(sourceDatabase.getDefaultCatalogName(), sourceDatabase.getLiquibaseSchemaName());
            hibernateDatabase.setObjectQuotingStrategy(ObjectQuotingStrategy.QUOTE_ALL_OBJECTS);
            hibernateDatabase.setConnection(new JdbcConnection(
                new HibernateConnection("hibernate:spring:" + packagesToScan + "?dialect=" + applicationContext.getEnvironment().getProperty("spring.jpa.database-platform"))));

            // Use liquibase to do a difference of schema between hibernate and database
            String changeLogFile = null;
            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), sourceDatabase);

            // Retrieve the difference
            DiffResult diffResult = liquibase.diff(hibernateDatabase, sourceDatabase, compareControl);

            // Build the changelogs if any changes
            DiffToChangeLog diffToChangeLog = new DiffToChangeLog(diffResult, new DiffOutputControl(false, false, true));

            // Ignore the database changeLog table
            ignoreDatabaseChangeLogTable(diffResult);
            ignoreActivitiChangeLogTable(diffResult);

            // If no changes do nothing
            if (diffToChangeLog.generateChangeSets().size() == 0) {
                log.debug("Reload - No database change");
                return;
            }

            // Write the db-changelog-[SEQUENCE].xml file
            String changeLogString = toChangeLog(diffToChangeLog);
            String changeLogName = "db-changelog-" + calculateNextSequence() + ".xml";
            final File changelogFile = FileSystems.getDefault().getPath(CHANGELOG_FOLDER + changeLogName).toFile();
            final FileOutputStream out = new FileOutputStream(changelogFile);
            IOUtils.write(changeLogString, out);
            IOUtils.closeQuietly(out);
            log.debug("Reload - the db-changelog file '{}' has been generated", changelogFile.getAbsolutePath());

            // Re-write the master.xml files
            rewriteMasterFiles();

            // Execute the new changelog on the database
            SpringLiquibase springLiquibase = new SpringLiquibase() {
                @Override
                protected void performUpdate(Liquibase liquibase) throws LiquibaseException {
                    // Override to be able to add
                    liquibase.setChangeLogParameter("logicalFilePath", "none");
                    super.performUpdate(liquibase);
                }
            };
            springLiquibase.setResourceLoader(new FileSystemResourceLoader());
            springLiquibase.setDataSource(dataSource);
            springLiquibase.setChangeLog("file:" + changelogFile.getAbsolutePath());
            springLiquibase.setContexts("development");

            try {
                springLiquibase.afterPropertiesSet();
                log.debug("Reload - Successful database update");
            } catch (LiquibaseException e) {
                log.error("Failed to reload the database", e);
            }

        } catch (Exception e) {
            log.error("Failed to generate the db-changelog.xml file", e);
        } finally {
            // close the database
            if (sourceDatabase != null) {
                try {
                    sourceDatabase.close();
                } catch (DatabaseException e) {
                    log.error("Failed to close the source database", e);
                }
            }

            if (hibernateDatabase != null) {
                try {
                    hibernateDatabase.close();
                } catch (DatabaseException e) {
                    log.error("Failed to close the reference database", e);
                }
            }
        }
    }

    /**
     *
     * @param diffToChangeLog
     * @return
     * @throws Exception
     */
    private String toChangeLog(DiffToChangeLog diffToChangeLog) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out, true, "UTF-8");
        diffToChangeLog.setChangeSetAuthor("System");
        //CustomXMLChangeLogSerializer customXMLChangeLogSerializer = new CustomXMLChangeLogSerializer();
        XMLChangeLogSerializer xmlChangeLogSerializer = new XMLChangeLogSerializer();
        diffToChangeLog.print(printStream, xmlChangeLogSerializer);
        printStream.close();
        return out.toString("UTF-8");
    }

    /**
     *
     * @param diffResult
     */
    private void ignoreActivitiChangeLogTable(DiffResult diffResult) throws Exception {

        List<String> activitiTables = new ArrayList<>();

        final String excludeTables = applicationContext.getEnvironment().getProperty("reload.liquibase.excludeTables", "");

        if (StringUtils.isNotEmpty(excludeTables)) {
            activitiTables.addAll(Arrays.asList(excludeTables.toUpperCase().split(",")));
        }

        Set<Table> unexpectedTables = diffResult
            .getUnexpectedObjects(Table.class);
        for (Table table : unexpectedTables) {
            if (activitiTables.contains(table.getName().toUpperCase().toUpperCase())) {
                diffResult.getUnexpectedObjects().remove(table);
            }
        }

        Set<Table> missingTables = diffResult
            .getMissingObjects(Table.class);
        for (Table table : missingTables) {
            if (activitiTables.contains(table.getName().toUpperCase())) {
                diffResult.getMissingObjects().remove(table);
            }
        }

        Set<Column> unexpectedColumns = diffResult.getUnexpectedObjects(Column.class);
        for (Column column : unexpectedColumns) {
            if (activitiTables.contains(column.getRelation().getName().toUpperCase())) {
                diffResult.getUnexpectedObjects().remove(column);
            }
        }

        Set<Column> missingColumns = diffResult.getMissingObjects(Column.class);
        for (Column column : missingColumns) {
            if (activitiTables.contains(column.getRelation().getName().toUpperCase())) {
                diffResult.getMissingObjects().remove(column);
            }
        }

        Set<Index> unexpectedIndexes = diffResult.getUnexpectedObjects(Index.class);
        for (Index index : unexpectedIndexes) {
            if (activitiTables.contains(index.getTable().getName().toUpperCase())) {
                diffResult.getUnexpectedObjects().remove(index);
            }
        }

        Set<Index> missingIndexes = diffResult.getMissingObjects(Index.class);
        for (Index index : missingIndexes) {
            if (activitiTables.contains(index.getTable().getName().toUpperCase())) {
                diffResult.getMissingObjects().remove(index);
            }
        }

        Set<PrimaryKey> unexpectedPrimaryKeys = diffResult.getUnexpectedObjects(PrimaryKey.class);
        for (PrimaryKey primaryKey : unexpectedPrimaryKeys) {
            if (activitiTables.contains(primaryKey.getTable().getName().toUpperCase())) {
                diffResult.getUnexpectedObjects().remove(primaryKey);
            }
        }

        Set<PrimaryKey> missingPrimaryKeys = diffResult.getMissingObjects(PrimaryKey.class);
        for (PrimaryKey primaryKey : missingPrimaryKeys) {
            if (activitiTables.contains(primaryKey.getTable().getName().toUpperCase())) {
                diffResult.getMissingObjects().remove(primaryKey);
            }
        }
    }

    /**
     *
     * @param diffResult
     * @throws Exception
     */
    private void ignoreDatabaseChangeLogTable(DiffResult diffResult)
        throws Exception {

        Set<Table> unexpectedTables = diffResult
            .getUnexpectedObjects(Table.class);
        for (Table table : unexpectedTables) {
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(table.getName())
                || "DATABASECHANGELOG".equalsIgnoreCase(table.getName())) {

                diffResult.getUnexpectedObjects().remove(table);
            }
        }

        Set<Table> missingTables = diffResult
            .getMissingObjects(Table.class);
        for (Table table : missingTables) {
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(table.getName())
                || "DATABASECHANGELOG".equalsIgnoreCase(table.getName())) {

                diffResult.getMissingObjects().remove(table);
            }
        }

        Set<Column> unexpectedColumns = diffResult.getUnexpectedObjects(Column.class);
        for (Column column : unexpectedColumns) {
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(column.getRelation().getName())
                || "DATABASECHANGELOG".equalsIgnoreCase(column.getRelation().getName())) {

                diffResult.getUnexpectedObjects().remove(column);
            }
        }

        Set<Column> missingColumns = diffResult.getMissingObjects(Column.class);
        for (Column column : missingColumns) {
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(column.getRelation().getName())
                || "DATABASECHANGELOG".equalsIgnoreCase(column.getRelation().getName())) {
                diffResult.getMissingObjects().remove(column);
            }
        }

        Set<Index> unexpectedIndexes = diffResult.getUnexpectedObjects(Index.class);
        for (Index index : unexpectedIndexes) {
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(index.getTable().getName())
                || "DATABASECHANGELOG".equalsIgnoreCase(index.getTable().getName())) {

                diffResult.getUnexpectedObjects().remove(index);
            }
        }

        Set<Index> missingIndexes = diffResult.getMissingObjects(Index.class);
        for (Index index : missingIndexes) {
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(index.getTable().getName())
                || "DATABASECHANGELOG".equalsIgnoreCase(index.getTable().getName())) {

                diffResult.getMissingObjects().remove(index);
            }
        }

        Set<PrimaryKey> unexpectedPrimaryKeys = diffResult.getUnexpectedObjects(PrimaryKey.class);
        for (PrimaryKey primaryKey : unexpectedPrimaryKeys) {
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(primaryKey.getTable().getName())
                || "DATABASECHANGELOG".equalsIgnoreCase(primaryKey.getTable().getName())) {

                diffResult.getUnexpectedObjects().remove(primaryKey);
            }
        }

        Set<PrimaryKey> missingPrimaryKeys = diffResult.getMissingObjects(PrimaryKey.class);
        for (PrimaryKey primaryKey : missingPrimaryKeys) {
            if ("DATABASECHANGELOGLOCK".equalsIgnoreCase(primaryKey.getTable().getName())
                || "DATABASECHANGELOG".equalsIgnoreCase(primaryKey.getTable().getName())) {

                diffResult.getMissingObjects().remove(primaryKey);
            }
        }
    }

    /**
     * @return the source database
     */
    private Database getSourceDatabase(DataSource dataSource) {
        String currentDatabase = applicationContext.getEnvironment().getProperty("spring.jpa.database");

        String liquibaseDatabase;

        switch (currentDatabase) {
            case "MYSQL":
                liquibaseDatabase = "liquibase.database.core.MySQLDatabase";
                break;
            case "POSTGRESQL":
                liquibaseDatabase = "liquibase.database.core.PostgresDatabase";
                break;
            case "H2":
                liquibaseDatabase = "liquibase.database.core.H2Database";
                break;
            default:
                throw new IllegalStateException("The database named '" + currentDatabase + "' is not supported");
        }

        try {
            Database database = (Database) Class.forName(liquibaseDatabase).newInstance();
            database.setConnection(new JdbcConnection(dataSource.getConnection()));

            return database;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate the liquibase database: " + liquibaseDatabase, e);
        }
    }

    /**
     * Calculate the next sequence used to generate the db-changelog file.
     *
     * The sequence is formatted as follow:
     *    leftpad with 0 + number
     * @return the next sequence
     */
    private String calculateNextSequence() {
        final File changeLogFolder = FileSystems.getDefault().getPath(CHANGELOG_FOLDER).toFile();

        final File[] allChangelogs = changeLogFolder.listFiles((FileFilter) new SuffixFileFilter(".xml"));

        Integer sequence = 0;

        for (File changelog : allChangelogs) {
            String fileName = FilenameUtils.getBaseName(changelog.getName());
            String currentSequence = StringUtils.substringAfterLast(fileName, "-");
            int cpt = Integer.parseInt(currentSequence);
            if (cpt > sequence) {
                sequence = cpt;
            }
        }
        sequence++;
        return StringUtils.leftPad(sequence.toString(), 3, "0");
    }

    /**
     * The master.xml file will be rewritten to include the new changelogs.
     */
    private void rewriteMasterFiles() {
        try {
            File masterFile = FileSystems.getDefault().getPath(MASTER_FILE).toFile();
            FileOutputStream fileOutputStream = new FileOutputStream(masterFile);

            final File changeLogFolder = FileSystems.getDefault().getPath(CHANGELOG_FOLDER).toFile();

            final File[] allChangelogs = changeLogFolder.listFiles((FileFilter) new SuffixFileFilter(".xml"));

            String begin = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<databaseChangeLog\n" +
                "        xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\"\n" +
                "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "        xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.0.xsd\">\n\r"+
                " <include file=\"classpath:db/changelog-create.sql\" />\n\r";
            String end = "</databaseChangeLog>";

            IOUtils.write(begin, fileOutputStream);

            // Writer the changelogs
            StringBuilder sb = new StringBuilder();

            for (File allChangelog : allChangelogs) {
                String fileName = allChangelog.getName();
                sb.append("\t<include file=\"" + RELATIVE_CHANGELOG_FOLDER).append(fileName).append("\" relativeToChangelogFile=\"false\"/>").append("\r\n");
            }

            IOUtils.write(sb.toString(), fileOutputStream);
            IOUtils.write(end, fileOutputStream);
            IOUtils.closeQuietly(fileOutputStream);

            log.debug("The file '{}' has been updated", MASTER_FILE);
        } catch (Exception e) {
            log.error("Failed to write the master.xml file. This file must be updated manually");

        }
    }

    /**
     * Initialized the compare control.
     */
    protected void initCompareControl() {
        Set<Class<? extends DatabaseObject>> typesToInclude = new HashSet<>();
        typesToInclude.add(Table.class);
        typesToInclude.add(Column.class);
        compareControl = new CompareControl(typesToInclude);
        compareControl.addSuppressedField(Table.class, "remarks");
        compareControl.addSuppressedField(Column.class, "remarks");
        compareControl.addSuppressedField(Column.class, "certainDataType");
        compareControl.addSuppressedField(Column.class, "autoIncrementInformation");
    }

}
