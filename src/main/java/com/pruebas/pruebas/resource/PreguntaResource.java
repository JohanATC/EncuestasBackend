package com.pruebas.pruebas.resource;

import com.pruebas.pruebas.dto.PreguntaDTO;
import com.pruebas.pruebas.service.PreguntaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar las operaciones sobre {@link PreguntaDTO}.
 * <p>
 * Permite crear, consultar, actualizar y eliminar preguntas asociadas a una encuesta.
 * </p>
 *
 * <h3>Rutas base:</h3>
 * <ul>
 *   <li>POST   → {@code /api/preguntas}</li>
 *   <li>GET    → {@code /api/preguntas}</li>
 *   <li>GET    → {@code /api/preguntas/{id}}</li>
 *   <li>PUT    → {@code /api/preguntas/{id}}</li>
 *   <li>DELETE → {@code /api/preguntas/{id}}</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/preguntas")
public class PreguntaResource {

    private final PreguntaService preguntaService;

    public PreguntaResource(PreguntaService preguntaService) {
        this.preguntaService = preguntaService;
    }

    /**
     * Crea una nueva pregunta asociada a una encuesta.
     *
     * @param preguntaDTO datos de la pregunta.
     * @return pregunta creada con su ID asignado.
     */
    @PostMapping
    public ResponseEntity<PreguntaDTO> create(@Valid @RequestBody PreguntaDTO preguntaDTO) {
        PreguntaDTO nuevaPregunta = preguntaService.create(preguntaDTO);
        return ResponseEntity.ok(nuevaPregunta);
    }

    /**
     * Obtiene todas las preguntas disponibles.
     *
     * @return lista de {@link PreguntaDTO}.
     */
    @GetMapping
    public ResponseEntity<List<PreguntaDTO>> getAll() {
        List<PreguntaDTO> lista = preguntaService.getAll();
        return ResponseEntity.ok(lista);
    }

    /**
     * Busca una pregunta por su identificador.
     *
     * @param id identificador único de la pregunta.
     * @return la pregunta si existe, o 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PreguntaDTO> getById(@PathVariable Long id) {
        PreguntaDTO pregunta = preguntaService.getById(id);
        if (pregunta != null) {
            return ResponseEntity.ok(pregunta);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Actualiza una pregunta existente.
     *
     * @param id identificador de la pregunta.
     * @param preguntaDTO datos nuevos a actualizar.
     * @return pregunta actualizada o 404 si no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PreguntaDTO> update(@PathVariable Long id, @RequestBody PreguntaDTO preguntaDTO) {
        PreguntaDTO actualizada = preguntaService.update(id, preguntaDTO);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Elimina una pregunta por su ID.
     *
     * @param id identificador de la pregunta a eliminar.
     * @return código 204 si se elimina correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        preguntaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
