package com.pruebas.pruebas.mapper;

import com.pruebas.pruebas.dto.PreguntaDTO;
import com.pruebas.pruebas.entity.Pregunta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

/**
 * Mapper encargado de la conversión entre la entidad {@link Pregunta}
 * y su objeto de transferencia de datos {@link PreguntaDTO}.
 * <p>
 * Utiliza {@link RespuestaMapper} para manejar la lista de respuestas asociadas
 * a cada pregunta.
 * </p>
 */
@Mapper(componentModel = "spring", uses = {RespuestaMapper.class})
public interface PreguntaMapper {

    /**
     * Convierte una entidad {@link Pregunta} a su {@link PreguntaDTO} correspondiente.
     *
     * @param entity Entidad JPA de tipo {@link Pregunta}.
     * @return DTO con los datos de la pregunta.
     */
    @Mapping(target = "id", source = "idPregunta")
    @Mapping(target = "idEncuesta", source = "encuesta.idEncuesta")
    PreguntaDTO toDTO(Pregunta entity);

    /**
     * Convierte un {@link PreguntaDTO} en una entidad {@link Pregunta}.
     *
     * @param dto DTO con los datos de la pregunta.
     * @return Entidad persistente equivalente.
     */
    @Mapping(target = "idPregunta", source = "id")
    @Mapping(target = "encuesta.idEncuesta", source = "idEncuesta")
    Pregunta toEntity(PreguntaDTO dto);

    /** Convierte una lista de entidades a una lista de DTOs. */
    List<PreguntaDTO> toDTO(List<Pregunta> entities);

    /** Convierte una lista de DTOs a una lista de entidades. */
    List<Pregunta> toEntity(List<PreguntaDTO> dtos);
}
