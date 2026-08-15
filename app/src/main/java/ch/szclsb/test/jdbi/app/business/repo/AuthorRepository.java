package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.store.Author;
import org.jdbi.v3.spring.JdbiRepository;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;
import java.util.Optional;

@JdbiRepository
public interface AuthorRepository {
    @SqlQuery("""
    SELECT *
    FROM store.author
    """)
    List<Author> findAll();

    @SqlQuery("""
    SELECT *
    FROM store.author
    WHERE id = :id
    """)
    Optional<Author> findById(final Long id);
}
