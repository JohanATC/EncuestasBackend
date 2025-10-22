// com.pruebas.pruebas.service.EstadisticasService.java
package com.pruebas.pruebas.service;

import com.pruebas.pruebas.entity.*;
import com.pruebas.pruebas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstadisticasService {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private EncuestaRepository encuestaRepository;

    public Map<String, Object> obtenerEstadisticasEncuesta(Long encuestaId) {
        Map<String, Object> estadisticas = new HashMap<>();

        // Datos generales de la encuesta
        Optional<Encuesta> encuestaOpt = encuestaRepository.findById(encuestaId);
        if (encuestaOpt.isEmpty()) {
            throw new RuntimeException("Encuesta no encontrada");
        }

        Encuesta encuesta = encuestaOpt.get();
        estadisticas.put("encuesta_id", encuestaId);
        estadisticas.put("titulo", encuesta.getTitulo());
        estadisticas.put("descripcion", encuesta.getDescripcion());

        // Obtener preguntas de esta encuesta
        List<Pregunta> preguntas = preguntaRepository.findByEncuestaIdEncuesta(encuestaId);
    
        // Calcular total de respuestas (contando respuestas por cada pregunta)
        long totalRespuestas = 0;
        for (Pregunta pregunta : preguntas) {
            totalRespuestas += respuestaRepository.countByPreguntaIdPregunta(pregunta.getIdPregunta());
        }
        estadisticas.put("total_respuestas", totalRespuestas);

        // Generar estadísticas por pregunta
        List<Map<String, Object>> estadisticasPreguntas = new ArrayList<>();

        for (Pregunta pregunta : preguntas) {
            Map<String, Object> statsPregunta = generarEstadisticasPregunta(pregunta);
            estadisticasPreguntas.add(statsPregunta);
        }

        estadisticas.put("estadisticas", estadisticasPreguntas);
        return estadisticas;
    }

    private Map<String, Object> generarEstadisticasPregunta(Pregunta pregunta) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("pregunta_id", pregunta.getIdPregunta());
        stats.put("pregunta_texto", pregunta.getTextoPregunta());
        stats.put("tipo_pregunta", pregunta.getTipo());

        // Obtener todas las respuestas para esta pregunta
        List<Respuesta> respuestas = respuestaRepository.findByPreguntaIdPregunta(pregunta.getIdPregunta());
        stats.put("total_respuestas_pregunta", respuestas.size());

        switch (pregunta.getTipo().toLowerCase()) {
            case "seleccion_unica":
            case "si_no":
            case "verdadero_falso":
                stats.putAll(generarEstadisticasCategorica(respuestas, pregunta.getOpciones()));
                break;
            case "seleccion_multiple":
            case "checkbox":
                stats.putAll(generarEstadisticasMultiple(respuestas, pregunta.getOpciones()));
                break;
            case "escala":
                stats.putAll(generarEstadisticasEscala(respuestas));
                break;
            case "abierta":
                stats.putAll(generarEstadisticasAbierta(respuestas));
                break;
            default:
                stats.putAll(generarEstadisticasGenerica(respuestas));
        }

        return stats;
    }

    private Map<String, Object> generarEstadisticasCategorica(List<Respuesta> respuestas, List<String> opciones) {
        Map<String, Object> stats = new HashMap<>();
        Map<String, Integer> conteo = new HashMap<>();

        // Inicializar conteo con las opciones disponibles
        if (opciones != null) {
            for (String opcion : opciones) {
                conteo.put(opcion, 0);
            }
        }

        // Contar respuestas
        for (Respuesta respuesta : respuestas) {
            String valor = respuesta.getRespuesta();
            if (valor != null) {
                conteo.put(valor, conteo.getOrDefault(valor, 0) + 1);
            }
        }

        stats.put("tipo_grafico", "pie");
        stats.put("labels", new ArrayList<>(conteo.keySet()));
        stats.put("valores", new ArrayList<>(conteo.values()));
        stats.put("colores", generarColores(conteo.size()));

        return stats;
    }

    private Map<String, Object> generarEstadisticasEscala(List<Respuesta> respuestas) {
        Map<String, Object> stats = new HashMap<>();
        Map<String, Integer> conteo = new HashMap<>();

        // Para escala 1-5
        for (int i = 1; i <= 5; i++) {
            conteo.put(String.valueOf(i), 0);
        }

        for (Respuesta respuesta : respuestas) {
            String valor = respuesta.getRespuesta();
            if (valor != null && conteo.containsKey(valor)) {
                conteo.put(valor, conteo.get(valor) + 1);
            }
        }

        stats.put("tipo_grafico", "bar");
        stats.put("labels", Arrays.asList("1", "2", "3", "4", "5"));
        stats.put("valores", Arrays.asList(
                conteo.get("1"), conteo.get("2"), conteo.get("3"),
                conteo.get("4"), conteo.get("5")
        ));
        stats.put("promedio", calcularPromedioEscala(respuestas));

        return stats;
    }

    private double calcularPromedioEscala(List<Respuesta> respuestas) {
        if (respuestas.isEmpty()) return 0;

        double suma = 0;
        int count = 0;
        for (Respuesta respuesta : respuestas) {
            try {
                if (respuesta.getRespuesta() != null) {
                    suma += Double.parseDouble(respuesta.getRespuesta());
                    count++;
                }
            } catch (NumberFormatException e) {
                // Ignorar valores no numéricos
            }
        }
        return count > 0 ? suma / count : 0;
    }

    private Map<String, Object> generarEstadisticasMultiple(List<Respuesta> respuestas, List<String> opciones) {
        Map<String, Object> stats = new HashMap<>();
        Map<String, Integer> conteo = new HashMap<>();

        if (opciones != null) {
            for (String opcion : opciones) {
                conteo.put(opcion, 0);
            }
        }

        for (Respuesta respuesta : respuestas) {
            if (respuesta.getRespuesta() != null) {
                // Para múltiple selección, separar por comas
                String[] selecciones = respuesta.getRespuesta().split(",");
                for (String seleccion : selecciones) {
                    String opcion = seleccion.trim();
                    if (conteo.containsKey(opcion)) {
                        conteo.put(opcion, conteo.get(opcion) + 1);
                    } else {
                        conteo.put(opcion, conteo.getOrDefault(opcion, 0) + 1);
                    }
                }
            }
        }

        stats.put("tipo_grafico", "bar");
        stats.put("labels", new ArrayList<>(conteo.keySet()));
        stats.put("valores", new ArrayList<>(conteo.values()));

        return stats;
    }

    private Map<String, Object> generarEstadisticasAbierta(List<Respuesta> respuestas) {
        Map<String, Object> stats = new HashMap<>();
        // Para preguntas abiertas, devolver las respuestas textuales
        List<String> textos = respuestas.stream()
                .map(Respuesta::getRespuesta)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        stats.put("tipo_grafico", "texto");
        stats.put("respuestas", textos);
        stats.put("total_respuestas", textos.size());

        return stats;
    }

    private Map<String, Object> generarEstadisticasGenerica(List<Respuesta> respuestas) {
        Map<String, Object> stats = new HashMap<>();
        // Para tipos desconocidos, contar frecuencias genéricas
        Map<String, Integer> conteo = new HashMap<>();

        for (Respuesta respuesta : respuestas) {
            String valor = respuesta.getRespuesta();
            if (valor != null) {
                conteo.put(valor, conteo.getOrDefault(valor, 0) + 1);
            }
        }

        stats.put("tipo_grafico", "bar");
        stats.put("labels", new ArrayList<>(conteo.keySet()));
        stats.put("valores", new ArrayList<>(conteo.values()));
        stats.put("colores", generarColores(conteo.size()));

        return stats;
    }

    private List<String> generarColores(int cantidad) {
        // Paleta de colores atractivos
        String[] coloresBase = {
                "#4CAF50", "#2196F3", "#FFC107", "#F44336", "#9C27B0",
                "#00BCD4", "#FF9800", "#795548", "#607D8B", "#3F51B5"
        };

        List<String> colores = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            colores.add(coloresBase[i % coloresBase.length]);
        }
        return colores;
    }
}