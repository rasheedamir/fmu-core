package se.inera.fmu.core.config;

import org.activiti.engine.ProcessEngine;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.annotations.ActivitiConfigurer;
import org.activiti.spring.annotations.EnableActiviti;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rasheed on 7/16/14.
 *
 * <p>
 * Auto configuration for using Activiti from a <A href="http://spring.io/projects/spring-boot">Spring Boot application</a>.
 * Provides a configured {@link org.activiti.engine.ProcessEngine} if none other is detected.
 * <p>
 * Discovers any processes definitions deployed in the {@literal src/main/resources/processes} folder, and
 * uses the single {@link javax.sql.DataSource} bean discovered in the Spring application context..
 *
 * @author Josh Long
 * @author Rasheed Amir
 */
@Configuration
@ConditionalOnClass({ProcessEngine.class, EnableActiviti.class})
@ConditionalOnBean(javax.sql.DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnMissingBean(ProcessEngine.class)
public class ProcessEngineConfiguration {

    @Configuration
    @EnableActiviti
    public static class DefaultActivitiConfiguration implements ActivitiConfigurer, EnvironmentAware {

        private Log log = LogFactory.getLog(getClass());

        public static final String PROCESS_DEFINITIONS_DEFAULT_PREFIX =
                "classpath:/processes/";

        public static final String PROCESS_DEFINITIONS_DEFAULT_SUFFIX =
                "**.bpmn";

        private RelaxedPropertyResolver environment;

        @SuppressWarnings("SpringJavaAutowiringInspection")
        @Autowired
        private DataSource[] dataSources;

        @Autowired
        private ResourcePatternResolver applicationContext;

        @Override
        public void processDefinitionResources(List<Resource> resourceList) {
            List<Resource> resources;
            String prefix, suffix;
            try {
                prefix = this.environment.getProperty("prefix", PROCESS_DEFINITIONS_DEFAULT_PREFIX);
                suffix = this.environment.getProperty("suffix", PROCESS_DEFINITIONS_DEFAULT_SUFFIX);
                String path = prefix + suffix;

                if (this.environment.getProperty("checkProcessDefinitions", Boolean.class, true)) {
                    Assert.state(this.applicationContext.getResource(prefix).exists(), String.format("No processes definitions were found deployed at %s. Are you actually using Activiti? ", path));
                }

                // loop through the processes definitions discovered.
                resources = Arrays.asList(this.applicationContext.getResources(path));

                if (log.isInfoEnabled()) {
                    log.info(String.format(
                            "found %s process definitions in %s.", resources.size(), prefix));
                    for (Resource resource : resources) {
                        log.info(String.format("found process definition: %s", resource.getURI().toString()));
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            resourceList.addAll(resources);
        }

        @Override
        public void postProcessSpringProcessEngineConfiguration(
                SpringProcessEngineConfiguration config) {

            config.setDeploymentName(
                    this.environment.getProperty("deploymentName", config.getDeploymentName()));

            config.setDatabaseSchemaUpdate(
                    this.environment.getProperty("databaseSchemaUpdate", config.getDatabaseSchemaUpdate()));

            config.setDatabaseSchema(
                    this.environment.getProperty("databaseSchema", config.getDatabaseSchema()));

        }

        @Override
        public DataSource dataSource() {
            Assert.isTrue(this.dataSources.length > 0, "you must have configured at least " +
                    "one javax.sql.DataSource bean in your Spring application context.");
            return this.dataSources[0];
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = new RelaxedPropertyResolver(environment,
                    "spring.activiti.");

        }
    }
}
