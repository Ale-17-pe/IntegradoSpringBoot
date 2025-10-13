package com.integrador.gym.Service;

import com.integrador.gym.Dto.Actualizacion.ClienteActualizacionDTO;
import com.integrador.gym.Dto.Creacion.ClienteCreacionDTO;
import com.integrador.gym.Dto.ClienteDTO;
import com.integrador.gym.Model.ClienteModel;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<ClienteModel> listarTodos();
    Optional<ClienteModel> obtenerPorId(Long id);
    ClienteDTO crear(ClienteCreacionDTO dto);
    ClienteDTO actualizar(String dni, ClienteActualizacionDTO dto);
    void eliminar(String dni);
    boolean existePorDni(String dni);
    Optional<ClienteModel> obtenerClientePorDni(String dni);

}
