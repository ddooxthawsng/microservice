package com.thangdm.menu_service.controller;

import com.thangdm.common.dto.ApiResponse;
import com.thangdm.menu_service.dto.request.MenuRequest;
import com.thangdm.menu_service.dto.response.MenuResponse;
import com.thangdm.menu_service.dto.response.MenuTreeResponse;
import com.thangdm.menu_service.service.MenuService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuController {
    MenuService menuService;

    @PostMapping
    @PreAuthorize("hasAuthority('MENU_CREATE')")
    ApiResponse<MenuResponse> createMenu(@RequestBody @Valid MenuRequest request) {
        MenuResponse result = menuService.createMenu(request);
        return ApiResponse.<MenuResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MENU_VIEW')")
    ApiResponse<List<MenuResponse>> getAllMenus() {
        List<MenuResponse> result = menuService.getAllMenus();
        return ApiResponse.<List<MenuResponse>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('MENU_VIEW')")
    ApiResponse<List<MenuResponse>> getActiveMenus() {
        List<MenuResponse> result = menuService.getActiveMenus();
        return ApiResponse.<List<MenuResponse>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('MENU_VIEW')")
    ApiResponse<List<MenuTreeResponse>> getMenuTree() {
        List<MenuTreeResponse> result = menuService.getMenuTree();
        return ApiResponse.<List<MenuTreeResponse>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MENU_VIEW')")
    ApiResponse<MenuResponse> getMenuById(@PathVariable String id) {
        MenuResponse result = menuService.getMenuById(id);
        return ApiResponse.<MenuResponse>builder()
                .result(result)
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MENU_UPDATE')")
    ApiResponse<MenuResponse> updateMenu(@PathVariable String id, @RequestBody @Valid MenuRequest request) {
        MenuResponse result = menuService.updateMenu(id, request);
        return ApiResponse.<MenuResponse>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MENU_DELETE')")
    ApiResponse<Void> deleteMenu(@PathVariable String id) {
        menuService.deleteMenu(id);
        return ApiResponse.<Void>builder().build();
    }
}
