package com.example.newsservice.exception;

public class UserNotAuthorizedException extends AbstractExceptionWithMessageKey {


    public UserNotAuthorizedException(String messageKey, String username) {
        super(messageKey, username);
    }
}
