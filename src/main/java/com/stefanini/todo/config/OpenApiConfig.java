package com.stefanini.todo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI todoOpenApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("To-Do List API")
                        .description("API para gerenciamento de tarefas (criar, listar, editar, excluir).")
                        .version("v1")
                        .contact(new Contact().name("Equipe Stefannini"))
                        .license(new License().name("MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Requisitos do desafio")
                        .url("https://example.com"));
    }
}
