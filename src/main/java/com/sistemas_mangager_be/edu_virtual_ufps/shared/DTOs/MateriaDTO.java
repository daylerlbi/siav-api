package com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MateriaDTO {
    private Integer id;
    private String codigo;
    private String nombre;
    private String creditos;
    private String semestre;
    private Integer pensumId;
    private String moodleId;
    private Integer semestrePensumId;

}
