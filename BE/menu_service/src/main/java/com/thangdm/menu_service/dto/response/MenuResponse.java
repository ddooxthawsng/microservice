package com.thangdm.menu_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuResponse {
    String id;
    String name;
    String description;
    String path;
    String icon;
    String parentId;
    Integer sortOrder;
    Boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
