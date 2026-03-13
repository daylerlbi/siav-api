package com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.divisist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MateriaMatriculadaDivisistDTO {
    private String codigo;
    private String codMateria;
    private String codCarrera;
    private String grupo;
    private String semestre;
    private String ciclo;
    private Boolean activo;
}