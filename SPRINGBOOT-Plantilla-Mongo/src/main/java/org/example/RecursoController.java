package org.example;


import org.example.RecursoDTO;
import org.example.Recurso;
import org.example.RecursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController // Indica que devuelve JSON
@RequestMapping("/api/v1/recursos") // GUÍA: Ruta base para todo
@RequiredArgsConstructor
public class RecursoController {

    private final RecursoService service;

    // 1. GET ALL
    @GetMapping
    public ResponseEntity<List<Recurso>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // 2. GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Recurso> getOne(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // 3. GET FILTER (Por Tipo) -> /api/v1/recursos/tipo/coche
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Recurso>> getByTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.findByTipo(tipo));
    }

    // 4. GET SEARCH (Query Param) -> /api/v1/recursos/buscar?nombre=algo
    @GetMapping("/buscar")
    public ResponseEntity<List<Recurso>> search(@RequestParam String nombre) {
        return ResponseEntity.ok(service.buscarPorNombre(nombre));
    }

    // 5. GET ACTIVOS -> /api/v1/recursos/activos
    @GetMapping("/activos")
    public ResponseEntity<List<Recurso>> getActivos() {
        return ResponseEntity.ok(service.buscarActivos());
    }

    // 6. GET TOTAL -> /api/v1/recursos/total
    @GetMapping("/total")
    public ResponseEntity<Map<String, Long>> count() {
        return ResponseEntity.ok(Map.of("total", service.count()));
    }

    // 7. POST (Crear)
    // @Valid activa las validaciones del DTO (@NotNull, etc)
    @PostMapping
    public ResponseEntity<Recurso> create(@Valid @RequestBody RecursoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    // 8. PUT (Actualizar)
    @PutMapping("/{id}")
    public ResponseEntity<Recurso> update(@PathVariable String id, @Valid @RequestBody RecursoDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // 9. DELETE (Borrar uno)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 (Sin contenido)
    }

    // 10. DELETE ALL (Reset)
    @DeleteMapping("/borrar-todo")
    public ResponseEntity<Void> deleteAll() {
        service.deleteAll();
        return ResponseEntity.noContent().build();
    }
}