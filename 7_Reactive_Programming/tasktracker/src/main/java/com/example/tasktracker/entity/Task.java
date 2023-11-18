package com.example.tasktracker.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Document(collection = "items")
public class Task {
    @Id
    private String id;
    @NotBlank
    private String name;

    private String description;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
    @NotNull
    private TaskStatus status;
    @NotBlank
    private String authorId;
    @NotBlank
    private String assigneeId;
    @Builder.Default
    private Set<String> observerIds = new HashSet<>();
    @ReadOnlyProperty
    private User author;
    @ReadOnlyProperty
    private User assignee;
    @ReadOnlyProperty
    private Set<User> observers;

}
