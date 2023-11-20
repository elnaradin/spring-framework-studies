package com.example.newsservice.service;

import com.example.newsservice.dto.category.CategoryListResponse;
import com.example.newsservice.dto.category.CategoryResponse;
import com.example.newsservice.dto.category.UpsertCategoryRequest;
import com.example.newsservice.model.Category;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CategoryService {
    CategoryListResponse findAll(PageRequest of);

    CategoryResponse findById(Long id);

    CategoryResponse save(UpsertCategoryRequest category);

    CategoryResponse update(String id, UpsertCategoryRequest request);

    void deleteById(Long id);

    List<Category> findByIdsIn(List<Long> categoryIds);

    List<CategoryResponse> findByNewsId(Long id);

    List<String> getNamesByNewsId(Long id);
}
