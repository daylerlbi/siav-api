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
public class MateriaDivisistId implements Serializable {
    private String codCarrera;
    private String codMateria;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MateriaDivisistId that = (MateriaDivisistId) o;
        return Objects.equals(codCarrera, that.codCarrera) &&
                Objects.equals(codMateria, that.codMateria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCarrera, codMateria);
    }
}
