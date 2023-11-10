package com.example.newsservice;

import com.example.newsservice.model.Category;
import com.example.newsservice.model.Comment;
import com.example.newsservice.model.News;
import com.example.newsservice.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class AbstractTestController {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    private final LocalDateTime someTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);


    protected News createNews(Long id, User author, List<Category> categoryList) {
        return News
                .builder()
                .id(id)
                .creationTime(someTime)
                .title("Title " + id)
                .text("Text " + id)
                .categories(categoryList)
                .author(author)
                .build();
    }

    protected Category createCategory(Long id) {
        return new Category(id, "Category " + id);
    }

    protected User createUser(Long id) {
        return User
                .builder()
                .id(id)
                .name("User " + id)
                .build();
    }

    protected Comment createComment(Long id, User user) {
        return Comment
                .builder()
                .id(id)
                .user(user)
                .creationTime(someTime)
                .text("Text " + id)
                .build();

    }
}
