package com.example.newsservice.mapper;

import com.example.newsservice.dto.news.MultipleNewsResponse;
import com.example.newsservice.dto.news.NewsListResponse;
import com.example.newsservice.dto.news.SingleNewsResponse;
import com.example.newsservice.dto.news.UpsertNewsRequest;
import com.example.newsservice.model.News;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(NewsMapperDecorator.class)
public interface NewsMapper {

    SingleNewsResponse newsToSingleResponse(News news);

    News requestToNews(UpsertNewsRequest request);

    News requestToNews(Long id, UpsertNewsRequest request);

    MultipleNewsResponse newsToMultipleResponse(News news);

    List<MultipleNewsResponse> newsToMultipleResponse(List<News> news);

    default NewsListResponse newsListToListResponse(List<News> news) {
        List<MultipleNewsResponse> newsWithCommentsResponse = newsToMultipleResponse(news);
        return new NewsListResponse(newsWithCommentsResponse);
    }
}
