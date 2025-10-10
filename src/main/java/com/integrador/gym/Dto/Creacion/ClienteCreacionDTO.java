package com.integrador.gym.Dto.Creacion;

import com.integrador.gym.Model.Enum.Genero;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Setter @Getter
public class ClienteCreacionDTO {
    @NotBlank(message = "DNI es obligatorio")
    @Pattern(regexp = "\\d{7,8}", message = "DNI debe tener 7 u 8 dígitos")
    private String dni;

    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Apellido es obligatorio")
    private String apellido;

    @Pattern(regexp = "\\d{6,15}", message = "Teléfono debe contener entre 6 y 15 dígitos")
    private String telefono;

    private Genero genero;

    private String direccion;

    private String password;

    private String email;

    @NotNull(message = "Fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;

    @NotNull(message = "ID del usuario creador es obligatorio")
    private Long idUsuarioCreador;
}
