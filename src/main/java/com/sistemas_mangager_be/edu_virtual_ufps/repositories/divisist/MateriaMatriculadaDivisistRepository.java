package com.sistemas_mangager_be.edu_virtual_ufps.repositories.divisist;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist.MateriaMatriculadaDivisist;
import com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist.MateriaMatriculadaDivisistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaMatriculadaDivisistRepository extends JpaRepository<MateriaMatriculadaDivisist, MateriaMatriculadaDivisistId> {

    List<MateriaMatriculadaDivisist> findByCodAlumno(String codAlumno);

    List<MateriaMatriculadaDivisist> findByCodCarrera(String codCarrera);

    List<MateriaMatriculadaDivisist> findByCodMateria(String codMateria);

    List<MateriaMatriculadaDivisist> findByActivoTrue();

    List<MateriaMatriculadaDivisist> findBySemestre(String semestre);


    List<MateriaMatriculadaDivisist> findByCiclo(String ciclo);

    List<MateriaMatriculadaDivisist> findByCodAlumnoAndSemestre(String codAlumno, String semestre);

    List<MateriaMatriculadaDivisist> findByCodCarreraAndCodMateria(String codCarrera, String codMateria);

    List<MateriaMatriculadaDivisist> findByCodCarreraAndCodMateriaAndGrupo(String codCarrera, String codMateria, String grupo);

    @Query("SELECT m FROM MateriaMatriculadaDivisist m WHERE m.codAlumno = :codAlumno AND m.estado = :estado")
    List<MateriaMatriculadaDivisist> findByEstudianteAndEstado(@Param("codAlumno") String codAlumno,
                                                               @Param("estado") String estado);

    @Query("SELECT m FROM MateriaMatriculadaDivisist m WHERE " +
            "m.codCarrera = :codCarrera AND m.codMateria = :codMateria AND m.grupo = :grupo AND m.ciclo = :ciclo")
    List<MateriaMatriculadaDivisist> findByGrupoCompleto(@Param("codCarrera") String codCarrera,
                                                         @Param("codMateria") String codMateria,
                                                         @Param("grupo") String grupo,
                                                         @Param("ciclo") String ciclo);

    @Query("SELECT COUNT(m) FROM MateriaMatriculadaDivisist m WHERE " +
            "m.codCarrera = :codCarrera AND m.codMateria = :codMateria AND m.grupo = :grupo AND m.activo = true")
    Long countMatriculadosByGrupo(@Param("codCarrera") String codCarrera,
                                  @Param("codMateria") String codMateria,
                                  @Param("grupo") String grupo);

    @Query("SELECT m FROM MateriaMatriculadaDivisist m WHERE m.semestre = :semestre AND m.estado IN :estados")
    List<MateriaMatriculadaDivisist> findBySemestreAndEstadoIn(@Param("semestre") String semestre,
                                                               @Param("estados") List<String> estados);
}
