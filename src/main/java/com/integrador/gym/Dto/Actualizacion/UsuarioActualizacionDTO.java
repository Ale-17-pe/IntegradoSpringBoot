package com.integrador.gym.Dto.Actualizacion;

import com.integrador.gym.Model.Enum.Roles;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter @Getter
public class UsuarioActualizacionDTO {

    @Id
    private Long idUsuario;
    @NotBlank(message = "DNI es obligatorio")
    @Pattern(regexp = "\\d{7,8}", message = "DNI debe tener 7 u 8 dígitos")
    private String dni;

    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "Email es obligatorio")
    @Email(message = "Email inválido")
    private String email;

    // Contraseña es opcional en actualización
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Pattern(regexp = "\\d{6,15}", message = "Teléfono debe contener entre 6 y 15 dígitos")
    private String telefono;

    @NotNull(message = "Rol es obligatorio")
    private Roles roles;

    @NotNull(message = "Fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;
}
