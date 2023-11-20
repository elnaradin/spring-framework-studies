package com.example.newsservice.mapper;

import com.example.newsservice.dto.comment.CommentResponse;
import com.example.newsservice.dto.comment.CreateCommentRequest;
import com.example.newsservice.dto.comment.UpdateCommentRequest;
import com.example.newsservice.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {

    @Mapping(source = "user.name", target = "authorName")
    CommentResponse commentToResponse(Comment comment);

    Comment requestToComment(CreateCommentRequest request);

    List<CommentResponse> commentToResponse(List<Comment> comments);

    void update(Long id, UpdateCommentRequest request, @MappingTarget Comment comment);

}
