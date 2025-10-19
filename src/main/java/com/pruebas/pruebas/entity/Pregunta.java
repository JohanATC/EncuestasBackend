package com.pruebas.pruebas.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;

/**
 * <h2>Entidad: Pregunta</h2>
 *
 * Representa una pregunta perteneciente a una encuesta.
 * Cada pregunta define su tipo (abierta, opción múltiple, sí/no)
 * y puede almacenar un conjunto de opciones en formato JSONB.
 *
 * <p>Se almacena en la tabla <b>pregunta</b> de PostgreSQL.</p>
 *
 * <h3>Relaciones:</h3>
 * <ul>
 *   <li>🔹 Muchas preguntas pertenecen a una {@link Encuesta}.</li>
 *   <li>🔹 Una pregunta puede tener múltiples {@link Respuesta} asociadas.</li>
 * </ul>
 *
 * <h3>Detalles técnicos:</h3>
 * <ul>
 *   <li>El campo {@code opciones} se guarda como JSONB mediante {@link JdbcTypeCode}.</li>
 *   <li>La relación con {@link Encuesta} es <b>perezosa (LAZY)</b> para optimizar rendimiento.</li>
 * </ul>
 *
 * @author Johan
 * @version 1.0
 */
@Entity
@Table(name = "pregunta")
public class Pregunta {

    /** Identificador único de la pregunta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPregunta;

    /** Texto visible de la pregunta. */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String textoPregunta;

    /** Tipo de la pregunta (por ejemplo: 'abierta', 'opcion_multiple', 'si_no'). */
    @Column(nullable = false, length = 50)
    private String tipo;

    /** Posición que ocupa dentro de la encuesta. */
    @Column(nullable = false)
    private Integer orden;

    /**
     * Opciones posibles de respuesta (solo si aplica).
     * <p>Ejemplo: ["Producto A", "Producto B"]</p>
     */
    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> opciones;

    /**
     * Encuesta a la que pertenece esta pregunta.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_encuesta", nullable = false)
    private Encuesta encuesta;

    /** Lista de respuestas asociadas a esta pregunta. */
    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Respuesta> respuestas;

    // Getters y setters (sin Lombok para mantener claridad en la capa Entity)
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
