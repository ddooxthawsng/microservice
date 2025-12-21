package com.thangdm.auth_service.enums;

import lombok.Getter;

@Getter
public enum PredefinedPermission {
    USER_VIEW("USER_VIEW", "View user information"),
    USER_CREATE("USER_CREATE", "Create new user"),
    USER_UPDATE("USER_UPDATE", "Update user information"),
    USER_DELETE("USER_DELETE", "Delete user"),
    
    MENU_VIEW("MENU_VIEW", "View menu items"),
    MENU_CREATE("MENU_CREATE", "Create menu items"),
    MENU_UPDATE("MENU_UPDATE", "Update menu items"),
    MENU_DELETE("MENU_DELETE", "Delete menu items"),
    
    AUDIT_VIEW("AUDIT_VIEW", "View system audit logs"),
    AUDIT_DELETE("AUDIT_DELETE", "Delete audit logs (Caution)"),
    ;

    private final String name;
    private final String description;

    PredefinedPermission(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
