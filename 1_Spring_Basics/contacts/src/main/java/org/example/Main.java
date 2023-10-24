package org.example;

import org.example.config.DefaultAppConfig;
import org.example.console.Console;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(DefaultAppConfig.class);
        context.getBean(Console.class).start();
    }
}