package com.example.tasktracker.controller;

import com.example.tasktracker.AbstractTest;
import com.example.tasktracker.dto.ErrorResponse;
import com.example.tasktracker.dto.task.TaskResponse;
import com.example.tasktracker.dto.task.UpsertTaskRequest;
import com.example.tasktracker.entity.RoleType;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.security.AppUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;


class TaskControllerTest extends AbstractTest {
    private static final String BASE_URI = "/api/v1/task";
    @Autowired
    private TaskMapper taskMapper;

    @Test
    void findAll() throws JsonProcessingException {
        List<TaskResponse> taskResponses = taskMapper.taskToResponse(taskList);
        webTestClient.get().uri(BASE_URI)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TaskResponse.class)
                .hasSize(2)
                .equals(objectMapper.writeValueAsString(taskResponses));
    }

    @Test
    void findById() {
        Task task1 = taskList.get(0);
        TaskResponse taskResponse = taskMapper.taskToResponse(task1);
        webTestClient.get().uri(BASE_URI + "/{id}", taskResponse.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .value(response -> assertEquals(taskResponse.getId(), response.getId()));

    }

    @Test
    void create() {
        StepVerifier.create(taskRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();
        Task task = taskList.get(0);
        task.setId(null);
        task.setName("task 3");
        User user = userList.get(0);
        user.setRoles(Set.of(RoleType.ROLE_MANAGER));
        UpsertTaskRequest request = taskMapper.taskToRequest(task);
        webTestClient
                .mutateWith(mockUser(new AppUserDetails(user)))
                .post().uri(BASE_URI)
                .body(Mono.just(request), UpsertTaskRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TaskResponse.class)
                .value(response -> {
                    TaskResponse expected = taskMapper.taskToResponse(task);
                    assertEquals(expected.getName(), response.getName());
                });
        StepVerifier.create(taskRepository.count())
                .expectNext(3L)
                .expectComplete()
                .verify();
    }

    @Test
    void update() {
        Task task = taskList.get(0);
        String oldName = task.getName();
        String id = task.getId();
        task.setId(null);
        task.setName("new task name");
        UpsertTaskRequest request = taskMapper.taskToRequest(task);
        User user = userList.get(0);
        user.setRoles(Set.of(RoleType.ROLE_MANAGER));
        webTestClient
                .mutateWith(mockUser(new AppUserDetails(user)))
                .put().uri(BASE_URI + "/{id}", id)
                .body(Mono.just(request), UpsertTaskRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .value(response -> {
                    TaskResponse expected = taskMapper.taskToResponse(task);
                    assertEquals(id, response.getId());
                    assertEquals(expected.getName(), response.getName());
                });
        StepVerifier.create(taskRepository.findByName(oldName))
                .expectNextCount(0L)
                .verifyComplete();
        StepVerifier.create(taskRepository.findByName(task.getName()))
                .expectNextCount(1L)
                .verifyComplete();

    }

    @Test
    void delete() {
        StepVerifier.create(taskRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();
        Task task = taskList.get(0);
        webTestClient.delete().uri(BASE_URI + "/{id}", task.getId())
                .exchange()
                .expectStatus().isOk();
        StepVerifier.create(taskRepository.count())
                .expectNext(1L)
                .expectComplete()
                .verify();

    }

    @Test
    void addAndRemoveObserverByManager() {
        StepVerifier.create(taskRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();
        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path(BASE_URI + "/addObserver")
                        .queryParam("taskId", FIRST_TASK_ID)
                        .queryParam("observerId", FIRST_USER_ID).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .value(data -> assertEquals(1, data.getObservers().size()));
        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path(BASE_URI + "/removeObserver")
                        .queryParam("taskId", FIRST_TASK_ID)
                        .queryParam("observerId", FIRST_USER_ID).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .value(data -> assertEquals(0, data.getObservers().size()));

    }

    @Test
    @WithMockUser(username = "User1", roles = "USER")
    void addAndRemoveObserverByUser() {
        StepVerifier.create(taskRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();
        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path(BASE_URI + "/addObserver")
                        .queryParam("taskId", FIRST_TASK_ID)
                        .queryParam("observerId", FIRST_USER_ID).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .value(data -> assertEquals(1, data.getObservers().size()));
        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path(BASE_URI + "/removeObserver")
                        .queryParam("taskId", FIRST_TASK_ID)
                        .queryParam("observerId", FIRST_USER_ID).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskResponse.class)
                .value(data -> assertEquals(0, data.getObservers().size()));
    }

    @Test
    @WithMockUser(username = "User2", roles = "USER")
    void addAndRemoveObserverByUserFail() {
        StepVerifier.create(taskRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();
        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path(BASE_URI + "/addObserver")
                        .queryParam("taskId", FIRST_TASK_ID)
                        .queryParam("observerId", FIRST_USER_ID).build())
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorResponse.class)
                .value(response -> assertEquals("Unable to add or remove observer. User with name 'User2' isn't the owner of this observer's account", response.getMessage()));
        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path(BASE_URI + "/removeObserver")
                        .queryParam("taskId", FIRST_TASK_ID)
                        .queryParam("observerId", FIRST_USER_ID).build())
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorResponse.class)
                .value(response -> assertEquals("Unable to add or remove observer. User with name 'User2' isn't the owner of this observer's account", response.getMessage()));

    }
}