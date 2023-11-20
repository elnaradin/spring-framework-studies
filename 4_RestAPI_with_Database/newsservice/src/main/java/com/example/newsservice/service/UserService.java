package com.example.newsservice.service;


import com.example.newsservice.dto.user.UpsertUserRequest;
import com.example.newsservice.dto.user.UserListResponse;
import com.example.newsservice.dto.user.UserResponse;
import org.springframework.data.domain.PageRequest;

public interface UserService {
    UserResponse findById(Long id);

    UserResponse save(UpsertUserRequest user);

    UserListResponse findAll(PageRequest of);

    void deleteById(Long id);

    UserResponse update(String id, UpsertUserRequest request);

    boolean existsByUserAndNews(Long userId, Long newsId);

    boolean existsByUserAndComment(Long userId, Long commentId);
}
