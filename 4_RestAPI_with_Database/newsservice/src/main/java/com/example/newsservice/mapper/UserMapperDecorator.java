package com.example.newsservice.mapper;

import com.example.newsservice.dto.user.CreateUserRequest;
import com.example.newsservice.model.Role;
import com.example.newsservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public abstract class UserMapperDecorator implements UserMapper {
    @Autowired
    private UserMapper delegate;

    @Override
    public User requestToUser(CreateUserRequest request) {
        User user = delegate.requestToUser(request);
        List<Role> roles = request.getRoles().stream().map(Role::from).peek(role -> role.setUser(user)).collect(Collectors.toList());
        user.setRoles(roles);
        return user;
    }
}
