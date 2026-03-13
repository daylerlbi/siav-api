package com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.divisist;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrupoDivisistDTO {
    private String codCarrera;
    private String codMateria;
    private String grupo;
    private String codProfesor;
    private String semestre;
    private String ciclo;
    private Integer numAlumMatriculados;
    private Integer numMaxAlumnos;
    private String moodleId;
    private Boolean activo;
}