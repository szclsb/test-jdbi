package ch.szclsb.test.jdbi.app.apis.closed;

import ch.szclsb.test.jdbi.app.business.service.BookBusinessService;
import ch.szclsb.test.jdbi.app.exception.ApiException;
import ch.szclsb.test.jdbi.model.store.Book;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store/v1/book")
public class BookEndpoint {
    private final BookBusinessService bookService;

    public BookEndpoint(BookBusinessService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public Book findAll(@PathVariable Long id, @RequestParam(required = false) String graph) {
        return bookService.findById(id, graph)
                .orElseThrow(() -> ApiException.notFound("book id=%d not found".formatted(id)));
    }
}
