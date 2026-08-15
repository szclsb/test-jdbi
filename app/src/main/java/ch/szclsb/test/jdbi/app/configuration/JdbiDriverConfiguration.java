package ch.szclsb.test.jdbi.app.configuration;

import ch.szclsb.test.jdbi.model.Entity;
import ch.szclsb.test.jdbi.model.EntityBean;
import ch.szclsb.test.jdbi.model.store.Author;
import ch.szclsb.test.jdbi.model.store.Book;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.spring.SpringConnectionFactory;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class JdbiDriverConfiguration {
    @Bean
    public Jdbi jdbi(DataSource dataSource) {
        var cf = new SpringConnectionFactory(dataSource);
        final var jdbi = Jdbi.create(cf);
        //jdbi.installPlugin(new SqlObjectPlugin());  // enable if @JdbiRepository are used
        jdbi.installPlugin(new PostgresPlugin());

        var provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(EntityBean.class));
        provider.findCandidateComponents(Entity.class.getPackageName()).forEach(beanDefinition -> {
            try {
                var entityClass = Class.forName(beanDefinition.getBeanClassName());
                jdbi.registerRowMapper(BeanMapper.factory(entityClass));
                log.debug("registered entity bean {} as row mapper", entityClass.getSimpleName());
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Could not load class", e);
            }
        });

        return jdbi;
    }
}
