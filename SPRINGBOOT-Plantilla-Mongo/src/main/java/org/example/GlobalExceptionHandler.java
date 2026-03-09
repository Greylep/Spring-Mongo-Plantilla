package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice // Escucha errores de TODOS los controladores automáticamente
public class GlobalExceptionHandler {

    // Captura nuestra excepción personalizada (RecursoNotFoundException)
    @ExceptionHandler(RecursoNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(RecursoNotFoundException e) {
        // Devuelve JSON: { "error": "Mensaje..." } con Status 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }

    // GUÍA: Puedes añadir más @ExceptionHandler para otros tipos de error.
    // Por ejemplo, para errores de validación del DTO (@Valid):
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e) { ... }
}

// CORRECCION: La clase RecursoNotFoundException se ha movido a su propio archivo.
// Así es más limpia la arquitectura y no hay imports raros como:
// import org.example.GlobalExceptionHandler.RecursoNotFoundException;