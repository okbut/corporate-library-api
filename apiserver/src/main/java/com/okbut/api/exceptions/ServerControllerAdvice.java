package com.okbut.api.exceptions;

import com.okbut.error.ErrorResponse;
import com.okbut.error.expcetion.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServerControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({
            RuntimeException.class,
    })
    public ErrorResponse handleInternalServerError(final RuntimeException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
