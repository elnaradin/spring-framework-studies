package com.example.tasktracker.mapper;

import com.example.tasktracker.dto.user.UpsertUserRequest;
import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User requestToUser(UpsertUserRequest request);

    UpsertUserRequest userToRequest(User user);

    UserResponse userToResponse(User user);

    List<UserResponse> userToResponse(List<User> users);

    void update(UpsertUserRequest source, @MappingTarget User target);
}
