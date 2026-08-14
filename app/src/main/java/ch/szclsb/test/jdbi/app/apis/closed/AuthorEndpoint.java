package ch.szclsb.test.jdbi.app.apis.closed;

import ch.szclsb.test.jdbi.app.business.data.Author;
import ch.szclsb.test.jdbi.app.business.service.AuthorService;
import ch.szclsb.test.jdbi.app.exception.ApiException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/store/v1/author")
public class AuthorEndpoint {
    private final AuthorService authorService;

    public AuthorEndpoint(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<Author> findAll() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public Author findAll(@PathVariable long id) {
        return authorService.findById(id).orElseThrow(() -> ApiException.notFound("author id=%d not found".formatted(id)));
    }
}
