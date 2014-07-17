package se.inera.fmu.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public DataSource dataSource() {
        // Embedded H2 Database
        // Read Configurations from property file
        EmbeddedDatabase dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
        return dataSource;
    }
}

