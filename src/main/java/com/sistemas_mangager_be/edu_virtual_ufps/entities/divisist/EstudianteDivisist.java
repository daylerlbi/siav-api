package com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "estudiantes_divisist")
public class EstudianteDivisist {

    @Id
    @Column(name = "codigo", length = 20)
    private String codigo;

    @Column(name = "nom_carrera", length = 200)
    private String nomCarrera;

    @Column(name = "primer_nombre", length = 50)
    private String primerNombre;

    @Column(name = "segundo_nombre", length = 50)
    private String segundoNombre;

    @Column(name = "primer_apellido", length = 50)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 50)
    private String segundoApellido;

    @Column(name = "documento", length = 20)
    private String documento;

    @Column(name = "tipo_documento", length = 10)
    private String tipoDocumento;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "t_matriculado", length = 50)
    private String tMatriculado;

    @Column(name = "desc_tipo_car", length = 100)
    private String descTipoCar;

    @Column(name = "email", length = 100)
    private String email;


    @Column(name = "moodle_id", length = 20)
    private String moodleId;


    @Column(name = "fecha_sincronizacion")
    private LocalDateTime fechaSincronizacion;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;

    // Relaciones
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MateriaMatriculadaDivisist> materiasMatriculadas;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
