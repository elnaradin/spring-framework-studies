package com.example.newsservice.repository;

import com.example.newsservice.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    List<RefreshToken> findByUserId(Long userId);

    Optional<RefreshToken> findByToken(String token);

}
