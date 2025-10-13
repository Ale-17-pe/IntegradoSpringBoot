package com.integrador.gym.Service.Impl;

import com.integrador.gym.Dto.Actualizacion.PlanActualizacionDTO;
import com.integrador.gym.Dto.Creacion.PlanCreacionDTO;
import com.integrador.gym.Dto.PlanDTO;
import com.integrador.gym.Dto.PlanDTOBase;
import com.integrador.gym.Exception.PlanInvalido;
import com.integrador.gym.Exception.PlanNoEncontrado;
import com.integrador.gym.Exception.PlanNombreDuplicado;
import com.integrador.gym.Factory.PlanFactory;
import com.integrador.gym.Model.Enum.EstadoPlan;
import com.integrador.gym.Model.PlanModel;
import com.integrador.gym.Repository.PlanRepository;
import com.integrador.gym.Service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanFactory planFactory;

    @Override
    public List<PlanModel> listarTodos() {
        return planRepository.findAll();
    }

    @Override
    public List<PlanModel> listarActivos() {
        return planRepository.findByEstado(EstadoPlan.ACTIVO);
    }

    @Override
    public Optional<PlanModel> obtenerPorId(Long id) {
        return planRepository.findById(id);
    }

    @Override
    public PlanDTO crear(PlanCreacionDTO dto) {

        if (planRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe un plan con el nombre: " + dto.getNombre());
        }

        validarPlan(dto); // Reglas de negocio
        asegurarNombreUnico(dto.getNombre(), null); // Verificar unicidad

        PlanModel plan = planFactory.crearDesdeDTO(dto);
        PlanModel guardado = planRepository.save(plan);
        return toDTO(guardado);
    }

    private void validarPlan(PlanCreacionDTO dto) {
        if (dto.getPrecio() == null || dto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlanInvalido("El precio del plan debe ser mayor a 0.");
        }
        if (dto.getDuracionDias() == null || dto.getDuracionDias() <= 0) {
            throw new PlanInvalido("La duración del plan debe ser al menos 1 día.");
        }
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new PlanInvalido("El nombre del plan no puede estar vacío.");
        }
    }

    private void asegurarNombreUnico(String nombre, Long idExcluido) {
        if (idExcluido == null) {
            if (planRepository.existsByNombre(nombre)) {
                throw new PlanNombreDuplicado(nombre);
            }
        } else {
            if (planRepository.existsByNombreAndIdPlanNot(nombre, idExcluido)) {
                throw new PlanNombreDuplicado(nombre);
            }
        }
    }

    private PlanDTO toDTO(PlanModel plan) {
        PlanDTO dto = new PlanDTO();
        dto.setIdPlan(plan.getIdPlan());
        dto.setNombre(plan.getNombre());
        dto.setDescripcion(plan.getDescripcion());
        dto.setPrecio(plan.getPrecio());
        dto.setDuracionDias(plan.getDuracionDias());
        dto.setBeneficios(plan.getBeneficios());
        dto.setEstado(plan.getEstado());
        dto.setFechaCreacion(plan.getFechaCreacion());
        return dto;
    }

    @Override
    public PlanDTO actualizar(Long id, PlanActualizacionDTO dto) {
        // Validar que el plan exista
        PlanModel planExistente = obtenerPorIdOrThrow(id);

        // Validar reglas de negocio
        validarPlan((PlanDTOBase) dto); // Ahora funciona con PlanActualizacionDTO

        // Validar unicidad del nombre (excluyendo el propio plan)
        if (planRepository.existsByNombreAndIdPlanNot(dto.getNombre(), id)) {
            throw new PlanNombreDuplicado(dto.getNombre());
        }

        // Actualizar campos desde el DTO
        planExistente.setNombre(dto.getNombre());
        planExistente.setDescripcion(dto.getDescripcion());
        planExistente.setPrecio(dto.getPrecio());
        planExistente.setDuracionDias(dto.getDuracionDias());
        planExistente.setBeneficios(dto.getBeneficios());
        planExistente.setEstado(dto.getEstado());

        // Guardar en la base de datos
        PlanModel guardado = planRepository.save(planExistente);

        // Convertir a DTO para retornar
        return toDTO(guardado);
    }

    // Método genérico para validar ambos DTOs
    private void validarPlan(PlanDTOBase dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new PlanInvalido("El nombre del plan no puede estar vacío.");
        }
        if (dto.getPrecio() == null || dto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PlanInvalido("El precio del plan debe ser mayor a 0.");
        }
        if (dto.getDuracionDias() == null || dto.getDuracionDias() <= 0) {
            throw new PlanInvalido("La duración del plan debe ser al menos 1 día.");
        }
    }

    @Override
    public void eliminar(Long id) {
        PlanModel plan = obtenerPorIdOrThrow(id);
        plan.setEstado(EstadoPlan.INACTIVO);
        planRepository.save(plan);
    }

    private PlanModel obtenerPorIdOrThrow(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new PlanNoEncontrado(id));
    }
}