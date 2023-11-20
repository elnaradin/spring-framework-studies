package com.example.newsservice.repository;

import com.example.newsservice.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNameAndId(String name, Long id);

    boolean existsByName(String name);

    @Query("select (count(u) > 0) from User u inner join u.comments comments where u.name = ?1 and comments.id = ?2")
    boolean existsByNameAndCommentsId(String name, Long id1);

    @Query("select (count(u) > 0) from User u inner join u.newsList newsList where u.id = ?1 and newsList.id = ?2")
    boolean existsByNameAndNewsListId(String name, Long id1);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByName(String name);

}
