// com.pruebas.pruebas.controller.EstadisticasController.java
package com.pruebas.pruebas.resource;

import com.pruebas.pruebas.service.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/estadisticas")
@CrossOrigin(origins = {"http://localhost:8080", "http://192.168.0.117:8080"})
public class EstadisticasResource {

    @Autowired
    private EstadisticasService estadisticasService;

    @GetMapping("/encuesta/{encuestaId}")
    public ResponseEntity<?> obtenerEstadisticasEncuesta(@PathVariable Long encuestaId) {
        try {
            Map<String, Object> estadisticas = estadisticasService.obtenerEstadisticasEncuesta(encuestaId);
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Encuesta no encontrada: " + e.getMessage()));
        }
    }

    @GetMapping("/encuesta/{encuestaId}/resumen")
    public ResponseEntity<?> obtenerResumenEncuesta(@PathVariable Long encuestaId) {
        try {
            Map<String, Object> resumen = estadisticasService.obtenerEstadisticasEncuesta(encuestaId);
            // Puedes personalizar el resumen aquí si necesitas menos datos
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Encuesta no encontrada: " + e.getMessage()));
        }
    }
}