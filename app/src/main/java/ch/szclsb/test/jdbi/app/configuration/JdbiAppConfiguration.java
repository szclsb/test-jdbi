package ch.szclsb.test.jdbi.app.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JdbiAppConfiguration {
    @Bean
    @ConfigurationProperties("app.security")
    public SecurityConfigProperties securityConfigProperties() {
        return new SecurityConfigProperties();
    }
}
