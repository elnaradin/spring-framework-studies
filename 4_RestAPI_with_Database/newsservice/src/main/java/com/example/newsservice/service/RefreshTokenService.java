package com.example.newsservice.service;

import com.example.newsservice.model.RefreshToken;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.Optional;

@ConditionalOnProperty(prefix = "app", name = "authentication-type", havingValue = "jwt")
public interface RefreshTokenService {
    Optional<RefreshToken> findByRefreshToken(String token);

    RefreshToken createRefreshToken(Long userid);

    RefreshToken checkRefreshToken(RefreshToken token);

    void deleteByUserId(Long userId);
}
