package com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@Table(name = "CONSULTA_PROFESOR")
public class ProfesorOracle {
    @Id
    @Column(name = "COD_PROFESOR")
    private String codProfesor;

    @Column(name = "DOCUMENTO")
    private String documento;

    @Column(name = "TIPO_DOCUMENTO")
    private String tipoDocumento;

    @Column(name = "FECHA_NACIMIENTO")
    private Date fechaNacimiento;

    @Column(name = "NOMBRE1")
    private String nombre1;

    @Column(name = "NOMBRE2")
    private String nombre2;

    @Column(name = "APELLIDO1")
    private String apellido1;

    @Column(name = "APELLIDO2")
    private String apellido2;

    @Column(name = "EMAILI")
    private String emaili;
}


