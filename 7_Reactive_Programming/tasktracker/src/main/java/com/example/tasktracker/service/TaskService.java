package com.example.tasktracker.service;

import com.example.tasktracker.dto.task.TaskResponse;
import com.example.tasktracker.dto.task.UpsertTaskRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
    Flux<TaskResponse> findAll();

    Mono<TaskResponse> findById(String id);

    Mono<TaskResponse> create(UpsertTaskRequest request);

    Mono<TaskResponse> update(String id, UpsertTaskRequest request);

    Mono<Void> deleteById(String id);

    Mono<TaskResponse> addObserver(String taskId, String observerId);

    Mono<TaskResponse> removeObserver(String taskId, String observerId);
}
