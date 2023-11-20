package com.example.newsservice.service;

import com.example.newsservice.dto.NewsFilter;
import com.example.newsservice.dto.news.NewsListResponse;
import com.example.newsservice.dto.news.SingleNewsResponse;
import com.example.newsservice.dto.news.UpsertNewsRequest;

public interface NewsService {
    NewsListResponse findAll(NewsFilter newsFilter);

    SingleNewsResponse findById(Long id);

    SingleNewsResponse save(UpsertNewsRequest news);

    SingleNewsResponse update(Long id, UpsertNewsRequest request);

    void deleteById(Long id);
}
