package com.thangdm.account_service.controller;

import com.thangdm.common.dto.ApiResponse;
import com.thangdm.account_service.dto.request.RoleRequest;
import com.thangdm.account_service.dto.response.RoleResponse;
import com.thangdm.account_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        RoleResponse result = roleService.create(request);
        return ApiResponse.<RoleResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        List<RoleResponse> result = roleService.getAll();
        return ApiResponse.<List<RoleResponse>>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{roleId}")
    ApiResponse<Void> delete(@PathVariable String roleId) {
        roleService.delete(roleId);
        return ApiResponse.<Void>builder().build();
    }
}
