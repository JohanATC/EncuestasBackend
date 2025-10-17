package com.pruebas.pruebas.service;

import com.pruebas.pruebas.dto.RespuestaDTO;
import com.pruebas.pruebas.entity.Pregunta;
import com.pruebas.pruebas.entity.Respuesta;
import com.pruebas.pruebas.mapper.RespuestaMapper;
import com.pruebas.pruebas.repository.PreguntaRepository;
import com.pruebas.pruebas.repository.RespuestaRepository;
import com.pruebas.pruebas.util.ValidacionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la lógica de negocio para gestionar respuestas.
 */
@Service
public class RespuestaService {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private RespuestaMapper respuestaMapper;

    public RespuestaDTO getById(Long id) {
        Respuesta r = respuestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada"));
        return respuestaMapper.toDTO(r);
    }

    public List<RespuestaDTO> getAll() {
        return respuestaRepository.findAll().stream()
                .map(respuestaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RespuestaDTO create(Long idPregunta, RespuestaDTO dto) {
        Pregunta pregunta = preguntaRepository.findById(idPregunta)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));

        if (pregunta.getEncuesta() == null) {
            throw new RuntimeException("La pregunta no está asociada a ninguna encuesta válida");
        }

        if (!ValidacionUtil.respuestaValida(dto.getRespuesta(), pregunta.getOpciones(), pregunta.getTipo())) {
            throw new RuntimeException("Respuesta no válida para el tipo de pregunta: " + pregunta.getTipo());
        }

        Respuesta r = respuestaMapper.toEntity(dto);
        r.setPregunta(pregunta);
        r.setFechaRespuesta(new Date());

        return respuestaMapper.toDTO(respuestaRepository.save(r));
    }

    public RespuestaDTO update(Long id, RespuestaDTO dto) {
        Respuesta existing = respuestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada"));

        Pregunta pregunta = existing.getPregunta();

        if (!ValidacionUtil.respuestaValida(dto.getRespuesta(), pregunta.getOpciones(), pregunta.getTipo())) {
            throw new RuntimeException("Respuesta no válida para el tipo de pregunta: " + pregunta.getTipo());
        }

        existing.setRespuesta(dto.getRespuesta());
        return respuestaMapper.toDTO(respuestaRepository.save(existing));
    }

    public boolean delete(Long id) {
        if (!respuestaRepository.existsById(id)) return false;
        respuestaRepository.deleteById(id);
        return true;
    }
}
