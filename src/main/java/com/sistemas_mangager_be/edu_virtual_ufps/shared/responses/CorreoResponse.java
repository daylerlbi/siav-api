package com.sistemas_mangager_be.edu_virtual_ufps.shared.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CorreoResponse {

    private String nombreEstudiante;

    private String correo;

    private String semestre;

    private Date fecha;

    public List<MatriculaResponse> matriculas;

}
