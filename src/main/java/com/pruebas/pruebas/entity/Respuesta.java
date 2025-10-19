package com.pruebas.pruebas.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Date;

/**
 * <h2>Entidad: Respuesta</h2>
 *
 * Representa una respuesta registrada a una pregunta específica.
 * Puede contener texto libre o un valor seleccionado de las opciones disponibles.
 *
 * <p>Se almacena en la tabla <b>respuesta</b> de PostgreSQL.</p>
 *
 * <h3>Relaciones:</h3>
 * <ul>
 *   <li>🔹 Muchas respuestas pertenecen a una {@link Pregunta}.</li>
 * </ul>
 *
 * <h3>Auditoría:</h3>
 * <ul>
 *   <li>La fecha de respuesta se asigna automáticamente al momento de la creación.</li>
 * </ul>
 *
 * @author Johan
 * @version 1.0
 */
@Entity
@Table(name = "respuesta")
@Data
public class Respuesta {

    /** Identificador único de la respuesta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRespuesta;

    /** Pregunta asociada a esta respuesta. */
    @ManyToOne
    @JoinColumn(name = "id_pregunta", nullable = false)
    @JsonBackReference
    private Pregunta pregunta;

    /** Contenido de la respuesta (texto libre o valor seleccionado). */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String respuesta;

    /** Fecha en la que se registró la respuesta. */
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRespuesta;

    /** Asigna automáticamente la fecha de registro antes de persistir. */
    @PrePersist
    protected void onCreate() {
        this.fechaRespuesta = new Date();
    }
}
