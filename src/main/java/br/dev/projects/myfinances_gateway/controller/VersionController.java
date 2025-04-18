package br.dev.projects.myfinances_gateway.controller;

import br.dev.projects.myfinances_gateway.bean.Version;
import br.dev.projects.myfinances_gateway.service.VersionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/gateway/version")
public class VersionController {

    private final VersionService versionService;

    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    @GetMapping
    public Mono<Version> getVersion() {
        return versionService.getVersion();
    }

    @GetMapping("/{gitVersion}")
    public Mono<Version> getVersionByGitVersion(@PathVariable(value = "gitVersion") String gitVersion) {
        return versionService.getVersionByGitVersion(gitVersion);
    }

}