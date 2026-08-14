package ch.szclsb.test.jdbi.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@Setter
public class AbstractEntity<ID extends Serializable> implements Entity<ID> {
    private ID id;
    private Integer version;
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime modifiedAt;
    private String modifiedBy;
}
