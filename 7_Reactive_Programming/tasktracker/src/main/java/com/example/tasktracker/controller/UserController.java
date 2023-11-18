package com.example.tasktracker.controller;

import com.example.tasktracker.dto.user.UpsertUserRequest;
import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Flux<UserResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable String id) {
        return userService.findById(id)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> create(@Valid @RequestBody UpsertUserRequest request) {
        return userService
                .create(request)
                .map((UserResponse response) -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(response)
                );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable String id,
                                                     @RequestBody UpsertUserRequest request) {
        return userService.update(id, request)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return userService.deleteById(id)
                .map(ResponseEntity::ok);
    }
}
