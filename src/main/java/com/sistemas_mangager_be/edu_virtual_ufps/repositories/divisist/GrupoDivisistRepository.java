package com.sistemas_mangager_be.edu_virtual_ufps.repositories.divisist;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist.GrupoDivisist;
import com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist.GrupoDivisistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoDivisistRepository extends JpaRepository<GrupoDivisist, GrupoDivisistId> {

    List<GrupoDivisist> findByCodCarrera(String codCarrera);

    List<GrupoDivisist> findByCodProfesor(String codProfesor);

    List<GrupoDivisist> findByActivoTrue();

    List<GrupoDivisist> findBySemestre(String semestre);


    List<GrupoDivisist> findByCodCarreraAndCodMateria(String codCarrera, String codMateria);

    List<GrupoDivisist> findByCodCarreraAndSemestre(String codCarrera, String semestre);

    List<GrupoDivisist> findByCiclo(String ciclo);

    Optional<GrupoDivisist> findByMoodleId(String moodleId);

    @Query("SELECT g FROM GrupoDivisist g WHERE g.moodleId IS NULL AND g.activo = true")
    List<GrupoDivisist> findGruposSinMoodleId();

    @Query("SELECT g FROM GrupoDivisist g WHERE g.numAlumMatriculados < g.numMaxAlumnos")
    List<GrupoDivisist> findGruposConCupoDisponible();

    @Query("SELECT g FROM GrupoDivisist g WHERE g.codProfesor = :codProfesor AND g.semestre = :semestre")
    List<GrupoDivisist> findByProfesorAndSemestre(@Param("codProfesor") String codProfesor,
                                                  @Param("semestre") String semestre);

    @Query("SELECT COUNT(g) FROM GrupoDivisist g WHERE g.semestre = :semestre AND g.activo = true")
    Long countBySemestreAndActivoTrue(@Param("semestre") String semestre);

    @Query("SELECT g FROM GrupoDivisist g WHERE " +
            "g.codCarrera = :codCarrera AND g.codMateria = :codMateria AND g.grupo = :grupo AND g.ciclo = :ciclo")
    Optional<GrupoDivisist> findByCodigoCompleto(@Param("codCarrera") String codCarrera,
                                                 @Param("codMateria") String codMateria,
                                                 @Param("grupo") String grupo,
                                                 @Param("ciclo") String ciclo);
}
