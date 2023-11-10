package com.example.newsservice.repository;

import com.example.newsservice.model.News;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
    @EntityGraph(attributePaths = {News.Fields.author, News.Fields.categories})
    @Override
    Optional<News> findById(Long aLong);
}
