package com.example.ms_pacientes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI custOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API 2026 Reservas horas servicio de psicologia")
                        .version("1.0")
                        .description("Documentacion de la API al sistema de reserva de horas"));
    }

}
