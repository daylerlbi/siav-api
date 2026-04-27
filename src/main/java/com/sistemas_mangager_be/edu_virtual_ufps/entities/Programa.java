package com.sistemas_mangager_be.edu_virtual_ufps.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "programas")
public class Programa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(name = "es_posgrado", nullable = false)
    private Boolean esPosgrado;

    @Column(unique = true)
    private String moodleId;

    private String historicoMoodleId;

    @Column(name = "semestre_actual")
    private String semestreActual;

    @ManyToOne
    @JoinColumn(name = "tipo_programa_id")
    private TipoPrograma tipoPrograma;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Usuario director;
}