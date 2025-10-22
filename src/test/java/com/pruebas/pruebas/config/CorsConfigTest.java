package com.pruebas.pruebas.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistration;

import static org.mockito.Mockito.*;

class CorsConfigTest {

    @Test
    void testCorsMappingsAreConfigured() {
        CorsConfig config = new CorsConfig();
        CorsRegistry registry = mock(CorsRegistry.class);

        // Crear UN mock que se devuelve a sí mismo en cada llamada
        CorsRegistration corsRegistration = mock(CorsRegistration.class, RETURNS_SELF);

        when(registry.addMapping("/api/**")).thenReturn(corsRegistration);

        config.addCorsMappings(registry);

        // Verificaciones
        verify(registry).addMapping("/api/**");
        verify(corsRegistration).allowedOrigins(
                "http://localhost:8080",
                "http://192.168.0.117:8080",
                "http://192.168.0.117:8081",
                "http://10.0.2.2:8080",
                "http://10.0.2.2:8081",
                "http://[::1]:8080",
                "http://[::1]:8081"
        );
        verify(corsRegistration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(corsRegistration).allowedHeaders("*");
        verify(corsRegistration).allowCredentials(true);
        verify(corsRegistration).maxAge(3600L);
    }
}