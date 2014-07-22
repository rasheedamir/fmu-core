package se.inera.fmu.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * Created by Rasheed on 7/17/14.
 */
@Configuration
public class DevFmuCoreConfiguration extends FmuCoreConfigurationSupport {

    @Bean
    @Override
    @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
    public DataSource dataSource() {
        // Embedded H2 Database
        // Read Configurations from property file
        EmbeddedDatabase dataSource = new EmbeddedDatabaseBuilder()
                                            .setType(EmbeddedDatabaseType.H2)
                                            .addScript("classpath:org/activiti/db/create/activiti.h2.create.engine.sql")
                                            .addScript("classpath:org/activiti/db/create/activiti.h2.create.history.sql")
                                            .addScript("classpath:org/activiti/db/create/activiti.h2.create.identity.sql")
                                            .build();
        return dataSource;
    }
}