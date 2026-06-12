package com.sistemas_mangager_be.edu_virtual_ufps.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EncodingUtils {

    /**
     * Corrige strings que llegaron de Oracle con encoding Windows-1252
     * interpretados incorrectamente como ISO-8859-1.
     * Ejemplo: "COMUNICACI??N" -> "COMUNICACIÓN"
     */
    public static String fixOracleString(String input) {
        if (input == null) return null;
        try {
            byte[] bytes = input.getBytes(Charset.forName("ISO-8859-1"));
            String result = new String(bytes, StandardCharsets.UTF_8);
            // Si la conversion produjo caracteres de reemplazo, devolver original
            if (result.contains("\uFFFD")) return input;
            return result;
        } catch (Exception e) {
            return input;
        }
    }
}
