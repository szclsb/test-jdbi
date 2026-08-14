package ch.szclsb.test.jdbi.model;

import java.io.Serializable;

public interface Entity<ID extends Serializable> {
    ID getId();
}
