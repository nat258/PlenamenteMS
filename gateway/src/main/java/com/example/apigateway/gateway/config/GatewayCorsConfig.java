package com.example.apigateway.gateway.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class GatewayCorsConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE) // Esto asegura que sea lo primero que se ejecute al llegar una petición externa
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // 1. Permitimos el acceso desde CUALQUIER origen (Muy útil para desarrollo local)
        corsConfig.setAllowedOriginPatterns(List.of("*"));
        
        // 2. Permitimos todos los métodos HTTP que usan tus microservicios clínicos
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // 3. Permitimos que viajen todas las cabeceras estándar o personalizadas (Tokens JWT, Content-Type, etc.)
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setExposedHeaders(List.of("*"));
        
        // 4. Si permites el origen "*", esto por seguridad DEBE estar en "false" para evitar conflictos
        corsConfig.setAllowCredentials(false);
        
        // 5. Tiempo en segundos que el navegador recordará esta configuración de permisos (1 hora)
        corsConfig.setMaxAge(3600L);

        // Usamos la fuente de configuración REACTIVA (especial para Spring Cloud Gateway)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}