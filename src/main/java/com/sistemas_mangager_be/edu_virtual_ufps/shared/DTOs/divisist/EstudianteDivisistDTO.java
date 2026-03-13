package com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.divisist;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstudianteDivisistDTO {
    private String codigo;
    private String nomCarrera;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String documento;
    private String tipoDocumento;
    private LocalDate fechaNacimiento;
    private String tMatriculado;
    private String descTipoCar;
    private String email;
    private String moodleId;
    private Boolean activo;
}