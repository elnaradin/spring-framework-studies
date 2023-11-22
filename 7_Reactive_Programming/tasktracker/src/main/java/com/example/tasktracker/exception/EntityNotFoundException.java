package com.example.tasktracker.exception;

public class EntityNotFoundException extends AbstractExceptionWithMessageKey {

    public EntityNotFoundException(String messageKey, String id) {
        super(messageKey, id);
    }
}
