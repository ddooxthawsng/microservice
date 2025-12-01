package com.thangdm.auth_service.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {
    private SecurityUtils() {
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getCurrentUsername() {
        Authentication authentication = getAuthentication();
        if (authentication == null) return null;

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof Jwt) {
            return ((Jwt) principal).getClaimAsString("preferred_username");
        } else if (principal instanceof String) {
            return (String) principal;
        }
        return null;
    }

    public static String getCurrentToken() {
        Authentication authentication = getAuthentication();
        if (authentication == null) return null;

        Object credentials = authentication.getCredentials();
        if (credentials instanceof String) {
            return (String) credentials;
        }
        return null;
    }
}
