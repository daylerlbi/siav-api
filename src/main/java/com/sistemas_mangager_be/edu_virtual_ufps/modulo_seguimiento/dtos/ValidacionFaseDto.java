package com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.dtos;

import com.sistemas_mangager_be.edu_virtual_ufps.modulo_seguimiento.entities.enums.EstadoRevision;
import lombok.Data;

@Data
public class ValidacionFaseDto {
    private EstadoRevision estadoRevision;
    private String comentarioRevision;
}
