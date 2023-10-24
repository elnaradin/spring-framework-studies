package org.example.config;

import org.example.repository.ContactRepository;
import org.example.repository.InMemoryContactRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("org.example")
@PropertySource("classpath:application.properties")
public class DefaultAppConfig {
    @Bean
    public ContactRepository inMemoryContactRepository() {
        return new InMemoryContactRepository();
    }
}
