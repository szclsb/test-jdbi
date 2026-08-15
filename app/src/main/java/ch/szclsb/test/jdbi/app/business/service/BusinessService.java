package ch.szclsb.test.jdbi.app.business.service;

import java.util.List;
import java.util.Optional;

public interface BusinessService<T> {
    List<T> findAll();

    Optional<T> findById(Long id);
}
