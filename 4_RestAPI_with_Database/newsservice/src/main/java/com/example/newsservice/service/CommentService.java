package com.example.newsservice.service;

import com.example.newsservice.dto.comment.CommentResponse;
import com.example.newsservice.dto.comment.CreateCommentRequest;
import com.example.newsservice.dto.comment.UpdateCommentRequest;
import com.example.newsservice.model.Comment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {


    List<CommentResponse> findByNewsId(Long id);

    CommentResponse create(CreateCommentRequest comment);

    CommentResponse update(Long id, UpdateCommentRequest request);

    void deleteById(Long id);

    List<Comment> findAll(Pageable pageable);

    CommentResponse findById(Long id);

    Long countByNewsId(Long id);
}
