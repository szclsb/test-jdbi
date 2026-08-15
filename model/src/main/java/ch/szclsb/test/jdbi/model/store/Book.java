package ch.szclsb.test.jdbi.model.store;

import ch.szclsb.test.jdbi.model.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book extends AbstractEntity<Long> {
    private String title;
    private String summary;
    private Author author;
}
