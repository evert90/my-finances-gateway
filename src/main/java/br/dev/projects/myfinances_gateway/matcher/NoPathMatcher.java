package br.dev.projects.myfinances_gateway.matcher;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NoPathMatcher implements ServerWebExchangeMatcher {

    private final List<PathPattern> excludedPatterns;

    public NoPathMatcher(String... patterns) {
        PathPatternParser parser = new PathPatternParser();
        this.excludedPatterns = Stream.of(patterns)
                .map(parser::parse)
                .collect(Collectors.toList());
    }

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        PathContainer path = exchange.getRequest().getPath().pathWithinApplication();
        HttpMethod method = exchange.getRequest().getMethod();

        // Aplica CSRF apenas para métodos mutáveis (POST, PUT, PATCH, DELETE)
        if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH || method == HttpMethod.DELETE) {
            // Se o método for mutável e o path não for um dos excluídos, aplica CSRF
            boolean match = excludedPatterns.stream()
                    .noneMatch(pattern -> pattern.matches(path));
            return match ? MatchResult.match() : MatchResult.notMatch();
        }

        // Para métodos GET, HEAD, OPTIONS, não aplica CSRF
        return MatchResult.notMatch();
    }
}
