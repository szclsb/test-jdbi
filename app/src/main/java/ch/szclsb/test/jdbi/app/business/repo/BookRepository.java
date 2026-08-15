package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.store.Book;
import org.jdbi.v3.spring.JdbiRepository;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;
import java.util.Optional;

@JdbiRepository
public interface BookRepository {
    @SqlQuery("""
    SELECT *
    FROM store.book
    """)
    @RegisterBeanMapper(Book.class)
    List<Book> findAll();

    @SqlQuery("""
    SELECT *
    FROM store.book
    WHERE id = :id
    """)
    @RegisterBeanMapper(Book.class)
    Optional<Book> findById(final Long id);
}
