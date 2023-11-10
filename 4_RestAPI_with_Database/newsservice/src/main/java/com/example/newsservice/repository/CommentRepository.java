package com.example.newsservice.repository;

import com.example.newsservice.model.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    long countByNewsId(Long id);

    @EntityGraph(attributePaths = {"user"})
    List<Comment> findByNewsId(Long id);

}
