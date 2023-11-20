package com.example.newsservice.service.impl;

import com.example.newsservice.dto.user.CreateUserRequest;
import com.example.newsservice.dto.user.UpdateUserRequest;
import com.example.newsservice.dto.user.UserListResponse;
import com.example.newsservice.dto.user.UserResponse;
import com.example.newsservice.exception.DuplicateEntryException;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.mapper.UserMapper;
import com.example.newsservice.model.User;
import com.example.newsservice.repository.UserRepository;
import com.example.newsservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserListResponse findAll(PageRequest pageRequest) {
        Page<User> userPage = userRepository.findAll(pageRequest);
        return userMapper.userListToListResponse(userPage);
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found. ID: " + id));
        return userMapper.userToResponse(user);
    }

    @Override
    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByName(request.getName())) {
            throw new DuplicateEntryException(MessageFormat.format(
                    "User with name ''{0}'' already exists", request.getName()
            ));
        }
        User user = userMapper.requestToUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.userToResponse(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found when updating. ID: " + id));
        userMapper.update(id, request, user);
        return userMapper.userToResponse(userRepository.save(user));
    }

    @Override
    public boolean existsByUsernameAndNewsId(String username, Long newsId) {
        return userRepository.existsByNameAndNewsListId(username, newsId);
    }

    @Override
    public boolean existsByUsernameAndCommentId(String username, Long commentId) {
        return userRepository.existsByNameAndCommentsId(username, commentId);
    }

    @Override
    public boolean existsByUsernameAndId(String username, Long userId) {
        return userRepository.existsByNameAndId(username, userId);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }


}
