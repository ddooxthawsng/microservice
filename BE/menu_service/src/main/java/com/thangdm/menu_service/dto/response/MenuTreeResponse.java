package com.thangdm.menu_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuTreeResponse {
    String id;
    String name;
    String description;
    String path;
    String icon;
    String parentId;
    Integer sortOrder;
    Boolean isActive;
    
    @Builder.Default
    List<MenuTreeResponse> children = new ArrayList<>();
}
