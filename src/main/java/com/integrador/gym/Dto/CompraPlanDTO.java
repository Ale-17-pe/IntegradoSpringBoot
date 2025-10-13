package com.integrador.gym.Dto;

import com.integrador.gym.Model.Enum.MetodoPago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CompraPlanDTO {
    @NotNull(message = "ID del cliente es obligatorio para la compra")
    private Long idCliente;

    @NotNull(message = "ID del plan es obligatorio")
    private Long idPlan;

    @NotNull(message = "Método de pago es obligatorio")
    private MetodoPago metodoPago;

    @NotNull(message = "Monto del pago es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser positivo")
    private BigDecimal monto;

    private Long idUsuarioGestor;
}
