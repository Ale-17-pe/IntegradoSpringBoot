package com.integrador.gym.Repository;

import com.integrador.gym.Dto.PlanDTO;
import com.integrador.gym.Model.Enum.EstadoPlan;
import com.integrador.gym.Model.PlanModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<PlanModel, Long> {
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdPlanNot(String nombre, Long id);
    List<PlanDTO> findByEstado(EstadoPlan estado);
}
