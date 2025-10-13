package com.integrador.gym.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.integrador.gym.Model.Enum.EstadoMembresia;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "membresia")
@Builder
public class MembresiaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMembresia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClienteModel cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plan", nullable = false)
    private PlanModel plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_membresia")
    private EstadoMembresia estadoMembresia;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioModel usuario;


    @OneToMany(mappedBy = "membresia", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<PagoModel> pagos;

    private Double precio;
}
