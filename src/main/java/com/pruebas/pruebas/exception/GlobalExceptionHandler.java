package com.pruebas.pruebas.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la aplicación.
 *
 * Captura excepciones de tipo {@link RuntimeException} y devuelve
 * un mensaje estandarizado en el cuerpo de la respuesta HTTP.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de tipo {@link RuntimeException}.
     *
     * @param ex Excepción lanzada en cualquier parte de la aplicación
     * @return ResponseEntity con un mapa que contiene el mensaje de error
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}

