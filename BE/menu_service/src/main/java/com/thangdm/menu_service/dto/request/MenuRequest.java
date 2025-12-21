package com.thangdm.menu_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuRequest {
    @NotBlank(message = "Menu name is required")
    String name;

    String description;

    @NotBlank(message = "Menu path is required")
    String path;

    String icon;

    String parentId;

    @NotNull(message = "Sort order is required")
    Integer sortOrder;

    @NotNull(message = "Active status is required")
    Boolean isActive;
}
