package com.example.bookservice.service;

import com.example.bookservice.dto.BookResponse;
import com.example.bookservice.dto.UpsertBookRequest;

import java.util.List;

public interface BookService {
    BookResponse findByTitleAndAuthor(String title, String authorName);

    List<BookResponse> findByCategory(String categoryName);

    BookResponse create(UpsertBookRequest request);

    BookResponse update(Long id, UpsertBookRequest request);

    void deleteById(Long id);
}
