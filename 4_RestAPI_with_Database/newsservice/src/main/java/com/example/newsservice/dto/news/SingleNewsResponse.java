package com.example.newsservice.dto.news;

import com.example.newsservice.dto.category.CategoryResponse;
import com.example.newsservice.dto.comment.CommentResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SingleNewsResponse {
    private Long id;
    private String authorName;
    private String title;
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationTime;

    private List<CategoryResponse> categories;
    private List<CommentResponse> comments;
}
