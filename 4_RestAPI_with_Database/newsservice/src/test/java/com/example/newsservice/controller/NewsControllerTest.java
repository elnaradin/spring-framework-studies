package com.example.newsservice.controller;

import com.example.newsservice.AbstractTestController;
import com.example.newsservice.StringTestUtils;
import com.example.newsservice.dto.NewsFilter;
import com.example.newsservice.dto.news.UpsertNewsRequest;
import com.example.newsservice.model.Category;
import com.example.newsservice.model.Comment;
import com.example.newsservice.model.News;
import com.example.newsservice.model.User;
import com.example.newsservice.repository.CategoryRepository;
import com.example.newsservice.repository.CommentRepository;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NewsControllerTest extends AbstractTestController {
    @MockBean
    private NewsRepository newsRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private CommentRepository commentRepository;
    private User user;
    private Category category;
    private News news;
    private Comment comment;
    private UpsertNewsRequest request;
    private final String expectedResponse = StringTestUtils
            .readStringFromResource("response/news/single_object_response.json");
    private final String expectedResponse2 = StringTestUtils
            .readStringFromResource("response/news/multiple_objects_response.json");

    @BeforeEach
    void setUp() {
        user = createUser(1L);
        comment = createComment(1L, createUser(2L));
        category = createCategory(1L);
        news = createNews(1L, user, List.of(category));
        request = UpsertNewsRequest
                .builder()
                .text("Text 1")
                .title("Title 1")
                .authorId(1L)
                .categoryIds(List.of(1L))
                .build();
    }

    @AfterEach
    void tearDown() {
        user = null;
        comment = null;
        category = null;
        news = null;
        request = null;
    }

    @Test
    public void findById() throws Exception {
        when(newsRepository.findById(anyLong())).thenReturn(Optional.of(news));
        when(commentRepository.findByNewsId(anyLong())).thenReturn(List.of(comment));
        when(categoryRepository.findByNewsId(anyLong())).thenReturn(List.of(category));
        String actualResponse = mockMvc
                .perform(get("/api/v1/news/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(newsRepository, times(1)).findById(anyLong());
        verify(commentRepository, times(1)).findByNewsId(anyLong());
        verify(categoryRepository, times(1)).findByNewsId(anyLong());
        assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void findAll() throws Exception {
        NewsFilter newsFilter = NewsFilter
                .builder()
                .pageNumber(0)
                .pageSize(5)
                .build();
        when(newsRepository.findAll(ArgumentMatchers.<Specification<News>>any(), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(news)));
        when(commentRepository.countByNewsId(anyLong())).thenReturn(1L);
        when(categoryRepository.getNamesByNewsId(anyLong())).thenReturn(List.of(category.getName()));
        String actualResponse = mockMvc
                .perform(post("/api/v1/news/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsFilter)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(newsRepository, times(1)).findAll(ArgumentMatchers.<Specification<News>>any(), any(PageRequest.class));
        verify(commentRepository, times(1)).countByNewsId(anyLong());
        verify(categoryRepository, times(1)).getNamesByNewsId(anyLong());
        assertJsonEquals(expectedResponse2, actualResponse);
    }

    @Test
    public void update() throws Exception {
        when(userRepository.existsByIdAndNewsListId(anyLong(), anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(newsRepository.findById(anyLong())).thenReturn(Optional.of(news));
        when(newsRepository.save(any(News.class))).thenReturn(news);
        when(commentRepository.findByNewsId(anyLong())).thenReturn(List.of(comment));
        when(categoryRepository.findByNewsId(anyLong())).thenReturn(List.of(category));
        String actualResponse = mockMvc
                .perform(put("/api/v1/news/1")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(newsRepository, times(1)).findById(anyLong());
        verify(newsRepository, times(1)).save(any(News.class));
        verify(commentRepository, times(1)).findByNewsId(anyLong());
        verify(categoryRepository, times(1)).findByNewsId(anyLong());
        assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void updateFail() throws Exception {
        when(userRepository.existsByIdAndNewsListId(anyLong(), anyLong())).thenReturn(false);
        mockMvc
                .perform(put("/api/v1/news/1")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(newsRepository, times(0)).findById(anyLong());
        verify(newsRepository, times(0)).save(any(News.class));
        verify(commentRepository, times(0)).findByNewsId(anyLong());
    }

    @Test
    void create() throws Exception {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(categoryRepository.findAllById(anyIterable())).thenReturn(List.of(category));
        when(newsRepository.save(any(News.class))).thenReturn(news);
        when(commentRepository.findByNewsId(anyLong())).thenReturn(List.of(comment));
        when(categoryRepository.findByNewsId(anyLong())).thenReturn(List.of(category));
        String actualResponse = mockMvc
                .perform(post("/api/v1/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).findAllById(anyIterable());
        verify(newsRepository, times(1)).save(any(News.class));
        verify(commentRepository, times(1)).findByNewsId(anyLong());
        verify(categoryRepository, times(1)).findByNewsId(anyLong());
        assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    void deleteById() throws Exception {
        when(userRepository.existsByIdAndNewsListId(anyLong(), anyLong())).thenReturn(true);
        mockMvc
                .perform(delete("/api/v1/news/1")
                        .param("userId", "1"))
                .andExpect(status().isNoContent());
        verify(newsRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteByIdFail() throws Exception {
        when(userRepository.existsByIdAndNewsListId(anyLong(), anyLong())).thenReturn(false);
        mockMvc
                .perform(delete("/api/v1/news/1")
                        .param("userId", "1"))
                .andExpect(status().isForbidden());
        verify(newsRepository, times(0)).deleteById(anyLong());
    }
}
