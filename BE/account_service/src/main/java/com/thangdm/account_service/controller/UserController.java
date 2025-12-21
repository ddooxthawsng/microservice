package com.thangdm.account_service.controller;

import com.thangdm.common.dto.ApiResponse;
import com.thangdm.account_service.dto.request.UserCreationRequest;
import com.thangdm.account_service.dto.request.UserUpdateRequest;
import com.thangdm.account_service.dto.response.UserResponse;
import com.thangdm.account_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import com.thangdm.account_service.annotation.LogAudit;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @LogAudit(action = "CREATE_USER", details = "Create new user")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        UserResponse result = userService.createUser(request);
        return ApiResponse.<UserResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_VIEW')")
    ApiResponse<List<UserResponse>> getUsers() {
        List<UserResponse> result = userService.getUsers();
        return ApiResponse.<List<UserResponse>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        UserResponse result = userService.getUser(userId);
        return ApiResponse.<UserResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    ApiResponse<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse result = userService.getUserByUsername(username);
        return ApiResponse.<UserResponse>builder()
                .result(result)
                .build();
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @LogAudit(action = "UPDATE_USER", details = "Update user info")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        UserResponse result = userService.updateUser(userId, request);
        return ApiResponse.<UserResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        UserResponse result = userService.getMyInfo();
        return ApiResponse.<UserResponse>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @LogAudit(action = "DELETE_USER", details = "Delete user")
    ApiResponse<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<Void>builder().build();
    }
}
