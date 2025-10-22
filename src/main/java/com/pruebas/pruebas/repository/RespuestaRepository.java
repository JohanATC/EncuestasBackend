package com.pruebas.pruebas.repository;

import com.pruebas.pruebas.entity.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Respuesta}.
 * <p>
 * Proporciona operaciones CRUD y consultas básicas sobre la tabla
 * {@code respuesta}, la cual almacena las respuestas individuales
 * asociadas a cada pregunta.
 * </p>
 *
 * <h3>Métodos heredados más comunes:</h3>
 * <ul>
 *   <li>{@code save(Respuesta respuesta)} → Guarda o actualiza una respuesta.</li>
 *   <li>{@code findById(Long id)} → Busca una respuesta por su ID.</li>
 *   <li>{@code findAll()} → Lista todas las respuestas almacenadas.</li>
 *   <li>{@code deleteById(Long id)} → Elimina una respuesta específica.</li>
 * </ul>
 *
 * <p>
 * Se pueden definir métodos adicionales con {@code @Query} para obtener
 * respuestas según criterios personalizados (por ejemplo, por pregunta o fecha).
 * </p>
 */
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    // Contar respuestas por pregunta
    long countByPreguntaIdPregunta(Long idPregunta);

    // Obtener respuestas por pregunta
    List<Respuesta> findByPreguntaIdPregunta(Long idPregunta);

    // Consultas personalizadas adicionales se pueden definir aquí
}
