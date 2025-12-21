package com.thangdm.account_service.service;

import com.thangdm.account_service.dto.request.PermissionRequest;
import com.thangdm.account_service.dto.response.PermissionResponse;
import com.thangdm.account_service.entity.Permission;
import com.thangdm.account_service.mapper.PermissionMapper;
import com.thangdm.account_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(mapper::toPermissionResponse).toList();
    }

    public void delete(String id) {
        permissionRepository.deleteById(id);
    }
}
