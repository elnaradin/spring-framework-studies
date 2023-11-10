package com.example.newsservice.service.impl;

import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.model.Category;
import com.example.newsservice.repository.CategoryRepository;
import com.example.newsservice.service.CategoryService;
import com.example.newsservice.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll(PageRequest pageRequest) {
        return categoryRepository.findAll(pageRequest).getContent();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found. ID: " + id));
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public Category update(Category source) {
        Category category = categoryRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found when updating. ID: " + source.getId()));
        BeanUtils.copyNonNullProperties(source, category);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> findByIdsIn(List<Long> categoryIds) {
        return categoryRepository.findAllById(categoryIds);
    }

    @Override
    public List<Category> findByNewsId(Long id) {
        return categoryRepository.findByNewsId(id);
    }

    @Override
    public List<String> getNamesByNewsId(Long id) {
        return categoryRepository.getNamesByNewsId(id);
    }
}
