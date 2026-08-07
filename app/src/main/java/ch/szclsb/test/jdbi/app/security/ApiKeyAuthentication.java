package ch.szclsb.test.jdbi.app.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {
    private final String apiKeyToken;

    public ApiKeyAuthentication(String apiKeyToken, @Nullable Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKeyToken = apiKeyToken;
        setAuthenticated(true);
    }

    @Override
    public @Nullable Object getCredentials() {
        return null;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return apiKeyToken;
    }
}
