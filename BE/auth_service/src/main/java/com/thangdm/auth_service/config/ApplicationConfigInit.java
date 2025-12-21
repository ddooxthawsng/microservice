package com.thangdm.auth_service.config;

import com.thangdm.auth_service.entity.Permission;
import com.thangdm.auth_service.entity.Role;
import com.thangdm.auth_service.entity.User;
import com.thangdm.auth_service.repository.PermissionRepository;
import com.thangdm.auth_service.repository.RoleRepository;
import com.thangdm.auth_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thangdm.auth_service.enums.PredefinedPermission;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationConfigInit {

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            // Seed Permissions from Enum
            Set<Permission> permissions = new HashSet<>();
            for (PredefinedPermission predefinedPermission : PredefinedPermission.values()) {
                Permission permission = permissionRepository.findById(predefinedPermission.getName())
                    .orElseGet(() -> permissionRepository.save(
                        Permission.builder()
                            .name(predefinedPermission.getName())
                            .description(predefinedPermission.getDescription())
                            .build()
                    ));
                permissions.add(permission);
            }

            // Seed Role
            Role adminRole = roleRepository.findById("ADMIN")
                .orElseGet(() -> roleRepository.save(
                    Role.builder().name("ADMIN").description("Administrator").permissions(permissions).build()
                ));
            
            // Update Role permissions if needed
            adminRole.setPermissions(permissions);
            roleRepository.save(adminRole);

            // Seed User
            if (userRepository.findByUsername("admin").isEmpty()) {
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .firstName("Admin")
                        .lastName("System")
                        .build();

                userRepository.save(user);
                log.warn("ADMIN USER CREATED DEFAULT : admin / admin");
            }
        };
    }
}
