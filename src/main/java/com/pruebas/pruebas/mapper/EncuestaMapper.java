package com.pruebas.pruebas.mapper;

import com.pruebas.pruebas.dto.EncuestaDTO;
import com.pruebas.pruebas.entity.Encuesta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

/**
 * Mapper encargado de convertir entre la entidad {@link Encuesta} y su representación {@link EncuestaDTO}.
 * <p>
 * Este mapper utiliza {@link PreguntaMapper} para incluir la conversión de las preguntas
 * asociadas a la encuesta, garantizando la correcta correspondencia de los datos.
 * </p>
 *
 * <p><b>Uso típico:</b></p>
 * <pre>
 *     Encuesta encuesta = encuestaMapper.toEntity(dto);
 *     EncuestaDTO dto = encuestaMapper.toDTO(encuesta);
 * </pre>
 */
@Mapper(componentModel = "spring", uses = {PreguntaMapper.class})
public interface EncuestaMapper {

    /**
     * Convierte una entidad {@link Encuesta} a su DTO equivalente {@link EncuestaDTO}.
     *
     * @param entity Entidad de tipo {@link Encuesta}.
     * @return DTO equivalente.
     */
    @Mapping(target = "id", source = "idEncuesta")
    EncuestaDTO toDTO(Encuesta entity);

    /**
     * Convierte un DTO {@link EncuestaDTO} a su entidad {@link Encuesta}.
     *
     * @param dto DTO a convertir.
     * @return Entidad JPA correspondiente.
     */
    @Mapping(target = "idEncuesta", source = "id")
    Encuesta toEntity(EncuestaDTO dto);

    /** Convierte una lista de entidades {@link Encuesta} a una lista de {@link EncuestaDTO}. */
    List<EncuestaDTO> toDTO(List<Encuesta> entities);

    /** Convierte una lista de DTOs {@link EncuestaDTO} a una lista de entidades {@link Encuesta}. */
    List<Encuesta> toEntity(List<EncuestaDTO> dtos);
}
