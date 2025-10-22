package com.pruebas.pruebas.repository;

import com.pruebas.pruebas.entity.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Pregunta}.
 * <p>
 * Gestiona las operaciones CRUD sobre las preguntas asociadas a una encuesta.
 * Incluye métodos personalizados para manejar el orden de las preguntas dentro
 * de una misma encuesta.
 * </p>
 */
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {

    /**
     * Incrementa el valor del campo {@code orden} de todas las preguntas
     * pertenecientes a una encuesta específica, comenzando desde una posición dada.
     * <p>
     * Este método es útil para insertar una nueva pregunta en una posición
     * intermedia dentro del cuestionario sin sobrescribir el orden existente.
     * </p>
     *
     * @param orden      posición desde la cual se incrementará el orden.
     * @param idEncuesta identificador de la encuesta a modificar.
     */
    @Modifying
    @Query("UPDATE Pregunta p SET p.orden = p.orden + 1 WHERE p.encuesta.idEncuesta = :idEncuesta AND p.orden >= :orden")
    void incrementarOrdenDesde(@Param("orden") Integer orden, @Param("idEncuesta") Long idEncuesta);

    /**
     * Obtiene el valor máximo del campo {@code orden} para las preguntas
     * pertenecientes a una encuesta determinada.
     * <p>
     * Se utiliza principalmente para calcular el próximo orden disponible
     * al agregar una nueva pregunta.
     * </p>
     *
     * @param idEncuesta identificador de la encuesta.
     * @return el número máximo de orden registrado o {@code null} si no existen preguntas.
     */
    @Query("SELECT MAX(p.orden) FROM Pregunta p WHERE p.encuesta.idEncuesta = :idEncuesta")
    Integer findMaxOrdenByEncuesta(@Param("idEncuesta") Long idEncuesta);

    // NUEVO: Obtener preguntas por encuesta
    List<Pregunta> findByEncuestaIdEncuesta(Long idEncuesta);
}
