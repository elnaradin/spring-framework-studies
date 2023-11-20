package com.example.newsservice.service;

import com.example.newsservice.dto.category.CategoryListResponse;
import com.example.newsservice.dto.category.CategoryResponse;
import com.example.newsservice.dto.category.UpsertCategoryRequest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CategoryService {
    CategoryListResponse findAll(PageRequest of);

    CategoryResponse findById(Long id);

    CategoryResponse create(UpsertCategoryRequest category);

    CategoryResponse update(Long id, UpsertCategoryRequest request);

    void deleteById(Long id);

    List<CategoryResponse> findByNewsId(Long id);

    List<String> getNamesByNewsId(Long id);
}
