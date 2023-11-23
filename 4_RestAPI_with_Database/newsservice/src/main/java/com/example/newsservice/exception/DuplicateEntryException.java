package com.example.newsservice.exception;

public class DuplicateEntryException extends AbstractExceptionWithMessageKey {


    public DuplicateEntryException(String messageKey, String userName) {
        super(messageKey, userName);
    }
}
