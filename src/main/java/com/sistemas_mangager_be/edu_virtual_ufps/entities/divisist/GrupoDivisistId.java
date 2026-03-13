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
public class GrupoDivisistId implements Serializable {
    private String codCarrera;
    private String codMateria;
    private String grupo;
    private String ciclo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrupoDivisistId that = (GrupoDivisistId) o;
        return Objects.equals(codCarrera, that.codCarrera) &&
                Objects.equals(codMateria, that.codMateria) &&
                Objects.equals(grupo, that.grupo) &&
                Objects.equals(ciclo, that.ciclo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codCarrera, codMateria, grupo, ciclo);
    }
}
