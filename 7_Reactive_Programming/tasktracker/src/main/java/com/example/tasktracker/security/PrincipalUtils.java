package com.example.tasktracker.security;

import com.example.tasktracker.entity.RoleType;
import com.example.tasktracker.exception.UserNotAuthenticatedException;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;


@UtilityClass
public class PrincipalUtils {
    public Mono<UserDetails> getUserDetails() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    Authentication authentication = securityContext.getAuthentication();
                    if (authentication != null) {
                        Object principal = authentication.getPrincipal();
                        return (UserDetails) principal;
                    }
                    throw new UserNotAuthenticatedException("User not authenticated");
                });

    }

    public Mono<Boolean> isManager() {
        return getUserDetails().map(UserDetails::getAuthorities)
                .map(grantedAuthorities -> grantedAuthorities
                        .stream()
                        .anyMatch(authority -> authority.getAuthority()
                                .equals(RoleType.ROLE_MANAGER.name())));

    }
}
