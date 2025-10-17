package com.pruebas.pruebas.resource;

import com.pruebas.pruebas.dto.RespuestaDTO;
import com.pruebas.pruebas.service.RespuestaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas con {@link RespuestaDTO}.
 * <p>
 * Permite registrar, consultar, actualizar y eliminar respuestas asociadas a preguntas específicas.
 * </p>
 *
 * <h3>Rutas base:</h3>
 * <ul>
 *   <li>GET    → {@code /api/respuestas}</li>
 *   <li>GET    → {@code /api/respuestas/{id}}</li>
 *   <li>POST   → {@code /api/respuestas/pregunta/{idPregunta}}</li>
 *   <li>PUT    → {@code /api/respuestas/{id}}</li>
 *   <li>DELETE → {@code /api/respuestas/{id}}</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/respuestas")
public class RespuestaResource {

    private final RespuestaService respuestaService;

    public RespuestaResource(RespuestaService respuestaService) {
        this.respuestaService = respuestaService;
    }

    /**
     * Obtiene todas las respuestas registradas.
     *
     * @return lista de {@link RespuestaDTO}.
     */
    @GetMapping
    public ResponseEntity<List<RespuestaDTO>> getAll() {
        List<RespuestaDTO> respuestas = respuestaService.getAll();
        return ResponseEntity.ok(respuestas);
    }

    /**
     * Obtiene una respuesta por su ID.
     *
     * @param id identificador de la respuesta.
     * @return objeto {@link RespuestaDTO} correspondiente.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RespuestaDTO> getById(@PathVariable Long id) {
        RespuestaDTO respuesta = respuestaService.getById(id);
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Crea una nueva respuesta asociada a una pregunta específica.
     *
     * @param idPregunta identificador de la pregunta a la que se asocia.
     * @param dto datos de la respuesta.
     * @return respuesta creada con código 201 (Created).
     */
    @PostMapping("/pregunta/{idPregunta}")
    public ResponseEntity<RespuestaDTO> create(
            @PathVariable Long idPregunta,
            @Valid @RequestBody RespuestaDTO dto) {

        RespuestaDTO nuevaRespuesta = respuestaService.create(idPregunta, dto);
        return ResponseEntity.status(201).body(nuevaRespuesta);
    }

    /**
     * Actualiza una respuesta existente.
     *
     * @param id identificador de la respuesta.
     * @param dto datos actualizados.
     * @return respuesta modificada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RespuestaDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RespuestaDTO dto) {

        RespuestaDTO actualizada = respuestaService.update(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    /**
     * Elimina una respuesta por su identificador.
     *
     * @param id identificador de la respuesta.
     * @return código 204 (sin contenido) si la eliminación fue exitosa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        respuestaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
