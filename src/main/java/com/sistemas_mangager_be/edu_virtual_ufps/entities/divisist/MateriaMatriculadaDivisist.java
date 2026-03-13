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
@IdClass(MateriaMatriculadaDivisistId.class)
@Table(name = "materias_matriculadas_divisist")
public class MateriaMatriculadaDivisist {

    @Id
    @Column(name = "cod_carrera", length = 10)
    private String codCarrera;

    @Id
    @Column(name = "cod_alumno", length = 20)
    private String codAlumno;

    @Id
    @Column(name = "cod_car_mat", length = 10)
    private String codCarMat;

    @Id
    @Column(name = "grupo", length = 5)
    private String grupo;

    @Id
    @Column(name = "cod_mat_mat", length = 10)
    private String codMatMat;

    @Column(name = "cod_materia", length = 10)
    private String codMateria;

    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "seccional", length = 10)
    private String seccional;

    @Column(name = "semestre", length = 10)
    private String semestre;

    @Column(name = "ciclo", length = 10)
    private String ciclo;

    // Campos de auditoría
    @Column(name = "fecha_sincronizacion")
    private LocalDateTime fechaSincronizacion;

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

    @OneToMany(mappedBy = "materiaMatriculada", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
