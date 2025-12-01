package com.thangdm.auth_service.service;

import com.thangdm.auth_service.dto.request.PermissionRequest;
import com.thangdm.auth_service.dto.request.UserCreationRequest;
import com.thangdm.auth_service.dto.request.UserUpdateRequest;
import com.thangdm.auth_service.dto.response.PermissionResponse;
import com.thangdm.auth_service.dto.response.UserResponse;
import com.thangdm.auth_service.entity.Permission;
import com.thangdm.auth_service.entity.User;
import com.thangdm.auth_service.enums.Role;
import com.thangdm.auth_service.exception.ErrorCode;
import com.thangdm.auth_service.exception.UserException;
import com.thangdm.auth_service.mapper.PermissionMapper;
import com.thangdm.auth_service.mapper.UserMapper;
import com.thangdm.auth_service.repository.PermissionRepository;
import com.thangdm.auth_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper mapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = mapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return mapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(mapper::toPermissionResponse).toList();
    }

    public void delete(String id) {
        permissionRepository.deleteById(id);
    }
}