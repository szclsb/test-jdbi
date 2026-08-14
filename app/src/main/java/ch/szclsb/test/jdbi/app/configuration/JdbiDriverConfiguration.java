package ch.szclsb.test.jdbi.app.configuration;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.spring.SpringConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JdbiDriverConfiguration {
    @Bean
    public Jdbi jdbi(DataSource dataSource) {
        var cf = new SpringConnectionFactory(dataSource);
        final var jdbi = Jdbi.create(cf);
        /* additional configuration goes here */
        return jdbi;
    }
}
