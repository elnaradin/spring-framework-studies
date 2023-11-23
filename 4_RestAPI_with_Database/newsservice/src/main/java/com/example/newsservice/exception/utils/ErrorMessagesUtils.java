package com.example.newsservice.exception.utils;

import lombok.experimental.UtilityClass;

import java.util.ResourceBundle;

@UtilityClass
public class ErrorMessagesUtils {
    public String getMessage(String key) {
        return ResourceBundle.getBundle("messages_error").getString(key);
    }
}
