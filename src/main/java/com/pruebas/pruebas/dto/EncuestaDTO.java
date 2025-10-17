package com.pruebas.pruebas.dto;

import java.util.Date;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) que representa los datos básicos de una encuesta.
 * <p>
 * Este objeto se utiliza para transferir información entre el cliente y el servidor,
 * sin exponer directamente las entidades JPA. Contiene los campos principales de la
 * encuesta, incluyendo su título, descripción, estado y la lista de preguntas asociadas.
 * </p>
 *
 * <h3>Usos comunes:</h3>
 * <ul>
 *   <li>Recepción y envío de datos en endpoints REST.</li>
 *   <li>Intercambio de información entre el front y el backend sin acoplar la capa de persistencia.</li>
 * </ul>
 *
 * @see com.pruebas.pruebas.entity.Encuesta
 */
public class EncuestaDTO {

    /** Identificador único de la encuesta. */
    private Long id;

    /** Título visible de la encuesta. */
    @NotBlank(message = "El título de la encuesta es obligatorio")
    private String titulo;

    /** Descripción general del propósito o tema de la encuesta. */
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    /** Estado actual de la encuesta (ACTIVA, INACTIVA, FINALIZADA, BORRADOR). */
    @NotBlank(message = "El estado de la encuesta es obligatorio")
    private String estado;

    /** Fecha en que se creó la encuesta. */
    @NotNull(message = "La fecha de creación no puede ser nula")
    private Date fechaCreacion;

    /** Lista de preguntas que componen la encuesta. */
    private List<@NotNull(message = "Cada pregunta debe ser válida") PreguntaDTO> preguntas;

    // ──────────────────────────────── Getters y Setters ────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public List<PreguntaDTO> getPreguntas() { return preguntas; }
    public void setPreguntas(List<PreguntaDTO> preguntas) { this.preguntas = preguntas; }
}
