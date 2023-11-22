package com.example.tasktracker.aop.aspect;

import com.example.tasktracker.entity.User;
import com.example.tasktracker.exception.UserNotAuthorizedException;
import com.example.tasktracker.security.PrincipalUtils;
import com.example.tasktracker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AccountOwnerVerifyingAspect {
    private final UserService userService;

    @Around(value = "@annotation(com.example.tasktracker.aop.annotation.AccountOwnerVerifiable) ")
    public Mono<?> verifyAccountOwner(ProceedingJoinPoint joinPoint) {
        return Mono.zip(PrincipalUtils.getUserDetails(), PrincipalUtils.isManager()).flatMap((data) -> {
            Mono<User> userMono = userService.findByUsername(data.getT1().getUsername());
            return userMono.flatMap(user -> {
                boolean isAuthor = user.getId().equals(joinPoint.getArgs()[0]);
                if (!isAuthor && !data.getT2()) {
                    return Mono.error(new UserNotAuthorizedException(
                            "user.notAccountOwner",
                            data.getT1().getUsername()
                    ));
                }
                try {
                    return (Mono<?>) joinPoint.proceed();
                } catch (Throwable e) {
                    return Mono.error(new RuntimeException(e));
                }
            });

        });
    }


}
