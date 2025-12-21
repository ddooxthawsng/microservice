package com.thangdm.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * ⚠️ CORS CONFIGURATION - DISABLED
 * 
 * IMPORTANT: CORS configuration has been moved to API Gateway.
 * This class is kept for reference but should NOT be enabled in production.
 * 
 * WHY CORS SHOULD BE AT API GATEWAY:
 * 
 * 1. SINGLE POINT OF ENTRY
 *    - All client requests go through API Gateway (port 8080)
 *    - Backend services (auth, product, order) are internal
 *    - CORS only needed at the entry point (Gateway)
 * 
 * 2. AVOID DUPLICATE CONFIGURATION
 *    - If CORS enabled here + Gateway → duplicate headers
 *    - Can cause CORS errors or conflicts
 *    - Hard to maintain across multiple services
 * 
 * 3. SECURITY
 *    - Backend services should only accept requests from Gateway
 *    - No direct client access to backend services
 *    - Gateway acts as security layer
 * 
 * 4. CENTRALIZED MANAGEMENT
 *    - One place to configure CORS for all services
 *    - Easy to update allowed origins
 *    - Consistent CORS policy across microservices
 * 
 * ARCHITECTURE:
 * ┌──────────┐
 * │  Client  │
 * └────┬─────┘
 *      │
 *      ▼
 * ┌─────────────────┐
 * │  API Gateway    │ ← ✅ CORS enabled here
 * │  (port 8080)    │
 * └────┬────────────┘
 *      │
 *      ├─────────────────┐
 *      ▼                 ▼
 * ┌──────────┐    ┌──────────┐
 * │   Auth   │    │ Product  │ ← ❌ NO CORS needed
 * │ (8081)   │    │ (8082)   │
 * └──────────┘    └──────────┘
 * 
 * WHEN TO ENABLE THIS:
 * - Only for local development/testing when bypassing Gateway
 * - Use Spring Profile: @Profile("dev-direct")
 * - NOT for production use
 * 
 * CURRENT STATUS: DISABLED (commented out)
 * CORS LOCATION: api-gateway/src/main/java/com/thangdm/gateway/config/CorsConfig.java
 * 
 * Last updated: 2025-12-19
 */

// ❌ DISABLED - CORS now handled at API Gateway
// Uncomment only for direct service testing (bypass Gateway)

/*
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allowed origins
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        
        // Allowed methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Allowed headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Allow credentials
        configuration.setAllowCredentials(true);
        
        // Max age
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
*/

