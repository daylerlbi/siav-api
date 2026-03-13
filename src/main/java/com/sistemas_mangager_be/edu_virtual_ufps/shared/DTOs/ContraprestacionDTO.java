package com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContraprestacionDTO {
    private Integer id;
    private Integer estudianteId;
    private String actividades;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer tipoContraprestacionId;
}
