package com.docubot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409
public class GoogleAccountOnlyException extends RuntimeException {
    public GoogleAccountOnlyException(String message) {
        super(message);
    }
    public GoogleAccountOnlyException(String message, Throwable cause) {
        super(message, cause);
    }
}
