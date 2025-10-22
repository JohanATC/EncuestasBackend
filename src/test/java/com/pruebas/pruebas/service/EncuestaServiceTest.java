package com.pruebas.pruebas.service;

import com.pruebas.pruebas.dto.EncuestaDTO;
import com.pruebas.pruebas.entity.Encuesta;
import com.pruebas.pruebas.mapper.EncuestaMapper;
import com.pruebas.pruebas.repository.EncuestaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EncuestaServiceTest {

    @Mock
    private EncuestaRepository encuestaRepository;

    @Mock
    private EncuestaMapper encuestaMapper;

    @InjectMocks
    private EncuestaService encuestaService;

    private Encuesta encuesta;
    private EncuestaDTO encuestaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        encuesta = new Encuesta();
        encuesta.setIdEncuesta(1L);
        encuesta.setTitulo("Satisfacción del cliente");
        encuesta.setDescripcion("Encuesta sobre atención al cliente");
        encuesta.setEstado("Activa");

        encuestaDTO = new EncuestaDTO();
        encuestaDTO.setId(1L);
        encuestaDTO.setTitulo("Satisfacción del cliente");
        encuestaDTO.setDescripcion("Encuesta sobre atención al cliente");
        encuestaDTO.setEstado("Activa");
    }

    @Test
    void testCreateEncuesta() {
        when(encuestaMapper.toEntity(encuestaDTO)).thenReturn(encuesta);
        when(encuestaRepository.save(encuesta)).thenReturn(encuesta);
        when(encuestaMapper.toDTO(encuesta)).thenReturn(encuestaDTO);

        EncuestaDTO result = encuestaService.create(encuestaDTO);

        assertNotNull(result);
        assertEquals("Satisfacción del cliente", result.getTitulo());
        verify(encuestaRepository, times(1)).save(encuesta);
    }

    @Test
    void testGetByIdEncuestaFound() {
        when(encuestaRepository.findById(1L)).thenReturn(Optional.of(encuesta));
        when(encuestaMapper.toDTO(encuesta)).thenReturn(encuestaDTO);

        EncuestaDTO result = encuestaService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetByIdEncuestaNotFound() {
        when(encuestaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> encuestaService.getById(99L));
        assertEquals("Encuesta no encontrada", exception.getMessage());
    }

    @Test
    void testGetAllEncuestas() {
        when(encuestaRepository.findAll()).thenReturn(List.of(encuesta));
        when(encuestaMapper.toDTO(encuesta)).thenReturn(encuestaDTO);

        List<EncuestaDTO> result = encuestaService.getAll();

        assertEquals(1, result.size());
        assertEquals("Satisfacción del cliente", result.get(0).getTitulo());
    }

    @Test
    void testUpdateEncuesta() {
        EncuestaDTO updateDTO = new EncuestaDTO();
        updateDTO.setTitulo("Encuesta actualizada");
        updateDTO.setDescripcion("Nueva descripción");
        updateDTO.setEstado("Inactiva");

        when(encuestaRepository.findById(1L)).thenReturn(Optional.of(encuesta));
        when(encuestaRepository.save(any(Encuesta.class))).thenReturn(encuesta);
        when(encuestaMapper.toDTO(any(Encuesta.class))).thenReturn(updateDTO);

        EncuestaDTO result = encuestaService.update(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Encuesta actualizada", result.getTitulo());
        verify(encuestaRepository, times(1)).save(encuesta);
    }

    @Test
    void testDeleteEncuestaExists() {
        when(encuestaRepository.existsById(1L)).thenReturn(true);

        boolean result = encuestaService.delete(1L);

        assertTrue(result);
        verify(encuestaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEncuestaNotExists() {
        when(encuestaRepository.existsById(1L)).thenReturn(false);

        boolean result = encuestaService.delete(1L);

        assertFalse(result);
        verify(encuestaRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteAllEncuestas() {
        encuestaService.deleteAll();
        verify(encuestaRepository, times(1)).deleteAll();
    }
}
