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
@Table(name = "grupos_cohortes")
public class GrupoCohorte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupoId;

    @ManyToOne
    @JoinColumn(name = "cohorte_grupo_id")
    private CohorteGrupo cohorteGrupoId;


    @ManyToOne
    @JoinColumn(name = "cohorte_id")
    private Cohorte cohorteId;

    private Date fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "docente_id")
    private Usuario docenteId;

    private String moodleId;

    private String semestre;

    private boolean semestreTerminado;
}
