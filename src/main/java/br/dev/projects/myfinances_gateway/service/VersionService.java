package br.dev.projects.myfinances_gateway.service;

import br.dev.projects.myfinances_gateway.bean.Version;
import br.dev.projects.myfinances_gateway.exception.VersionNotFoundException;
import org.springframework.boot.info.GitProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

@Service
public class VersionService {

    public static final String NOT_FOUND_MESSAGE = "Versão não encontrada";

    private final GitProperties gitProperties;

    public VersionService(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    public Mono<Version> getVersion() {
        return just(new Version(gitProperties.getCommitId(), gitProperties.getCommitTime()));
    }

    public Mono<Version> getVersionByGitVersion(@PathVariable(value = "gitVersion") String gitVersion) {
        if(gitProperties.getCommitId().equals(gitVersion)) {
            return just(new Version(gitProperties.getCommitId(), gitProperties.getCommitTime()));
        }

        throw new VersionNotFoundException(NOT_FOUND_MESSAGE);
    }
}

