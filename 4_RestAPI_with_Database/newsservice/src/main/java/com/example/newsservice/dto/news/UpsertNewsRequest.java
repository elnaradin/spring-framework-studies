package com.example.newsservice.dto.news;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpsertNewsRequest {
    private String title;
    private String text;
    private Long authorId;
    private List<Long> categoryIds;
}
