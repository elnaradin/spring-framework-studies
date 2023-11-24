package com.example.newsservice.service;

import com.example.newsservice.dto.news.NewsListResponse;
import com.example.newsservice.dto.news.SingleNewsResponse;
import com.example.newsservice.dto.news.UpsertNewsRequest;
import com.example.newsservice.dto.user.NewsFilter;

public interface NewsService {
    NewsListResponse findAll(NewsFilter newsFilter);

    SingleNewsResponse findById(Long id);

    SingleNewsResponse create(UpsertNewsRequest news);

    SingleNewsResponse update(Long id, UpsertNewsRequest request);

    void deleteById(Long id);
}
