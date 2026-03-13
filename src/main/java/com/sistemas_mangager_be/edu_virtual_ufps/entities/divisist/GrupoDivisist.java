package com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@IdClass(GrupoDivisistId.class)
@Table(name = "grupos_divisist")
public class GrupoDivisist {

    @Id
    @Column(name = "cod_carrera", length = 10)
    private String codCarrera;

    @Id
    @Column(name = "cod_materia", length = 10)
    private String codMateria;

    @Id
    @Column(name = "grupo", length = 5)
    private String grupo;

    @Id
    @Column(name = "ciclo", length = 10)
    private String ciclo;

    @Column(name = "num_alum_matriculados")
    private Integer numAlumMatriculados;

    @Column(name = "num_max_alumnos")
    private Integer numMaxAlumnos;

    @Column(name = "cod_profesor", length = 20)
    private String codProfesor;

    @Column(name = "notas_procesadas", length = 1)
    private String notasProcesadas;

    @Column(name = "cedido", length = 1)
    private String cedido;

    @Column(name = "seccional", length = 10)
    private String seccional;

    @Column(name = "dirigido", length = 1)
    private String dirigido;

    // Nuevos campos según indicaciones
    @Column(name = "moodle_id", length = 20)
    private String moodleId;

    @Column(name = "semestre", length = 10)
    private String semestre;

    // Campos de auditoría
    @Column(name = "fecha_sincronizacion")
    private LocalDateTime fechaSincronizacion;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_profesor", insertable = false, updatable = false)
    private ProfesorDivisist profesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "cod_carrera", referencedColumnName = "cod_carrera", insertable = false, updatable = false),
            @JoinColumn(name = "cod_materia", referencedColumnName = "cod_materia", insertable = false, updatable = false)
    })
    private MateriaDivisist materia;
    @OneToMany(mappedBy = "grupoDivisist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MateriaMatriculadaDivisist> materiasMatriculadas;

    @OneToMany(mappedBy = "grupoDivisist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NotaDivisist> notas;

    @PrePersist
    public void prePersist() {
        this.fechaSincronizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaSincronizacion = LocalDateTime.now();
    }
}
