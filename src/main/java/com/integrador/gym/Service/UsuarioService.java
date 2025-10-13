package com.integrador.gym.Service;

import com.integrador.gym.Dto.Actualizacion.UsuarioActualizacionDTO;
import com.integrador.gym.Dto.Creacion.UsuarioCreacionDTO;
import com.integrador.gym.Dto.UsuarioDTO;
import com.integrador.gym.Model.UsuarioModel;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<UsuarioDTO> listarTodos();
    Optional<UsuarioModel> obtenerPorId(Long id);
    UsuarioDTO crear(UsuarioCreacionDTO dto);
    UsuarioDTO actualizar(Long id, UsuarioActualizacionDTO dto);
    void eliminar(Long id);
    UsuarioActualizacionDTO obtenerDTOParaEdicion(Long id); // ⬅️ ¡Agregamos este!
}
