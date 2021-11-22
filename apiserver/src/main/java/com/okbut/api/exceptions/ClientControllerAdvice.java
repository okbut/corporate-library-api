package com.okbut.api.exceptions;

import com.okbut.error.ErrorResponse;
import com.okbut.error.expcetion.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClientControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            UserNotFoundException.class,
    })
    public ErrorResponse handleNotFound(final RuntimeException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
