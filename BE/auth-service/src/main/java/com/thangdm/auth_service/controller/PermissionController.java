package com.thangdm.auth_service.controller;

import com.nimbusds.jose.JOSEException;
import com.thangdm.auth_service.dto.request.ApiResponse;
import com.thangdm.auth_service.dto.request.AuthenticationRequest;
import com.thangdm.auth_service.dto.request.IntrospectRequest;
import com.thangdm.auth_service.dto.request.PermissionRequest;
import com.thangdm.auth_service.dto.response.AuthenticationResponse;
import com.thangdm.auth_service.dto.response.IntrospectResponse;
import com.thangdm.auth_service.dto.response.PermissionResponse;
import com.thangdm.auth_service.service.AuthenticationService;
import com.thangdm.auth_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping("/create")
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request){
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

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permissionId) {
        permissionService.delete(permissionId);
        return ApiResponse.<Void>builder().build();
    }
}
