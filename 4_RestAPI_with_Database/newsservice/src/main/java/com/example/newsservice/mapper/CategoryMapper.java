package com.example.newsservice.mapper;


import com.example.newsservice.dto.category.CategoryListResponse;
import com.example.newsservice.dto.category.CategoryResponse;
import com.example.newsservice.dto.category.UpsertCategoryRequest;
import com.example.newsservice.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    CategoryResponse categoryToResponse(Category category);

    Category requestToCategory(UpsertCategoryRequest request);

    Category requestToCategory(String id, UpsertCategoryRequest request);

    List<CategoryResponse> categoryToResponse(List<Category> categories);

    default CategoryListResponse categoryListToListResponse(List<Category> categories) {
        List<CategoryResponse> categoryResponses = categoryToResponse(categories);
        return new CategoryListResponse(categoryResponses);
    }

}
