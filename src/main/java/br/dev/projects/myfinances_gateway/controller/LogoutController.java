package br.dev.projects.myfinances_gateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
public class LogoutController {

    @Value("${IDP_CLIENT_ID}")
    private String idpClientId;

    @Value("${IDP_URL}")
    private String idpUrl;

    @Value("${APP_URL}")
    private String app_url;

    private final SecurityContextServerLogoutHandler logoutHandler = new SecurityContextServerLogoutHandler();

    @GetMapping("/gateway/logout")
    public Mono<Void> logout(ServerWebExchange exchange, Authentication authentication, WebSession session) {
        session.invalidate();
        String targetUrl = UriComponentsBuilder
                .fromUri(URI.create(idpUrl + "/v2/logout"))
                .queryParam("client_id", idpClientId)
                .queryParam("returnTo", app_url)
                .encode(UTF_8)
                .build()
                .toUriString();

        WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, (e) -> Mono.empty());

        return logoutHandler.logout(webFilterExchange, authentication)
                .then(Mono.fromRunnable(() -> {
                    exchange.getResponse().setStatusCode(HttpStatus.FOUND);
                    exchange.getResponse().getHeaders().setLocation(URI.create(targetUrl));
                }))
                .then(exchange.getResponse().setComplete());
    }
}
