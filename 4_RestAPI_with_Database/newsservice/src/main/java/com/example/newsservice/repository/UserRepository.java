package com.example.newsservice.repository;

import com.example.newsservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByIdAndCommentsId(Long id, Long id1);

    @Query("select (count(u) > 0) from User u inner join u.newsList newsList where u.id = ?1 and newsList.id = ?2")
    boolean existsByIdAndNewsListId(Long id, Long id1);

}
