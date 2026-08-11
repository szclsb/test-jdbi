package ch.szclsb.test.jdbi.app.apis.open;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/public")
public class PublicEndpoint {
    private final DateTimeFormatter datetimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

    @GetMapping("/now")
    public String getTestMessage() {
        return datetimeFormatter.format(OffsetDateTime.now());
    }
}
