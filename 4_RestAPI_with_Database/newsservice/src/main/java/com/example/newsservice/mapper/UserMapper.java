package com.example.newsservice.mapper;

import com.example.newsservice.dto.user.CreateUserRequest;
import com.example.newsservice.dto.user.UpdateUserRequest;
import com.example.newsservice.dto.user.UserListResponse;
import com.example.newsservice.dto.user.UserResponse;
import com.example.newsservice.model.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {
    UserResponse userToResponse(User user);

    User requestToUser(CreateUserRequest request);

    List<UserResponse> userListToResponseList(List<User> all);

    void update(Long id, UpdateUserRequest request, @MappingTarget User user);

    default UserListResponse userListToListResponse(Page<User> users) {
        List<UserResponse> userResponses = userListToResponseList(users.getContent());
        return new UserListResponse(users.getTotalElements(), userResponses);
    }

}
