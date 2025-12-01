package com.thangdm.auth_service.controller;

import com.thangdm.auth_service.dto.request.ApiResponse;
import com.thangdm.auth_service.dto.request.LoginRequest;
import com.thangdm.auth_service.dto.request.UserCreationRequest;
import com.thangdm.auth_service.dto.request.UserUpdateRequest;
import com.thangdm.auth_service.dto.response.UserResponse;
import com.thangdm.auth_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse response = new ApiResponse();
        response.setResult(userService.createUser(request));
        return response;
    }

    @GetMapping
    List<UserResponse> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }

    @PostMapping("/login")
    ApiResponse<UserResponse> login(@RequestBody @Valid LoginRequest request) {
        UserResponse user = userService.login(request.getUsername(), request.getPassword());

        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Login successful");
        response.setResult(user);

        return response;
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> myInfo(@RequestBody @Valid LoginRequest request) {
        UserResponse user = userService.getMyInfo();
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setResult(user);
        return response;
    }
}