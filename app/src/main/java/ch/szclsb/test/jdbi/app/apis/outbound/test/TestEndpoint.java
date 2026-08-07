package ch.szclsb.test.jdbi.app.apis.outbound.test;

import ch.szclsb.test.jdbi.app.business.service.TestBusinessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestEndpoint {
    private final TestBusinessService testBusinessService;

    public TestEndpoint(TestBusinessService testBusinessService) {
        this.testBusinessService = testBusinessService;
    }

    @GetMapping("/message/{name}")
    public String getTestMessage(@PathVariable String name) {
        return testBusinessService.getTestMessage(name);
    }
}
