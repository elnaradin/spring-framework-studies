package com.example.tasktracker.exception;

public class UserNotAuthorizedException extends AbstractExceptionWithMessageKey {

    public UserNotAuthorizedException(String messageKey, String id) {
        super(messageKey, id);
    }
}
