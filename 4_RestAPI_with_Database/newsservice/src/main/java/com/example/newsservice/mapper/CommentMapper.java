package com.example.newsservice.mapper;

import com.example.newsservice.dto.comment.CommentListResponse;
import com.example.newsservice.dto.comment.CommentResponse;
import com.example.newsservice.dto.comment.InsertCommentRequest;
import com.example.newsservice.dto.comment.UpdateCommentRequest;
import com.example.newsservice.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(source = "user.name", target = "authorName")
    CommentResponse commentToResponse(Comment comment);

    Comment requestToComment(InsertCommentRequest request);

    Comment requestToComment(String id, UpdateCommentRequest request);

    List<CommentResponse> commentToResponse(List<Comment> comments);

    default CommentListResponse commentListToListResponse(List<Comment> comments) {
        List<CommentResponse> commentListResponses = commentToResponse(comments);
        return new CommentListResponse(commentListResponses);
    }
}
