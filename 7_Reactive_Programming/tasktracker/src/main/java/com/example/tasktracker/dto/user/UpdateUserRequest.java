package com.example.tasktracker.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest {
    @NotBlank(message = "{requirements.name}")
    private String userName;
    @NotBlank(message = "{requirements.email}")
    @Email(message = "{requirements.emailFormat}")
    private String email;
}
