package com.pruebas.pruebas.service;

import com.pruebas.pruebas.dto.EncuestaDTO;
import com.pruebas.pruebas.entity.Encuesta;
import com.pruebas.pruebas.mapper.EncuestaMapper;
import com.pruebas.pruebas.repository.EncuestaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EncuestaServiceTest {

    @Mock
    private EncuestaRepository encuestaRepository;

    @Mock
    private EncuestaMapper encuestaMapper;

    @InjectMocks
    private EncuestaService encuestaService;

    private Encuesta encuesta;
    private EncuestaDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encuesta = new Encuesta();
        encuesta.setIdEncuesta(1L);
        encuesta.setTitulo("Encuesta de Satisfacción");

        dto = new EncuestaDTO();
        dto.setId(1L);
        dto.setTitulo("Encuesta de Satisfacción");
    }

    @Test
    void testCreateEncuesta() {
        when(encuestaMapper.toEntity(dto)).thenReturn(encuesta);
        when(encuestaRepository.save(encuesta)).thenReturn(encuesta);
        when(encuestaMapper.toDTO(encuesta)).thenReturn(dto);

        EncuestaDTO result = encuestaService.create(dto);

        assertNotNull(result);
        assertEquals("Encuesta de Satisfacción", result.getTitulo());
        verify(encuestaRepository, times(1)).save(any(Encuesta.class));
    }

    @Test
    void testGetByIdSuccess() {
        when(encuestaRepository.findById(1L)).thenReturn(Optional.of(encuesta));
        when(encuestaMapper.toDTO(encuesta)).thenReturn(dto);


        EncuestaDTO result = encuestaService.getById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetByIdNotFound() {
        when(encuestaRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> encuestaService.getById(99L));
        assertEquals("Encuesta no encontrada", ex.getMessage());
    }

    @Test
    void testDeleteEncuesta() {
        when(encuestaRepository.existsById(1L)).thenReturn(true);
        boolean deleted = encuestaService.delete(1L);
        assertTrue(deleted);
        verify(encuestaRepository).deleteById(1L);
    }
}
