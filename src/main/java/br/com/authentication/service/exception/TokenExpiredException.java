package br.com.authentication.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException() {
        super("Token expirado");
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
