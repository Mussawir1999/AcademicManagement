package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI schoolServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Academic Management API")
                        .description("A simple REST API for managing students, courses, enrollments, and grades.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your@email.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
