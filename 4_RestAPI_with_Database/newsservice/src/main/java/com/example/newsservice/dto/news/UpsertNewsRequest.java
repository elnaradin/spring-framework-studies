package com.example.newsservice.dto.news;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpsertNewsRequest {
    @NotBlank(message = "title must not be blank")
    private String title;
    @NotBlank(message = "text must not be blank")
    private String text;
    @NotNull(message = "author ID must not be null")
    private Long authorId;
    @NotEmpty(message = "must have at least one category")
    private List<Long> categoryIds;
}
