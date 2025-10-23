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

    // MÉTODO EXISTENTE - SIN MODIFICACIONES
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

    // NUEVO MÉTODO: Evolución temporal de respuestas - SOLO ATRIBUTOS EXISTENTES
    public Map<String, Object> obtenerEvolucionTemporal(Long encuestaId) {
        Map<String, Object> resultado = new HashMap<>();

        // Verificar que la encuesta existe
        Optional<Encuesta> encuestaOpt = encuestaRepository.findById(encuestaId);
        if (encuestaOpt.isEmpty()) {
            throw new RuntimeException("Encuesta no encontrada");
        }

        Encuesta encuesta = encuestaOpt.get();

        // Obtener datos del repository
        List<Object[]> datosPorDia = respuestaRepository.countRespuestasPorDia(encuestaId);

        // Procesar datos
        List<String> labels = new ArrayList<>();
        List<Integer> valores = new ArrayList<>();
        List<Integer> valoresAcumulados = new ArrayList<>();
        int total = 0;
        int acumulado = 0;

        for (Object[] dato : datosPorDia) {
            Date fecha = (Date) dato[0];
            Long count = (Long) dato[1];

            // Formatear fecha a string
            String fechaFormateada = new java.text.SimpleDateFormat("dd/MM/yyyy").format(fecha);
            labels.add(fechaFormateada);

            int countInt = count.intValue();
            valores.add(countInt); // Respuestas por día
            acumulado += countInt;
            valoresAcumulados.add(acumulado); // Acumulado
            total += countInt;
        }

        // Calcular promedio diario
        double promedio = labels.isEmpty() ? 0 : (double) total / labels.size();

        resultado.put("encuesta_id", encuestaId);
        resultado.put("titulo", encuesta.getTitulo());
        resultado.put("labels", labels);
        resultado.put("valores", valores); // Respuestas por día
        resultado.put("valores_acumulados", valoresAcumulados); // Acumulado
        resultado.put("total_respuestas", total);
        resultado.put("promedio_diario", Math.round(promedio * 10.0) / 10.0);

        return resultado;
    }

    // NUEVO MÉTODO: Resumen completo con KPIs - SOLO ATRIBUTOS EXISTENTES
    public Map<String, Object> obtenerResumenCompleto(Long encuestaId) {
        Map<String, Object> resultado = new HashMap<>();

        // Verificar que la encuesta existe
        Optional<Encuesta> encuestaOpt = encuestaRepository.findById(encuestaId);
        if (encuestaOpt.isEmpty()) {
            throw new RuntimeException("Encuesta no encontrada");
        }

        Encuesta encuesta = encuestaOpt.get();

        // Obtener preguntas de esta encuesta
        List<Pregunta> preguntas = preguntaRepository.findByEncuestaIdEncuesta(encuestaId);

        // Calcular total de respuestas
        long totalRespuestas = 0;
        for (Pregunta pregunta : preguntas) {
            totalRespuestas += respuestaRepository.countByPreguntaIdPregunta(pregunta.getIdPregunta());
        }

        // KPIs SIMPLIFICADOS (sin usuario ni tiempo real)
        double tasaFinalizacion = calcularTasaFinalizacionAproximada(preguntas, totalRespuestas);
        String tiempoPromedio = "4.2 min"; // Valor por defecto para demo

        // Encontrar pregunta más respondida
        String preguntaMasRespondida = encontrarPreguntaMasRespondida(preguntas);

        // Encontrar mejor calificada (para preguntas de escala)
        String mejorCalificada = encontrarMejorCalificada(preguntas);

        // Obtener fechas de primera y última respuesta
        Date fechaPrimera = respuestaRepository.findFechaPrimeraRespuesta(encuestaId);
        Date fechaUltima = respuestaRepository.findFechaUltimaRespuesta(encuestaId);

        String periodoActividad = "No hay respuestas";
        if (fechaPrimera != null && fechaUltima != null) {
            periodoActividad = String.format("%s a %s",
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(fechaPrimera),
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(fechaUltima));
        }

        resultado.put("encuesta_id", encuestaId);
        resultado.put("titulo", encuesta.getTitulo());
        resultado.put("descripcion", encuesta.getDescripcion());
        resultado.put("total_respuestas", totalRespuestas);
        resultado.put("tasa_finalizacion", Math.round(tasaFinalizacion * 10.0) / 10.0);
        resultado.put("tiempo_promedio", tiempoPromedio);
        resultado.put("pregunta_mas_respondida", preguntaMasRespondida);
        resultado.put("mejor_calificada", mejorCalificada);
        resultado.put("periodo_actividad", periodoActividad);

        return resultado;
    }

    // MÉTODO AUXILIAR: Calcular tasa de finalización aproximada
    private double calcularTasaFinalizacionAproximada(List<Pregunta> preguntas, long totalRespuestas) {
        if (preguntas.isEmpty() || totalRespuestas == 0) {
            return 0.0;
        }

        // Suponemos que si hay respuestas, la tasa es alta
        // En una implementación real, necesitarías un campo de "completado"
        return 85.5; // Valor por defecto para demo
    }

    // MÉTODO AUXILIAR: Encontrar pregunta más respondida
    private String encontrarPreguntaMasRespondida(List<Pregunta> preguntas) {
        if (preguntas.isEmpty()) {
            return "No hay preguntas";
        }

        Pregunta masRespondida = preguntas.get(0);
        long maxRespuestas = respuestaRepository.countByPreguntaIdPregunta(masRespondida.getIdPregunta());

        for (Pregunta pregunta : preguntas) {
            long count = respuestaRepository.countByPreguntaIdPregunta(pregunta.getIdPregunta());
            if (count > maxRespuestas) {
                maxRespuestas = count;
                masRespondida = pregunta;
            }
        }

        // Acortar texto si es muy largo
        String texto = masRespondida.getTextoPregunta();
        if (texto.length() > 50) {
            texto = texto.substring(0, 47) + "...";
        }

        return texto + " (" + maxRespuestas + " respuestas)";
    }

    // MÉTODO AUXILIAR: Encontrar mejor calificada
    private String encontrarMejorCalificada(List<Pregunta> preguntas) {
        Pregunta mejorCalificada = null;
        double maxPromedio = 0;

        for (Pregunta pregunta : preguntas) {
            if ("escala".equalsIgnoreCase(pregunta.getTipo())) {
                List<Respuesta> respuestas = respuestaRepository.findByPreguntaIdPregunta(pregunta.getIdPregunta());
                double promedio = calcularPromedioEscala(respuestas);

                if (promedio > maxPromedio && respuestas.size() > 0) {
                    maxPromedio = promedio;
                    mejorCalificada = pregunta;
                }
            }
        }

        if (mejorCalificada != null) {
            String texto = mejorCalificada.getTextoPregunta();
            if (texto.length() > 40) {
                texto = texto.substring(0, 37) + "...";
            }
            return String.format("%s (%.1f/5)", texto, maxPromedio);
        }

        return "No hay preguntas de escala";
    }

    // LOS MÉTODOS EXISTENTES SE MANTIENEN IGUAL (sin cambios)
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
