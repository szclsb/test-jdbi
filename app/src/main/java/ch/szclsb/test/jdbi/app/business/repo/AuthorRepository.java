package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.store.Author;
import org.jdbi.v3.spring.JdbiRepository;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;
import java.util.Optional;

@JdbiRepository
public interface AuthorRepository {
    @SqlQuery("""
    SELECT *
    FROM store.author
    """)
    @RegisterBeanMapper(Author.class)
    List<Author> findAll();

    @SqlQuery("""
    SELECT *
    FROM store.author
    WHERE id = :id
    """)
    @RegisterConstructorMapper(Author.class)
    Optional<Author> findById(long id);
}
