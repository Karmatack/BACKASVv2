package com.adoptasalvavidas.adoptasalvavidasBack.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adoptasalvavidas.adoptasalvavidasBack.Models.DTO.FormularioAdopcionDTO;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Entity.FormularioAdopcion;
import com.adoptasalvavidas.adoptasalvavidasBack.Models.Enums.EstadoSolicitud;
import com.adoptasalvavidas.adoptasalvavidasBack.Respository.FormularioAdopcionRepository;
import com.adoptasalvavidas.adoptasalvavidasBack.Respository.PerroRepository;
import com.adoptasalvavidas.adoptasalvavidasBack.Respository.UsuarioRepository;
@Service
public class FormularioAdopcionService {
    @Autowired
    private FormularioAdopcionRepository formularioRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PerroRepository perroRepo;

    public FormularioAdopcion crearFormularioDesdeDTO(FormularioAdopcionDTO dto) {
        FormularioAdopcion formulario = new FormularioAdopcion();

        usuarioRepo.findById(dto.getUsuarioId()).ifPresent(formulario::setUsuario);
        perroRepo.findById(dto.getPerroId()).ifPresent(formulario::setPerro);

        formulario.setComentario(dto.getComentario());
        formulario.setNombreCompleto(dto.getNombreCompleto());
        formulario.setTelefono(dto.getTelefono());
        formulario.setDireccion(dto.getDireccion());
        formulario.setTipoVivienda(dto.getTipoVivienda());
        formulario.setTienePatios(dto.getTienePatios());
        formulario.setViveEnFamilia(dto.getViveEnFamilia());
        formulario.setOtrasMascotas(dto.getOtrasMascotas());
        formulario.setExperienciaMascotas(dto.getExperienciaMascotas());
        formulario.setTiempoDisponible(dto.getTiempoDisponible());
        formulario.setRazonesAdopcion(dto.getRazonesAdopcion());
        formulario.setCondicionesAceptadas(dto.getCondicionesAceptadas());

        formulario.setFechaSolicitud(LocalDateTime.now());
        formulario.setEstado(EstadoSolicitud.PENDIENTE);

        return formularioRepo.save(formulario);
    }


    public List<FormularioAdopcion> obtenerTodos() {
        return formularioRepo.findAll();
    }

    public Optional<FormularioAdopcion> obtenerPorId(Long id) {
        return formularioRepo.findById(id);
    }

    public List<FormularioAdopcion> obtenerPorUsuario(Long usuarioId) {
        return formularioRepo.findByUsuarioId(usuarioId);
    }

    public List<FormularioAdopcion> obtenerPorEstado(EstadoSolicitud estado) {
        return formularioRepo.findByEstado(estado);
    }

    public FormularioAdopcion aprobarSolicitud(Long id) {
        Optional<FormularioAdopcion> optFormulario = formularioRepo.findById(id);
        if (optFormulario.isPresent()) {
            FormularioAdopcion formulario = optFormulario.get();
            formulario.setEstado(EstadoSolicitud.APROBADO);
            return formularioRepo.save(formulario);
        }
        throw new RuntimeException("Formulario no encontrado con ID: " + id);
    }

    public FormularioAdopcion rechazarSolicitud(Long id) {
        Optional<FormularioAdopcion> optFormulario = formularioRepo.findById(id);
        if (optFormulario.isPresent()) {
            FormularioAdopcion formulario = optFormulario.get();
            formulario.setEstado(EstadoSolicitud.RECHAZADO);
            return formularioRepo.save(formulario);
        }
        throw new RuntimeException("Formulario no encontrado con ID: " + id);
    }

}
