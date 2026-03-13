package com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.divisist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActualizarNotaDivisistRequest {
    // Campos obligatorios para identificar la nota
    private String codAlumno;
    private String codMateria;
    private String codCarrera;
    private String grupo;
    private String semestre;
    private String ciclo;

    // Campos que se pueden actualizar (solo se envían los que se quieren cambiar)
    private Map<String, Object> camposActualizar;
}
