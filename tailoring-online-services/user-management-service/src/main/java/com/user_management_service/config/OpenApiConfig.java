package com.user_management_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "User Management Service API",
                version = "v1",
                description = "API documentation for the User Management Service",
                license = @License(name = "Portfolio", url = "https://yassineoularbi.github.io"),
                contact = @Contact(
                            name = "Yassine Oularbi",
                            email = "yassineoularbi4@gmail.com",
                            url = "tel:+212610446080"
                )
        ),
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@RequiredArgsConstructor
public class OpenApiConfig {

    private final Environment environment;

    @Bean
    public OpenAPI userOpenAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url(environment.getProperty("openapi.service.url"))))
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title(environment.getProperty("openapi.service.title"))
                        .version(environment.getProperty("openapi.service.version")))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement()
                        .addList("Bearer Authentication"));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
