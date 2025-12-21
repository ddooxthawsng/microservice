package com.thangdm.account_service.controller;

import com.thangdm.account_service.dto.request.PermissionRequest;
import com.thangdm.account_service.dto.response.PermissionResponse;
import com.thangdm.account_service.service.PermissionService;
import com.thangdm.common.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        PermissionResponse result = permissionService.create(request);
        return ApiResponse.<PermissionResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        List<PermissionResponse> result = permissionService.getAll();
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{permissionId}")
    ApiResponse<Void> delete(@PathVariable String permissionId) {
        permissionService.delete(permissionId);
        return ApiResponse.<Void>builder().build();
    }
}
