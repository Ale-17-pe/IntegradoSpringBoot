package com.integrador.gym.CQRS.Impl;

import com.integrador.gym.CQRS.UsuarioWriteService;
import com.integrador.gym.Dto.Actualizacion.UsuarioActualizacionDTO;
import com.integrador.gym.Dto.Creacion.UsuarioCreacionDTO;
import com.integrador.gym.Dto.UsuarioDTO;
import com.integrador.gym.Exception.DniYaRegistrado;
import com.integrador.gym.Exception.EmailYaRegistrado;
import com.integrador.gym.Exception.RolNoPermitido;
import com.integrador.gym.Exception.UsuarioNoEncontrado;
import com.integrador.gym.Factory.UsuarioFactory;
import com.integrador.gym.Model.Enum.Roles;
import com.integrador.gym.Model.UsuarioModel;
import com.integrador.gym.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UsuarioWriteServiceImpl implements UsuarioWriteService {

    @Autowired
    private UsuarioFactory usuarioFactory;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO crear(UsuarioCreacionDTO dto) {
        UsuarioModel usuario = usuarioFactory.crearDesdeDTO(dto);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        UsuarioModel guardado = usuarioRepository.save(usuario);
        return toDTO(guardado);
    }

    @Override
    public UsuarioDTO actualizar(Long id, UsuarioActualizacionDTO dto) {
        UsuarioModel existente = obtenerPorIdOrThrow(id);

        if (existente.getRoles() == Roles.CLIENTE && esEmpleado(dto.getRoles())) {
            throw new RolNoPermitido(
                    "No se puede convertir un cliente en empleado directamente."
            );
        }

        validarUnicidad(dto.getEmail(), dto.getDni(), id);
        validarEdadMinima(dto.getFechaNacimiento());

        existente.setDni(dto.getDni());
        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setEmail(dto.getEmail());
        existente.setTelefono(dto.getTelefono());
        existente.setRoles(dto.getRoles());
        existente.setFechaNacimiento(dto.getFechaNacimiento());
        existente.setEstado(existente.getEstado());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existente.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        UsuarioModel actualizado = usuarioRepository.save(existente);
        return toDTO(actualizado);
    }

    private void validarUnicidad(String email, String dni, Long idExcluido) {
        if (idExcluido == null) {
            if (usuarioRepository.existsByEmail(email)) {
                throw new EmailYaRegistrado(email);
            }
            if (usuarioRepository.existsByDni(dni)) {
                throw new DniYaRegistrado(dni);
            }
        } else {
            if (usuarioRepository.existsByEmailAndIdUsuarioNot(email, idExcluido)) {
                throw new EmailYaRegistrado(email);
            }
            if (usuarioRepository.existsByDniAndIdUsuarioNot(dni, idExcluido)) {
                throw new DniYaRegistrado(dni);
            }
        }
    }

    private boolean esEmpleado(Roles rol) {
        return rol == Roles.ADMIN || rol == Roles.RECEPCIONISTA;
    }

    private void validarEdadMinima(LocalDate fechaNacimiento) {
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < 16) {
            throw new IllegalArgumentException("El usuario debe tener al menos 16 años.");
        }
    }

    private UsuarioModel obtenerPorIdOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontrado(id));
    }

    private UsuarioDTO toDTO(UsuarioModel usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setDni(usuario.getDni());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setRoles(usuario.getRoles());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setFechaContratacion(usuario.getFechaContratacion());
        dto.setEstado(usuario.getEstado());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        return dto;
    }
}
