package com.example.testdistancecalculato.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotDistanceException extends RuntimeException {

    public NotDistanceException(String message) {
        super(message);
    }

}
