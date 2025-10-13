package com.integrador.gym.Service.Impl;

import com.integrador.gym.Dto.CompraPlanDTO;
import com.integrador.gym.Dto.Creacion.MembresiaCreacionDTO;
import com.integrador.gym.Dto.Creacion.PagoCreacionDTO;
import com.integrador.gym.Dto.MembresiaDto;
import com.integrador.gym.Service.MatriculaService;
import com.integrador.gym.Service.MembresiaService;
import com.integrador.gym.Service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class MatriculaServiceImpl implements MatriculaService {

    @Autowired
    private MembresiaService membresiaService;

    @Autowired
    private PagoService pagoService;

    @Override
    @Transactional
    public MembresiaDto matricularCliente(CompraPlanDTO compraDto) {
        // 1. Validar la existencia y disponibilidad del Plan y Cliente
        // (Esto se hará implícitamente en MembresiaService.crear,
        // pero podrías añadir validaciones explícitas aquí, como saldo, etc.)

        // 2. Crear el DTO de creación de Membresía
        MembresiaCreacionDTO membresiaDto = MembresiaCreacionDTO.builder()
                .idCliente(compraDto.getIdCliente())
                .idPlan(compraDto.getIdPlan())
                // La fecha de inicio generalmente es hoy al momento de la compra
                .fechaInicio(LocalDate.now())
                // Asumimos que si un cliente se matricula él mismo,
                // es el idCliente quien actúa como creador/gestor.
                // Si es un administrador, usarías idUsuarioGestor.
                .idUsuarioCreador(compraDto.getIdUsuarioGestor() != null
                        ? compraDto.getIdUsuarioGestor()
                        : compraDto.getIdCliente())
                .build();

        // 3. Crear la Membresía (y validar reglas de negocio, ej. ya tiene activa)
        MembresiaDto membresiaCreada = membresiaService.crear(membresiaDto);

        // 4. Crear el DTO de creación de Pago
        PagoCreacionDTO pagoDto = PagoCreacionDTO.builder()
                .idMembresia(membresiaCreada.getIdMembresia())
                .monto(compraDto.getMonto())
                .metodoPago(compraDto.getMetodoPago()) // Asegúrate de que CompraPlanDTO use el tipo correcto (MetodoPago)
                .fechaPago(LocalDateTime.now())
                // Opcional: añadir más detalles del pago (referencia, etc.)
                .build();

        // 5. Registrar el Pago
        // Este paso podría implicar una llamada a una pasarela de pago real.
        // Asumimos que pagoService.crear() registra el pago en la DB.
        pagoService.crear(pagoDto);

        // 6. Devolver la membresía recién creada
        return membresiaCreada;
    }
}
