package org.example.config;

import org.example.repository.FileContactRepository;
import org.example.repository.ContactRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("org.example")
@PropertySource("classpath:application-init.properties")
@Profile("init")
public class InitFileRepositoryAppConfig {
    @Bean
    @Primary
    public ContactRepository initContactRepository() {
        return new FileContactRepository();
    }
}
