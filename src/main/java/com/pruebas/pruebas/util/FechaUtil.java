package com.pruebas.pruebas.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase de utilidades para formateo de fechas.
 *
 * Proporciona métodos estáticos para convertir objetos {@link Date} en {@link String}
 * con un formato estándar.
 */
public class FechaUtil {

    /**
     * Formatea una fecha al patrón "dd/MM/yyyy HH:mm".
     *
     * @param fecha Fecha a formatear
     * @return Fecha formateada como String, o cadena vacía si la fecha es null
     */
    public static String format(Date fecha) {
        if (fecha == null) return "";
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(fecha);
    }
}
