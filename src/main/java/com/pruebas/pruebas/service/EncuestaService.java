package com.pruebas.pruebas.service;

import com.pruebas.pruebas.entity.Encuesta;
import com.pruebas.pruebas.dto.EncuestaDTO;
import com.pruebas.pruebas.mapper.EncuestaMapper;
import com.pruebas.pruebas.repository.EncuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la lógica de negocio para las entidades {@link Encuesta}.
 * <p>
 * Permite realizar operaciones CRUD (crear, leer, actualizar y eliminar)
 * y utiliza {@link EncuestaMapper} para convertir entre entidades y DTOs.
 */
@Service
public class EncuestaService {

    @Autowired
    private EncuestaRepository encuestaRepository;

    @Autowired
    private EncuestaMapper encuestaMapper;

    /**
     * Crea una nueva encuesta en la base de datos.
     *
     * @param dto DTO que contiene los datos de la encuesta a crear
     * @return DTO de la encuesta creada
     */
    public EncuestaDTO create(EncuestaDTO dto) {
        Encuesta e = encuestaMapper.toEntity(dto);
        return encuestaMapper.toDTO(encuestaRepository.save(e));
    }

    /**
     * Obtiene una encuesta existente por su identificador único.
     *
     * @param id ID de la encuesta
     * @return DTO de la encuesta encontrada
     * @throws RuntimeException si no se encuentra la encuesta
     */
    public EncuestaDTO getById(Long id) {
        Encuesta e = encuestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));
        return encuestaMapper.toDTO(e);
    }

    /**
     * Recupera todas las encuestas registradas.
     *
     * @return Lista de encuestas en formato DTO
     */
    public List<EncuestaDTO> getAll() {
        return encuestaRepository.findAll().stream()
                .map(encuestaMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza los datos de una encuesta existente.
     *
     * @param id  ID de la encuesta a actualizar
     * @param dto DTO con los nuevos valores
     * @return DTO de la encuesta actualizada
     * @throws RuntimeException si la encuesta no existe
     */
    public EncuestaDTO update(Long id, EncuestaDTO dto) {
        Encuesta existing = encuestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));

        existing.setTitulo(dto.getTitulo());
        existing.setDescripcion(dto.getDescripcion());
        existing.setEstado(dto.getEstado());

        return encuestaMapper.toDTO(encuestaRepository.save(existing));
    }

    /**
     * Elimina una encuesta específica por su ID.
     *
     * @param id ID de la encuesta a eliminar
     * @return {@code true} si la encuesta fue eliminada, {@code false} si no existía
     */
    public boolean delete(Long id) {
        if (!encuestaRepository.existsById(id)) return false;
        encuestaRepository.deleteById(id);
        return true;
    }

    /**
     * Elimina todas las encuestas registradas en la base de datos.
     */
    public void deleteAll() {
        encuestaRepository.deleteAll();
    }
}
