package com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudDTO {

    private Long id;

    private String descripcion;

    private Integer estudianteId;

    private Long matriculaId; //Solo para tipo de solicitud de cancelacion de materias

}
