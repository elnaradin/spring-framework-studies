package com.example.bookservice.config;

import com.example.bookservice.config.properties.AppCacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@EnableConfigurationProperties(AppCacheProperties.class)
public class CacheConfiguration {
    @Bean
    public CacheManager redisCacheManager(AppCacheProperties properties,
                                          LettuceConnectionFactory factory) {

        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        properties.getCacheNames().forEach((cacheName) ->
                cacheConfigurations.put(cacheName, RedisCacheConfiguration.defaultCacheConfig().entryTtl(
                        properties.getCaches().get(cacheName).getExpiry()
                )));
        return RedisCacheManager
                .builder(factory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
