package br.dev.projects.myfinances_gateway.bean;

import java.time.Instant;

public record Version(String gitVersion, Instant buildDateTime) {
}
