package com.example.tasktracker.service;

import com.example.tasktracker.dto.user.UpsertUserRequest;
import com.example.tasktracker.dto.user.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Flux<UserResponse> findAll();

    Mono<UserResponse> findById(String id);

    Mono<UserResponse> create(UpsertUserRequest request);

    Mono<UserResponse> update(String id, UpsertUserRequest request);

    Mono<Void> deleteById(String id);
}
