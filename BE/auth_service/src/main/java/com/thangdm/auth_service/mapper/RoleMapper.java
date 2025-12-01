package com.thangdm.auth_service.mapper;

import com.thangdm.auth_service.dto.request.PermissionRequest;
import com.thangdm.auth_service.dto.request.RoleRequest;
import com.thangdm.auth_service.dto.response.PermissionResponse;
import com.thangdm.auth_service.dto.response.RoleResponse;
import com.thangdm.auth_service.entity.Permission;
import com.thangdm.auth_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions",ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
