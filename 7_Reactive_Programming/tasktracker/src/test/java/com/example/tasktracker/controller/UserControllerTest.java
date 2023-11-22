package com.example.tasktracker.controller;

import com.example.tasktracker.AbstractTest;
import com.example.tasktracker.dto.ErrorResponse;
import com.example.tasktracker.dto.user.CreateUserRequest;
import com.example.tasktracker.dto.user.UpdateUserRequest;
import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.entity.RoleType;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

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
        user.setRoles(Set.of(RoleType.ROLE_USER));
        CreateUserRequest request = userMapper.userToCreateRequest(user);
        webTestClient.post().uri(BASE_URI)
                .body(Mono.just(request), CreateUserRequest.class)
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
        User user = userList.get(1);
        String oldName = user.getUserName();
        user.setUserName("new user name");
        UpdateUserRequest request = userMapper.userToUpdateRequest(user);
        webTestClient.put().uri(BASE_URI + "/{id}", SECOND_USER_ID)
                .body(Mono.just(request), UpdateUserRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(response -> {
                    UserResponse expected = userMapper.userToResponse(user);
                    assertEquals(SECOND_USER_ID, response.getId());
                    assertEquals(expected.getUserName(), response.getUserName());
                });
        StepVerifier.create(userRepository.findByUserName(oldName))
                .expectNextCount(0L)
                .thenConsumeWhile(x -> true)
                .verifyComplete();
        StepVerifier.create(userRepository.findByUserName(user.getUserName()))
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    @WithMockUser(username = "User1", roles = "USER")
    void updateFail() {
        User user = userList.get(1);
        String oldName = user.getUserName();
        user.setUserName("new user name");
        UpdateUserRequest request = userMapper.userToUpdateRequest(user);
        webTestClient.put().uri(BASE_URI + "/{id}", SECOND_USER_ID)
                .body(Mono.just(request), UpdateUserRequest.class)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorResponse.class)
                .value(response -> assertEquals("User with name 'User1' isn't the owner of this account", response.getMessage()));
        StepVerifier.create(userRepository.findByUserName(oldName))
                .expectNextCount(1L)
                .verifyComplete();
        StepVerifier.create(userRepository.findByUserName(user.getUserName()))
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void delete() {
        StepVerifier.create(userRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();
        webTestClient.delete().uri(BASE_URI + "/{id}", SECOND_USER_ID)
                .exchange()
                .expectStatus().isOk();
        StepVerifier.create(userRepository.count())
                .expectNext(1L)
                .expectComplete()
                .verify();

    }

    @Test
    @WithMockUser(username = "User1", roles = "USER")
    void deleteFail() {
        StepVerifier.create(userRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();
        webTestClient.delete().uri(BASE_URI + "/{id}", SECOND_USER_ID)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorResponse.class)
                .value(response -> assertEquals("User with name 'User1' isn't the owner of this account", response.getMessage()));
        StepVerifier.create(userRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();

    }

}