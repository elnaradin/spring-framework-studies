package com.example.tasktracker.dto.task;

import com.example.tasktracker.entity.TaskStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpsertTaskRequest {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private TaskStatus status;
    @NotBlank
    private String authorId;
    @NotBlank
    private String assigneeId;
}
