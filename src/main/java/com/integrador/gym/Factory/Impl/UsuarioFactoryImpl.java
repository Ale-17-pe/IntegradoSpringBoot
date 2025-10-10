package com.integrador.gym.Factory.Impl;

import com.integrador.gym.Dto.Creacion.UsuarioCreacionDTO;
import com.integrador.gym.Factory.UsuarioFactory;
import com.integrador.gym.Model.Enum.EstadoUsuario;
import com.integrador.gym.Model.Enum.Roles;
import com.integrador.gym.Model.UsuarioModel;
import org.springframework.stereotype.Component;

@Component
public class UsuarioFactoryImpl implements UsuarioFactory {
    @Override
    public UsuarioModel crearDesdeDTO(UsuarioCreacionDTO dto) {
        return UsuarioModel.builder()
                .dni(dto.getDni())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .password(dto.getPassword()) // En producción: encriptar con BCrypt
                .telefono(dto.getTelefono())
                .roles(Roles.CLIENTE)
                .fechaNacimiento(dto.getFechaNacimiento())
                .estado(EstadoUsuario.ACTIVO)
                .build();
    }
}
