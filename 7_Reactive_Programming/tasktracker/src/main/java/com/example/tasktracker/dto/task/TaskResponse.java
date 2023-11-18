package com.example.tasktracker.dto.task;

import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.entity.TaskStatus;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class TaskResponse {
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private TaskStatus status;
    private UserResponse author;
    private UserResponse assignee;
    private Set<UserResponse> observers;
}
