package com.example.newsservice.dto.user;

import com.example.newsservice.model.RoleType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class CreateUserRequest {
    @NotBlank(message = "{requirements.user.name}")
    private String name;
    @NotBlank(message = "{requirements.user.password}")
    private String password;
    @NotEmpty(message = "{requirements.user.roles}")
    private List<RoleType> roles;
}
