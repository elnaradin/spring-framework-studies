package com.example.tasktracker.controller;

import com.example.tasktracker.dto.task.TaskResponse;
import com.example.tasktracker.dto.task.UpsertTaskRequest;
import com.example.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public Flux<TaskResponse> findAll() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> findById(@PathVariable String id) {
        return taskService.findById(id)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<TaskResponse>> create(@Valid @RequestBody UpsertTaskRequest request) {
        return taskService.create(request)
                .map((TaskResponse response) -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(response)
                );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskResponse>> update(@PathVariable String id,
                                                     @RequestBody UpsertTaskRequest request) {
        return taskService.update(id, request)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return taskService.deleteById(id)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/addObserver")
    public Mono<ResponseEntity<TaskResponse>> addObserver(@RequestParam String taskId,
                                                          @RequestParam String observerId) {
        return taskService.addObserver(taskId, observerId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/removeObserver")
    public Mono<ResponseEntity<TaskResponse>> removeObserver(@RequestParam String taskId,
                                                             @RequestParam String observerId) {
        return taskService.removeObserver(taskId, observerId)
                .map(ResponseEntity::ok);
    }


}
