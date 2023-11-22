package com.example.tasktracker.dto.user;

import com.example.tasktracker.entity.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class CreateUserRequest {
    @NotBlank(message = "{requirements.name}")
    private String userName;
    @NotBlank(message = "{requirements.email}")
    @Email(message = "{requirements.emailFormat}")
    private String email;
    @NotBlank(message = "{requirements.password}")
    private String password;
    @NotEmpty(message = "{requirements.roles}")
    private Set<RoleType> roles;
}
