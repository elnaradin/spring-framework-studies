package com.example.bookservice.mapper;

import com.example.bookservice.dto.UpsertBookRequest;
import com.example.bookservice.entity.Book;
import com.example.bookservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

public abstract class BookMapperDecorator implements BookMapper {
    @Autowired
    private BookMapper delegate;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Book requestToBook(UpsertBookRequest request) {
        Book book = delegate.requestToBook(request);
        String categoryName = request.getCategoryName().trim().toLowerCase(Locale.ROOT);
        book.setCategory(categoryService.findByNameOrCreate(categoryName));
        return book;
    }

    @Override
    public void update(UpsertBookRequest request, Book book) {
        delegate.update(request, book);
        if (request.getCategoryName() != null) {
            book.setCategory(categoryService.findByNameOrCreate(request.getCategoryName()));
        }
    }
}
