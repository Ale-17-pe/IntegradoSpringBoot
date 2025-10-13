package com.integrador.gym.Model;
import com.integrador.gym.Model.Enum.EstadoUsuario;
import com.integrador.gym.Model.Enum.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @NotBlank(message = "DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "DNI debe tener 8 dígitos")
    private String dni;

    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "EmailYaRegistrado es obligatorio")
    @Email(message = "EmailYaRegistrado inválido")
    private String email;

    @NotBlank(message = "Contraseña es obligatoria")
    @Column(length = 100, nullable = false)
    private String password;

    @Pattern(regexp = "\\d{6,15}", message = "Teléfono debe contener entre 6 y 15 dígitos")
    private String telefono;

    @NotNull(message = "Rol es obligatorio")
    @Enumerated(EnumType.STRING)
    private Roles roles;

    @NotNull(message = "Fecha de nacimiento es obligatoria")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_contratacion")
    private LocalDate fechaContratacion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaCreacion = now;
        this.fechaActualizacion = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public boolean isActivo() {
        return estado == EstadoUsuario.ACTIVO;
    }


}
