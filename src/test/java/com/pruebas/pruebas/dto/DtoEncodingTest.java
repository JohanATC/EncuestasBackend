package com.pruebas.pruebas.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

/**
 * Pruebas unitarias para validar que los DTOs soporten correctamente caracteres especiales.
 */
public class DtoEncodingTest {

    @Test
    @DisplayName("Verificar que EncuestaDTO maneje tildes, ñ y caracteres especiales correctamente")
    void testCaracteresEspecialesEnEncuestaDTO() {
        EncuestaDTO dto = new EncuestaDTO();
        dto.setTitulo("Encuesta de opinión pública — Año 2025 (versión ß)");
        dto.setDescripcion("¿Cuál es tu canción favorita? ¡Dímelo ahora mismo! Á, É, Í, Ó, Ú, ñ, /, (), %, &");
        dto.setEstado("ACTIVA");
        dto.setFechaCreacion(new Date());

        assertTrue(dto.getTitulo().contains("—"));
        assertTrue(dto.getDescripcion().contains("Á"));
        assertTrue(dto.getDescripcion().contains("ñ"));
        assertTrue(dto.getDescripcion().contains("/"));
    }

    @Test
    @DisplayName("Verificar que PreguntaDTO y RespuestaDTO mantengan codificación UTF-8")
    void testCaracteresEspecialesEnPreguntaYRespuestaDTO() {
        PreguntaDTO pregunta = new PreguntaDTO();
        pregunta.setIdEncuesta(1L);
        pregunta.setTextoPregunta("¿Qué opinas de la canción 'Corazón (💖) roto'?");
        pregunta.setTipo("texto");
        pregunta.setOpciones(List.of("Sí", "No", "Tal vez (🤔)"));
        pregunta.setOrden(1);

        RespuestaDTO respuesta = new RespuestaDTO();
        respuesta.setIdPregunta(1L);
        respuesta.setRespuesta("Me encanta, ¡es genial! 😍 ñ / () ÁÉÍÓÚ");
        respuesta.setFechaRespuesta(new Date());

        assertTrue(pregunta.getTextoPregunta().contains("💖"));
        assertTrue(pregunta.getOpciones().get(2).contains("🤔"));
        assertTrue(respuesta.getRespuesta().contains("ñ"));
        assertTrue(respuesta.getRespuesta().contains("😍"));
        assertTrue(respuesta.getRespuesta().contains("/"));
    }
}
