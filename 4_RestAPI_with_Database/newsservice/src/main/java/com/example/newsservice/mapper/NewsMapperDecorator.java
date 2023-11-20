package com.example.newsservice.mapper;

import com.example.newsservice.dto.news.MultipleNewsResponse;
import com.example.newsservice.dto.news.SingleNewsResponse;
import com.example.newsservice.model.News;
import com.example.newsservice.service.CategoryService;
import com.example.newsservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public abstract class NewsMapperDecorator implements NewsMapper {
    @Autowired
    private NewsMapper delegate;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommentService commentService;

    @Override
    public SingleNewsResponse newsToSingleResponse(News news) {
        SingleNewsResponse response = delegate.newsToSingleResponse(news);
        response.setComments(commentService.findByNewsId(news.getId()));
        response.setCategories(categoryService.findByNewsId(news.getId()));
        response.setAuthorName(news.getAuthor().getName());
        return response;
    }
//
//    @Override
//    public News requestToNews(UpsertNewsRequest request) {
//        return getNews(request);
//    }
//
//    @Override
//    public News requestToNews(Long id, UpsertNewsRequest request) {
//        News news = getNews(request);
//        news.setId(id);
//        return news;
//    }
//
//    private News getNews(UpsertNewsRequest request) {
//        News news = delegate.requestToNews(request);
//        return news;
//    }

    @Override
    public MultipleNewsResponse newsToMultipleResponse(News news) {
        MultipleNewsResponse response = delegate.newsToMultipleResponse(news);
        response.setAuthorName(news.getAuthor().getName());
        response.setCommentCount(commentService.countByNewsId(news.getId()));
        response.setCategoryNames(categoryService.getNamesByNewsId(news.getId()));
        return response;
    }

    @Override
    public List<MultipleNewsResponse> newsToMultipleResponse(List<News> news) {
        return news
                .stream()
                .map(this::newsToMultipleResponse)
                .collect(Collectors.toList());
    }
}
