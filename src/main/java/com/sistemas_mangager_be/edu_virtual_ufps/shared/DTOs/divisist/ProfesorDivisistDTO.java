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
public class ProfesorDivisistDTO {
    private String codProfesor;
    private String documento;
    private String tipoDocumento;
    private String nombre1;
    private String nombre2;
    private String apellido1;
    private String apellido2;
    private String email;
    private String moodleId;
    private Boolean activo;
}