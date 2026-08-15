package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.store.Author;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Component;

@Component
public class AuthorRepository extends AbstractEntityRepository<Author, Long> {
    public AuthorRepository(Jdbi jdbi, EntityMetadataService entityMetadataService) {
        super(jdbi, entityMetadataService, Author.class);
    }
}
