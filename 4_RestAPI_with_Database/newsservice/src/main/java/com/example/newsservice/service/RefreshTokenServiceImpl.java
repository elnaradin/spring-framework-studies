package com.example.newsservice.service;

import com.example.newsservice.exception.RefreshTokenException;
import com.example.newsservice.model.RefreshToken;
import com.example.newsservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "authentication-type", havingValue = "jwt")
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${app.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Optional<RefreshToken> findByRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(Long userid) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userid)
                .token(UUID.randomUUID().toString())
                .expireDate(Instant.now().plusMillis(refreshTokenExpiration.toMillis()))
                .build();
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken checkRefreshToken(RefreshToken token) {
        if (token.getExpireDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(token.getToken(), "Refresh token was expired. Repeat sign in action");
        }
        return token;
    }

    @Override
    public void deleteByUserId(Long userId) {
        List<RefreshToken> tokens = refreshTokenRepository.findByUserId(userId);
        refreshTokenRepository.deleteAll(tokens);
    }
}
