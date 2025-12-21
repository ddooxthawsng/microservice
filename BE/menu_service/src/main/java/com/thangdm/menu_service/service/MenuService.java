package com.thangdm.menu_service.service;

import com.thangdm.menu_service.dto.request.MenuRequest;
import com.thangdm.menu_service.dto.response.MenuResponse;
import com.thangdm.menu_service.dto.response.MenuTreeResponse;
import com.thangdm.menu_service.entity.Menu;
import com.thangdm.menu_service.mapper.MenuMapper;
import com.thangdm.menu_service.repository.MenuRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuService {
    MenuRepository menuRepository;
    MenuMapper menuMapper;

    public MenuResponse createMenu(MenuRequest request) {
        Menu menu = menuMapper.toMenu(request);
        menu = menuRepository.save(menu);
        return menuMapper.toMenuResponse(menu);
    }

    public MenuResponse updateMenu(String id, MenuRequest request) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + id));
        
        menuMapper.updateMenu(menu, request);
        menu = menuRepository.save(menu);
        return menuMapper.toMenuResponse(menu);
    }

    public void deleteMenu(String id) {
        menuRepository.deleteById(id);
    }

    public List<MenuResponse> getAllMenus() {
        return menuRepository.findAllByOrderBySortOrderAsc()
                .stream()
                .map(menuMapper::toMenuResponse)
                .toList();
    }

    public MenuResponse getMenuById(String id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + id));
        return menuMapper.toMenuResponse(menu);
    }

    public List<MenuResponse> getActiveMenus() {
        return menuRepository.findByIsActive(true)
                .stream()
                .map(menuMapper::toMenuResponse)
                .toList();
    }

    public List<MenuTreeResponse> getMenuTree() {
        List<Menu> allMenus = menuRepository.findAllByOrderBySortOrderAsc();
        
        // Convert to tree responses
        Map<String, MenuTreeResponse> menuMap = allMenus.stream()
                .collect(Collectors.toMap(
                        Menu::getId,
                        menuMapper::toMenuTreeResponse
                ));

        // Build tree structure
        List<MenuTreeResponse> rootMenus = new ArrayList<>();
        
        for (Menu menu : allMenus) {
            MenuTreeResponse treeNode = menuMap.get(menu.getId());
            
            if (menu.getParentId() == null || menu.getParentId().isEmpty()) {
                // Root level menu
                rootMenus.add(treeNode);
            } else {
                // Child menu - add to parent's children
                MenuTreeResponse parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    parent.getChildren().add(treeNode);
                }
            }
        }
        
        return rootMenus;
    }
}
