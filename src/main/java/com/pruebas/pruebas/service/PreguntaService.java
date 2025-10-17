package com.pruebas.pruebas.service;

import com.pruebas.pruebas.dto.PreguntaDTO;
import com.pruebas.pruebas.entity.Encuesta;
import com.pruebas.pruebas.entity.Pregunta;
import com.pruebas.pruebas.mapper.PreguntaMapper;
import com.pruebas.pruebas.repository.EncuestaRepository;
import com.pruebas.pruebas.repository.PreguntaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * Servicio que gestiona la lógica de negocio asociada a {@link Pregunta}.
 * <p>
 * Controla la creación, edición, eliminación y reordenamiento de preguntas
 * dentro de una encuesta específica.
 */
@Service
public class PreguntaService {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private EncuestaRepository encuestaRepository;

    @Autowired
    private PreguntaMapper preguntaMapper;

    private static final Logger log = LoggerFactory.getLogger(PreguntaService.class);

    /**
     * Crea opciones automáticas según el tipo de pregunta
     */
    private List<String> crearOpcionesAutomaticas(String tipo) {
        switch (tipo.toLowerCase()) {
            case "verdadero_falso":
                return List.of("Verdadero", "Falso"); // CORREGIDO: Cambiado de "Sí"/"No" a "Verdadero"/"Falso"
            case "si_no":
                return List.of("Sí", "No");
            case "escala":
                return List.of("1", "2", "3", "4", "5");
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Crea una nueva pregunta asociada a una encuesta.
     * Si no se especifica el orden, se asigna automáticamente el siguiente disponible.
     * Si es un tipo con opciones automáticas y no tiene opciones, se crean automáticamente.
     *
     * @param dto DTO de la pregunta a crear
     * @return DTO de la pregunta creada
     * @throws RuntimeException si la encuesta asociada no existe
     */
    @Transactional
    public PreguntaDTO create(PreguntaDTO dto) {
        log.info("Creando nueva pregunta en encuesta {}", dto.getIdEncuesta());

        Encuesta encuesta = encuestaRepository.findById(dto.getIdEncuesta())
                .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));

        // Si es un tipo con opciones automáticas y no tiene opciones, crearlas automáticamente
        if (necesitaOpcionesAutomaticas(dto.getTipo()) &&
                (dto.getOpciones() == null || dto.getOpciones().isEmpty())) {
            dto.setOpciones(crearOpcionesAutomaticas(dto.getTipo()));
            log.info("Opciones automáticas creadas para tipo: {}", dto.getTipo());
        }

        // Asignar o ajustar orden automáticamente
        if (dto.getOrden() == null || dto.getOrden() <= 0) {
            Integer maxOrden = preguntaRepository.findMaxOrdenByEncuesta(encuesta.getIdEncuesta());
            dto.setOrden(maxOrden == null ? 1 : maxOrden + 1);
        } else {
            preguntaRepository.incrementarOrdenDesde(dto.getOrden(), encuesta.getIdEncuesta());
        }

        Pregunta pregunta = preguntaMapper.toEntity(dto);
        pregunta.setEncuesta(encuesta);

        return preguntaMapper.toDTO(preguntaRepository.save(pregunta));
    }

    /**
     * Determina si un tipo de pregunta necesita opciones automáticas
     */
    private boolean necesitaOpcionesAutomaticas(String tipo) {
        if (tipo == null) return false;
        String tipoLower = tipo.toLowerCase();
        return "verdadero_falso".equals(tipoLower) ||
                "si_no".equals(tipoLower) ||
                "escala".equals(tipoLower);
        // NOTA: seleccion_unica NO necesita opciones automáticas porque el usuario las debe definir
    }

    /**
     * Obtiene una pregunta por su identificador.
     *
     * @param id ID de la pregunta
     * @return DTO de la pregunta encontrada
     * @throws RuntimeException si no se encuentra la pregunta
     */
    public PreguntaDTO getById(Long id) {
        Pregunta p = preguntaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));
        return preguntaMapper.toDTO(p);
    }

    /**
     * Obtiene todas las preguntas existentes en el sistema.
     *
     * @return Lista de preguntas en formato DTO
     */
    public List<PreguntaDTO> getAll() {
        return preguntaRepository.findAll().stream()
                .map(preguntaMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza los datos de una pregunta específica.
     * Si cambia el orden, ajusta automáticamente las posiciones del resto.
     *
     * @param id  ID de la pregunta a actualizar
     * @param dto DTO con los nuevos datos
     * @return DTO de la pregunta actualizada
     * @throws RuntimeException si no se encuentra la pregunta
     */
    @Transactional
    public PreguntaDTO update(Long id, PreguntaDTO dto) {
        Pregunta existing = preguntaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));

        // Si es un tipo con opciones automáticas y no tiene opciones, crearlas automáticamente
        if (necesitaOpcionesAutomaticas(dto.getTipo()) &&
                (dto.getOpciones() == null || dto.getOpciones().isEmpty())) {
            dto.setOpciones(crearOpcionesAutomaticas(dto.getTipo()));
            log.info("Opciones automáticas actualizadas para tipo: {}", dto.getTipo());
        }

        if (dto.getOrden() != null && !dto.getOrden().equals(existing.getOrden())) {
            preguntaRepository.incrementarOrdenDesde(dto.getOrden(), existing.getEncuesta().getIdEncuesta());
        }

        existing.setTextoPregunta(dto.getTextoPregunta());
        existing.setTipo(dto.getTipo());
        existing.setOpciones(dto.getOpciones());
        existing.setOrden(dto.getOrden());

        return preguntaMapper.toDTO(preguntaRepository.save(existing));
    }

    /**
     * Elimina una pregunta específica por su ID.
     *
     * @param id ID de la pregunta a eliminar
     * @return {@code true} si existía y fue eliminada, {@code false} si no existía
     */
    public boolean delete(Long id) {
        if (!preguntaRepository.existsById(id)) return false;
        preguntaRepository.deleteById(id);
        return true;
    }
}