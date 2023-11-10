package com.example.newsservice.service;

import com.example.newsservice.model.Comment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {


    List<Comment> findByNewsId(Long id);

    Comment save(Comment comment);

    Comment update(Comment comment);

    void deleteById(Long id);

    List<Comment> findAll(Pageable pageable);

    Comment findById(Long id);

    Long countByNewsId(Long id);
}
