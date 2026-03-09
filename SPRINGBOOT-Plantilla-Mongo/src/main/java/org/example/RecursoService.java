package org.example;

// CORRECCION: Import limpio ahora que RecursoNotFoundException está en su propio archivo
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Inyección automática del Repositorio por constructor
public class RecursoService {

    private final RecursoRepository repository;

    // --- MÉTODOS DE LECTURA ---

    public List<Recurso> findAll() {
        return repository.findAll();
    }

    public Recurso findById(String id) {
        // Si no existe, lanza RecursoNotFoundException -> GlobalExceptionHandler devuelve 404
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNotFoundException(id));
    }

    // Métodos que llaman a las consultas custom del repositorio
    public List<Recurso> findByTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    public List<Recurso> buscarPorNombre(String texto) {
        return repository.findByNombreContainingIgnoreCase(texto);
    }

    public List<Recurso> buscarActivos() {
        return repository.findByDetalleActivoTrue();
    }

    public long count() {
        return repository.count();
    }

    // --- MÉTODOS DE ESCRITURA ---

    public Recurso save(RecursoDTO dto) {
        Recurso recurso = new Recurso();
        mapDtoToEntity(dto, recurso); // Convierte DTO a Entidad
        return repository.save(recurso);
    }

    public Recurso update(String id, RecursoDTO dto) {
        Recurso existente = findById(id); // Verifica si existe (lanza 404 si no)
        mapDtoToEntity(dto, existente);   // Actualiza datos
        return repository.save(existente);
    }

    public void deleteById(String id) {
        // Primero verificamos que existe antes de borrar
        if (!repository.existsById(id)) throw new RecursoNotFoundException(id);
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    // GUÍA: Método auxiliar privado para pasar datos del DTO a la Entidad.
    // Así no repetimos código entre save() y update().
    // Patrón común: se llama "mapper" o "mapDtoToEntity".
    private void mapDtoToEntity(RecursoDTO dto, Recurso recurso) {
        recurso.setNombre(dto.getNombre());
        recurso.setTipo(dto.getTipo());
        recurso.setCantidad(dto.getCantidad());
        recurso.setPrecio(dto.getPrecio());

        // Mapeo del objeto anidado con comprobación de null para evitar NullPointerException
        if (dto.getDetalle() != null) {
            // Si la entidad aún no tiene Detalle, creamos uno nuevo
            if (recurso.getDetalle() == null) recurso.setDetalle(new Recurso.Detalle());
            recurso.getDetalle().setDescripcion(dto.getDetalle().getDescripcion());
            recurso.getDetalle().setActivo(dto.getDetalle().getActivo());
        }
    }
}