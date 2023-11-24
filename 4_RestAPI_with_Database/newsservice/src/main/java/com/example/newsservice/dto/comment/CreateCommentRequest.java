package com.example.newsservice.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {
    @NotBlank(message = "{requirements.comment.text}")
    private String text;
    //    @NotNull(message = "{requirements.comment.userId}")
//    private Long userId;
    @NotNull(message = "{requirements.comment.newsId}")
    private Long newsId;
}
