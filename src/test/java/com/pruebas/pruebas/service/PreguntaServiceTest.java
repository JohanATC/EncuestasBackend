package com.pruebas.pruebas.service;

import com.pruebas.pruebas.dto.PreguntaDTO;
import com.pruebas.pruebas.entity.Pregunta;
import com.pruebas.pruebas.mapper.PreguntaMapper;
import com.pruebas.pruebas.repository.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PreguntaServiceTest {

    @Mock
    private PreguntaRepository preguntaRepository;

    @Mock
    private PreguntaMapper preguntaMapper;

    @InjectMocks
    private PreguntaService preguntaService;

    private Pregunta pregunta;
    private PreguntaDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pregunta = new Pregunta();
        pregunta.setIdPregunta(1L);
        pregunta.setTextoPregunta("¿Te gustó la atención?");
        pregunta.setTipo("abierta");

        dto = new PreguntaDTO();
        dto.setId(1L);
        dto.setTextoPregunta("¿Te gustó la atención?");
        dto.setTipo("abierta");
    }

    @Test
    void testCreatePregunta() {
        when(preguntaMapper.toEntity(dto)).thenReturn(pregunta);
        when(preguntaRepository.save(pregunta)).thenReturn(pregunta);
        when(preguntaMapper.toDTO(pregunta)).thenReturn(dto);

        PreguntaDTO result = preguntaService.create(dto);

        assertNotNull(result);
        assertEquals("¿Te gustó la atención?", result.getTextoPregunta());
        verify(preguntaRepository, times(1)).save(any(Pregunta.class));
    }

    @Test
    void testGetByIdSuccess() {
        when(preguntaRepository.findById(1L)).thenReturn(Optional.of(pregunta));
        when(preguntaMapper.toDTO(pregunta)).thenReturn(dto);

        PreguntaDTO result = preguntaService.getById(1L);

        assertEquals(1L, result.getId());
        assertEquals("abierta", result.getTipo());
    }

    @Test
    void testGetByIdNotFound() {
        when(preguntaRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> preguntaService.getById(99L));
        assertEquals("Pregunta no encontrada", ex.getMessage());
    }

    @Test
    void testUpdatePregunta() {
        when(preguntaRepository.findById(1L)).thenReturn(Optional.of(pregunta));
        when(preguntaRepository.save(any())).thenReturn(pregunta);
        when(preguntaMapper.toDTO(any(Pregunta.class))).thenReturn(dto);


        dto.setTextoPregunta("¿Cambiarías algo del servicio?");
        PreguntaDTO result = preguntaService.update(1L, dto);

        assertEquals("¿Cambiarías algo del servicio?", result.getTextoPregunta());
    }

    @Test
    void testDeletePregunta() {
        when(preguntaRepository.existsById(1L)).thenReturn(true);
        boolean deleted = preguntaService.delete(1L);
        assertTrue(deleted);
        verify(preguntaRepository).deleteById(1L);
    }
}
