package com.example.newsservice.service.impl;

import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.model.Comment;
import com.example.newsservice.repository.CommentRepository;
import com.example.newsservice.service.CommentService;
import com.example.newsservice.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;


    @Override
    public List<Comment> findByNewsId(Long id) {
        return commentRepository.findByNewsId(id);
    }


    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment update(Comment source) {
        Comment comment = commentRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("Comment not found when updating. ID: " + source.getId()));
        BeanUtils.copyNonNullProperties(source, comment);
        return commentRepository.save(comment);
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
    public Comment findById(Long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found. ID: " + id));
    }

    @Override
    public Long countByNewsId(Long id) {
        return commentRepository.countByNewsId(id);
    }
}
