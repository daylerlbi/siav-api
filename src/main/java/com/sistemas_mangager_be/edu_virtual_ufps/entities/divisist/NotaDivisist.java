package com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "notas_divisist", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cod_alumno", "cod_carrera", "cod_materia", "grupo", "ciclo", "semestre"})
})
public class NotaDivisist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cod_alumno", length = 20, nullable = false)
    private String codAlumno;

    @Column(name = "cod_carrera", length = 10, nullable = false)
    private String codCarrera;

    @Column(name = "cod_materia", length = 10, nullable = false)
    private String codMateria;

    @Column(name = "grupo", length = 5, nullable = false)
    private String grupo;

    @Column(name = "ciclo", length = 10, nullable = false)
    private String ciclo;

    @Column(name = "semestre", length = 10, nullable = false)
    private String semestre; // Notas según indicaciones
    @Column(name = "p1")
    private Double p1; // Previo 1

    @Column(name = "p2")
    private Double p2; // Previo 2

    @Column(name = "p3")
    private Double p3; // Previo 3

    @Column(name = "ex")
    private Double ex; // Examen Final

    @Column(name = "hab")
    private Double hab; // Habilitación

    @Column(name = "def")
    private Double def; // Definitiva

    @Column(name = "estado_nota", length = 20)
    private String estadoNota; // APROBADO, REPROBADO, PENDIENTE, etc.

    // Campos de auditoría
    @Column(name = "fecha_sincronizacion")
    private LocalDateTime fechaSincronizacion;

    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_alumno", insertable = false, updatable = false)
    private EstudianteDivisist estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "cod_carrera", referencedColumnName = "cod_carrera", insertable = false, updatable = false),
            @JoinColumn(name = "cod_materia", referencedColumnName = "cod_materia", insertable = false, updatable = false),
            @JoinColumn(name = "grupo", referencedColumnName = "grupo", insertable = false, updatable = false),
            @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    })
    private GrupoDivisist grupoDivisist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "cod_carrera", referencedColumnName = "cod_carrera", insertable = false, updatable = false),
            @JoinColumn(name = "cod_alumno", referencedColumnName = "cod_alumno", insertable = false, updatable = false),
            @JoinColumn(name = "cod_car_mat", referencedColumnName = "cod_car_mat", insertable = false, updatable = false),
            @JoinColumn(name = "grupo", referencedColumnName = "grupo", insertable = false, updatable = false),
            @JoinColumn(name = "cod_mat_mat", referencedColumnName = "cod_mat_mat", insertable = false, updatable = false)
    })
    private MateriaMatriculadaDivisist materiaMatriculada;

    @PrePersist
    public void prePersist() {
        this.fechaSincronizacion = LocalDateTime.now();
        this.fechaUltimaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaUltimaActualizacion = LocalDateTime.now();
    }
}
