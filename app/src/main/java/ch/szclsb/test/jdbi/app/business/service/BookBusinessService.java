package ch.szclsb.test.jdbi.app.business.service;

import ch.szclsb.test.jdbi.app.business.repo.BookRepository;
import ch.szclsb.test.jdbi.model.store.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookBusinessService implements BusinessService<Book> {
    private final BookRepository bookRepository;

    public BookBusinessService(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(final Long id) {
        return bookRepository.findById(id);
    }
}
