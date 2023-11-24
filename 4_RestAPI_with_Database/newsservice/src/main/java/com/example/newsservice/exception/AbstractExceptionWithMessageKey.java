package com.example.newsservice.exception;

import com.example.newsservice.exception.utils.ErrorMessagesUtils;

import java.text.MessageFormat;

public abstract class AbstractExceptionWithMessageKey extends RuntimeException {
    public AbstractExceptionWithMessageKey(String messageKey, Long id) {
        super(MessageFormat.format(ErrorMessagesUtils.getMessage(messageKey), id));
    }

    public AbstractExceptionWithMessageKey(String messageKey, String userName) {
        super(MessageFormat.format(ErrorMessagesUtils.getMessage(messageKey), userName));
    }
}
