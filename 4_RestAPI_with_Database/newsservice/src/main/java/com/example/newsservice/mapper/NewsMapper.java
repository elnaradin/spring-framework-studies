package com.example.newsservice.mapper;

import com.example.newsservice.dto.news.MultipleNewsResponse;
import com.example.newsservice.dto.news.NewsListResponse;
import com.example.newsservice.dto.news.SingleNewsResponse;
import com.example.newsservice.dto.news.UpsertNewsRequest;
import com.example.newsservice.model.News;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@DecoratedWith(NewsMapperDecorator.class)
public interface NewsMapper {

    SingleNewsResponse newsToSingleResponse(News news);

    News requestToNews(UpsertNewsRequest request);

    MultipleNewsResponse newsToMultipleResponse(News news);

    List<MultipleNewsResponse> newsToMultipleResponse(List<News> news);

    void update(Long id, UpsertNewsRequest request, @MappingTarget News news);

    default NewsListResponse newsListToListResponse(Page<News> newsPage) {
        List<MultipleNewsResponse> newsWithCommentsResponse = newsToMultipleResponse(newsPage.getContent());
        return new NewsListResponse(newsPage.getTotalElements(), newsWithCommentsResponse);
    }
}
