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
public class NotaDivisistDTO {
    private String codigo;
    private String codMateria;
    private String codCarrera;
    private String grupo;
    private String semestre;
    private String ciclo;
    private Double nota1;
    private Double nota2;
    private Double nota3;
    private Double notaDefinitiva;
    private String observaciones;
    private Boolean activo;
}