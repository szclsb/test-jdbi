package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.store.Author;
import ch.szclsb.test.jdbi.model.store.Book;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;
import org.jdbi.v3.spring.JdbiRepository;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMappers;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@JdbiRepository
public interface BookRepository {
    @SqlQuery("""
    SELECT *
    FROM store.book
    """)
    List<Book> findAll();

    @SqlQuery("""
    SELECT *
    FROM store.book
    WHERE id = :id
    """)
    Optional<Book> findById(final Long id);


    @SqlQuery("""
    SELECT
            b.id AS b_id,
            b.version AS b_version,
            b.created_at AS b_created_at,
            b.created_by AS b_created_by,
            b.modified_at AS b_modified_at,
            b.modified_by AS b_modified_by,
            b.title AS b_title,
            b.summary AS b_summary,
            a.id AS a_id,
            a.version AS a_version,
            a.created_at AS a_created_at,
            a.created_by AS a_created_by,
            a.modified_at AS a_modified_at,
            a.modified_by AS a_modified_by,
            a.first_name AS a_first_name,
            a.last_name AS a_last_name
    FROM store.book b
    JOIN store.author a ON b.author_id = a.id
    WHERE b.id = :id
    """)
    @RegisterBeanMapper(value = Book.class, prefix = "b")
    @RegisterBeanMapper(value = Author.class, prefix = "a")
    @UseRowReducer(BookAuthorReducer.class)
    Optional<Book> findByIdWithAuthor(final Long id);  // TODO optimize

    class BookAuthorReducer implements LinkedHashMapRowReducer<Long, Book> {
        @Override
        public void accumulate(Map<Long, Book> map, RowView rowView) {
            var book = map.computeIfAbsent(rowView.getColumn("b_id", Long.class),
                    id -> rowView.getRow(Book.class));
            if (rowView.getColumn("a_id", Long.class) != null) {
                book.setAuthor(rowView.getRow(Author.class));
            }
        }
    }
}
