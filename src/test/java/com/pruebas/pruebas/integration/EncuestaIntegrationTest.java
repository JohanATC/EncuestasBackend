// src/test/java/com/pruebas/pruebas/integration/EncuestaIntegrationTest.java
package com.pruebas.pruebas.integration;

import com.pruebas.pruebas.dto.EncuestaDTO;
import com.pruebas.pruebas.dto.PreguntaDTO;
import com.pruebas.pruebas.dto.RespuestaDTO;
import com.pruebas.pruebas.service.EncuestaService;
import com.pruebas.pruebas.service.PreguntaService;
import com.pruebas.pruebas.service.RespuestaService;
import com.pruebas.pruebas.util.TestDataBuilder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class EncuestaIntegrationTest {

    @Autowired
    private EncuestaService encuestaService;

    @Autowired
    private PreguntaService preguntaService;

    @Autowired
    private RespuestaService respuestaService;

    private static Long encuestaId;
    private static Long preguntaOpcionMultipleId;
    private static Long preguntaSiNoId;

    @Test
    @Order(1)
    void testCrearEncuestaAutomatica() {
        System.out.println("=== TEST 1: CREANDO ENCUESTA AUTOMÁTICA ===");

        EncuestaDTO encuestaDTO = TestDataBuilder.buildEncuestaDTO();
        EncuestaDTO encuestaCreada = encuestaService.create(encuestaDTO);

        assertNotNull(encuestaCreada.getId());
        assertEquals("ACTIVA", encuestaCreada.getEstado());
        encuestaId = encuestaCreada.getId();

        System.out.println("✅ Encuesta creada - ID: " + encuestaId + " - Título: " + encuestaCreada.getTitulo());
    }

    @Test
    @Order(2)
    void testCrearPreguntasAutomaticas() {
        System.out.println("=== TEST 2: CREANDO PREGUNTAS AUTOMÁTICAS ===");

        List<PreguntaDTO> preguntas = TestDataBuilder.buildMultiplePreguntasDTO(encuestaId);

        for (PreguntaDTO preguntaDTO : preguntas) {
            PreguntaDTO preguntaCreada = preguntaService.create(preguntaDTO);
            assertNotNull(preguntaCreada.getId());
            assertNotNull(preguntaCreada.getOrden());

            // Guardar IDs para usar en respuestas
            if (preguntaCreada.getTipo().equals("opcion_multiple")) {
                preguntaOpcionMultipleId = preguntaCreada.getId();
            } else if (preguntaCreada.getTipo().equals("si_no")) {
                preguntaSiNoId = preguntaCreada.getId();
            }

            System.out.println("✅ Pregunta creada - ID: " + preguntaCreada.getId() +
                    " - Tipo: " + preguntaCreada.getTipo() +
                    " - Orden: " + preguntaCreada.getOrden());
        }

        assertNotNull(preguntaOpcionMultipleId);
        assertNotNull(preguntaSiNoId);
    }

    @Test
    @Order(3)
    void testCrearRespuestasAutomaticas() {
        System.out.println("=== TEST 3: CREANDO RESPUESTAS AUTOMÁTICAS ===");

        // Respuestas para pregunta de opción múltiple
        String[] respuestasMultiples = {"Excelente", "Bueno", "Regular", "Malo"};
        for (String respuestaValor : respuestasMultiples) {
            RespuestaDTO respuestaDTO = TestDataBuilder.buildRespuestaDTO(preguntaOpcionMultipleId, respuestaValor);
            RespuestaDTO respuestaCreada = respuestaService.create(preguntaOpcionMultipleId, respuestaDTO);

            assertNotNull(respuestaCreada.getId());
            assertEquals(respuestaValor, respuestaCreada.getRespuesta());
            System.out.println("✅ Respuesta creada - Pregunta " + preguntaOpcionMultipleId +
                    " - Valor: " + respuestaValor);
        }

        // Respuestas para pregunta Sí/No
        String[] respuestasSiNo = {"Sí", "No", "Sí", "No", "Sí"};
        for (String respuestaValor : respuestasSiNo) {
            RespuestaDTO respuestaDTO = TestDataBuilder.buildRespuestaDTO(preguntaSiNoId, respuestaValor);
            RespuestaDTO respuestaCreada = respuestaService.create(preguntaSiNoId, respuestaDTO);

            assertNotNull(respuestaCreada.getId());
            System.out.println("✅ Respuesta creada - Pregunta " + preguntaSiNoId +
                    " - Valor: " + respuestaValor);
        }
    }

    @Test
    @Order(4)
    void testFlujoCompletoConValidaciones() {
        System.out.println("=== TEST 4: VALIDANDO FLUJO COMPLETO ===");

        // Verificar encuesta
        EncuestaDTO encuestaRecuperada = encuestaService.getById(encuestaId);
        assertNotNull(encuestaRecuperada);
        assertEquals("ACTIVA", encuestaRecuperada.getEstado());
        System.out.println("✅ Encuesta verificada - Estado: " + encuestaRecuperada.getEstado());

        // Verificar preguntas
        List<PreguntaDTO> todasPreguntas = preguntaService.getAll();
        long preguntasDeEstaEncuesta = todasPreguntas.stream()
                .filter(p -> p.getIdEncuesta().equals(encuestaId))
                .count();
        assertTrue(preguntasDeEstaEncuesta >= 3);
        System.out.println("✅ Preguntas verificadas - Total: " + preguntasDeEstaEncuesta);
    }

    @Test
    @Order(5)
    void testCrearEncuestaCompletaEnUnMetodo() {
        System.out.println("=== TEST 5: ENCUESTA COMPLETA EN UN SOLO MÉTODO ===");

        // Crear encuesta
        EncuestaDTO encuestaCompleta = TestDataBuilder.buildEncuestaCompleta();
        EncuestaDTO encuestaCreada = encuestaService.create(encuestaCompleta);
        Long nuevaEncuestaId = encuestaCreada.getId();

        // Crear diferentes tipos de preguntas
        String[] tiposPreguntas = {"opcion_multiple", "si_no", "escala", "verdadero_falso", "texto_libre"};

        for (int i = 0; i < tiposPreguntas.length; i++) {
            PreguntaDTO preguntaDTO = TestDataBuilder.buildPreguntaDTO(nuevaEncuestaId, tiposPreguntas[i]);
            preguntaDTO.setOrden(i + 1);
            preguntaDTO.setTextoPregunta("Pregunta de prueba " + (i + 1) + " - Tipo: " + tiposPreguntas[i]);

            PreguntaDTO preguntaCreada = preguntaService.create(preguntaDTO);

            // Crear algunas respuestas para cada pregunta
            if (!"texto_libre".equals(tiposPreguntas[i])) {
                crearRespuestasDePrueba(preguntaCreada.getId(), tiposPreguntas[i]);
            }
        }

        System.out.println("✅ Encuesta completa creada - ID: " + nuevaEncuestaId + " con " +
                tiposPreguntas.length + " tipos de preguntas diferentes");
    }

    private void crearRespuestasDePrueba(Long preguntaId, String tipoPregunta) {
        String[] respuestas;

        switch (tipoPregunta) {
            case "opcion_multiple":
                respuestas = new String[]{"Excelente", "Bueno", "Regular"};
                break;
            case "si_no":
                respuestas = new String[]{"Sí", "No", "Sí"};
                break;
            case "escala":
                respuestas = new String[]{"3", "4", "5", "2"};
                break;
            case "verdadero_falso":
                respuestas = new String[]{"Verdadero", "Falso", "Verdadero"};
                break;
            default:
                respuestas = new String[]{"Respuesta de prueba"};
        }

        for (String respuestaValor : respuestas) {
            RespuestaDTO respuestaDTO = TestDataBuilder.buildRespuestaDTO(preguntaId, respuestaValor);
            respuestaService.create(preguntaId, respuestaDTO);
        }
    }
}