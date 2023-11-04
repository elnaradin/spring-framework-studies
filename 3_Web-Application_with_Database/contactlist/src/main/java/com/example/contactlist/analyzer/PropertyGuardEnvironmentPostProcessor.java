package com.example.contactlist.analyzer;

import com.example.contactlist.exception.RepositoryTypePropertyException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.text.MessageFormat;

public class PropertyGuardEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String repositoryType = environment.getProperty("repository-impl.type");
        if (repositoryType == null || (!repositoryType.equals("jooq") && !repositoryType.equals("jdbc"))) {
            throw new RepositoryTypePropertyException(MessageFormat.format("repository type {0} doesn''t exist", repositoryType));
        }
    }
}
