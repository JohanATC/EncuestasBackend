package com.pruebas.pruebas.integration;

import com.pruebas.pruebas.dto.EncuestaDTO;
import com.pruebas.pruebas.dto.PreguntaDTO;
import com.pruebas.pruebas.dto.RespuestaDTO;
import com.pruebas.pruebas.service.EncuestaService;
import com.pruebas.pruebas.service.PreguntaService;
import com.pruebas.pruebas.service.RespuestaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EncuestaPreguntaRespuestaIntegrationTest {

    @Autowired
    private EncuestaService encuestaService;

    @Autowired
    private PreguntaService preguntaService;

    @Autowired
    private RespuestaService respuestaService;

    @Test
    void testFlujoCompleto() {
        // Crear encuesta
        EncuestaDTO encuesta = new EncuestaDTO();
        encuesta.setTitulo("Encuesta General");
        EncuestaDTO encuestaCreada = encuestaService.create(encuesta);
        assertNotNull(encuestaCreada.getId());

        // Crear pregunta asociada
        PreguntaDTO pregunta = new PreguntaDTO();
        pregunta.setTextoPregunta("¿Te gustó la atención?");
        pregunta.setTipo("abierta");
        pregunta.setIdEncuesta(encuestaCreada.getId());
        PreguntaDTO preguntaCreada = preguntaService.create(pregunta);
        assertNotNull(preguntaCreada.getId());

        // Crear respuesta asociada
        RespuestaDTO respuesta = new RespuestaDTO();
        respuesta.setRespuesta("Excelente servicio");
        RespuestaDTO respuestaCreada = respuestaService.create(preguntaCreada.getId(), respuesta);

        assertEquals("Excelente servicio", respuestaCreada.getRespuesta());
    }
}
