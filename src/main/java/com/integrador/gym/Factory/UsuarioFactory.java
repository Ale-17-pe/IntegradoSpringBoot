package com.integrador.gym.Factory;

import com.integrador.gym.Dto.Creacion.UsuarioCreacionDTO;
import com.integrador.gym.Dto.UsuarioDTO;
import com.integrador.gym.Model.UsuarioModel;


public interface UsuarioFactory {
    UsuarioModel crearDesdeDTO(UsuarioCreacionDTO dto);
}
