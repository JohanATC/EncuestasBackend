package com.pruebas.pruebas.service;

import com.pruebas.pruebas.dto.RespuestaDTO;
import com.pruebas.pruebas.entity.Pregunta;
import com.pruebas.pruebas.entity.Respuesta;
import com.pruebas.pruebas.mapper.RespuestaMapper;
import com.pruebas.pruebas.repository.PreguntaRepository;
import com.pruebas.pruebas.repository.RespuestaRepository;
import com.pruebas.pruebas.util.ValidacionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de manejar la lógica de negocio relacionada con la entidad {@link Respuesta}.
 * <p>
 * Este servicio gestiona operaciones CRUD sobre las respuestas registradas en la base de datos,
 * asegurando que las respuestas sean válidas según el tipo y configuración de cada {@link Pregunta}.
 * </p>
 *
 * <h3>Responsabilidades principales:</h3>
 * <ul>
 *   <li>Validar y crear nuevas respuestas asociadas a una pregunta.</li>
 *   <li>Actualizar respuestas existentes con validación previa.</li>
 *   <li>Eliminar respuestas de forma segura.</li>
 *   <li>Convertir entidades a DTOs y viceversa mediante {@link RespuestaMapper}.</li>
 * </ul>
 *
 * @author
 *     Johan Terán
 */
@Service
public class RespuestaService {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private RespuestaMapper respuestaMapper;

    /**
     * Obtiene una respuesta específica según su identificador.
     *
     * @param id identificador único de la respuesta.
     * @return el objeto {@link RespuestaDTO} correspondiente.
     * @throws RuntimeException si la respuesta no existe.
     */
    public RespuestaDTO getById(Long id) {
        Respuesta r = respuestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada"));
        return respuestaMapper.toDTO(r);
    }

    /**
     * Obtiene todas las respuestas almacenadas en el sistema.
     *
     * @return lista de objetos {@link RespuestaDTO}.
     */
    public List<RespuestaDTO> getAll() {
        return respuestaRepository.findAll().stream()
                .map(respuestaMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea una nueva respuesta asociada a una pregunta específica.
     * <p>
     * Antes de guardar, se valida que:
     * <ul>
     *   <li>La pregunta exista y esté asociada a una encuesta válida.</li>
     *   <li>La respuesta sea coherente con el tipo de pregunta (usando {@link ValidacionUtil}).</li>
     * </ul>
     * </p>
     *
     * @param idPregunta identificador de la pregunta a la que pertenece la respuesta.
     * @param dto        datos de la respuesta a registrar.
     * @return el objeto {@link RespuestaDTO} creado y persistido.
     * @throws RuntimeException si la pregunta no existe o la respuesta no es válida.
     */
    public RespuestaDTO create(Long idPregunta, RespuestaDTO dto) {
        Pregunta pregunta = preguntaRepository.findById(idPregunta)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));

        if (pregunta.getEncuesta() == null) {
            throw new RuntimeException("La pregunta no está asociada a ninguna encuesta válida");
        }

        if (!ValidacionUtil.respuestaValida(dto.getRespuesta(), pregunta.getOpciones(), pregunta.getTipo())) {
            throw new RuntimeException("Respuesta no válida para el tipo de pregunta: " + pregunta.getTipo());
        }

        Respuesta r = respuestaMapper.toEntity(dto);
        r.setPregunta(pregunta);
        r.setFechaRespuesta(new Date());

        return respuestaMapper.toDTO(respuestaRepository.save(r));
    }

    /**
     * Actualiza una respuesta existente en la base de datos.
     * <p>
     * Solo se permite modificar el contenido de la respuesta,
     * manteniendo su asociación con la pregunta original.
     * </p>
     *
     * @param id  identificador de la respuesta a actualizar.
     * @param dto datos actualizados de la respuesta.
     * @return la respuesta modificada como {@link RespuestaDTO}.
     * @throws RuntimeException si la respuesta no existe o si la nueva respuesta es inválida.
     */
    public RespuestaDTO update(Long id, RespuestaDTO dto) {
        Respuesta existing = respuestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada"));

        Pregunta pregunta = existing.getPregunta();

        if (!ValidacionUtil.respuestaValida(dto.getRespuesta(), pregunta.getOpciones(), pregunta.getTipo())) {
            throw new RuntimeException("Respuesta no válida para el tipo de pregunta: " + pregunta.getTipo());
        }

        existing.setRespuesta(dto.getRespuesta());
        return respuestaMapper.toDTO(respuestaRepository.save(existing));
    }

    /**
     * Elimina una respuesta por su identificador.
     *
     * @param id identificador único de la respuesta a eliminar.
     * @return {@code true} si la eliminación fue exitosa, {@code false} si no existe.
     */
    public boolean delete(Long id) {
        if (!respuestaRepository.existsById(id)) return false;
        respuestaRepository.deleteById(id);
        return true;
    }
}
