package ch.szclsb.test.jdbi.app.apis.closed;

import ch.szclsb.test.jdbi.app.apis.AbstractRestIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClosedEndpointIT extends AbstractRestIntegrationTest {
    @Test
    public void messageName() {
        var name = "randomNameABC";
        var expectedBody = "Server: hello %s".formatted(name);

        var body = client.get()
                .uri("test/message/{name}", name)
                .retrieve()
                .body(String.class);
        if (body != null) {
            Assertions.assertThat(expectedBody).isEqualTo(body);
        } else {
            Assertions.fail("body is null");
        }
    }
}
