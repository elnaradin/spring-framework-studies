package com.example.newsservice.repository;

import com.example.newsservice.dto.user.NewsFilter;
import com.example.newsservice.model.Category;
import com.example.newsservice.model.News;
import com.example.newsservice.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public interface NewsSpecification {
    static Specification<News> withFilter(NewsFilter newsFilter) {
        return Specification.where(byCategory(newsFilter.getCategoryId()))
                .and(byAuthor(newsFilter.getAuthorId()));
    }

    static Specification<News> byCategory(Long categoryId) {
        return ((root, query, cb) -> {
            if (categoryId == null) {
                return null;
            }
            Join<News, Category> category = root.join(News.Fields.categories);
            return cb.equal(category.get("id"), categoryId);
        });
    }

    static Specification<News> byAuthor(Long authorId) {
        return ((root, query, cb) -> {
            if (authorId == null) {
                return null;
            }
            Join<News, User> author = root.join(News.Fields.author);
            return cb.equal(author.get("id"), authorId);
        });
    }
}
