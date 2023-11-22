package com.example.tasktracker.service;

import com.example.tasktracker.dto.user.CreateUserRequest;
import com.example.tasktracker.dto.user.UpdateUserRequest;
import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Flux<UserResponse> findAll();

    Mono<UserResponse> findById(String id);

    Mono<UserResponse> create(CreateUserRequest request);

    Mono<UserResponse> update(String id, UpdateUserRequest request);

    Mono<Void> deleteById(String id);

    Mono<User> findByUsername(String username);

}
