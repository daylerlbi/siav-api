package com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MateriaMatriculadaDivisistId implements Serializable {
    private String codCarrera;
    private String codAlumno;
    private String codCarMat;
    private String grupo;
    private String codMatMat;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MateriaMatriculadaDivisistId that = (MateriaMatriculadaDivisistId) o;
        return Objects.equals(codCarrera, that.codCarrera) &&
                Objects.equals(codAlumno, that.codAlumno) &&
                Objects.equals(codCarMat, that.codCarMat) &&
                Objects.equals(grupo, that.grupo) &&
                Objects.equals(codMatMat, that.codMatMat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCarrera, codAlumno, codCarMat, grupo, codMatMat);
    }
}
