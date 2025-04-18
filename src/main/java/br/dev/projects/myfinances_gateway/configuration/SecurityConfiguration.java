package br.dev.projects.myfinances_gateway.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestHandler;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new WebSessionServerOAuth2AuthorizedClientRepository();
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        // https://docs.spring.io/spring-security/reference/5.8-SNAPSHOT/migration/reactive.html#reactive-csrf-breach-opt-out
        CookieServerCsrfTokenRepository tokenRepository = new CookieServerCsrfTokenRepository();
        tokenRepository.setCookieCustomizer(cookie -> cookie.httpOnly(false).sameSite(Cookie.SameSite.LAX.attributeValue()));
        XorServerCsrfTokenRequestAttributeHandler delegate = new XorServerCsrfTokenRequestAttributeHandler();
        ServerCsrfTokenRequestHandler requestHandler = delegate::handle;

        return http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(tokenRepository)
                        .csrfTokenRequestHandler(requestHandler)
                )
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/app/manifest.json", "/app/api/version/**", "/api/**", "/gateway/version/**")
                        .permitAll()
                        .anyExchange()
                        .authenticated()
                )
                .oauth2Login(login -> login
                        .authorizedClientRepository(authorizedClientRepository())
                        .authorizationRequestResolver(new CustomAuthorizationRequestResolver(clientRegistrationRepository)
                ))
                .build();
    }

    @Bean
    WebFilter csrfCookieWebFilter() {
        return (exchange, chain) -> {
            Mono<CsrfToken> csrfToken = exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty());
            return csrfToken.doOnSuccess(token -> {}).then(chain.filter(exchange));
        };
    }
}