package br.dev.projects.myfinances_gateway.service;

import br.dev.projects.myfinances_gateway.bean.Version;
import br.dev.projects.myfinances_gateway.exception.VersionNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

@Service
public class VersionService {

    private final BuildProperties buildProperties;

    public static final String NOT_FOUND_MESSAGE = "Versão não encontrada";

    @Value("${SOURCE_COMMIT:0.0.0}")
    private String version;

    public VersionService(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    public Mono<Version> getVersion() {
        return just(new Version(version, buildProperties.getTime()));
    }

    public Mono<Version> getVersionByGitVersion(@PathVariable(value = "gitVersion") String gitVersion) {
        if(version.equals(gitVersion)) {
            return just(new Version(version, buildProperties.getTime()));
        }

        throw new VersionNotFoundException(NOT_FOUND_MESSAGE);
    }
}

