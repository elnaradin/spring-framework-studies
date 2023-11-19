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
    @NotBlank(message = "{requirements.name}")
    private String name;
    private String description;
    @NotNull(message = "{requirements.status}")
    private TaskStatus status;
    @NotBlank(message = "{requirements.authorId}")
    private String authorId;
    @NotBlank(message = "{requirements.assigneeId}")
    private String assigneeId;
}
