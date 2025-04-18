package br.dev.projects.myfinances_gateway.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("gateway/sessions")
@Profile("!disable-redis")
public class SessionController {

    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    public SessionController(ReactiveRedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/active")
    public Mono<Map<String, Object>> countSessions() {
        // Spring Session salva as chaves com prefixo: "spring:session:sessions:"
        return redisTemplate
                .keys("spring:session:sessions:*")
                .count()
                .map(count -> Map.of("activeSessions", count));
    }
}