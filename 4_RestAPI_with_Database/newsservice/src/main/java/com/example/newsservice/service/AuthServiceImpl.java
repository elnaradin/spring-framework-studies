package com.example.newsservice.service;

import com.example.newsservice.dto.auth.AuthResponse;
import com.example.newsservice.dto.auth.LoginRequest;
import com.example.newsservice.dto.auth.RefreshTokenRequest;
import com.example.newsservice.dto.auth.RefreshTokenResponse;
import com.example.newsservice.exception.RefreshTokenException;
import com.example.newsservice.model.RefreshToken;
import com.example.newsservice.model.User;
import com.example.newsservice.repository.UserRepository;
import com.example.newsservice.security.AppUserDetails;
import com.example.newsservice.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "authentication-type", havingValue = "jwt")
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponse signIn(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails principal = (AppUserDetails) authentication.getPrincipal();
        List<String> roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(principal.getId());
        return AuthResponse.builder()
                .refreshToken(refreshToken.getToken())
                .token(jwtUtils.generateJwtToken(principal))
                .userName(principal.getUsername())
                .roles(roles)
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getUserId)
                .map(userId -> {
                    User tokenOwner = userRepository.findById(userId)
                            .orElseThrow(() -> new RefreshTokenException("Failed to find user by ID: " + userId));

                    String token = jwtUtils.generateTokenFromUsername(tokenOwner.getName());
                    return new RefreshTokenResponse(token, refreshTokenService.createRefreshToken(userId).getToken());
                }).orElseThrow(() -> new RefreshTokenException(requestRefreshToken, "Refresh token not found"));
    }

    @Override
    public void logout() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AppUserDetails) {
            refreshTokenService.deleteByUserId(((AppUserDetails) principal).getId());

        }
    }
}
