package com.adoptasalvavidas.adoptasalvavidasBack.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.DTO.FormularioAdopcionDTO;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.FormularioAdopcion;
import com.adoptasalvavidas.adoptasalvavidasBack.Services.FormularioAdopcionService;

@RestController
@RequestMapping("/formulario")
@CrossOrigin(origins = "*") 
public class FormularioAdopcionController {
    
    @Autowired
    private FormularioAdopcionService formularioService;

    @PostMapping
    public ResponseEntity<FormularioAdopcion> crear(@RequestBody FormularioAdopcionDTO formularioDto) {
        FormularioAdopcion nuevo = formularioService.crearFormularioDesdeDTO(formularioDto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping
    public List<FormularioAdopcion> obtenerTodos() {
        return formularioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormularioAdopcion> obtenerPorId(@PathVariable Long id) {
        return formularioService.obtenerPorId(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<FormularioAdopcion> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return formularioService.obtenerPorUsuario(usuarioId);
    }

    @PutMapping("/{id}/aprobar")
    public ResponseEntity<FormularioAdopcion> aprobarSolicitud(@PathVariable Long id) {
        try {
            FormularioAdopcion aprobado = formularioService.aprobarSolicitud(id);
            return ResponseEntity.ok(aprobado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Rechazar una solicitud
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<FormularioAdopcion> rechazarSolicitud(@PathVariable Long id) {
        try {
            FormularioAdopcion rechazado = formularioService.rechazarSolicitud(id);
            return ResponseEntity.ok(rechazado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
