package com.sistemas_mangager_be.edu_virtual_ufps.repositories.divisist;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist.ProfesorDivisist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesorDivisistRepository extends JpaRepository<ProfesorDivisist, String> {
    List<ProfesorDivisist> findByActivoTrue();

    Optional<ProfesorDivisist> findByDocumento(String documento);

    Optional<ProfesorDivisist> findByMoodleId(String moodleId);

    @Query("SELECT p FROM ProfesorDivisist p WHERE p.email LIKE %:domain%")
    List<ProfesorDivisist> findByEmailDomain(@Param("domain") String domain);

    @Query("SELECT p FROM ProfesorDivisist p WHERE " +
            "(LOWER(p.nombre1) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
            "LOWER(p.apellido1) LIKE LOWER(CONCAT('%', :nombre, '%')))")
    List<ProfesorDivisist> findByNombreOrApellido(@Param("nombre") String nombre);

    @Query("SELECT p FROM ProfesorDivisist p WHERE p.moodleId IS NULL AND p.activo = true")
    List<ProfesorDivisist> findProfesoresSinMoodleId();
}
