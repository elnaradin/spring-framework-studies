package com.example.newsservice.aop.aspect;

import com.example.newsservice.aop.utils.RequestAttributesUtils;
import com.example.newsservice.exception.UserNotAuthorizedException;
import com.example.newsservice.security.PrincipalUtils;
import com.example.newsservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class NewsAuthorVerifyingAspect {
    private final UserService userService;

    @Before(value = "@annotation(com.example.newsservice.aop.annotation.NewsAuthorVerifiable) ")
    public void verifyUser() {
        UserDetails principal = PrincipalUtils.getUserDetails();
        Long newsId = Long.parseLong(RequestAttributesUtils.getPathVariable("id"));
        boolean isAuthor = userService.existsByUsernameAndNewsId(principal.getUsername(), newsId);
        if (!isAuthor && !PrincipalUtils.isAdminOrModerator()) {
            throw new UserNotAuthorizedException(MessageFormat.format(
                    "User with name ''{0}'' isn''t the author of this news", principal.getUsername()
            ));
        }
    }
}
