package org.example;


public class RecursoNotFoundException extends RuntimeException {
    public RecursoNotFoundException(String id) {
        super("No se encontró el recurso con ID: " + id);
    }
}