package br.dev.projects.myfinances_gateway.configuration;

import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

public class CustomAuthorizationRequestResolver implements org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver {

    private final org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver defaultResolver;

    public CustomAuthorizationRequestResolver(ReactiveClientRegistrationRepository repo) {
        this.defaultResolver = new org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver(repo);
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
        return defaultResolver.resolve(exchange)
                .map(this::customize);
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId) {
        return defaultResolver.resolve(exchange, clientRegistrationId)
                .map(this::customize);
    }

    private OAuth2AuthorizationRequest customize(OAuth2AuthorizationRequest request) {
        String updatedUri = UriComponentsBuilder
                .fromUriString(request.getAuthorizationRequestUri())
                .queryParam("lang", "pt-BR")
                .build(true)
                .toUriString();

        return OAuth2AuthorizationRequest.from(request)
                .authorizationRequestUri(updatedUri)
                .build();
    }
}
