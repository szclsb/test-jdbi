package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.store.Author;
import ch.szclsb.test.jdbi.model.store.Book;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;
import org.jdbi.v3.core.statement.Query;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class BookRepository extends AbstractEntityRepository<Book, Long> {
    public BookRepository(Jdbi jdbi) {
        super(jdbi, Book.class);
    }

    @Override
    protected Query selectAll(Handle handle) {
        return handle.createQuery("""
                SELECT *
                FROM store.book
                """);
    }

    @Override
    protected Query selectById(Handle handle, Long id) {
        return handle.createQuery("""
                        SELECT *
                        FROM store.book
                        WHERE id = :id
                        """)
                .bind("id", id);

    }

    public Optional<Book> findByIdWithAuthor(final Long id) {
        return useHandle(handle -> handle.createQuery("""
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
                .bind("id", id)
                .registerRowMapper(BeanMapper.factory(Book.class, "b"))
                .registerRowMapper(BeanMapper.factory(Author.class, "a"))
                .reduceRows(new BookAuthorReducer())
                .findFirst());
    }

    static class BookAuthorReducer implements LinkedHashMapRowReducer<Long, Book> {
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
