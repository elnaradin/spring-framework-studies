package com.example.newsservice.mapper;


import com.example.newsservice.dto.category.CategoryListResponse;
import com.example.newsservice.dto.category.CategoryResponse;
import com.example.newsservice.dto.category.UpsertCategoryRequest;
import com.example.newsservice.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    CategoryResponse categoryToResponse(Category category);

    Category requestToCategory(UpsertCategoryRequest request);

    List<CategoryResponse> categoryToResponse(List<Category> categories);

    void update(Long id, UpsertCategoryRequest request, @MappingTarget Category category);

    default CategoryListResponse categoryListToListResponse(Page<Category> categories) {
        List<CategoryResponse> categoryResponses = categoryToResponse(categories.getContent());
        return new CategoryListResponse(categories.getTotalElements(), categoryResponses);
    }

}
