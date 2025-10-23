// src/test/java/com/pruebas/pruebas/util/TestDataBuilder.java
package com.pruebas.pruebas.util;

import com.pruebas.pruebas.dto.EncuestaDTO;
import com.pruebas.pruebas.dto.PreguntaDTO;
import com.pruebas.pruebas.dto.RespuestaDTO;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestDataBuilder {

    public static EncuestaDTO buildEncuestaDTO() {
        EncuestaDTO encuesta = new EncuestaDTO();
        encuesta.setTitulo("Encuesta de Satisfacción - Test " + System.currentTimeMillis());
        encuesta.setDescripcion("Encuesta automática generada para testing");
        encuesta.setFechaCreacion(new Date());
        encuesta.setEstado("ACTIVA");
        return encuesta;
    }

    public static PreguntaDTO buildPreguntaDTO(Long encuestaId, String tipo) {
        PreguntaDTO pregunta = new PreguntaDTO();
        pregunta.setTextoPregunta("¿Cómo calificarías nuestro servicio?");
        pregunta.setTipo(tipo);
        pregunta.setOrden(1);
        pregunta.setIdEncuesta(encuestaId);

        // Configurar opciones según el tipo
        switch (tipo.toLowerCase()) {
            case "opcion_multiple":
                pregunta.setOpciones(Arrays.asList("Excelente", "Bueno", "Regular", "Malo"));
                break;
            case "seleccion_unica":
                pregunta.setOpciones(Arrays.asList("Opción A", "Opción B", "Opción C"));
                break;
            case "si_no":
                pregunta.setOpciones(Arrays.asList("Sí", "No"));
                break;
            case "verdadero_falso":
                pregunta.setOpciones(Arrays.asList("Verdadero", "Falso"));
                break;
            case "escala":
                pregunta.setOpciones(Arrays.asList("1", "2", "3", "4", "5"));
                break;
            default:
                pregunta.setOpciones(null); // Para preguntas abiertas
        }

        return pregunta;
    }

    public static RespuestaDTO buildRespuestaDTO(Long preguntaId, String valor) {
        RespuestaDTO respuesta = new RespuestaDTO();
        respuesta.setRespuesta(valor);
        respuesta.setIdPregunta(preguntaId);
        respuesta.setFechaRespuesta(new Date());
        return respuesta;
    }

    public static List<PreguntaDTO> buildMultiplePreguntasDTO(Long encuestaId) {
        return Arrays.asList(
                buildPreguntaDTO(encuestaId, "opcion_multiple"),
                buildPreguntaDTO(encuestaId, "si_no"),
                buildPreguntaDTO(encuestaId, "escala"),
                buildPreguntaConTexto("¿Qué mejorarías en nuestro servicio?", encuestaId, "texto_libre")
        );
    }

    private static PreguntaDTO buildPreguntaConTexto(String texto, Long encuestaId, String tipo) {
        PreguntaDTO pregunta = buildPreguntaDTO(encuestaId, tipo);
        pregunta.setTextoPregunta(texto);
        pregunta.setOpciones(null);
        return pregunta;
    }

    public static EncuestaDTO buildEncuestaCompleta() {
        EncuestaDTO encuesta = buildEncuestaDTO();
        encuesta.setTitulo("Encuesta Completa de Usabilidad - " + System.currentTimeMillis());
        return encuesta;
    }
}