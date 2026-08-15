package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.store.Author;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AuthorRepository extends AbstractRepository<Author, Long> {
    public AuthorRepository(Jdbi jdbi) {
        super(jdbi);
    }

    public List<Author> findAll() {
        return useHandle(handle -> handle.createQuery("""
                        SELECT *
                        FROM store.author
                        """)
                .mapTo(Author.class)
                .collectIntoList());
    }

    public Optional<Author> findById(final Long id) {
        return useHandle(handle -> handle.createQuery("""
                        SELECT *
                        FROM store.author
                        WHERE id = :id
                        """)
                .bind("id", id)
                .mapTo(Author.class)
                .findOne());
    }
}
