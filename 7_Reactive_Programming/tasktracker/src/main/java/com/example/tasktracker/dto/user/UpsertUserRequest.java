package com.example.tasktracker.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpsertUserRequest {
    @NotBlank
    private String userName;
    @NotBlank
    @Email
    private String email;
}
