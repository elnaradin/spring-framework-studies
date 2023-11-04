package com.example.contactlist.analyzer;

import com.example.contactlist.exception.RepositoryTypePropertyException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

import java.text.MessageFormat;

public class RepositoryTypePropertyFailureAnalyser extends AbstractFailureAnalyzer<RepositoryTypePropertyException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, RepositoryTypePropertyException cause) {
        return new FailureAnalysis(MessageFormat.format("Exception when trying to set property: {0}", cause.getMessage()), "Type must be 'jdbc' or 'jooq'", cause);
    }
}
