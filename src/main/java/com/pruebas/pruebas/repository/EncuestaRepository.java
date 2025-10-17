package com.pruebas.pruebas.repository;

import com.pruebas.pruebas.entity.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad {@link Encuesta}.
 * <p>
 * Esta interfaz proporciona operaciones CRUD básicas y permite la creación
 * de consultas personalizadas para gestionar encuestas en la base de datos.
 * </p>
 *
 * <h3>Métodos heredados más comunes:</h3>
 * <ul>
 *   <li>{@code save(Encuesta encuesta)} → Guarda o actualiza una encuesta.</li>
 *   <li>{@code findById(Long id)} → Busca una encuesta por su ID.</li>
 *   <li>{@code findAll()} → Obtiene todas las encuestas registradas.</li>
 *   <li>{@code deleteById(Long id)} → Elimina una encuesta según su ID.</li>
 * </ul>
 *
 * <p>
 * Si se requiere lógica más compleja, se pueden definir consultas personalizadas
 * mediante anotaciones como {@code @Query}.
 * </p>
 */
public interface EncuestaRepository extends JpaRepository<Encuesta, Long> {
    // Consultas personalizadas adicionales se pueden agregar aquí
}
