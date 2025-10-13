package com.integrador.gym.Dto.Actualizacion;

import com.integrador.gym.Model.Enum.EstadoPlan;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter @Getter
public class PlanActualizacionDTO {

    @Id
    private Long idPlan;
    @NotBlank(message = "El nombre del plan es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio no es válido")
    private BigDecimal precio;

    @NotNull(message = "La duración en días es obligatoria")
    @Min(value = 1, message = "La duración debe ser al menos 1 día")
    @Max(value = 3650, message = "La duración no puede superar 10 años")
    private Integer duracionDias;

    @Lob
    private String beneficios;

    @NotNull(message = "El estado del plan es obligatorio")
    private EstadoPlan estado = EstadoPlan.ACTIVO;
}
