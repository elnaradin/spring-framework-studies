package com.example.newsservice.service;

import com.example.newsservice.dto.auth.AuthResponse;
import com.example.newsservice.dto.auth.LoginRequest;
import com.example.newsservice.dto.auth.RefreshTokenRequest;
import com.example.newsservice.dto.auth.RefreshTokenResponse;

public interface AuthService {
    //   UserResponse register(CreateUserRequest request);
    AuthResponse signIn(LoginRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void logout();
}
