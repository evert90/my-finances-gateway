package br.dev.projects.myfinances_gateway.configuration;

import br.dev.projects.myfinances_gateway.bean.ErrorMessage;
import br.dev.projects.myfinances_gateway.exception.VersionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionsHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            VersionNotFoundException.class
    })
    @ResponseBody
    ErrorMessage notFound(Exception e) {
        return new ErrorMessage(e.getMessage());
    }
}
