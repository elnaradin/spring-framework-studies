package com.example.newsservice.controller;

import com.example.newsservice.AbstractTestController;
import com.example.newsservice.StringTestUtils;
import com.example.newsservice.dto.comment.InsertCommentRequest;
import com.example.newsservice.model.Comment;
import com.example.newsservice.model.News;
import com.example.newsservice.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends AbstractTestController {
    private Comment comment;
    private User user;
    private News news;
    private InsertCommentRequest request;
    private final String expectedResponse = StringTestUtils
            .readStringFromResource("response/comment/single_object_response.json");
    private final String expectedResponse2 = StringTestUtils
            .readStringFromResource("response/comment/multiple_objects_response.json");

    @BeforeEach
    void setUp() {
        user = createUser(1L);
        comment = createComment(1L, user);
        request = new InsertCommentRequest("Text 1", 1L, 1L);
        news = createNews(1L, user, new ArrayList<>());
    }

    @AfterEach
    void tearDown() {
        comment = null;
        user = null;
        request = null;
        news = null;
    }

    @Test
    public void findById() throws Exception {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        String actualResponse = mockMvc
                .perform(get("/api/v1/comment/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(commentRepository, times(1)).findById(anyLong());
        assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void findAllByNewsId() throws Exception {
        when(commentRepository.findByNewsId(anyLong())).thenReturn(List.of(comment));
        String actualResponse = mockMvc
                .perform(get("/api/v1/comment/byNews/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(commentRepository, times(1)).findByNewsId(anyLong());
        assertJsonEquals(expectedResponse2, actualResponse);
    }


    @Test
    public void update() throws Exception {
        when(userRepository.existsByIdAndCommentsId(anyLong(), anyLong())).thenReturn(true);
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        String actualResponse = mockMvc
                .perform(put("/api/v1/comment/1")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(commentRepository, times(1)).findById(anyLong());
        verify(commentRepository, times(1)).save(any(Comment.class));
        assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void updateFail() throws Exception {
        when(userRepository.existsByIdAndCommentsId(anyLong(), anyLong())).thenReturn(false);
        mockMvc
                .perform(put("/api/v1/comment/1")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(commentRepository, times(0)).findById(anyLong());
        verify(commentRepository, times(0)).save(any(Comment.class));
    }

    @Test
    void create() throws Exception {
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(newsRepository.findById(anyLong())).thenReturn(Optional.of(news));
        String actualResponse = mockMvc
                .perform(post("/api/v1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(userRepository, times(1)).findById(anyLong());
        verify(newsRepository, times(1)).findById(anyLong());
        assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    void deleteById() throws Exception {
        when(userRepository.existsByIdAndCommentsId(anyLong(), anyLong())).thenReturn(true);
        mockMvc
                .perform(delete("/api/v1/comment/1")
                        .param("userId", "1"))
                .andExpect(status().isNoContent());
        verify(commentRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteByIdFail() throws Exception {
        when(userRepository.existsByIdAndCommentsId(anyLong(), anyLong())).thenReturn(false);
        mockMvc
                .perform(delete("/api/v1/comment/1")
                        .param("userId", "1"))
                .andExpect(status().isForbidden());
        verify(commentRepository, times(0)).deleteById(anyLong());
    }
}
