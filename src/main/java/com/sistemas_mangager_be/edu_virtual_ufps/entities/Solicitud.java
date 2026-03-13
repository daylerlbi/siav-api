package com.sistemas_mangager_be.edu_virtual_ufps.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "tipo_solicitud_id")
    private TipoSolicitud tipoSolicitudId;

    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudianteId;

    @ManyToOne
    @JoinColumn(name = "matricula_id")
    private Matricula matriculaId;

    private Date fechaCreacion;

    private Date fechaAprobacion;

    private Boolean estaAprobada;

    @ManyToOne
    @JoinColumn(name = "soporte_id")
    private Soporte soporteId;

    @ManyToOne
    @JoinColumn(name = "solicitud_aplazamiento_id")
    private Solicitud solicitudAplazamientoId;

}
