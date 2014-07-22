package se.inera.fmu.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Created by Rasheed on 7/17/14.
 */
@Configuration
public class DevFmuCoreConfiguration extends FmuCoreConfigurationSupport {

    private final Logger log = LoggerFactory.getLogger(DevFmuCoreConfiguration.class);

    @Bean
    @Override
    @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
    public DataSource dataSource() {
        if (log.isDebugEnabled()) {
            log.debug("Configuring Datasource");
        }
        // Embedded H2 Database
        // Read Configurations from property file
        if (propertyResolver.getProperty("url") == null && propertyResolver.getProperty("databaseName") == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                            " cannot start. Please check your Spring profile, current profiles are: {}",
                    Arrays.toString(environment.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        EmbeddedDatabase dataSource = new EmbeddedDatabaseBuilder()
                                            .setType(EmbeddedDatabaseType.H2)
                                            .setName(propertyResolver.getProperty("databaseName"))
                                            //.addScript("classpath:org/activiti/db/create/activiti.h2.create.engine.sql")
                                            //.addScript("classpath:org/activiti/db/create/activiti.h2.create.history.sql")
                                            //.addScript("classpath:org/activiti/db/create/activiti.h2.create.identity.sql")
                                            .build();
        return dataSource;
    }
}