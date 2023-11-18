package com.example.tasktracker.controller;

import com.example.tasktracker.AbstractTest;
import com.example.tasktracker.dto.user.UpsertUserRequest;
import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest extends AbstractTest {
    private static final String BASE_URI = "/api/v1/user";
    @Autowired
    private UserMapper userMapper;


    @Test
    void findAll() throws JsonProcessingException {
        List<UserResponse> userResponses = userMapper.userToResponse(userList);
        webTestClient.get().uri(BASE_URI)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(2)
                .equals(objectMapper.writeValueAsString(userResponses));
    }

    @Test
    void findById() {
        User user1 = userList.get(0);
        UserResponse userResponse = userMapper.userToResponse(user1);
        webTestClient.get().uri(BASE_URI + "/{id}", userResponse.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(response -> assertEquals(userResponse.getId(), response.getId()));

    }

    @Test
    void create() {
        StepVerifier.create(userRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();
        User user = userList.get(0);
        user.setId(null);
        user.setUserName("user 3");
        UpsertUserRequest request = userMapper.userToRequest(user);
        webTestClient.post().uri(BASE_URI)
                .body(Mono.just(request), UpsertUserRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .value(response -> {
                    UserResponse expected = userMapper.userToResponse(user);
                    assertEquals(expected.getUserName(), response.getUserName());
                });
        StepVerifier.create(userRepository.count())
                .expectNext(3L)
                .expectComplete()
                .verify();
    }

    @Test
    void update() {
        User user = userList.get(0);
        String oldName = user.getUserName();
        String id = user.getId();
        user.setId(null);
        user.setUserName("new user name");
        UpsertUserRequest request = userMapper.userToRequest(user);
        webTestClient.put().uri(BASE_URI + "/{id}", id)
                .body(Mono.just(request), UpsertUserRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(response -> {
                    UserResponse expected = userMapper.userToResponse(user);
                    assertEquals(id, response.getId());
                    assertEquals(expected.getUserName(), response.getUserName());
                });
        StepVerifier.create(userRepository.findByUserName(oldName))
                .expectNextCount(0L)
                .verifyComplete();
        StepVerifier.create(userRepository.findByUserName(user.getUserName()))
                .expectNextCount(1L)
                .verifyComplete();

    }

    @Test
    void delete() {
        StepVerifier.create(userRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();
        User user = userList.get(0);
        webTestClient.delete().uri(BASE_URI + "/{id}", user.getId())
                .exchange()
                .expectStatus().isOk();
        StepVerifier.create(userRepository.count())
                .expectNext(1L)
                .expectComplete()
                .verify();

    }

}