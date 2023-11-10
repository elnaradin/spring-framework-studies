package com.example.newsservice.service;

import com.example.newsservice.model.Category;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CategoryService {
    List<Category> findAll(PageRequest of);

    Category findById(Long id);

    Category save(Category category);

    Category update(Category category);

    void deleteById(Long id);

    List<Category> findByIdsIn(List<Long> categoryIds);

    List<Category> findByNewsId(Long id);

    List<String> getNamesByNewsId(Long id);
}
