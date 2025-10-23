// src/test/java/com/pruebas/pruebas/service/EncuestaServiceTest.java
package com.pruebas.pruebas.service;

import com.pruebas.pruebas.dto.EncuestaDTO;
import com.pruebas.pruebas.entity.Encuesta;
import com.pruebas.pruebas.mapper.EncuestaMapper;
import com.pruebas.pruebas.repository.EncuestaRepository;
import com.pruebas.pruebas.util.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EncuestaServiceTest {

    @Mock
    private EncuestaRepository encuestaRepository;

    @Mock
    private EncuestaMapper encuestaMapper;

    @InjectMocks
    private EncuestaService encuestaService;

    @Test
    void testCrearEncuesta() {
        // Given
        EncuestaDTO encuestaDTO = TestDataBuilder.buildEncuestaDTO();
        Encuesta encuestaEntity = new Encuesta();
        encuestaEntity.setIdEncuesta(1L);

        when(encuestaMapper.toEntity(encuestaDTO)).thenReturn(encuestaEntity);
        when(encuestaRepository.save(any(Encuesta.class))).thenReturn(encuestaEntity);
        when(encuestaMapper.toDTO(encuestaEntity)).thenReturn(encuestaDTO);

        // When
        EncuestaDTO resultado = encuestaService.create(encuestaDTO);

        // Then
        assertNotNull(resultado);
        verify(encuestaRepository, times(1)).save(any(Encuesta.class));
    }

    @Test
    void testObtenerEncuestaPorId() {
        // Given
        Long encuestaId = 1L;
        Encuesta encuestaEntity = new Encuesta();
        encuestaEntity.setIdEncuesta(encuestaId);
        EncuestaDTO encuestaDTO = TestDataBuilder.buildEncuestaDTO();

        when(encuestaRepository.findById(encuestaId)).thenReturn(Optional.of(encuestaEntity));
        when(encuestaMapper.toDTO(encuestaEntity)).thenReturn(encuestaDTO);

        // When
        EncuestaDTO resultado = encuestaService.getById(encuestaId);

        // Then
        assertNotNull(resultado);
        verify(encuestaRepository, times(1)).findById(encuestaId);
    }
}