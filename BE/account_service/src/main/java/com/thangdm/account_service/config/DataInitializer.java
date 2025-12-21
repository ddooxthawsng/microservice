package com.thangdm.account_service.config;

import com.thangdm.account_service.constants.PredefinedPermission;
import com.thangdm.account_service.entity.Permission;
import com.thangdm.account_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataInitializer implements CommandLineRunner {

    PermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (permissionRepository.count() == 0) {
            permissionRepository.saveAll(List.of(
                    Permission.builder()
                            .name(PredefinedPermission.MENU_VIEW)
                            .description("View menus")
                            .build(),
                    Permission.builder()
                            .name(PredefinedPermission.MENU_CREATE)
                            .description("Create new menu")
                            .build(),
                    Permission.builder()
                            .name(PredefinedPermission.MENU_UPDATE)
                            .description("Update existing menu")
                            .build(),
                    Permission.builder()
                            .name(PredefinedPermission.MENU_DELETE)
                            .description("Delete menu")
                            .build()
            ));
        } else {
             // Ensure Menu permissions exist even if other permissions exist
             // This uses upsert since 'name' is @Id
             permissionRepository.saveAll(List.of(
                    Permission.builder()
                            .name(PredefinedPermission.MENU_VIEW)
                            .description("View menus")
                            .build(),
                    Permission.builder()
                            .name(PredefinedPermission.MENU_CREATE)
                            .description("Create new menu")
                            .build(),
                    Permission.builder()
                            .name(PredefinedPermission.MENU_UPDATE)
                            .description("Update existing menu")
                            .build(),
                    Permission.builder()
                            .name(PredefinedPermission.MENU_DELETE)
                            .description("Delete menu")
                            .build(),
                    Permission.builder()
                            .name(PredefinedPermission.USER_VIEW)
                            .description("View users")
                            .build(),
                    Permission.builder()
                            .name(PredefinedPermission.USER_CREATE)
                            .description("Create new user")
                            .build(),
                    Permission.builder()
                            .name(PredefinedPermission.USER_UPDATE)
                            .description("Update existing user")
                            .build(),
                    Permission.builder()
                            .name(PredefinedPermission.USER_DELETE)
                            .description("Delete user")
                            .build()
            ));
        }
    }
}
