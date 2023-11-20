package com.example.newsservice.service;


import com.example.newsservice.dto.user.CreateUserRequest;
import com.example.newsservice.dto.user.UpdateUserRequest;
import com.example.newsservice.dto.user.UserListResponse;
import com.example.newsservice.dto.user.UserResponse;
import com.example.newsservice.model.User;
import org.springframework.data.domain.PageRequest;

public interface UserService {
    UserResponse findById(Long id);

    UserResponse create(CreateUserRequest user);

    UserListResponse findAll(PageRequest of);

    void deleteById(Long id);

    UserResponse update(Long id, UpdateUserRequest request);

    boolean existsByUsernameAndNewsId(String username, Long newsId);

    boolean existsByUsernameAndCommentId(String username, Long commentId);

    User findByUsername(String username);

    boolean existsByUsernameAndId(String username, Long userId);
}
