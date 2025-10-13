package com.integrador.gym.Dto;

import com.integrador.gym.Model.Enum.EstadoUsuario;
import com.integrador.gym.Model.Enum.Roles;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter @Getter
public class UsuarioDTO {
    private Long idUsuario;
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private  String password;
    private String telefono;
    private Roles roles;
    private LocalDate fechaNacimiento;
    private LocalDate fechaContratacion;
    private EstadoUsuario estado;
    private LocalDateTime fechaCreacion;
}
