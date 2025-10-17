package com.pruebas.pruebas.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Date;

/**
 * Entidad que representa una respuesta registrada a una pregunta específica.
 */
@Entity
@Table(name = "respuesta")
@Data
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespuesta;

    @ManyToOne
    @JoinColumn(name = "idPregunta", nullable = false)
    @JsonBackReference
    private Pregunta pregunta;

    private String respuesta;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRespuesta;

    @PrePersist
    protected void onCreate() {
        this.fechaRespuesta = new Date();
    }
}
