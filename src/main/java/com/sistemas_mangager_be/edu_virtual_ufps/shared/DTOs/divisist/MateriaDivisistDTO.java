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
public class MateriaDivisistDTO {
    private String codMateria;
    private String codCarrera;
    private String nomMateria;
    private Integer creditos;
    private String semestre;
    private String moodleId;
    private Boolean activo;
    private Integer pensum;
}