package com.example.bookservice.repository;

import com.example.bookservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitleIgnoreCaseAndAuthorNameIgnoreCase(String title, String authorName);

    List<Book> findByCategoryNameIgnoreCase(String name);

    Optional<Book> findByTitleIgnoreCaseAndAuthorNameIgnoreCase(String title, String authorName);

    boolean existsByTitleAndAuthorName(String title, String authorName);

}
