package com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.Pensum;
import com.sistemas_mangager_be.edu_virtual_ufps.entities.SemestrePensum;
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
@IdClass(MateriaDivisistId.class)
@Table(name = "materias_divisist")
public class MateriaDivisist {

    @Id
    @Column(name = "cod_carrera", length = 10)
    private String codCarrera;

    @Id
    @Column(name = "cod_materia", length = 10)
    private String codMateria;

    @Column(name = "cod_dpto", length = 10)
    private String codDpto;

    @Column(name = "nombre", length = 200)
    private String nombre;

    @Column(name = "ht")
    private Integer ht;

    @Column(name = "hp")
    private Integer hp;

    @Column(name = "creditos")
    private Integer creditos;

    @Column(name = "multi_p", length = 1)
    private String multiP;

    @Column(name = "semestre", length = 10)
    private String semestre;

    @ManyToOne
    @JoinColumn(name = "pensum_id")
    private Pensum pensumId;

    @ManyToOne
    @JoinColumn(name = "semestre_pensum_id")
    private SemestrePensum semestrePensum;

    @Column(name = "activa", length = 1)
    private String activa;

    @Column(name = "nbc", length = 10)
    private String nbc;

    @Column(name = "hti")
    private Integer hti;

    @Column(name = "hasa")
    private Integer hasa;

    @Column(name = "hasl")
    private Integer hasl;

    @Column(name = "unica_nota", length = 1)
    private String unicaNota;

    @Column(name = "modulo_acu_012", length = 10)
    private String moduloAcu012;

    @Column(name = "tipo_materia", length = 50)
    private String tipoMateria;

    @Column(name = "id_micro")
    private Integer idMicro;

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
    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
