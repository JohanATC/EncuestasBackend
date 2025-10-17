package com.pruebas.pruebas.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;

/**
 * Entidad que representa una pregunta dentro de una encuesta.
 * <p>
 * Cada pregunta puede ser de diferentes tipos (texto libre, opción múltiple, sí/no, etc.),
 * y puede incluir una lista de opciones almacenadas en formato JSONB.
 * </p>
 *
 * <h3>Relaciones:</h3>
 * <ul>
 *   <li>Muchas preguntas pertenecen a una {@link Encuesta}.</li>
 *   <li>Una pregunta puede tener múltiples {@link Respuesta}.</li>
 * </ul>
 *
 * <h3>Características:</h3>
 * <ul>
 *   <li>Uso de tipo JSONB para almacenar opciones en PostgreSQL.</li>
 *   <li>Relación bidireccional con {@link Encuesta} y {@link Respuesta}.</li>
 * </ul>
 */
@Entity
@Table(name = "pregunta")
public class Pregunta {

    /** Identificador único de la pregunta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPregunta;

    /** Texto visible de la pregunta. */
    private String textoPregunta;

    /** Tipo de la pregunta (por ejemplo: 'abierta', 'opcion_multiple', 'si_no'). */
    private String tipo;

    /** Posición que ocupa dentro de la encuesta. */
    private Integer orden;

    /**
     * Opciones posibles de respuesta (solo si aplica).
     * <p>
     * Se almacena en formato JSONB en PostgreSQL.
     * Ejemplo: ["Producto A", "Producto B"]
     * </p>
     */
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> opciones;

    /**
     * Encuesta a la que pertenece esta pregunta.
     * <p>
     * Se establece con carga diferida (LAZY) para optimizar consultas.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_encuesta", nullable = false)
    private Encuesta encuesta;

    /** Lista de respuestas asociadas a esta pregunta. */
    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Respuesta> respuestas;

// Getters y setters omitidos por claridad (Lombok puede usarse aquí también)

    public Long getIdPregunta() {return idPregunta;}

    public void setIdPregunta(Long idPregunta) {this.idPregunta = idPregunta;}

    public List<String> getOpciones() {return opciones;}

    public void setOpciones(List<String> opciones) {this.opciones = opciones;}

    public String getTextoPregunta() {return textoPregunta;}

    public void setTextoPregunta(String textoPregunta) {this.textoPregunta = textoPregunta;}

    public String getTipo() {return tipo;}

    public void setTipo(String tipo) {this.tipo = tipo;}

    public Integer getOrden() {return orden;}

    public void setOrden(Integer orden) {this.orden = orden;}

    public Encuesta getEncuesta() {return encuesta;}

    public void setEncuesta(Encuesta encuesta) {this.encuesta = encuesta;}

    public List<Respuesta> getRespuestas() {return respuestas;}

    public void setRespuestas(List<Respuesta> respuestas) {this.respuestas = respuestas;}
}
