package my.project.hotel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class DatabasePropertiesConfig {

    @Bean
    @ConfigurationProperties(prefix = "custom.datasource")
    public DbConnectionProperties dbConnectionProperties() {
        return new DbConnectionProperties();
    }
}
