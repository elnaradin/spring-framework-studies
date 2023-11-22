package com.example.tasktracker.service;

import com.example.tasktracker.aop.annotation.AccountOwnerVerifiable;
import com.example.tasktracker.dto.user.CreateUserRequest;
import com.example.tasktracker.dto.user.UpdateUserRequest;
import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.exception.DuplicateEntryException;
import com.example.tasktracker.exception.EntityNotFoundException;
import com.example.tasktracker.mapper.UserMapper;
import com.example.tasktracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Flux<UserResponse> findAll() {
        return userRepository.findAll()
                .map(userMapper::userToResponse);
    }

    @Override
    public Mono<UserResponse> findById(String id) {
        return userRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("user.findById", id)))
                .map(userMapper::userToResponse);
    }

    @Override
    public Mono<UserResponse> create(CreateUserRequest request) {
        User user = userMapper.requestToUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.existsByUserName(request.getUserName())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new DuplicateEntryException(
                                "user.nameAlreadyExists", request.getUserName()
                        ));
                    }
                    return userRepository.save(user).map(userMapper::userToResponse);
                });
    }


    @AccountOwnerVerifiable
    @Override
    public Mono<UserResponse> update(String id, UpdateUserRequest request) {
        return userRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("user.update", id)))
                .flatMap(user -> {
                    userMapper.update(request, user);
                    return userRepository
                            .save(user)
                            .map(userMapper::userToResponse);
                });
    }


    @AccountOwnerVerifiable
    @Override
    public Mono<Void> deleteById(String id) {
        return userRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("user.deleteById", id)))
                .flatMap(userRepository::delete);
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUserName(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(
                        MessageFormat.format("Username ''{0}'' not found", username
                        ))));
    }

}
