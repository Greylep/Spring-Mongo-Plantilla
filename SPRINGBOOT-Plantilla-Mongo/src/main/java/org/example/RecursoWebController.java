package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller; // ¡OJO! @Controller, NO @RestController
import org.springframework.ui.Model;              // @RestController devolvería JSON, @Controller devuelve vistas
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/recursos")
@RequiredArgsConstructor
public class RecursoWebController {

    private final RecursoService service;

    // 1. LISTAR TODOS (Vista: lista_recursos.html)
    @GetMapping
    public String listarRecursos(Model model) {
        // Model es como una mochila que pasamos a la vista con datos
        model.addAttribute("listaRecursos", service.findAll());
        return "lista_recursos"; // Nombre del archivo en src/main/resources/templates/
    }

    // 2. MOSTRAR FORMULARIO DE CREACIÓN (Vista: formulario_recurso.html)
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        // Pasamos un DTO vacío para que Thymeleaf lo enlace con th:object
        model.addAttribute("recursoDTO", new RecursoDTO());
        model.addAttribute("idRecurso", null); // Sin ID = modo creación
        return "formulario_recurso";
    }

    // 3. GUARDAR (Recibe datos del formulario HTML via POST)
    @PostMapping("/guardar")
    public String guardarRecurso(@ModelAttribute("recursoDTO") RecursoDTO dto) {
        service.save(dto);
        return "redirect:/web/recursos"; // Redirige al listado tras guardar (patrón PRG)
    }

    // 4. MOSTRAR FORMULARIO DE EDICIÓN
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable String id, Model model) {
        Recurso recursoExistente = service.findById(id);

        // Convertimos Entity a DTO para rellenar el formulario
        RecursoDTO dto = new RecursoDTO();
        dto.setNombre(recursoExistente.getNombre());
        dto.setTipo(recursoExistente.getTipo());
        dto.setCantidad(recursoExistente.getCantidad());
        dto.setPrecio(recursoExistente.getPrecio());

        // CORRECCION: Antes faltaba mapear tipo, precio y detalle.
        // Mapeo del objeto anidado con comprobación de null
        if (recursoExistente.getDetalle() != null) {
            RecursoDTO.DetalleDTO detalleDTO = new RecursoDTO.DetalleDTO();
            detalleDTO.setDescripcion(recursoExistente.getDetalle().getDescripcion());
            detalleDTO.setActivo(recursoExistente.getDetalle().getActivo());
            dto.setDetalle(detalleDTO);
        }

        model.addAttribute("recursoDTO", dto);
        model.addAttribute("idRecurso", id); // Pasamos el ID para el action del formulario
        return "formulario_recurso"; // Reutilizamos el mismo formulario para crear y editar
    }

    // 5. ACTUALIZAR (Recibe datos del formulario de edición)
    @PostMapping("/actualizar/{id}")
    public String actualizarRecurso(@PathVariable String id,
                                    @ModelAttribute("recursoDTO") RecursoDTO dto) {
        service.update(id, dto);
        return "redirect:/web/recursos";
    }

    // 6. ELIMINAR
    // GUÍA: En HTML puro los enlaces son GET, no DELETE.
    // Para DELETE estricto necesitarías JavaScript o un formulario oculto con método POST.
    // Para el examen, usar GET para borrar es aceptable.
    @GetMapping("/eliminar/{id}")
    public String eliminarRecurso(@PathVariable String id) {
        service.deleteById(id);
        return "redirect:/web/recursos";
    }
}