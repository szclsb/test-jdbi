package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.app.business.data.Author;
import org.jdbi.v3.spring.JdbiRepository;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@JdbiRepository
public interface AuthorRepository {
    @SqlQuery("""
    SELECT *
    FROM store.author
    """)
    @RegisterConstructorMapper(Author.class)
    List<Author> findAll();
}
