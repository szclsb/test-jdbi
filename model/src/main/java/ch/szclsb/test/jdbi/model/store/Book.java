package ch.szclsb.test.jdbi.model.store;

import ch.szclsb.test.jdbi.model.AbstractEntity;
import ch.szclsb.test.jdbi.model.EntityBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EntityBean
public class Book extends AbstractEntity<Long> {
    private String title;
    private String summary;
    private Author author;
}
