package com.thangdm.auth_service.service;

import com.thangdm.auth_service.dto.request.RoleRequest;
import com.thangdm.auth_service.dto.response.RoleResponse;
import com.thangdm.auth_service.entity.Role;
import com.thangdm.auth_service.mapper.RoleMapper;
import com.thangdm.auth_service.repository.PermissionRepository;
import com.thangdm.auth_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper mapper;

    public RoleResponse create(RoleRequest request) {
        Role role = mapper.toRole(request);
        var permission = new HashSet<>(permissionRepository.findAllById(request.getPermissions()));

        role.setPermissions(permission);
        role = roleRepository.save(role);
        return mapper.toRoleResponse(role);

    }

    public List<RoleResponse> getAll() {
        var roles = roleRepository.findAll();
        return roles.stream().map(mapper::toRoleResponse).toList();
    }

    public void delete(String id) {
        roleRepository.deleteById(id);
    }
}