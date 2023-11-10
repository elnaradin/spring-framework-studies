package com.example.newsservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsFilter {
    @NotNull(message = "Page number must not be null")
    private Integer pageNumber;
    @NotNull(message = "Page size must not be null")
    private Integer pageSize;
    private Long categoryId;
    private Long authorId;
}
