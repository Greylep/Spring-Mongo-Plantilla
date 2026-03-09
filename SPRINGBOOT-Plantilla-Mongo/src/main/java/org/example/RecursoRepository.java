package org.example;


import org.example.Recurso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecursoRepository extends MongoRepository<Recurso, String> {

    // GUÍA: Consultas Derivadas (Magic Methods)
    // Solo escribes el nombre y Spring crea la consulta.

    // 1. Buscar por coincidencia exacta
    List<Recurso> findByTipo(String tipo);

    // 2. Buscar texto (LIKE) ignorando mayúsculas
    List<Recurso> findByNombreContainingIgnoreCase(String texto);

    // 3. Buscar numéricos (Mayor que)
    List<Recurso> findByCantidadGreaterThan(Integer cantidad);

    // 4. Buscar dentro del objeto anidado (Detalle -> Activo)
    List<Recurso> findByDetalleActivoTrue();

    // GUÍA: Consulta Manual con JSON (@Query)
    // Úsala si te piden algo muy raro que el nombre automático no cubra.
    // ?0 es el primer parámetro.
    @Query("{ 'precio': { $lt: ?0 } }") // Busca precio menor que parámetro
    List<Recurso> buscarBaratos(Double precioMaximo);
}