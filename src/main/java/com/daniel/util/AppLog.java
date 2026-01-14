package com.daniel.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppLog {

    // Archivo por defecto (en la carpeta del proyecto donde se ejecute la app)
    private static final Path LOG_PATH = Paths.get("app.log");

    // Formato de fecha/hora legible
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Nivel INFO (para operaciones normales)
    public static void info(String accion, String detalle) {
        write("INFO", accion, detalle);
    }

    // Nivel ERROR (para fallos)
    public static void error(String accion, String detalle) {
        write("ERROR", accion, detalle);
    }

    private static void write(String nivel, String accion, String detalle) {
        String ts = LocalDateTime.now().format(FMT);
        String linea = String.format("%s | %s | %s | %s%n", ts, nivel, accion, detalle);

        try {
            // Escribir en archivo
            Files.writeString(
                    // Referencia al archivo log
                    LOG_PATH,
                    // Info a copiar
                    linea,
                    // Formato de los caracteres
                    StandardCharsets.UTF_8,
                    // Si el archivo no existe, se crea
                    StandardOpenOption.CREATE,
                    // Si existe, se puede escribir en él
                    StandardOpenOption.WRITE,
                    // Añadir el texto
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            // Si el log falla, NO tumbamos la app. Como mucho lo mostramos en consola.
            System.err.println("No se pudo escribir en el log: " + e.getMessage());
        }
    }

    // Para leerlo y mostrarlo en UI
    public static String readAll() {
        try {
            if (!Files.exists(LOG_PATH)) return "(Log vacío)";
            return Files.readString(LOG_PATH, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "(Error leyendo el log: " + e.getMessage() + ")";
        }
    }
}

