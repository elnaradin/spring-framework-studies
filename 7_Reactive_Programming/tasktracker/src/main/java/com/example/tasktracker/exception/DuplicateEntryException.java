package com.example.tasktracker.exception;

public class DuplicateEntryException extends AbstractExceptionWithMessageKey {

    public DuplicateEntryException(String messageKey, String id) {
        super(messageKey, id);
    }
}
