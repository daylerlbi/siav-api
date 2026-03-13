package com.sistemas_mangager_be.edu_virtual_ufps.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cambio_estado_matriculas")
public class CambioEstadoMatricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "matricula_id")
    private Matricula matriculaId;

    @ManyToOne
    @JoinColumn(name = "estado_matricula_id")
    private EstadoMatricula estadoMatriculaId;

    private Date fechaCambioEstado;

    private String usuarioCambioEstado;

    private String semestre;
}
