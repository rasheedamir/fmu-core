package se.inera.fmu.core.config;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * Created by Rasheed on 7/15/14.
 */
@Configuration
abstract public class FmuCoreConfigurationSupport implements EnvironmentAware {

    @Bean
    public abstract DataSource dataSource();

    protected RelaxedPropertyResolver propertyResolver;

    protected Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
    }
}