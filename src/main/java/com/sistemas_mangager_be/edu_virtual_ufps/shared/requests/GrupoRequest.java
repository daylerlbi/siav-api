package com.sistemas_mangager_be.edu_virtual_ufps.shared.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrupoRequest {

    private Integer grupoId;

    private Integer cohorteGrupoId;

    private Integer docenteId;

    private Date fechaCreacion;
}
