package com.example.newsservice.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertCommentRequest {
    @NotBlank(message = "Comment text must not be empty")
    private String text;
    @NotNull(message = "User id must ne not null")
    private Long userId;
    @NotNull(message = "News id must ne not null")
    private Long newsId;
}
