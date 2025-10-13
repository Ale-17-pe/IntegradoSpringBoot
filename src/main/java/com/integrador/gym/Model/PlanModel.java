package com.integrador.gym.Model;

import com.integrador.gym.Model.Enum.EstadoPlan;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "plan")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class  PlanModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(columnDefinition = "TEXT")
    private String beneficios;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado del plan es obligatorio")
    private EstadoPlan estado = EstadoPlan.ACTIVO;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }
}
