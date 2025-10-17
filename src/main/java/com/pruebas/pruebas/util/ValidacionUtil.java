package com.pruebas.pruebas.util;

import java.util.List;

/**
 * Clase utilitaria para validar las respuestas de las preguntas de encuestas.
 */
public class ValidacionUtil {

    public static boolean respuestaValida(String respuesta, List<String> opciones, String tipo) {
        if (tipo == null) return false;
        if (respuesta == null || respuesta.trim().isEmpty()) return false;

        switch (tipo.toLowerCase()) {
            case "abierta":
                return !respuesta.trim().isEmpty();

            case "seleccion_unica":
            case "si_no": // Mantener compatibilidad
                return opciones != null && opciones.contains(respuesta);

            case "seleccion_multiple":
            case "checkbox": // Mantener compatibilidad
                if (opciones == null) return false;
                try {
                    String[] seleccionadas = respuesta.split(",");
                    for (String opcion : seleccionadas) {
                        if (!opciones.contains(opcion.trim())) {
                            return false;
                        }
                    }
                    return seleccionadas.length > 0;
                } catch (Exception e) {
                    return false;
                }

            case "escala":
                try {
                    int valor = Integer.parseInt(respuesta);
                    return valor >= 1 && valor <= 5;
                } catch (NumberFormatException e) {
                    return false;
                }

            case "verdadero_falso":
                return "Verdadero".equals(respuesta) || "Falso".equals(respuesta) ||
                        "true".equals(respuesta) || "false".equals(respuesta) ||
                        "Sí".equals(respuesta) || "No".equals(respuesta); // Compatibilidad

            default:
                return false;
        }
    }
}