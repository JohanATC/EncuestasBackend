package com.pruebas.pruebas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:8080",        // NetBeans Tomcat local
                        "http://192.168.0.117:8080",   // Tu IP + Tomcat
                        "http://192.168.0.117:8081",   // Tu IP + Spring Boot
                        "http://10.0.2.2:8080",        // Emulador Android
                        "http://10.0.2.2:8081",        // Emulador Android + Spring Boot
                        "http://[::1]:8080",           // IPv6 localhost
                        "http://[::1]:8081"            // IPv6 Spring Boot
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // Cache preflight requests por 1 hora
    }
}