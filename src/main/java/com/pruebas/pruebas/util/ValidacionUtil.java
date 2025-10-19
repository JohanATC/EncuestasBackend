package com.pruebas.pruebas.util;

import java.util.List;

/**
 * Clase utilitaria que centraliza las validaciones relacionadas con las respuestas
 * de las preguntas dentro de una encuesta.
 * <p>
 * Su objetivo es asegurar que las respuestas ingresadas cumplan con las reglas
 * definidas por el tipo de pregunta (por ejemplo: abierta, selección múltiple, etc.).
 * </p>
 *
 * <h3>Tipos de pregunta soportados:</h3>
 * <ul>
 *   <li><b>abierta</b>: Acepta cualquier texto no vacío.</li>
 *   <li><b>seleccion_unica</b>: Debe coincidir con una opción de la lista.</li>
 *   <li><b>seleccion_multiple</b>: Puede contener varias opciones separadas por comas.</li>
 *   <li><b>si_no</b>: Se valida igual que <i>seleccion_unica</i>.</li>
 *   <li><b>escala</b>: Valor numérico entre 1 y 5.</li>
 *   <li><b>verdadero_falso</b>: Acepta distintas variantes como “Verdadero/Falso”, “Sí/No” o “true/false”.</li>
 * </ul>
 *
 * <p>
 * Esta clase no tiene estado y puede ser utilizada directamente mediante sus métodos estáticos.
 * </p>
 *
 * @author
 *     Johan Terán
 */
public class ValidacionUtil {

    /**
     * Valida si una respuesta es válida según el tipo de pregunta y las opciones permitidas.
     *
     * @param respuesta la respuesta proporcionada por el usuario.
     * @param opciones  lista de opciones válidas (solo se usa para preguntas cerradas).
     * @param tipo      tipo de pregunta, que determina las reglas de validación.
     * @return {@code true} si la respuesta cumple las reglas del tipo de pregunta,
     *         {@code false} en caso contrario.
     *
     * <h3>Reglas de validación:</h3>
     * <ul>
     *   <li><b>abierta:</b> debe contener texto no vacío.</li>
     *   <li><b>seleccion_unica / si_no:</b> debe coincidir con alguna de las opciones disponibles.</li>
     *   <li><b>seleccion_multiple / checkbox:</b> todas las respuestas separadas por coma deben existir en las opciones.</li>
     *   <li><b>escala:</b> debe ser un número entero entre 1 y 5.</li>
     *   <li><b>verdadero_falso:</b> acepta equivalentes como “Sí/No” o “true/false”.</li>
     * </ul>
     */
    public static boolean respuestaValida(String respuesta, List<String> opciones, String tipo) {
        if (tipo == null) return false;
        if (respuesta == null || respuesta.trim().isEmpty()) return false;

        switch (tipo.toLowerCase()) {

            case "abierta":
                // Acepta cualquier texto no vacío
                return !respuesta.trim().isEmpty();

            case "seleccion_unica":
            case "si_no": // Compatibilidad con nombre alternativo
                // Debe coincidir exactamente con una opción válida
                return opciones != null && opciones.contains(respuesta);

            case "seleccion_multiple":
            case "checkbox": // Compatibilidad con nombre alternativo
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
                // Valida un rango numérico entre 1 y 5
                try {
                    int valor = Integer.parseInt(respuesta);
                    return valor >= 1 && valor <= 5;
                } catch (NumberFormatException e) {
                    return false;
                }

            case "verdadero_falso":
                // Acepta equivalentes textuales y booleanos
                return "Verdadero".equalsIgnoreCase(respuesta) ||
                        "Falso".equalsIgnoreCase(respuesta) ||
                        "true".equalsIgnoreCase(respuesta) ||
                        "false".equalsIgnoreCase(respuesta) ||
                        "Sí".equalsIgnoreCase(respuesta) ||
                        "No".equalsIgnoreCase(respuesta);

            default:
                return false;
        }
    }
}
