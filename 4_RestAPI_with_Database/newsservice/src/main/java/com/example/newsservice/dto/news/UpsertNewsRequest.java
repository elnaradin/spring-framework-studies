package com.example.newsservice.dto.news;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpsertNewsRequest {
    @NotBlank(message = "{requirements.news.title}")
    private String title;
    @NotBlank(message = "{requirements.news.text}")
    private String text;
    //    @NotNull(message = "{requirements.news.authorId}")
//    private Long authorId;
    @NotEmpty(message = "{requirements.news.categoryIds}")
    private List<Long> categoryIds;
}
