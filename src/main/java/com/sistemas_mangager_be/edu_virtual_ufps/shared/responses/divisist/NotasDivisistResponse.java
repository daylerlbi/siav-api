package com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.divisist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotasDivisistResponse {
    private String codAlumno;
    private String codCarrera;
    private String codMateria;
    private String nombreMateria;
    private String grupo;
    private String ciclo;
    private String semestre;
    private String codProfesor;
    private String nombreProfesor;
    private Double nota1;
    private Double nota2;
    private Double nota3;
    private Double examen;
    private Double habilitacion;
    private Double notaDefinitiva;
    private String estadoNota;
    private Integer creditos;
    private Boolean activo;
    private String fechaUltimaActualizacion;
}