package com.example.newsservice.exception;

public class EntityNotFoundException extends AbstractExceptionWithMessageKey {


    public EntityNotFoundException(String messageKey, Long id) {
        super(messageKey, id);
    }
}
