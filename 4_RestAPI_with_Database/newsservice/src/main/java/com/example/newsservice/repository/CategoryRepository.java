package com.example.newsservice.repository;

import com.example.newsservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from News n join n.categories c where n.id = ?1")
    List<Category> findByNewsId(Long id);

    @Query("select c.name from News n join n.categories c where n.id = ?1")
    List<String> getNamesByNewsId(Long id);
}
