package com.okbut.error.expcetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class UserNotFoundException extends HttpClientErrorException {
    public UserNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public UserNotFoundException(Long id) {
        this("사용자를 찾을 수 없습니다: " + id);
    }
}
