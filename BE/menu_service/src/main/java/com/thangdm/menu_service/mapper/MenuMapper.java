package com.thangdm.menu_service.mapper;

import com.thangdm.menu_service.dto.request.MenuRequest;
import com.thangdm.menu_service.dto.response.MenuResponse;
import com.thangdm.menu_service.dto.response.MenuTreeResponse;
import com.thangdm.menu_service.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MenuMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Menu toMenu(MenuRequest request);

    MenuResponse toMenuResponse(Menu menu);

    MenuTreeResponse toMenuTreeResponse(Menu menu);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateMenu(@MappingTarget Menu menu, MenuRequest request);
}
