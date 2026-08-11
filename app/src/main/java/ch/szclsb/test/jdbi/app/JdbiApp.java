package ch.szclsb.test.jdbi.app;

import ch.szclsb.test.jdbi.app.configuration.SecurityConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class
})
@Configuration
public class JdbiApp {
    static void main(String[] args) {
        SpringApplication.run(JdbiApp.class, args);
    }

    @Bean
    @ConfigurationProperties("app.security")
    public SecurityConfigProperties securityConfigProperties() {
        return new SecurityConfigProperties();
    }
}
