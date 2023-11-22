package com.example.tasktracker.exception;

import com.example.tasktracker.exception.utils.ErrorMessagesUtils;

import java.text.MessageFormat;

public abstract class AbstractExceptionWithMessageKey extends RuntimeException {
    public AbstractExceptionWithMessageKey(String messageKey, String id) {
        super(MessageFormat.format(ErrorMessagesUtils.getMessage(messageKey), id));
    }
}
