package com.sistemas_mangager_be.edu_virtual_ufps.shared.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotasPosgradoRequest {

    private Long matriculaId;
    private Double nota;
    private String realizadoPor;

}
