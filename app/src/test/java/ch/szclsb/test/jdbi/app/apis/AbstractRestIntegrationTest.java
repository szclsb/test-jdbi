package ch.szclsb.test.jdbi.app.apis;

import ch.szclsb.test.jdbi.app.AbstractIntegrationTest;
import ch.szclsb.test.jdbi.app.configuration.SecurityConfigProperties;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

public abstract class AbstractRestIntegrationTest extends AbstractIntegrationTest {
    @LocalServerPort
    private int port;
    @Autowired
    private SecurityConfigProperties securityConfigProperties;

    private RestClient.Builder clientBuilder;
    protected RestClient client;

    @PostConstruct
    private void clientBuilder() {
        // RestTestClient.bindToServer() fails with:
        // java.lang.ClassCastException: class org.springframework.test.web.servlet.client.DefaultRestTestClientBuilder cannot be cast to class org.springframework.test.web.servlet.client.RestTestClient (org.springframework.test.web.servlet.client.DefaultRestTestClientBuilder and org.springframework.test.web.servlet.client.RestTestClient are in unnamed module of loader 'app')
        this.clientBuilder = RestClient.builder()
                .baseUrl("http://localhost:%d".formatted(port))
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(
                            securityConfigProperties.getApiKeyHeader(),
                            securityConfigProperties.getApiKeyToken()
                    );
                });
    }

    @BeforeEach
    void builder() {
        this.client = clientBuilder.build();
    }
}
