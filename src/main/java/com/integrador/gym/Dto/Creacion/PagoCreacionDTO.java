package com.integrador.gym.Dto.Creacion;

import com.integrador.gym.Model.Enum.MetodoPago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @Builder
public class PagoCreacionDTO {
    @NotNull(message = "ID de membresía es obligatorio")
    private Long idMembresia;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotNull(message = "Método de pago es obligatorio")
    private MetodoPago metodoPago;

    @NotNull(message = "Fecha y hora del pago son obligatorios")
    private LocalDateTime fechaPago;

    private String comprobante;

    // Opcional: Para auditoría o si el pago lo gestiona un tercero
    private Long idUsuarioGestor;
}
