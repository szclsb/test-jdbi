package ch.szclsb.test.jdbi.app.business.service;

import ch.szclsb.test.jdbi.app.business.repo.BookRepository;
import ch.szclsb.test.jdbi.model.store.Book;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.mapper.JoinRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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

    @Transactional(readOnly = true)
    public Optional<Book> findById(final Long id, String graph) {
        return Objects.equals(graph, "author")
                ? bookRepository.findByIdWithAuthor(id)
                : bookRepository.findById(id);
    }
}
