package com.example.newsservice.service.impl;

import com.example.newsservice.dto.category.CategoryListResponse;
import com.example.newsservice.dto.category.CategoryResponse;
import com.example.newsservice.dto.category.UpsertCategoryRequest;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.mapper.CategoryMapper;
import com.example.newsservice.model.Category;
import com.example.newsservice.repository.CategoryRepository;
import com.example.newsservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryListResponse findAll(PageRequest pageRequest) {
        Page<Category> categoryPage = categoryRepository.findAll(pageRequest);
        return categoryMapper.categoryListToListResponse(categoryPage);
    }

    @Override
    public CategoryResponse findById(Long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("category.findById", id));
        return categoryMapper.categoryToResponse(category);
    }

    @Override
    public CategoryResponse create(UpsertCategoryRequest request) {
        Category category = categoryMapper.requestToCategory(request);
        return categoryMapper.categoryToResponse(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public CategoryResponse update(Long id, UpsertCategoryRequest request) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("category.update", id));
        categoryMapper.update(id, request, category);
        return categoryMapper.categoryToResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryResponse> findByNewsId(Long id) {
        return categoryMapper.categoryToResponse(categoryRepository.findByNewsId(id));
    }

    @Override
    public List<String> getNamesByNewsId(Long id) {
        return categoryRepository.getNamesByNewsId(id);
    }
}
