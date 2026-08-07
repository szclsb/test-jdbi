package ch.szclsb.test.jdbi.app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

public class ApiKeyAuthenticationFilter extends GenericFilterBean {
    private final String apiKeyHeader;
    private final String apiKeyToken;

    public ApiKeyAuthenticationFilter(String apiKeyHeader, String apiKeyToken) {
        this.apiKeyHeader = apiKeyHeader;
        this.apiKeyToken = apiKeyToken;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var requestApiKeyToken = httpRequest.getHeader(apiKeyHeader);
        if (Objects.equals(apiKeyToken, requestApiKeyToken)) {
            SecurityContextHolder.getContext().setAuthentication(new ApiKeyAuthentication(apiKeyToken, AuthorityUtils.NO_AUTHORITIES));
        }
        chain.doFilter(request, response);
    }
}
