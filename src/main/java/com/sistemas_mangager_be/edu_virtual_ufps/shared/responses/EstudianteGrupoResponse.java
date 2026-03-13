package com.sistemas_mangager_be.edu_virtual_ufps.shared.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteGrupoResponse {

    private Long id;
    private String grupoNombre;
    private String grupoCodigo;
    private String grupoCohorte;
    private Integer grupoCohorteId;
    private String docenteNombre;
    private List<estudianteResponse> estudiantes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class estudianteResponse {
        private Integer id;
        private String nombre;
        private String nombre2;
        private String apellido;
        private String apellido2;
        private String codigo;
        private String email;
        private String moodleId;
    }
}
