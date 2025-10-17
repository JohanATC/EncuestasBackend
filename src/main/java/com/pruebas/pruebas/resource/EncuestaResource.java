package com.pruebas.pruebas.resource;

import com.pruebas.pruebas.dto.EncuestaDTO;
import com.pruebas.pruebas.service.EncuestaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que gestiona los endpoints relacionados con {@link EncuestaDTO}.
 * <p>
 * Permite realizar operaciones CRUD sobre encuestas mediante peticiones HTTP.
 * </p>
 *
 * <h3>Rutas base:</h3>
 * <ul>
 *   <li>GET    → {@code /api/encuestas}</li>
 *   <li>GET    → {@code /api/encuestas/{id}}</li>
 *   <li>POST   → {@code /api/encuestas}</li>
 *   <li>PUT    → {@code /api/encuestas/{id}}</li>
 *   <li>DELETE → {@code /api/encuestas/{id}}</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/encuestas")
public class EncuestaResource {

    private final EncuestaService encuestaService;

    public EncuestaResource(EncuestaService encuestaService) {
        this.encuestaService = encuestaService;
    }

    /**
     * Obtiene todas las encuestas registradas.
     *
     * @return lista de encuestas en formato {@link EncuestaDTO}.
     */
    @GetMapping
    public List<EncuestaDTO> getAll() {
        return encuestaService.getAll();
    }

    /**
     * Obtiene una encuesta por su identificador único.
     *
     * @param id identificador de la encuesta.
     * @return objeto {@link EncuestaDTO} si existe.
     */
    @GetMapping("/{id}")
    public EncuestaDTO getById(@PathVariable Long id) {
        return encuestaService.getById(id);
    }

    /**
     * Crea una nueva encuesta en la base de datos.
     *
     * @param encuestaDTO datos de la encuesta a registrar.
     * @return la encuesta creada con su ID asignado.
     */
    @PostMapping
    public EncuestaDTO create(@RequestBody EncuestaDTO encuestaDTO) {
        return encuestaService.create(encuestaDTO);
    }

    /**
     * Actualiza una encuesta existente identificada por su ID.
     *
     * @param id identificador de la encuesta a actualizar.
     * @param encuestaDTO datos actualizados.
     * @return la encuesta actualizada.
     */
    @PutMapping("/{id}")
    public EncuestaDTO update(@PathVariable Long id, @RequestBody EncuestaDTO encuestaDTO) {
        return encuestaService.update(id, encuestaDTO);
    }

    /**
     * Elimina una encuesta por su identificador.
     *
     * @param id identificador de la encuesta a eliminar.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        encuestaService.delete(id);
    }

    /**
     * Elimina todas las encuestas del sistema.
     *
     * @return {@link ResponseEntity} con código 204 (sin contenido).
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAll() {
        encuestaService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
