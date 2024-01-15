package fpt.gymmanagement.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomValidationException extends ResponseStatusException {
    public CustomValidationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}