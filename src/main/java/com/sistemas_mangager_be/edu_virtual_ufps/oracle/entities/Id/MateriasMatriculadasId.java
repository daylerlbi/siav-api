package com.sistemas_mangager_be.edu_virtual_ufps.oracle.entities.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MateriasMatriculadasId implements Serializable {

    private String codAlumno;
    private String codCarrera;
    private String codMateria;
    private String grupo;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MateriasMatriculadasId that))
            return false;
        return Objects.equals(codAlumno, that.codAlumno) &&
                Objects.equals(codCarrera, that.codCarrera) &&
                Objects.equals(codMateria, that.codMateria) &&
                Objects.equals(grupo, that.grupo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codAlumno, codCarrera, codMateria, grupo);
    }
}
