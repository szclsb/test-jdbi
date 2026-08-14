package ch.szclsb.test.jdbi.model.store;

import ch.szclsb.test.jdbi.model.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Author extends AbstractEntity<Long> {
    private String firstName;
    private String lastName;
}
