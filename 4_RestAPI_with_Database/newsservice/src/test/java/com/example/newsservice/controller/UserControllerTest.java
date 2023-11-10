package com.example.newsservice.controller;


import com.example.newsservice.AbstractTestController;
import com.example.newsservice.StringTestUtils;
import com.example.newsservice.dto.user.UpsertUserRequest;
import com.example.newsservice.model.User;
import com.example.newsservice.repository.UserRepository;
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

public class UserControllerTest extends AbstractTestController {
    @MockBean
    private UserRepository userRepository;
    private User user;
    private final String expectedResponse = StringTestUtils
            .readStringFromResource("response/user/single_object_response.json");
    private final String expectedResponse2 = StringTestUtils
            .readStringFromResource("response/user/multiple_objects_response.json");

    @BeforeEach
    void setUp() {
        user = createUser(1L);

    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    @Test
    public void findById() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        String actualResponse = mockMvc
                .perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userRepository, times(1)).findById(anyLong());
        assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void findAll() throws Exception {
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(user)));
        String actualResponse = mockMvc
                .perform(get("/api/v1/user")
                        .param("pageNumber", "0")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userRepository, times(1)).findAll(any(PageRequest.class));
        assertJsonEquals(expectedResponse2, actualResponse);
    }


    @Test
    public void update() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest("User 1");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        String actualResponse = mockMvc
                .perform(put("/api/v1/user/1")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
        assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    void create() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest("User 1");
        when(userRepository.save(any(User.class))).thenReturn(user);
        String actualResponse = mockMvc
                .perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userRepository, times(1)).save(any(User.class));
        assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    void deleteById() throws Exception {
        mockMvc
                .perform(delete("/api/v1/user/1"))
                .andExpect(status().isNoContent());
        verify(userRepository, times(1)).deleteById(anyLong());
    }
}
