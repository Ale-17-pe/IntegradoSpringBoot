package com.integrador.gym.Service;

import com.integrador.gym.Dto.Actualizacion.PlanActualizacionDTO;
import com.integrador.gym.Dto.Creacion.PlanCreacionDTO;
import com.integrador.gym.Dto.PlanDTO;
import com.integrador.gym.Model.PlanModel;

import java.util.List;
import java.util.Optional;

public interface    PlanService {
    List<PlanDTO> listarTodos();
    List<PlanDTO> listarActivos();
    Optional<PlanModel> obtenerPorId(Long id);
    PlanDTO crear(PlanCreacionDTO dto);
    PlanDTO actualizar(Long id, PlanActualizacionDTO dto);
    void eliminar(Long id);
    PlanActualizacionDTO obtenerDTOParaEdicion(Long id);

}
