    package com.integrador.gym.Dto.Creacion;

    import com.integrador.gym.Model.Enum.Genero;
    import com.integrador.gym.Model.Enum.Roles;
    import jakarta.validation.constraints.*;
    import java.lang.String;

    import lombok.*;
    import org.springframework.stereotype.Component;

    import java.time.LocalDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class UsuarioCreacionDTO {
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

        @NotBlank(message = "Contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        private String password;

        @Pattern(regexp = "\\d{6,15}", message = "Teléfono debe contener entre 6 y 15 dígitos")
        private String telefono;

        private Roles roles;

        @NotNull(message = "Fecha de nacimiento es obligatoria")
        private LocalDate fechaNacimiento;

        private String direccion;


        @NotNull(message = "Género es obligatorio")
        private Genero genero;

        private Long idUsuarioCreador;
    }