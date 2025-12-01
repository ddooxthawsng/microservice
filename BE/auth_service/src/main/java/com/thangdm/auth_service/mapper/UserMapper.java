package com.thangdm.auth_service.mapper;

import com.thangdm.auth_service.dto.request.UserCreationRequest;
import com.thangdm.auth_service.dto.request.UserUpdateRequest;
import com.thangdm.auth_service.dto.response.UserResponse;
import com.thangdm.auth_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "id", ignore = true)        // Don't update ID
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
