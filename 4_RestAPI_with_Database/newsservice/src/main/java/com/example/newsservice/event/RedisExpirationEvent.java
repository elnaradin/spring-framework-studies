package com.example.newsservice.event;

import com.example.newsservice.model.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;

@Configuration
@Slf4j
@ConditionalOnProperty(prefix = "app", name = "authentication-type", havingValue = "jwt")
public class RedisExpirationEvent {
    @EventListener
    public void handleRedisKeyExpiredEvent(RedisKeyExpiredEvent<RefreshToken> event) {
        RefreshToken refreshToken = (RefreshToken) event.getValue();
        if (refreshToken == null) {
            throw new RuntimeException("Refresh token is null in handleRedisKeyExpiredEvent");
        }
        log.info("Refresh token with key={} has expired. Refresh token is: {}", refreshToken.getId(), refreshToken.getToken());
    }
}
