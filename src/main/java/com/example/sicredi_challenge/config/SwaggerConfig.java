package com.example.sicredi_challenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI sicrediOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sicredi Challenge API")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de pautas e votações"));
    }
}