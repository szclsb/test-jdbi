package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.store.Author;
import ch.szclsb.test.jdbi.model.store.Book;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class BookRepository extends AbstractEntityRepository<Book, Long> {
    private final QueryEntityUtility<Author> authorQueryEntityUtility;

    public BookRepository(Jdbi jdbi) {
        super(jdbi, Book.class);
        this.authorQueryEntityUtility = new QueryEntityUtility<>(Author.class);  // todo read from cache
    }

    public Optional<Book> findByIdWithAuthor(final Long id) {
        return useHandle(handle -> {
            var query = handle.createQuery("""
                    SELECT
                            <book_columns>,
                            <author_columns>
                    FROM <book_table> b
                    JOIN <author_table> a ON b.author_id = a.id
                    WHERE b.id = :id
                    """);
            queryEntityUtility.defineEntity(query, "book_table", "book_columns", "b");
            authorQueryEntityUtility.defineEntity(query, "author_table", "author_columns", "a");
            return query
                    .bind("id", id)
                    .reduceRows(ReducerFactory.rowIdReducer(  // TODO use defineEntity prefixes
                            Book.class, "b",
                            ReducerFactory.Reducer.ofId(Author.class, "a", Book::setAuthor)
                    ))
                    .findFirst();
        });
    }
}
