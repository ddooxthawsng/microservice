package com.thangdm.auth_service.controller;

import com.thangdm.auth_service.dto.request.ApiResponse;
import com.thangdm.auth_service.dto.request.RoleRequest;
import com.thangdm.auth_service.dto.response.RoleResponse;
import com.thangdm.auth_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping("/create")
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request){
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

    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String roleId) {
        roleService.delete(roleId);
        return ApiResponse.<Void>builder().build();
    }
}
