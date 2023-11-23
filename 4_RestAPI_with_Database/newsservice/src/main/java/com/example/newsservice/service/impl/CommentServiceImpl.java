package com.example.newsservice.service.impl;

import com.example.newsservice.dto.comment.CommentResponse;
import com.example.newsservice.dto.comment.CreateCommentRequest;
import com.example.newsservice.dto.comment.UpdateCommentRequest;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.mapper.CommentMapper;
import com.example.newsservice.model.Comment;
import com.example.newsservice.repository.CommentRepository;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.UserRepository;
import com.example.newsservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    @Override
    public List<CommentResponse> findByNewsId(Long id) {
        return commentMapper.commentToResponse(commentRepository.findByNewsId(id));
    }


    @Transactional
    @Override
    public CommentResponse create(CreateCommentRequest request) {
        Comment comment = commentMapper.requestToComment(request);
        comment.setNews(newsRepository.findById(request.getNewsId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "comment.create.newsId", request.getNewsId()
                )));
        comment.setUser(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "comment.create.userId", request.getNewsId()
                )));
        return commentMapper.commentToResponse(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentResponse update(Long id, UpdateCommentRequest request) {
        Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("comment.update", id));
        commentMapper.update(id, request, comment);
        return commentMapper.commentToResponse(commentRepository.save(comment));
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable).getContent();
    }

    @Override
    public CommentResponse findById(Long id) {
        Comment comment = commentRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("comments.findById", id));
        return commentMapper.commentToResponse(comment);
    }

    @Override
    public Long countByNewsId(Long id) {
        return commentRepository.countByNewsId(id);
    }
}
