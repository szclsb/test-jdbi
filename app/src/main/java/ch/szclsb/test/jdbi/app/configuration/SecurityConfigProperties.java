package ch.szclsb.test.jdbi.app.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityConfigProperties {
    private String apiKeyHeader;
    private String apiKeyToken;

    private String[] corsDomains;
    private String[] corsMethods;
}
