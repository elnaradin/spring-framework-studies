package com.example.newsservice.service;


import com.example.newsservice.model.User;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface UserService {
    User findById(Long id);

    User save(User user);

    List<User> findAll(PageRequest of);

    void deleteById(Long id);

    User update(User user);

    boolean existsByUserAndNews(Long userId, Long newsId);

    boolean existsByUserAndComment(Long userId, Long commentId);
}
