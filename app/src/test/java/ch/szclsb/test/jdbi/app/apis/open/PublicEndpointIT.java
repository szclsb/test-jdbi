package ch.szclsb.test.jdbi.app.apis.open;

import ch.szclsb.test.jdbi.app.apis.AbstractRestIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

public class PublicEndpointIT extends AbstractRestIntegrationTest {
    @Test
    public void now() {
        var now = OffsetDateTime.now();
        var body = client.get()
                .uri("/public/now")
                .retrieve()
                .body(String.class);
        if (body != null) {
            var nowServer = OffsetDateTime.parse(body);
            Assertions.assertThat(now).isBeforeOrEqualTo(nowServer);
        } else {
            Assertions.fail("body is null");
        }
    }
}
