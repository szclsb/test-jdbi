package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.store.Author;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;
import org.springframework.stereotype.Component;

@Component
public class AuthorRepository extends AbstractEntityRepository<Author, Long> {
    public AuthorRepository(Jdbi jdbi) {
        super(jdbi, Author.class);
    }

    @Override
    protected Query selectAll(Handle handle) {
        return handle.createQuery("""
                SELECT *
                FROM store.author
                """);
    }

    @Override
    protected Query selectById(Handle handle, Long id) {
        return handle.createQuery("""
                        SELECT *
                        FROM store.author
                        WHERE id = :id
                        """)
                .bind("id", id);
    }
}
