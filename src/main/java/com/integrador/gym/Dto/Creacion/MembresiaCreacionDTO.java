package com.integrador.gym.Dto.Creacion;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter @Getter @Builder
public class MembresiaCreacionDTO {
    @NotNull(message = "ID del cliente es obligatorio")
    private Long idCliente;

    @NotNull(message = "ID del plan es obligatorio")
    private Long idPlan;

    private LocalDate fechaInicio;

    @NotNull(message = "ID del usuario creador es obligatorio")
    private Long idUsuarioCreador;

    @NotNull(message = "El ID del cliente es obligatorio.")
    private Long clienteId;
}
