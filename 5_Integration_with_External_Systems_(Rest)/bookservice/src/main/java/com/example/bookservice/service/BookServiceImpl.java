package com.example.bookservice.service;


import com.example.bookservice.dto.BookResponse;
import com.example.bookservice.dto.UpsertBookRequest;
import com.example.bookservice.entity.Book;
import com.example.bookservice.exception.DuplicateBookException;
import com.example.bookservice.exception.EntityNotFoundException;
import com.example.bookservice.mapper.BookMapper;
import com.example.bookservice.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

import static com.example.bookservice.config.properties.AppCacheProperties.CacheNames.BOOKS_BY_CATEGORY;
import static com.example.bookservice.config.properties.AppCacheProperties.CacheNames.BOOKS_BY_TITLE_AND_AUTHOR;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheManager = "redisCacheManager")
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CacheManager cacheManager;

    @Override
    @Cacheable(value = BOOKS_BY_TITLE_AND_AUTHOR, key = "#title + #authorName")
    public BookResponse findByTitleAndAuthor(String title, String authorName) {
        Book book = bookRepository.findByTitleIgnoreCaseAndAuthorNameIgnoreCase(title, authorName)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format(
                                "Book not found with title ''{0}'' and author ''{1}''",
                                title,
                                authorName
                        )));
        return bookMapper.bookToResponse(book);
    }

    @Override
    @Cacheable(value = BOOKS_BY_CATEGORY, key = "#categoryName")
    public List<BookResponse> findByCategory(String categoryName) {
        return bookMapper.bookToResponse(bookRepository.findByCategoryNameIgnoreCase(categoryName));
    }

    @Override
    @Transactional
    @CacheEvict(value = BOOKS_BY_TITLE_AND_AUTHOR, key = "#request.categoryName")
    public BookResponse create(UpsertBookRequest request) {
        Book book = bookMapper.requestToBook(request);
        if (bookRepository.existsByTitleAndAuthorName(book.getTitle(), book.getAuthorName())) {
            throw new DuplicateBookException(
                    MessageFormat.format(
                            "Book with title ''{0}'' and author ''{1}'' already exists. Unable to create.",
                            book.getTitle(),
                            book.getAuthorName()
                    ));
        }
        return bookMapper.bookToResponse(bookRepository.save(book));
    }

    @Override
    @Transactional
    @Caching(evict = {
            //evict new key value entries from cache if exist
            @CacheEvict(value = BOOKS_BY_CATEGORY, key = "#request.categoryName",
                    condition = "#request.categoryName != null"),
            @CacheEvict(value = BOOKS_BY_TITLE_AND_AUTHOR, key = "#request.title + #request.authorName",
                    condition = "#request.title != null && #request.authorName != null")
    })
    public BookResponse update(Long id, UpsertBookRequest request) {
        if ((request.getTitle() != null && request.getAuthorName() != null)
                && bookRepository.existsByTitleIgnoreCaseAndAuthorNameIgnoreCase(
                        request.getTitle().trim(), request.getAuthorName().trim()
        )) {
            throw new DuplicateBookException(MessageFormat.format(
                    "Book with title ''{0}'' and author ''{1}'' already exists. Unable to update.",
                    request.getTitle(),
                    request.getAuthorName()
            ));
        }
        Book book = bookRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Book not found when updating with ID ''{0}''", id)
                ));
        //evict old key value entries from cache
        Objects.requireNonNull(cacheManager.getCache(BOOKS_BY_CATEGORY)).evict(book.getCategory().getName());
        Objects.requireNonNull(cacheManager.getCache(BOOKS_BY_TITLE_AND_AUTHOR)).evict(book.getTitle() + book.getAuthorName());
        bookMapper.update(request, book);
        return bookMapper.bookToResponse(bookRepository.save(book));
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        bookRepository.findById(id).ifPresentOrElse((book) -> {
            bookRepository.delete(book);
            Objects.requireNonNull(cacheManager.getCache(BOOKS_BY_CATEGORY)).evict(book.getCategory().getName());
            Objects.requireNonNull(cacheManager.getCache(BOOKS_BY_TITLE_AND_AUTHOR)).evict(book.getTitle() + book.getAuthorName());
        }, () -> {
            throw new EntityNotFoundException(
                    MessageFormat.format("Book not found when deleting with ID ''{0}''", id)
            );
        });
    }
}
