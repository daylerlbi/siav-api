package com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CohorteDTO {

    private Integer id;
    private String nombre;
    private Date fechaCreacion;

}
