package com.example.newsservice.dto.news;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsListResponse {
    private List<MultipleNewsResponse> newsList;
}
