package com.sistemas_mangager_be.edu_virtual_ufps.repositories.divisist;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist.EstudianteDivisist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteDivisistRepository extends JpaRepository<EstudianteDivisist, String> {

    List<EstudianteDivisist> findByNomCarrera(String nomCarrera);

    List<EstudianteDivisist> findByCodigoStartingWith(String codigo);

    List<EstudianteDivisist> findByActivoTrue();

    Optional<EstudianteDivisist> findByDocumento(String documento);

    Optional<EstudianteDivisist> findByMoodleId(String moodleId);

    @Query("SELECT e FROM EstudianteDivisist e WHERE e.email LIKE %:domain%")
    List<EstudianteDivisist> findByEmailDomain(@Param("domain") String domain);

    @Query("SELECT e FROM EstudianteDivisist e WHERE e.moodleId IS NULL AND e.activo = true")
    List<EstudianteDivisist> findEstudiantesSinMoodleId();

    @Query("SELECT e FROM EstudianteDivisist e WHERE " +
            "(LOWER(e.primerNombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
            "LOWER(e.primerApellido) LIKE LOWER(CONCAT('%', :nombre, '%')))")
    List<EstudianteDivisist> findByNombreOrApellido(@Param("nombre") String nombre);
}
