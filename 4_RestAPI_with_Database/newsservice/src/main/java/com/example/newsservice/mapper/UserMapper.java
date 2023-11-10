package com.example.newsservice.mapper;

import com.example.newsservice.dto.user.UpsertUserRequest;
import com.example.newsservice.dto.user.UserListResponse;
import com.example.newsservice.dto.user.UserResponse;
import com.example.newsservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserResponse userToResponse(User user);

    User requestToUser(UpsertUserRequest request);

    User requestToUser(String id, UpsertUserRequest request);

    List<UserResponse> userListToResponseList(List<User> all);

    default UserListResponse userListToListResponse(List<User> categories) {
        List<UserResponse> userResponses = userListToResponseList(categories);
        return new UserListResponse(userResponses);
    }

}
