package com.example.studentrecorder.config;

import com.example.studentrecorder.config.properties.InitDataProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(InitDataProperties.class)
@ConditionalOnProperty("sample-data.enabled")
public class DefaultConfig {
}
