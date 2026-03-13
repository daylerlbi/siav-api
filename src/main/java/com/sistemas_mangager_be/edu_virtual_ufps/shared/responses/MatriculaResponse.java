package com.sistemas_mangager_be.edu_virtual_ufps.shared.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatriculaResponse {

    private Long id;
    private Integer estadoMatriculaId;
    private String estadoMatriculaNombre;
    private Integer estudianteId;
    private String estudianteNombre;
    private Date fechaMatriculacion;
    private Double nota;
    private Integer grupoId;
    private String grupoNombre;
    private String nombreMateria;
    private String codigoMateria;
    private String semestreMateria;
    private String creditos;
    private Boolean correoEnviado;
    private Date fechaCorreoEnviado;
    private Date fechaNota;
    private String semestre;
    private boolean notaAbierta;
}
