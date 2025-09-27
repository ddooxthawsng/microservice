package com.thangdm.auth_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "security")
@Data
public class BasicUserProperties {
    private List<UserConfig> users;

    @Data
    public static class UserConfig {
        private String username;
        private String password;
        private String roles;
    }
}