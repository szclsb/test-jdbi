package ch.szclsb.test.jdbi.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class
})
//@EnableJdbiRepositories  // enable if @JdbiRepository are used
public class JdbiApp {
    static void main(String[] args) {
        SpringApplication.run(JdbiApp.class, args);
    }
}
