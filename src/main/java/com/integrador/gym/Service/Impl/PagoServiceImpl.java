package com.integrador.gym.Service.Impl;

import com.integrador.gym.Dto.Actualizacion.PagoActualizacionDTO;
import com.integrador.gym.Dto.Creacion.PagoCreacionDTO;
import com.integrador.gym.Dto.PagoDTO;
import com.integrador.gym.Exception.*;
import com.integrador.gym.Factory.PagoFactory;
import com.integrador.gym.Model.Enum.EstadoMembresia;
import com.integrador.gym.Model.MembresiaModel;
import com.integrador.gym.Model.PagoModel;
import com.integrador.gym.Repository.MembresiaRepository;
import com.integrador.gym.Repository.PagoRepository;
import com.integrador.gym.Service.PagoService;
import com.integrador.gym.Strategy.Impl.PrimerPago;
import com.integrador.gym.Strategy.Impl.RenovacionPago;
import com.integrador.gym.Strategy.ValidacionPago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private MembresiaRepository membresiaRepository;

    @Autowired
    private PagoFactory pagoFactory;

    @Autowired
    private List<ValidacionPago> estrategias;

    @Override
    public List<PagoModel> listarPorMembresia(Long idMembresia) {
        return pagoRepository.findByMembresiaIdMembresia(idMembresia);
    }

    @Override
    public PagoDTO crear(PagoCreacionDTO dto) {
        MembresiaModel membresia = validarMembresia(dto.getIdMembresia());
        if (dto.getFechaPago() != null && dto.getFechaPago().isAfter(LocalDateTime.now().plusMinutes(5))) {
            throw new PagoInvalido("La fecha de pago no puede ser futura.");
        }
        if (membresia.getEstadoMembresia() == EstadoMembresia.CANCELADA) {
            throw new PagoInvalido("No se puede pagar una membresía cancelada.");
        }
        long cantidadPagos = pagoRepository.countByMembresiaIdMembresia(membresia.getIdMembresia());
        boolean esPrimerPago = cantidadPagos == 0;
        ValidacionPago estrategia = null;
        for (ValidacionPago s : estrategias) {
            if (esPrimerPago && s instanceof PrimerPago) {
                estrategia = s;
                break;
            } else if (!esPrimerPago && s instanceof RenovacionPago) {
                estrategia = s;
                break;
            }
        }
        if (estrategia == null) {
            throw new RuntimeException("No se encontró estrategia para este tipo de pago");
        }

        PagoModel pago = pagoFactory.crearDesdeDTO(dto, membresia);
        PagoModel guardado = pagoRepository.save(pago);
        return toDTO(guardado);
    }

    @Override
    public PagoDTO actualizar(Long id, PagoActualizacionDTO dto) {
        PagoModel pagoExistente = obtenerPorIdOrThrow(id);

        // Validar membresía
        MembresiaModel membresia = validarMembresia(dto.getIdMembresia());

        // Validar fecha de pago
        if (dto.getFechaPago() != null && dto.getFechaPago().isAfter(LocalDateTime.now().plusMinutes(5))) {
            throw new PagoInvalido("La fecha de pago no puede ser futura.");
        }

        // Validar monto
        if (dto.getMonto() == null || dto.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PagoInvalido("El monto debe ser mayor a 0.");
        }

        // Validar estado de membresía
        if (membresia.getEstadoMembresia() == EstadoMembresia.CANCELADA) {
            throw new PagoInvalido("No se puede pagar una membresía cancelada.");
        }

        // Actualizar campos
        pagoExistente.setMembresia(membresia);
        pagoExistente.setMonto(dto.getMonto());
        pagoExistente.setMetodoPago(dto.getMetodoPago());
        pagoExistente.setFechaPago(dto.getFechaPago());
        pagoExistente.setComprobante(dto.getComprobante());

        PagoModel guardado = pagoRepository.save(pagoExistente);
        return toDTO(guardado);
    }

    @Override
    public void eliminar(Long id) {
        PagoModel pago = obtenerPorIdOrThrow(id);
        pagoRepository.delete(pago);
    }

    // === REGLAS DE NEGOCIO ===

    private MembresiaModel validarMembresia(Long idMembresia) {
        return membresiaRepository.findById(idMembresia)
                .orElseThrow(() -> new PagoInvalido("Membresía no encontrada con ID: " + idMembresia));
    }

    private PagoDTO toDTO(PagoModel pago) {
        PagoDTO dto = new PagoDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setIdMembresia(pago.getMembresia().getIdMembresia());
        dto.setNombreMembresia(pago.getMembresia().getPlan().getNombre());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setFechaPago(pago.getFechaPago());
        dto.setComprobante(pago.getComprobante());
        return dto;
    }

    private PagoModel obtenerPorIdOrThrow(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new PagoNoEncontrado(id));
    }
}
