package com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatriculaDTO {

    private Integer id;
    private Integer estudianteId;
    private Long grupoCohorteId;
    private boolean nuevaMatricula;
    private Date fechaMatriculacion;

}
