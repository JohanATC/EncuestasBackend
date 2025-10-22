package com.pruebas.pruebas.mapper;

import com.pruebas.pruebas.dto.EncuestaDTO;
import com.pruebas.pruebas.entity.Encuesta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EncuestaMapperTest {

    @Autowired
    private EncuestaMapper encuestaMapper;

    @Test
    void testToDTO() {
        // Arrange
        Encuesta encuesta = new Encuesta();
        encuesta.setIdEncuesta(1L);
        encuesta.setTitulo("Encuesta de prueba");

        // Act
        EncuestaDTO result = encuestaMapper.toDTO(encuesta);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Encuesta de prueba", result.getTitulo());
    }

    @Test
    void testToEntity() {
        // Arrange
        EncuestaDTO dto = new EncuestaDTO();
        dto.setId(1L);
        dto.setTitulo("Encuesta DTO");

        // Act
        Encuesta result = encuestaMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdEncuesta());
        assertEquals("Encuesta DTO", result.getTitulo());
    }
}