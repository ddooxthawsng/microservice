package com.thangdm.account_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(
            @Value("${spring.application.name}") String appName) {
        return new OpenAPI()
                .servers(List.of(new Server().url("http://localhost:8082")))
                .info(new Info()
                        .title(appName + " API")
                        .version("1.0")
                        .description("Account Management Service - User, Role, Permission CRUD")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
