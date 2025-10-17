package com.pruebas.pruebas.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) que representa los datos de una pregunta dentro de una encuesta.
 * <p>
 * Este objeto transporta información sobre el texto de la pregunta, tipo,
 * opciones posibles, orden y respuestas asociadas.
 * </p>
 *
 * <h3>Ejemplo de uso:</h3>
 * <pre>
 * PreguntaDTO pregunta = new PreguntaDTO();
 * pregunta.setTextoPregunta("¿Cuál es tu producto favorito?");
 * pregunta.setTipo("opcion_multiple");
 * pregunta.setOpciones(List.of("A", "B", "C"));
 * pregunta.setOrden(1);
 * </pre>
 *
 * @see com.pruebas.pruebas.entity.Pregunta
 */
public class PreguntaDTO {

    /** Identificador único de la pregunta. */
    private Long id;

    /** Identificador de la encuesta a la que pertenece. */
    @NotNull(message = "Debe especificar el ID de la encuesta")
    private Long idEncuesta;

    /** Texto visible de la pregunta. */
    @NotBlank(message = "El texto de la pregunta es obligatorio")
    private String textoPregunta;

    /** Tipo de pregunta: 'abierta', 'opcion_multiple', 'si_no', etc. */
    @NotBlank(message = "El tipo de pregunta no puede estar vacío")
    private String tipo;

    /** Lista de opciones posibles (solo si la pregunta es cerrada). */
    private List<String> opciones;

    /** Posición que ocupa la pregunta dentro de la encuesta. */
    @NotNull(message = "El orden de la pregunta no puede ser nulo")
    private Integer orden;

    /** Respuestas registradas para esta pregunta (opcional en consultas extendidas). */
    private List<RespuestaDTO> respuestas;

    // ──────────────────────────────── Getters y Setters ────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdEncuesta() { return idEncuesta; }
    public void setIdEncuesta(Long idEncuesta) { this.idEncuesta = idEncuesta; }

    public String getTextoPregunta() { return textoPregunta; }
    public void setTextoPregunta(String textoPregunta) { this.textoPregunta = textoPregunta; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public List<String> getOpciones() { return opciones; }
    public void setOpciones(List<String> opciones) { this.opciones = opciones; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    public List<RespuestaDTO> getRespuestas() { return respuestas; }
    public void setRespuestas(List<RespuestaDTO> respuestas) { this.respuestas = respuestas; }
}
