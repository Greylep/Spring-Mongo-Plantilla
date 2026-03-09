package org.example;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data // Lombok genera getters/setters
@Document(collection = "recursos") // GUÍA: Nombre de la colección en Mongo (plural)
public class Recurso {

    @Id // Marca el campo como clave primaria (_id)
    private String id;

    // Campos normales
    private String nombre;
    private String tipo;
    private Integer cantidad;
    private Double precio;

    // GUÍA: Objeto Anidado (Embedded).
    // MongoDB permite guardar objetos dentro de objetos.
    private Detalle detalle;

    @Data
    public static class Detalle {
        private String descripcion;
        private Boolean activo;
    }
}