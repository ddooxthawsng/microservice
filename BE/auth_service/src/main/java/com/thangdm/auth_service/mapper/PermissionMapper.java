package com.thangdm.auth_service.mapper;

import com.thangdm.auth_service.dto.request.PermissionRequest;
import com.thangdm.auth_service.dto.request.UserCreationRequest;
import com.thangdm.auth_service.dto.request.UserUpdateRequest;
import com.thangdm.auth_service.dto.response.PermissionResponse;
import com.thangdm.auth_service.dto.response.UserResponse;
import com.thangdm.auth_service.entity.Permission;
import com.thangdm.auth_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
