package com.example.tasktracker.service;

import com.example.tasktracker.aop.annotation.TaskAuthorVerifiable;
import com.example.tasktracker.dto.task.TaskResponse;
import com.example.tasktracker.dto.task.UpsertTaskRequest;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.exception.EntityNotFoundException;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.UserRepository;
import com.example.tasktracker.security.PrincipalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;


    @Override
    public Flux<TaskResponse> findAll() {
        return taskRepository.findAll()
                .flatMap(this::fetchChildren)
                .map(taskMapper::taskToResponse);
    }

    @Override
    public Mono<TaskResponse> findById(String id) {
        return taskRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("task.findById", id)))
                .flatMap(this::fetchChildren)
                .map(taskMapper::taskToResponse);
    }

    @Override
    public Mono<TaskResponse> create(UpsertTaskRequest request) {
        Mono<Task> taskMono = Mono.just(taskMapper.requestToTask(request));
        Mono<String> authorIdMono = PrincipalUtils.getUserId();
        Mono<User> authorMono = authorIdMono.flatMap(authorId -> userRepository
                .findById(authorId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("task.create.authorId", authorId))));
        Mono<User> assigneeMono = authorIdMono.flatMap(authorId -> userRepository.findById(authorId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("task.create.assigneeId", authorId))));
        return taskMono
                .zipWith(authorMono, (task, author) -> {
                    task.setAuthorId(author.getId());
                    return task;
                })
                .zipWith(assigneeMono, (task, assignee) -> {
                    task.setAssigneeId(assignee.getId());
                    return task;
                })
                .flatMap(taskRepository::save)
                .flatMap(this::fetchChildren)
                .map(taskMapper::taskToResponse);
    }


    @Override
    public Mono<TaskResponse> update(String id, UpsertTaskRequest request) {
        String assigneeId = request.getAssigneeId();
        Mono<Task> taskMono = taskRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("task.update.taskId", id)));
        Mono<String> authorIdMono = PrincipalUtils.getUserId();
        Mono<User> authorMono = authorIdMono.flatMap(authorId -> userRepository.findById(authorId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("task.update.authorId", authorId))));
        taskMono = taskMono.zipWith(authorMono, (task, author) -> {
            task.setAuthorId(author.getId());
            return task;
        });
        if (assigneeId != null) {
            Mono<User> assigneeMono = userRepository.findById(assigneeId)
                    .switchIfEmpty(Mono.error(new EntityNotFoundException("task.update.assigneeId", assigneeId)));
            taskMono = taskMono.zipWith(assigneeMono, (task, assignee) -> {
                task.setAssigneeId(assignee.getId());
                return task;
            });
        }
        return taskMono
                .flatMap(task -> {
                    taskMapper.update(request, task);
                    return taskRepository
                            .save(task)
                            .flatMap(this::fetchChildren)
                            .map(taskMapper::taskToResponse);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return taskRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("task.deleteById", id)))
                .flatMap(taskRepository::delete);
    }

    @TaskAuthorVerifiable
    @Override
    public Mono<TaskResponse> addObserver(String taskId, String observerId) {
        Mono<User> userMono = userRepository.findById(observerId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("task.addObserver.userId", observerId)));
        return taskRepository.findById(taskId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("task.addObserver.taskId", taskId)))
                .zipWith(userMono, (task, user) -> {
                    task.getObserverIds().add(user.getId());
                    return task;
                })
                .flatMap(taskRepository::save)
                .flatMap(this::fetchChildren)
                .map(taskMapper::taskToResponse);
    }

    @TaskAuthorVerifiable
    @Override
    public Mono<TaskResponse> removeObserver(String taskId, String observerId) {
        Mono<User> userMono = userRepository.findById(observerId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("task.removeObserver.userId", observerId)));
        return taskRepository.findById(taskId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("task.removeObserver.taskId", taskId)))
                .zipWith(userMono, (task, user) -> {
                    Set<String> observers = task.getObserverIds();
                    if (observers == null || observers.stream().noneMatch(id -> id.equals(user.getId()))) {
                        throw new EntityNotFoundException("task.removeObserver.userNotObserver", user.getId());
                    }
                    task.getObserverIds().remove(user.getId());
                    return task;
                })
                .flatMap(taskRepository::save)
                .flatMap(this::fetchChildren)
                .map(taskMapper::taskToResponse);
    }

    private Mono<? extends Task> fetchChildren(Task initialTask) {
        Mono<User> authorMono = userRepository.findById(initialTask.getAuthorId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("task.find.authorId", initialTask.getAuthorId())));
        Mono<User> assigneeMono = userRepository.findById(initialTask.getAssigneeId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("task.find.assigneeId", initialTask.getAuthorId())));
        Flux<User> observersFlux;
        if (initialTask.getObserverIds() != null) {
            observersFlux = userRepository.findAllById(initialTask.getObserverIds());
        } else {
            observersFlux = Flux.fromIterable(Collections.emptySet());
        }
        return Mono.just(initialTask)
                .zipWith(authorMono, (task, author) -> {
                    task.setAuthor(author);
                    return task;
                })
                .zipWith(assigneeMono, (task, assignee) -> {
                    task.setAssignee(assignee);
                    return task;
                })
                .zipWith(observersFlux.collectList(), (task, observers) -> {
                    task.setObservers(new HashSet<>(observers));
                    return task;
                });
    }
}
