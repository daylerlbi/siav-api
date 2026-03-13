package com.sistemas_mangager_be.edu_virtual_ufps.repositories.divisist;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist.MateriaDivisist;
import com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist.MateriaDivisistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MateriaDivisistRepository extends JpaRepository<MateriaDivisist, MateriaDivisistId> {
    List<MateriaDivisist> findByCodCarrera(String codCarrera);

    Optional<MateriaDivisist> findByCodCarreraAndCodMateria(String codCarrera, String codMateria);

    List<MateriaDivisist> findByActivoTrue();

    List<MateriaDivisist> findBySemestre(String semestre);

    List<MateriaDivisist> findByCodCarreraAndSemestre(String codCarrera, String semestre);

    Optional<MateriaDivisist> findByMoodleId(String moodleId);

    @Query("SELECT m FROM MateriaDivisist m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<MateriaDivisist> findByNombreContaining(@Param("nombre") String nombre);

    @Query("SELECT m FROM MateriaDivisist m WHERE m.moodleId IS NULL AND m.activo = true")
    List<MateriaDivisist> findMateriasSinMoodleId();

    @Query("SELECT m FROM MateriaDivisist m WHERE m.creditos >= :minCreditos AND m.creditos <= :maxCreditos")
    List<MateriaDivisist> findByCreditosRange(@Param("minCreditos") Integer minCreditos,
                                              @Param("maxCreditos") Integer maxCreditos);

    @Query("SELECT COUNT(m) FROM MateriaDivisist m WHERE m.semestre = :semestre AND m.activo = true")
    Long countBySemestreAndActivoTrue(@Param("semestre") String semestre);

    List<MateriaDivisist> findByActivaAndSemestre(String activa, String semestre);
}
