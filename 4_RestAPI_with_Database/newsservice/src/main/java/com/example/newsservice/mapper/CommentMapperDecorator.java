package com.example.newsservice.mapper;

import com.example.newsservice.dto.comment.InsertCommentRequest;
import com.example.newsservice.model.Comment;
import com.example.newsservice.service.NewsService;
import com.example.newsservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommentMapperDecorator implements CommentMapper {
    @Autowired
    private CommentMapper delegate;
    @Autowired
    private UserService userService;
    @Autowired
    private NewsService newsService;

    @Override
    public Comment requestToComment(InsertCommentRequest request) {
        Comment comment = delegate.requestToComment(request);
        comment.setNews(newsService.findById(request.getNewsId()));
        comment.setUser(userService.findById(request.getUserId()));
        return comment;
    }
}
