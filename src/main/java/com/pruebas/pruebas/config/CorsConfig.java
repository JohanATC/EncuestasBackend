package com.pruebas.pruebas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <h2>Configuración global de CORS</h2>
 *
 * Esta clase define las reglas de intercambio de recursos de origen cruzado (CORS)
 * para el backend de encuestas.
 * Permite que el frontend, alojado localmente o en una red interna,
 * pueda comunicarse con los endpoints REST del backend.
 *
 * <p><b>Detalles:</b></p>
 * <ul>
 *   <li>Aplica las reglas a todos los endpoints bajo la ruta <code>/api/**</code>.</li>
 *   <li>Permite solicitudes desde orígenes locales y de red (localhost e IP interna).</li>
 *   <li>Admite los métodos HTTP más comunes: GET, POST, PUT, DELETE y OPTIONS.</li>
 *   <li>Autoriza el envío de credenciales (cookies, encabezados de autenticación, etc.).</li>
 * </ul>
 *
 * <p>Esta configuración es esencial cuando el frontend y el backend
 * se ejecutan en diferentes dominios o puertos, especialmente durante el desarrollo.</p>
 *
 * @author Johan
 * @version 1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configura las reglas CORS globales del backend.
     *
     * @param registry el registro de mapeos CORS donde se definen los orígenes, métodos y encabezados permitidos.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:8080",        // Frontend en Tomcat local
                        "http://192.168.0.108:8080",   // IP local + Tomcat
                        "http://192.168.0.108:8081"    // IP local + Spring Boot
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
