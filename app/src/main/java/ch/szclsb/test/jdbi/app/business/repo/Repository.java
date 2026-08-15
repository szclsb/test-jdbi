package ch.szclsb.test.jdbi.app.business.repo;

import ch.szclsb.test.jdbi.model.Entity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<T extends Entity<ID>, ID extends Serializable> {
    List<T> findAll();

    Optional<T> findById(ID id);
}
