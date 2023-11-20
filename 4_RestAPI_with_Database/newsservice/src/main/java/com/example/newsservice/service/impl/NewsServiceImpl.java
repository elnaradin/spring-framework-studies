package com.example.newsservice.service.impl;

import com.example.newsservice.dto.NewsFilter;
import com.example.newsservice.dto.news.NewsListResponse;
import com.example.newsservice.dto.news.SingleNewsResponse;
import com.example.newsservice.dto.news.UpsertNewsRequest;
import com.example.newsservice.exception.EntityNotFoundException;
import com.example.newsservice.mapper.NewsMapper;
import com.example.newsservice.model.News;
import com.example.newsservice.repository.CategoryRepository;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.NewsSpecification;
import com.example.newsservice.repository.UserRepository;
import com.example.newsservice.service.NewsService;
import com.example.newsservice.utils.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public NewsListResponse findAll(NewsFilter newsFilter) {
        Pageable pageable = PageRequest.of(newsFilter.getPageNumber(), newsFilter.getPageSize());
        Page<News> newsPage = newsRepository
                .findAll(NewsSpecification.withFilter(newsFilter), pageable);
        return newsMapper.newsListToListResponse(newsPage);
    }

    @Override
    public SingleNewsResponse findById(Long id) {
        News news = newsRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News not found. ID: " + id));
        return newsMapper.newsToSingleResponse(news);
    }

    @Transactional
    @Override
    public SingleNewsResponse save(UpsertNewsRequest request) {
        News news = newsMapper.requestToNews(request);
        if (request.getAuthorId() != null) {
            news.setAuthor(userRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new EntityNotFoundException("Author not found when creating news. ID: " + request.getAuthorId())));
        }
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            news.setCategories(categoryRepository.findAllById(request.getCategoryIds()));
        }
        return newsMapper.newsToSingleResponse(newsRepository.save(news));
    }

    @Transactional
    @Override
    public SingleNewsResponse update(Long id, UpsertNewsRequest request) {
        News source = newsMapper.requestToNews(id, request);
        if (request.getAuthorId() != null) {
            source.setAuthor(userRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new EntityNotFoundException("Author not found when updating news. ID: " + request.getAuthorId())));
        }
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            source.setCategories(categoryRepository.findAllById(request.getCategoryIds()));
        }
        News news = newsRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("News not found when updating. ID: " + source.getId()));
        BeanUtils.copyNonNullProperties(source, news);
        return newsMapper.newsToSingleResponse(newsRepository.save(news));
    }

    @Override
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }
}
