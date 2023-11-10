package com.example.newsservice.controller;

import com.example.newsservice.AbstractTestController;
import com.example.newsservice.StringTestUtils;
import com.example.newsservice.dto.category.UpsertCategoryRequest;
import com.example.newsservice.dto.user.UpsertUserRequest;
import com.example.newsservice.model.Category;
import com.example.newsservice.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerTest extends AbstractTestController {
    @MockBean
    private CategoryRepository categoryRepository;
    private Category category;
    private final String expectedResponse = StringTestUtils
            .readStringFromResource("response/category/single_object_response.json");
    private final String expectedResponse2 = StringTestUtils
            .readStringFromResource("response/category/multiple_objects_response.json");

    @BeforeEach
    void setUp() {
        category = createCategory(1L);

    }

    @AfterEach
    void tearDown() {
        category = null;
    }

    @Test
    public void findAll() throws Exception {
        when(categoryRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(category)));
        String actualResponse = mockMvc
                .perform(get("/api/v1/category")
                        .param("pageNumber", "0")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(categoryRepository, times(1)).findAll(any(PageRequest.class));
        assertJsonEquals(expectedResponse2, actualResponse);
    }

    @Test
    public void findById() throws Exception {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        String actualResponse = mockMvc
                .perform(get("/api/v1/category/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(categoryRepository, times(1)).findById(anyLong());
        assertJsonEquals(expectedResponse, actualResponse);
    }


    @Test
    public void update() throws Exception {
        UpsertCategoryRequest request = new UpsertCategoryRequest("Category 1");
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        String actualResponse = mockMvc
                .perform(put("/api/v1/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(categoryRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).save(any(Category.class));
        assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    void create() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest("Category 1");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        String actualResponse = mockMvc
                .perform(post("/api/v1/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(categoryRepository, times(1)).save(any(Category.class));
        assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    void deleteById() throws Exception {
        mockMvc
                .perform(delete("/api/v1/category/1"))
                .andExpect(status().isNoContent());
        verify(categoryRepository, times(1)).deleteById(anyLong());
    }
}
