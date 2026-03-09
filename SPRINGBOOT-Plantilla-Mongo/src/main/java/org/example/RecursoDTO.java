package org.example;


import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Data
public class RecursoDTO {

    // GUÍA: Validaciones. Si no se cumplen, devuelve error 400.
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    private String tipo;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    private Double precio;

    // Mismo objeto anidado para recibirlo en el JSON
    private DetalleDTO detalle;

    @Data
    public static class DetalleDTO {
        private String descripcion;
        private Boolean activo;
    }
}