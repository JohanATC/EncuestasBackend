package com.pruebas.pruebas.service;

import com.pruebas.pruebas.dto.RespuestaDTO;
import com.pruebas.pruebas.entity.Pregunta;
import com.pruebas.pruebas.entity.Respuesta;
import com.pruebas.pruebas.mapper.RespuestaMapper;
import com.pruebas.pruebas.repository.PreguntaRepository;
import com.pruebas.pruebas.repository.RespuestaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class RespuestaServiceTest {

    @Mock
    private RespuestaRepository respuestaRepository;

    @Mock
    private PreguntaRepository preguntaRepository;

    @Mock
    private RespuestaMapper respuestaMapper;

    @InjectMocks
    private RespuestaService respuestaService;

    private Respuesta respuesta;
    private RespuestaDTO dto;
    private Pregunta pregunta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pregunta = new Pregunta();
        pregunta.setIdPregunta(1L);

        respuesta = new Respuesta();
        respuesta.setIdRespuesta(1L);
        respuesta.setRespuesta("Muy buena atención");
        respuesta.setPregunta(pregunta);

        dto = new RespuestaDTO();
        dto.setId(1L);
        dto.setRespuesta("Muy buena atención");
    }

    @Test
    void testCreateRespuesta() {
        when(preguntaRepository.findById(1L)).thenReturn(Optional.of(pregunta));
        when(respuestaMapper.toEntity(dto)).thenReturn(respuesta);
        when(respuestaRepository.save(respuesta)).thenReturn(respuesta);
        when(respuestaMapper.toDTO(respuesta)).thenReturn(dto);

        RespuestaDTO result = respuestaService.create(1L, dto);

        assertNotNull(result);
        assertEquals("Muy buena atención", result.getRespuesta());
        verify(respuestaRepository, times(1)).save(any(Respuesta.class));
    }

    @Test
    void testGetByIdSuccess() {
        when(respuestaRepository.findById(1L)).thenReturn(Optional.of(respuesta));
        when(respuestaMapper.toDTO(respuesta)).thenReturn(dto);

        RespuestaDTO result = respuestaService.getById(1L);
        assertEquals("Muy buena atención", result.getRespuesta());
    }

    @Test
    void testDeleteRespuesta() {
        when(respuestaRepository.existsById(1L)).thenReturn(true);
        boolean deleted = respuestaService.delete(1L);
        assertTrue(deleted);
        verify(respuestaRepository).deleteById(1L);
    }
}
