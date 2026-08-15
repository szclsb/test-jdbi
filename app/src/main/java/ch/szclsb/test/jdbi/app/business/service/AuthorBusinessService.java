package ch.szclsb.test.jdbi.app.business.service;

import ch.szclsb.test.jdbi.model.store.Author;
import ch.szclsb.test.jdbi.app.business.repo.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AuthorBusinessService implements BusinessService<Author> {
    private final AuthorRepository authorRepository;

    public AuthorBusinessService(final AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> findById(final Long id) {
        return authorRepository.findById(id);
    }



}
