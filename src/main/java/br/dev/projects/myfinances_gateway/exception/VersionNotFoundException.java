package br.dev.projects.myfinances_gateway.exception;

public class VersionNotFoundException extends RuntimeException {
    public VersionNotFoundException(String message) {
        super(message);
    }
}

