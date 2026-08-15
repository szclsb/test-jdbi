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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class BookRepository extends AbstractEntityRepository<Book, Long> {
    private final List<String> bookPrefixColumnNames;
    private final List<String> authorPrefixColumnNames;

    public BookRepository(Jdbi jdbi, EntityMetadataService entityMetadataService) {
        super(jdbi, entityMetadataService, Book.class);
        this.bookPrefixColumnNames = entityMetadataService.getColumnNames(Book.class, "b");
        this.authorPrefixColumnNames = entityMetadataService.getColumnNames(Author.class, "a");
    }

    public Optional<Book> findByIdWithAuthor(final Long id) {
        return useHandle(handle -> handle.createQuery("""
                        SELECT
                                <book_columns>,
                                <author_columns>
                        FROM store.book b
                        JOIN store.author a ON b.author_id = a.id
                        WHERE b.id = :id
                        """)
                .defineList("book_columns", bookPrefixColumnNames)
                .defineList("author_columns", authorPrefixColumnNames)
                .bind("id", id)
                .registerRowMapper(BeanMapper.factory(Book.class, "b"))
                .registerRowMapper(BeanMapper.factory(Author.class, "a"))
                .reduceRows(new BookAuthorReducer())
                .findFirst());
    }

    // todo simplify row reducer
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
