package com.pluralsight.springapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NotInOrderException extends RuntimeException {

    public NotInOrderException(String message) {
        super(message);
    }

}
