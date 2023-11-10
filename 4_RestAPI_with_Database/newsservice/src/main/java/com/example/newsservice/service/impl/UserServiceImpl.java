package com.example.newsservice.service.impl;

import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.model.User;
import com.example.newsservice.repository.UserRepository;
import com.example.newsservice.service.UserService;
import com.example.newsservice.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> findAll(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest).getContent();
    }

    @Override
    public User findById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found. ID: " + id));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(User source) {
        User user = userRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found when updating. ID: " + source.getId()));
        BeanUtils.copyNonNullProperties(source, user);
        return userRepository.save(user);
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
