package com.example.newsservice.service;

import com.example.newsservice.dto.NewsFilter;
import com.example.newsservice.model.News;

import java.util.List;

public interface NewsService {
    List<News> findAll(NewsFilter newsFilter);

    News findById(Long id);

    News save(News news);

    News update(News news);

    void deleteById(Long id);
}
