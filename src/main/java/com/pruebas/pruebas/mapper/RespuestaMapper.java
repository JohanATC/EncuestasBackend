package com.pruebas.pruebas.mapper;

import com.pruebas.pruebas.dto.RespuestaDTO;
import com.pruebas.pruebas.entity.Pregunta;
import com.pruebas.pruebas.entity.Respuesta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List;

/**
 * Mapper para convertir entre la entidad {@link Respuesta}
 * y su correspondiente DTO {@link RespuestaDTO}.
 * <p>
 * Incluye métodos personalizados con {@link Named} para mapear relaciones
 * con {@link Pregunta} mediante su identificador.
 * </p>
 */
@Mapper(componentModel = "spring")
public interface RespuestaMapper {

    /**
     * Convierte una entidad {@link Respuesta} a un {@link RespuestaDTO}.
     *
     * @param entity Entidad de tipo {@link Respuesta}.
     * @return DTO con los datos de la respuesta.
     */
    @Mapping(target = "id", source = "idRespuesta")
    @Mapping(target = "idPregunta", source = "pregunta", qualifiedByName = "mapPreguntaId")
    RespuestaDTO toDTO(Respuesta entity);

    /**
     * Convierte un {@link RespuestaDTO} a su entidad {@link Respuesta}.
     *
     * @param dto DTO con los datos de la respuesta.
     * @return Entidad correspondiente.
     */
    @Mapping(target = "idRespuesta", source = "id")
    @Mapping(target = "pregunta", source = "idPregunta", qualifiedByName = "mapToPregunta")
    Respuesta toEntity(RespuestaDTO dto);

    /** Convierte una lista de entidades a una lista de DTOs. */
    List<RespuestaDTO> toDTO(List<Respuesta> entities);

    /** Convierte una lista de DTOs a una lista de entidades. */
    List<Respuesta> toEntity(List<RespuestaDTO> dtos);

    /**
     * Extrae el identificador de una entidad {@link Pregunta}.
     *
     * @param pregunta Entidad asociada a la respuesta.
     * @return ID de la pregunta, o {@code null} si no existe.
     */
    @Named("mapPreguntaId")
    default Long mapPreguntaId(Pregunta pregunta) {
        if (pregunta == null) return null;
        return pregunta.getIdPregunta();
    }

    /**
     * Crea una instancia de {@link Pregunta} a partir de un identificador.
     *
     * @param idPregunta ID de la pregunta.
     * @return Entidad {@link Pregunta} con el ID establecido.
     */
    @Named("mapToPregunta")
    default Pregunta mapToPregunta(Long idPregunta) {
        if (idPregunta == null) return null;
        Pregunta p = new Pregunta();
        p.setIdPregunta(idPregunta);
        return p;
    }
}
