package se.inera.fmu.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by Rasheed on 7/15/14.
 */
@Configuration
abstract public class FmuCoreConfigurationSupport {

    @Bean
    public abstract DataSource dataSource();
}