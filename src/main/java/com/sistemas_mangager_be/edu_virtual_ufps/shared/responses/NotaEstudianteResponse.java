package com.sistemas_mangager_be.edu_virtual_ufps.shared.responses;

import lombok.Data;

@Data
public class NotaEstudianteResponse {
    private Long matriculaId;
    private String materiaNombre;
    private String materiaCodigo;
    private String materiaCreditos;
    private String materiaSemestre;
    private Double nota;
    private String semestre;
    private String estadoMatricula;
}