package com.thangdm.account_service.mapper;

import com.thangdm.account_service.dto.request.PermissionRequest;
import com.thangdm.account_service.dto.response.PermissionResponse;
import com.thangdm.account_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
