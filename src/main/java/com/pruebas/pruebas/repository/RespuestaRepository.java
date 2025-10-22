package com.pruebas.pruebas.repository;

import com.pruebas.pruebas.entity.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Date;

/**
 * Repositorio JPA para la entidad {@link Respuesta}.
 */
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

    // Contar respuestas por pregunta
    long countByPreguntaIdPregunta(Long idPregunta);

    // Obtener respuestas por pregunta
    List<Respuesta> findByPreguntaIdPregunta(Long idPregunta);

    // NUEVO: Contar respuestas por día para una encuesta
    @Query("SELECT FUNCTION('DATE', r.fechaRespuesta), COUNT(r) " +
            "FROM Respuesta r WHERE r.pregunta.encuesta.idEncuesta = :encuestaId " +
            "GROUP BY FUNCTION('DATE', r.fechaRespuesta) " +
            "ORDER BY FUNCTION('DATE', r.fechaRespuesta)")
    List<Object[]> countRespuestasPorDia(@Param("encuestaId") Long encuestaId);

    // NUEVO: Obtener fecha de la primera respuesta
    @Query("SELECT MIN(r.fechaRespuesta) FROM Respuesta r WHERE r.pregunta.encuesta.idEncuesta = :encuestaId")
    Date findFechaPrimeraRespuesta(@Param("encuestaId") Long encuestaId);

    // NUEVO: Obtener fecha de la última respuesta
    @Query("SELECT MAX(r.fechaRespuesta) FROM Respuesta r WHERE r.pregunta.encuesta.idEncuesta = :encuestaId")
    Date findFechaUltimaRespuesta(@Param("encuestaId") Long encuestaId);
}