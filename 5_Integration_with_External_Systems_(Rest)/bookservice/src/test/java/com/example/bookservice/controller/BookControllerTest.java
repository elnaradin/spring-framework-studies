package com.example.bookservice.controller;

import com.example.bookservice.BookServiceAbstractTests;
import com.example.bookservice.StringTestUtils;
import com.example.bookservice.dto.UpsertBookRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookControllerTest extends BookServiceAbstractTests {
    private static final String GET_BOOK_RESPONSE = StringTestUtils.readStringFromResource("get_book_response.json");
    private static final String INSERT_BOOK_RESPONSE = StringTestUtils.readStringFromResource("insert_book_response.json");
    private static final String UPDATE_BOOK_RESPONSE = StringTestUtils.readStringFromResource("update_book_response.json");
    private static final String BOOK_LIST_RESPONSE = StringTestUtils.readStringFromResource("book_list_response.json");
    private static final String REQUEST_MAPPING = "/api/v1/book";
    private UpsertBookRequest request;

    @BeforeEach
    void setUp() {
        request = new UpsertBookRequest();
        request.setAuthorName("author 2");
        request.setTitle("title 2");
        request.setCategoryName("category 2");
    }

    @AfterEach
    void tearDown() {
        request = null;
    }

    @Test
    void findByTitleAndAuthorName() throws Exception {
        String response = mockMvc.perform(get(REQUEST_MAPPING + "/byTitleAndAuthor")
                        .param("author", "author")
                        .param("title", "title"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        assertJsonEquals(GET_BOOK_RESPONSE, response);
    }

    @Test
    void findByCategory() throws Exception {
        String response = mockMvc.perform(get(REQUEST_MAPPING + "/byCategory")
                        .param("category", "category"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        assertJsonEquals(BOOK_LIST_RESPONSE, response);
    }

    @Test
    void create() throws Exception {
        String response = mockMvc.perform(post(REQUEST_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getContentAsString();
        assertJsonEquals(INSERT_BOOK_RESPONSE, response);
    }

    @Test
    void update() throws Exception {
        String response = mockMvc.perform(put(REQUEST_MAPPING + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        assertJsonEquals(UPDATE_BOOK_RESPONSE, response);
    }

    @Test
    void deleteBook() throws Exception {
        mockMvc.perform(delete(REQUEST_MAPPING + "/{id}", 1))
                .andExpect(status().isNoContent());
    }

}