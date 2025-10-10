package com.integrador.gym.Factory.Impl;

import com.integrador.gym.Dto.Creacion.ClienteCreacionDTO;
import com.integrador.gym.Factory.ClienteFactory;
import com.integrador.gym.Model.ClienteModel;
import com.integrador.gym.Model.UsuarioModel;
import org.springframework.stereotype.Component;

@Component
public class ClienteFactoryImpl implements ClienteFactory {

    @Override
    public ClienteModel crearDesdeDTO(ClienteCreacionDTO dto, UsuarioModel usuarioCreador) {
        return ClienteModel.builder()
                .dni(dto.getDni())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .fechaNacimiento(dto.getFechaNacimiento())
                .genero(dto.getGenero())
                .usuario(usuarioCreador) // ← Asociamos el usuario creador
                .build();
    }
}
