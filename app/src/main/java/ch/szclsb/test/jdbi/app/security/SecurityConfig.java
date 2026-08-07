package ch.szclsb.test.jdbi.app.security;

import ch.szclsb.test.jdbi.app.configuration.SecurityConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final SecurityConfigProperties securityConfigProperties;

    public SecurityConfig(SecurityConfigProperties securityConfigProperties) {
        this.securityConfigProperties = securityConfigProperties;
        log.info("ApiKey header: {}", securityConfigProperties.getApiKeyHeader());
        if (securityConfigProperties.getApiKeyToken() != null) {
            log.info("ApiKey token: ****");
        }
        log.info("CORS domains: {}", String.join(", ", securityConfigProperties.getCorsDomains()));
        log.info("CORS methods: {}", String.join(", ", securityConfigProperties.getCorsMethods()));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        var apiKeyAuthenticationFilter = new ApiKeyAuthenticationFilter(
                securityConfigProperties.getApiKeyHeader(),
                securityConfigProperties.getApiKeyToken()
        );
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList(securityConfigProperties.getCorsDomains()));
        corsConfiguration.setAllowedMethods(Arrays.asList(securityConfigProperties.getCorsMethods()));

        http.addFilterBefore(apiKeyAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(c -> c
                                .requestMatchers(
                                        PathPatternRequestMatcher.pathPattern("/public/**")
                                ).permitAll()
                                .anyRequest().authenticated()
//                                .requestMatchers(
//                                        PathPatternRequestMatcher.pathPattern("/api/**"),
//                                        PathPatternRequestMatcher.pathPattern("/test/**")
//                                ).authenticated()
//                                .anyRequest().denyAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> {
                    var source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", corsConfiguration);
                    c.configurationSource(source);
                });

        return http.build();
    }
}
