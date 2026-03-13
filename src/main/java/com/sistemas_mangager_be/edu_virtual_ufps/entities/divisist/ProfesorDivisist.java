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
@Table(name = "profesores_divisist")
public class ProfesorDivisist {

    @Id
    @Column(name = "cod_profesor", length = 20)
    private String codProfesor;

    @Column(name = "documento", length = 20)
    private String documento;

    @Column(name = "tipo_documento", length = 10)
    private String tipoDocumento;

    @Column(name = "nombre1", length = 50)
    private String nombre1;

    @Column(name = "nombre2", length = 50)
    private String nombre2;

    @Column(name = "apellido1", length = 50)
    private String apellido1;

    @Column(name = "apellido2", length = 50)
    private String apellido2;

    @Column(name = "email", length = 100)
    private String email;

    // Nuevos campos según indicaciones
    @Column(name = "moodle_id", length = 20)
    private String moodleId;

    // Campos de auditoría
    @Column(name = "fecha_sincronizacion")
    private LocalDateTime fechaSincronizacion;

    @Column(name = "activo")
    @Builder.Default
    private Boolean activo = true;

    // Relaciones
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GrupoDivisist> grupos;

    @PrePersist
    public void prePersist() {
        this.fechaSincronizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaSincronizacion = LocalDateTime.now();
    }
}
