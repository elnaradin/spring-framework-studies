package com.example.newsservice.aspect;

import com.example.newsservice.exception.UserNotAuthorizedException;
import com.example.newsservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.text.MessageFormat;
import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class UserVerifyingAspect {
    private final UserService userService;

    @Before(value = "@annotation(UserVerifiable) ")
    public void verifyUser() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (request.getRequestURI().startsWith("/api/v1/news")) {
            Long newsId = Long.parseLong(pathVariables.get("id"));
            Long userId = Long.parseLong(request.getParameter("userId"));
            if (!userService.existsByUserAndNews(userId, newsId)) {
                throw new UserNotAuthorizedException(MessageFormat.format("User with ID {0} isn't the author of this news", userId));
            }
        }
        if (request.getRequestURI().startsWith("/api/v1/comment")) {
            Long commentId = Long.parseLong(pathVariables.get("id"));
            Long userId = Long.parseLong(request.getParameter("userId"));
            if (!userService.existsByUserAndComment(userId, commentId)) {
                throw new UserNotAuthorizedException(MessageFormat.format("User with ID {0} isn't the author of this comment", userId));
            }
        }
    }
}
