package com.pruebas.pruebas.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Date;
import java.util.List;

/**
 * <h2>Entidad: Encuesta</h2>
 *
 * Representa la cabecera principal del sistema de encuestas.
 * Cada encuesta agrupa un conjunto de preguntas y define su estado de publicación.
 *
 * <p>Se almacena en la tabla <b>encuesta</b> de la base de datos PostgreSQL.</p>
 *
 * <h3>Posibles estados:</h3>
 * <ul>
 *   <li><b>ACTIVA:</b> Encuesta disponible para recibir respuestas.</li>
 *   <li><b>INACTIVA:</b> Encuesta deshabilitada temporalmente.</li>
 *   <li><b>FINALIZADA:</b> Encuesta cerrada definitivamente.</li>
 *   <li><b>BORRADOR:</b> Encuesta en edición, aún no publicada.</li>
 * </ul>
 *
 * <h3>Relaciones:</h3>
 * <ul>
 *   <li>1️⃣ Uno a muchos con {@link Pregunta} (bidireccional).</li>
 * </ul>
 *
 * <h3>Auditoría:</h3>
 * <ul>
 *   <li>La fecha de creación se asigna automáticamente antes de persistir.</li>
 * </ul>
 *
 * @author Johan
 * @version 1.0
 */
@Entity
@Table(name = "encuesta")
@Data
public class Encuesta {

    /** Identificador único de la encuesta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEncuesta;

    /** Título descriptivo de la encuesta. */
    @Column(nullable = false, length = 255)
    private String titulo;

    /** Descripción general o propósito de la encuesta. */
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /** Fecha de creación registrada automáticamente al persistir la entidad. */
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    /** Estado actual de la encuesta (ACTIVA, INACTIVA, FINALIZADA, BORRADOR). */
    @Column(length = 50)
    private String estado;

    /**
     * Lista de preguntas asociadas a la encuesta.
     * <p>
     * Relación bidireccional con {@link Pregunta}, donde esta contiene la
     * referencia inversa mediante el campo {@code encuesta}.
     * </p>
     */
    @OneToMany(mappedBy = "encuesta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Pregunta> preguntas;

    /** Asigna automáticamente la fecha de creación antes de guardar. */
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = new Date();
    }

// Getters y Setters manuales (Lombok ya los genera)
    public Long getIdEncuesta() {return idEncuesta;}

    public void setIdEncuesta(Long idEncuesta) {this.idEncuesta = idEncuesta;}

    public String getTitulo() {return titulo;}

    public void setTitulo(String titulo) {this.titulo = titulo;}

    public String getDescripcion() {return descripcion;}

    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public Date getFechaCreacion() {return fechaCreacion;}

    public void setFechaCreacion(Date fechaCreacion) {this.fechaCreacion = fechaCreacion;}

    public String getEstado() {return estado;}

    public void setEstado(String estado) {this.estado = estado;}

    public List<Pregunta> getPreguntas() {return preguntas;}

    public void setPreguntas(List<Pregunta> preguntas) {this.preguntas = preguntas;}
}
