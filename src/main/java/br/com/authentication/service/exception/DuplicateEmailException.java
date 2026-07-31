package br.com.authentication.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException() {
        super("Este e-mail já está em uso.");
    }

    public DuplicateEmailException(String message) {
        super(message);
    }
}
