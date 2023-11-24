package com.example.newsservice.security;

import com.example.newsservice.model.RoleType;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@UtilityClass
public class PrincipalUtils {
    public UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Long getUserId() {
        AppUserDetails userDetails = (AppUserDetails) getUserDetails();
        return userDetails.getId();

    }

    public boolean isAdminOrModerator() {
        return getUserDetails().getAuthorities().stream().anyMatch(authority -> {
            String authorityName = authority.getAuthority();
            return authorityName.equals(RoleType.ROLE_ADMIN.name()) ||
                    authorityName.equals(RoleType.ROLE_MODERATOR.name());
        });
    }
}
