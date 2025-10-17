package com.pruebas.pruebas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * DTO (Data Transfer Object) que representa los datos de una respuesta.
 */
public class RespuestaDTO {

    private Long id;

    @NotNull(message = "El id de la pregunta no puede ser nulo")
    private Long idPregunta;

    @NotBlank(message = "La respuesta no puede estar vacía")
    private String respuesta;

    private Date fechaRespuesta;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdPregunta() { return idPregunta; }
    public void setIdPregunta(Long idPregunta) { this.idPregunta = idPregunta; }

    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }

    public Date getFechaRespuesta() { return fechaRespuesta; }
    public void setFechaRespuesta(Date fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }
}
