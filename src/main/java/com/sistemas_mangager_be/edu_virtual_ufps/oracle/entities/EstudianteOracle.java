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

@Table(name = "CONSULTA_ESTUDIANTE")
public class EstudianteOracle {

    @Id
    @Column(name = "CODIGO")
    private String codigo;

    @Column(name = "NOMCARRERA")
    private String nomCarrera;

    @Column(name = "PRIMER_NOMBRE")
    private String primerNombre;

    @Column(name = "SEGUNDO_NOMBRE")
    private String segundoNombre;

    @Column(name = "PRIMER_APELLIDO")
    private String primerApellido;

    @Column(name = "SEGUNDO_APELLIDO")
    private String segundoApellido;

    @Column(name = "DOCUMENTO")
    private String documento;

    @Column(name = "TIPO_DOCUMENTO")
    private String tipoDocumento;

    @Column(name = "FECHA_NACIMIENTO")
    private Date fechaNacimiento;

    @Column(name = "TMATRICULADO")
    private String tMatriculado;

    @Column(name = "DESC_TIPOCAR")
    private String descTipoCar;

    @Column(name = "EMAIL")
    private String email;

}

