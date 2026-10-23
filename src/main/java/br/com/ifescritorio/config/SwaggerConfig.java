package br.com.ifescritorio.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName))

                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,

                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                ))

                .info(
                        new Info()
                                .title("IFES Escritório API")
                                .version("1.0")
                                .description("API do Sistema de Gestão Patrimonial do IFES Escritório")
                                .contact(
                                        new Contact()
                                                .name("Equipe IFES Escritório")
                                                .email("suporte@ifes.br")
                                ));
    }

    @Bean
    public GroupedOpenApi api() {

        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .pathsToExclude(
                        "/error",
                        "/actuator/**")
                .build();
    }
}