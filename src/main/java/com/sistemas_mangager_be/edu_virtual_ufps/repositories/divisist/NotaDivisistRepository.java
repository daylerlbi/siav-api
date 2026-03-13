package com.sistemas_mangager_be.edu_virtual_ufps.repositories.divisist;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist.NotaDivisist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotaDivisistRepository extends JpaRepository<NotaDivisist, Long> {

    List<NotaDivisist> findByCodAlumno(String codAlumno);

    List<NotaDivisist> findByCodCarrera(String codCarrera);

    List<NotaDivisist> findByCodMateria(String codMateria);

    List<NotaDivisist> findBySemestreAndCodAlumno(String semestre, String codAlumno);

    List<NotaDivisist> findByGrupo(String grupo);

    List<NotaDivisist> findBySemestre(String semestre);

    List<NotaDivisist> findByCiclo(String ciclo);

    List<NotaDivisist> findByActivoTrue();

    List<NotaDivisist> findByCodAlumnoAndSemestre(String codAlumno, String semestre);

    List<NotaDivisist> findByCodAlumnoAndCiclo(String codAlumno, String ciclo);

    @Query("SELECT n FROM NotaDivisist n WHERE " +
            "n.codCarrera = :codCarrera AND n.codMateria = :codMateria AND n.grupo = :grupo AND n.ciclo = :ciclo")
    List<NotaDivisist> findByGrupoCompleto(@Param("codCarrera") String codCarrera,
                                           @Param("codMateria") String codMateria,
                                           @Param("grupo") String grupo,
                                           @Param("ciclo") String ciclo);

    @Query("SELECT n FROM NotaDivisist n WHERE " +
            "n.codAlumno = :codAlumno AND n.codCarrera = :codCarrera AND n.codMateria = :codMateria " +
            "AND n.grupo = :grupo AND n.ciclo = :ciclo AND n.semestre = :semestre")
    Optional<NotaDivisist> findByEstudianteGrupoSemestre(@Param("codAlumno") String codAlumno,
                                                         @Param("codCarrera") String codCarrera,
                                                         @Param("codMateria") String codMateria,
                                                         @Param("grupo") String grupo,
                                                         @Param("ciclo") String ciclo,
                                                         @Param("semestre") String semestre);

    @Query("SELECT n FROM NotaDivisist n WHERE n.def IS NOT NULL AND n.def >= :notaMinima")
    List<NotaDivisist> findAprobados(@Param("notaMinima") Double notaMinima);

    @Query("SELECT n FROM NotaDivisist n WHERE n.def IS NOT NULL AND n.def < :notaMinima")
    List<NotaDivisist> findReprobados(@Param("notaMinima") Double notaMinima);

    @Query("SELECT n FROM NotaDivisist n WHERE n.def IS NULL AND n.activo = true")
    List<NotaDivisist> findSinNotaDefinitiva();

    @Query("SELECT n FROM NotaDivisist n WHERE n.hab IS NOT NULL AND n.activo = true")
    List<NotaDivisist> findConHabilitacion();

    @Query("SELECT AVG(n.def) FROM NotaDivisist n WHERE " +
            "n.codCarrera = :codCarrera AND n.codMateria = :codMateria AND n.grupo = :grupo " +
            "AND n.ciclo = :ciclo AND n.def IS NOT NULL")
    Double getPromedioGrupo(@Param("codCarrera") String codCarrera,
                            @Param("codMateria") String codMateria,
                            @Param("grupo") String grupo,
                            @Param("ciclo") String ciclo);

    @Query("SELECT COUNT(n) FROM NotaDivisist n WHERE " +
            "n.codCarrera = :codCarrera AND n.codMateria = :codMateria AND n.grupo = :grupo " +
            "AND n.ciclo = :ciclo AND n.def >= :notaMinima")
    Long countAprobadosByGrupo(@Param("codCarrera") String codCarrera,
                               @Param("codMateria") String codMateria,
                               @Param("grupo") String grupo,
                               @Param("ciclo") String ciclo,
                               @Param("notaMinima") Double notaMinima);

    @Query("SELECT n FROM NotaDivisist n WHERE " +
            "n.codAlumno = :codAlumno AND n.codCarrera = :codCarrera AND n.codMateria = :codMateria " +
            "AND n.grupo = :grupo AND n.activo = true")
    List<NotaDivisist> findByAlumnoCarreraMateriaGrupo(@Param("codAlumno") String codAlumno,
                                                       @Param("codCarrera") String codCarrera,
                                                       @Param("codMateria") String codMateria,
                                                       @Param("grupo") String grupo);

    // Agregar este método al NotaDivisistRepository
    @Query("SELECT n FROM NotaDivisist n WHERE " +
            "n.codCarrera = :codCarrera AND n.codMateria = :codMateria AND n.grupo = :grupo AND n.semestre = :semestre")
    List<NotaDivisist> findByGrupoCompletoConSemestre(@Param("codCarrera") String codCarrera,
                                                      @Param("codMateria") String codMateria,
                                                      @Param("grupo") String grupo,
                                                      @Param("semestre") String semestre);
}
