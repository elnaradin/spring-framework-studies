package com.example.tasktracker.service;

import com.example.tasktracker.dto.user.UpsertUserRequest;
import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.exception.EntityNotFoundException;
import com.example.tasktracker.mapper.UserMapper;
import com.example.tasktracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Flux<UserResponse> findAll() {
        return userRepository.findAll()
                .map(userMapper::userToResponse);
    }

    @Override
    public Mono<UserResponse> findById(String id) {
        return userRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found with id:" + id)))
                .map(userMapper::userToResponse);
    }

    @Override
    public Mono<UserResponse> create(UpsertUserRequest request) {
        Mono<User> userMono = userRepository.save(userMapper.requestToUser(request));
        return userMono.map(userMapper::userToResponse);
    }

    @Override
    public Mono<UserResponse> update(String id, UpsertUserRequest request) {
        return userRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found when updating with id: " + id)))
                .flatMap(user -> {
                    userMapper.update(request, user);
                    return userRepository
                            .save(user)
                            .map(userMapper::userToResponse);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("User not found when deleting with id: " + id)))
                .flatMap(userRepository::delete);
    }
}
