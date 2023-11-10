package com.example.newsservice.service.impl;

import com.example.newsservice.dto.NewsFilter;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.model.News;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.NewsSpecification;
import com.example.newsservice.service.NewsService;
import com.example.newsservice.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Override
    public List<News> findAll(NewsFilter newsFilter) {
        Pageable pageable = PageRequest.of(newsFilter.getPageNumber(), newsFilter.getPageSize());
        return newsRepository
                .findAll(NewsSpecification.withFilter(newsFilter), pageable)
                .getContent();
    }

    @Override
    public News findById(Long id) {
        return newsRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News not found. ID: " + id));
    }

    @Override
    public News save(News news) {
        return newsRepository.save(news);
    }

    @Transactional
    @Override
    public News update(News source) {
        News news = newsRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("News not found when updating. ID: " + source.getId()));
        BeanUtils.copyNonNullProperties(source, news);
        return newsRepository.save(news);
    }

    @Override
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }
}
