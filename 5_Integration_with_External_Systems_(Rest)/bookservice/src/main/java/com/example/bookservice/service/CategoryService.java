package com.example.bookservice.service;

import com.example.bookservice.entity.Category;

public interface CategoryService {
    Category findByNameOrCreate(String name);

}
