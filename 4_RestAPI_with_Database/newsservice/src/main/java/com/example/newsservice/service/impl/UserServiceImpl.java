package com.example.newsservice.service.impl;

import com.example.newsservice.dto.user.UpsertUserRequest;
import com.example.newsservice.dto.user.UserListResponse;
import com.example.newsservice.dto.user.UserResponse;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.mapper.UserMapper;
import com.example.newsservice.model.User;
import com.example.newsservice.repository.UserRepository;
import com.example.newsservice.service.UserService;
import com.example.newsservice.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
    public UserResponse save(UpsertUserRequest request) {
        User user = userMapper.requestToUser(request);
        return userMapper.userToResponse(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserResponse update(String id, UpsertUserRequest request) {
        User source = userMapper.requestToUser(id, request);
        User user = userRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found when updating. ID: " + source.getId()));
        BeanUtils.copyNonNullProperties(source, user);
        return userMapper.userToResponse(userRepository.save(user));
    }

    @Override
    public boolean existsByUserAndNews(Long userId, Long newsId) {
        return userRepository.existsByIdAndNewsListId(userId, newsId);
    }

    @Override
    public boolean existsByUserAndComment(Long userId, Long commentId) {
        return userRepository.existsByIdAndCommentsId(userId, commentId);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
