package com.integrador.gym.Factory.Impl;

import com.integrador.gym.Dto.Creacion.UsuarioCreacionDTO;
import com.integrador.gym.Factory.UsuarioFactory;
import com.integrador.gym.Model.Enum.EstadoUsuario;
import com.integrador.gym.Model.Enum.Roles;
import com.integrador.gym.Model.UsuarioModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UsuarioFactoryImpl implements UsuarioFactory {

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioFactoryImpl(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioModel crearDesdeDTO(UsuarioCreacionDTO dto) {
        Roles rol = (dto.getRoles() != null) ? dto.getRoles() : Roles.CLIENTE;

        UsuarioModel usuario = UsuarioModel.builder()
                .dni(dto.getDni())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .telefono(dto.getTelefono())
                .roles(rol)
                .fechaNacimiento(dto.getFechaNacimiento())
                .estado(EstadoUsuario.ACTIVO)
                .build();

        if (rol == Roles.ADMIN || rol == Roles.RECEPCIONISTA) {
            usuario.setFechaContratacion(LocalDate.now());
        }

        return usuario;
    }
}
