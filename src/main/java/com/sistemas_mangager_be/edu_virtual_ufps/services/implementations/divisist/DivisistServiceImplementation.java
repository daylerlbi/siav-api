package com.sistemas_mangager_be.edu_virtual_ufps.services.implementations.divisist;

import com.sistemas_mangager_be.edu_virtual_ufps.entities.Pensum;
import com.sistemas_mangager_be.edu_virtual_ufps.entities.SemestrePensum;
import com.sistemas_mangager_be.edu_virtual_ufps.entities.divisist.*;
import com.sistemas_mangager_be.edu_virtual_ufps.exceptions.PensumNotFoundException;
import com.sistemas_mangager_be.edu_virtual_ufps.repositories.PensumRepository;
import com.sistemas_mangager_be.edu_virtual_ufps.repositories.SemestrePensumRepository;
import com.sistemas_mangager_be.edu_virtual_ufps.repositories.divisist.*;
import com.sistemas_mangager_be.edu_virtual_ufps.services.interfaces.divisist.IDivisistService;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.DTOs.divisist.*;
import com.sistemas_mangager_be.edu_virtual_ufps.shared.responses.divisist.NotasDivisistResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DivisistServiceImplementation implements IDivisistService {

    private final EstudianteDivisistRepository estudianteDivisistRepository;

    private final ProfesorDivisistRepository profesorDivisistRepository;

    private final GrupoDivisistRepository grupoDivisistRepository;

    private final MateriaDivisistRepository materiaDivisistRepository;

    private final MateriaMatriculadaDivisistRepository materiaMatriculadaDivisistRepository;
    private final NotaDivisistRepository notaDivisistRepository;

    private final PensumRepository pensumRepository;

    private final SemestrePensumRepository semestrePensumRepository;


    public EstudianteDivisistDTO crearoActualizarEstudiante(EstudianteDivisistDTO estudianteDivisistDTO) {
        EstudianteDivisist estudiante;

        if (estudianteDivisistDTO.getCodigo() != null &&
                estudianteDivisistRepository.existsById(estudianteDivisistDTO.getCodigo())) {
            // Actualizar estudiante existente
            estudiante = estudianteDivisistRepository.findById(estudianteDivisistDTO.getCodigo()).orElse(null);
            if (estudiante != null) {
                BeanUtils.copyProperties(estudianteDivisistDTO, estudiante);
            }
        } else {
            // Crear nuevo estudiante
            estudiante = new EstudianteDivisist();
            BeanUtils.copyProperties(estudianteDivisistDTO, estudiante);
        }

        if (estudiante != null) {
            estudiante = estudianteDivisistRepository.save(estudiante);
        }

        EstudianteDivisistDTO responseDTO = new EstudianteDivisistDTO();
        if (estudiante != null) {
            BeanUtils.copyProperties(estudiante, responseDTO);
        }
        return responseDTO;
    }

    public ProfesorDivisistDTO crearOActualizarProfesor(ProfesorDivisistDTO profesorDivisistDTO) {
        ProfesorDivisist profesor;

        if (profesorDivisistDTO.getCodProfesor() != null &&
                profesorDivisistRepository.existsById(profesorDivisistDTO.getCodProfesor())) {
            // Actualizar profesor existente
            profesor = profesorDivisistRepository.findById(profesorDivisistDTO.getCodProfesor()).orElse(null);
            if (profesor != null) {
                BeanUtils.copyProperties(profesorDivisistDTO, profesor);
            }
        } else {
            // Crear nuevo profesor
            profesor = new ProfesorDivisist();
            BeanUtils.copyProperties(profesorDivisistDTO, profesor);
        }

        if (profesor != null) {
            profesor = profesorDivisistRepository.save(profesor);
        }

        ProfesorDivisistDTO responseDTO = new ProfesorDivisistDTO();
        if (profesor != null) {
            BeanUtils.copyProperties(profesor, responseDTO);
        }
        return responseDTO;
    }

    public MateriaDivisistDTO crearOActualizarMateria(MateriaDivisistDTO materiaDivisistDTO)
            throws PensumNotFoundException {
        MateriaDivisist materia;

        // Validar que el pensum existe
        if (materiaDivisistDTO.getPensum() == null) {
            throw new PensumNotFoundException("El ID del pensum es requerido");
        }

        Optional<Pensum> pensumOpt = pensumRepository.findById(materiaDivisistDTO.getPensum());
        if (!pensumOpt.isPresent()) {
            throw new PensumNotFoundException("Pensum con ID " + materiaDivisistDTO.getPensum() + " no encontrado");
        }

        Pensum pensum = pensumOpt.get();

        // Validar que el semestre está dentro del rango del pensum
        if (materiaDivisistDTO.getSemestre() == null) {
            throw new PensumNotFoundException("El semestre es requerido");
        }

        int semestreNumero;
        try {
            semestreNumero = Integer.parseInt(materiaDivisistDTO.getSemestre());
        } catch (NumberFormatException e) {
            throw new PensumNotFoundException("El semestre debe ser un número válido");
        }

        if (semestreNumero < 1 || semestreNumero > pensum.getCantidadSemestres()) {
            throw new PensumNotFoundException(
                    "El semestre " + semestreNumero + " no es válido para el pensum. Debe estar entre 1 y "
                            + pensum.getCantidadSemestres());
        }

        // Buscar el SemestrePensum correspondiente
        Optional<SemestrePensum> semestrePensumOpt = semestrePensumRepository
                .findFirstByPensumIdAndSemestreId_Numero(pensum, semestreNumero);

        if (!semestrePensumOpt.isPresent()) {
            throw new PensumNotFoundException(
                    "SemestrePensum no encontrado para pensum " + pensum.getId() + " y semestre " + semestreNumero);
        }

        SemestrePensum semestrePensum = semestrePensumOpt.get();

        // Crear el ID compuesto
        MateriaDivisistId materiaId = new MateriaDivisistId(
                materiaDivisistDTO.getCodCarrera(),
                materiaDivisistDTO.getCodMateria());

        if (materiaDivisistDTO.getCodMateria() != null && materiaDivisistDTO.getCodCarrera() != null &&
                materiaDivisistRepository.existsById(materiaId)) {
            // Actualizar materia existente
            materia = materiaDivisistRepository.findById(materiaId).orElse(null);
            if (materia != null) {
                // Mapear campos del DTO a la entidad, excluyendo campos de ID compuesto
                materia.setNombre(materiaDivisistDTO.getNomMateria());
                materia.setCreditos(materiaDivisistDTO.getCreditos());
                materia.setSemestre(materiaDivisistDTO.getSemestre());
                materia.setMoodleId(materiaDivisistDTO.getMoodleId());
                materia.setActivo(materiaDivisistDTO.getActivo());
                // Establecer las relaciones con Pensum y SemestrePensum
                materia.setPensumId(pensum);
                materia.setSemestrePensum(semestrePensum);
            }
        } else {
            // Crear nueva materia
            materia = new MateriaDivisist();
            materia.setCodCarrera(materiaDivisistDTO.getCodCarrera());
            materia.setCodMateria(materiaDivisistDTO.getCodMateria());
            materia.setNombre(materiaDivisistDTO.getNomMateria());
            materia.setCreditos(materiaDivisistDTO.getCreditos());
            materia.setSemestre(materiaDivisistDTO.getSemestre());
            materia.setMoodleId(materiaDivisistDTO.getMoodleId());
            materia.setActivo(materiaDivisistDTO.getActivo());
            // Establecer las relaciones con Pensum y SemestrePensum
            materia.setPensumId(pensum);
            materia.setSemestrePensum(semestrePensum);
        }

        if (materia != null) {
            materia = materiaDivisistRepository.save(materia);
        }

        MateriaDivisistDTO responseDTO = new MateriaDivisistDTO();
        if (materia != null) {
            responseDTO.setCodCarrera(materia.getCodCarrera());
            responseDTO.setCodMateria(materia.getCodMateria());
            responseDTO.setNomMateria(materia.getNombre());
            responseDTO.setCreditos(materia.getCreditos());
            responseDTO.setSemestre(materia.getSemestre());
            responseDTO.setMoodleId(materia.getMoodleId());
            responseDTO.setActivo(materia.getActivo());
            // Incluir el ID del pensum en la respuesta
            responseDTO.setPensum(materia.getPensumId() != null ? materia.getPensumId().getId() : null);
        }
        return responseDTO;
    }

    public GrupoDivisistDTO crearOActualizarGrupo(GrupoDivisistDTO grupoDivisistDTO) {
        GrupoDivisist grupo;

        // Crear el ID compuesto
        GrupoDivisistId grupoId = new GrupoDivisistId(
                grupoDivisistDTO.getCodCarrera(),
                grupoDivisistDTO.getCodMateria(),
                grupoDivisistDTO.getGrupo(),
                grupoDivisistDTO.getCiclo());

        if (grupoDivisistDTO.getCodCarrera() != null && grupoDivisistDTO.getCodMateria() != null &&
                grupoDivisistDTO.getGrupo() != null && grupoDivisistDTO.getCiclo() != null &&
                grupoDivisistRepository.existsById(grupoId)) {
            // Actualizar grupo existente
            grupo = grupoDivisistRepository.findById(grupoId).orElse(null);
            if (grupo != null) {
                grupo.setCodProfesor(grupoDivisistDTO.getCodProfesor());
                grupo.setSemestre(grupoDivisistDTO.getSemestre());
                grupo.setNumAlumMatriculados(grupoDivisistDTO.getNumAlumMatriculados());
                grupo.setNumMaxAlumnos(grupoDivisistDTO.getNumMaxAlumnos());
                grupo.setMoodleId(grupoDivisistDTO.getMoodleId());
                grupo.setActivo(grupoDivisistDTO.getActivo());
            }
        } else {
            // Crear nuevo grupo
            grupo = new GrupoDivisist();
            grupo.setCodCarrera(grupoDivisistDTO.getCodCarrera());
            grupo.setCodMateria(grupoDivisistDTO.getCodMateria());
            grupo.setGrupo(grupoDivisistDTO.getGrupo());
            grupo.setCiclo(grupoDivisistDTO.getCiclo());
            grupo.setCodProfesor(grupoDivisistDTO.getCodProfesor());
            grupo.setSemestre(grupoDivisistDTO.getSemestre());
            grupo.setNumAlumMatriculados(grupoDivisistDTO.getNumAlumMatriculados());
            grupo.setNumMaxAlumnos(grupoDivisistDTO.getNumMaxAlumnos());
            grupo.setMoodleId(grupoDivisistDTO.getMoodleId());
            grupo.setActivo(grupoDivisistDTO.getActivo());
        }

        if (grupo != null) {
            grupo = grupoDivisistRepository.save(grupo);
        }

        GrupoDivisistDTO responseDTO = new GrupoDivisistDTO();
        if (grupo != null) {
            responseDTO.setCodCarrera(grupo.getCodCarrera());
            responseDTO.setCodMateria(grupo.getCodMateria());
            responseDTO.setGrupo(grupo.getGrupo());
            responseDTO.setCiclo(grupo.getCiclo());
            responseDTO.setCodProfesor(grupo.getCodProfesor());
            responseDTO.setSemestre(grupo.getSemestre());
            responseDTO.setNumAlumMatriculados(grupo.getNumAlumMatriculados());
            responseDTO.setNumMaxAlumnos(grupo.getNumMaxAlumnos());
            responseDTO.setMoodleId(grupo.getMoodleId());
            responseDTO.setActivo(grupo.getActivo());
        }
        return responseDTO;
    }

    public MateriaMatriculadaDivisistDTO crearOActualizarMateriaMatriculada(
            MateriaMatriculadaDivisistDTO materiaMatriculadaDivisistDTO) {
        MateriaMatriculadaDivisist materiaMatriculada;

        // Crear el ID compuesto - usando los campos correctos de la entidad
        MateriaMatriculadaDivisistId materiaMatriculadaId = new MateriaMatriculadaDivisistId(
                materiaMatriculadaDivisistDTO.getCodCarrera(), // codCarrera
                materiaMatriculadaDivisistDTO.getCodigo(), // codAlumno
                materiaMatriculadaDivisistDTO.getCodCarrera(), // codCarMat (assuming same as codCarrera)
                materiaMatriculadaDivisistDTO.getGrupo(), // grupo
                materiaMatriculadaDivisistDTO.getCodMateria() // codMatMat (assuming same as codMateria)
        );

        if (materiaMatriculadaDivisistDTO.getCodigo() != null &&
                materiaMatriculadaDivisistDTO.getCodMateria() != null &&
                materiaMatriculadaDivisistDTO.getCodCarrera() != null &&
                materiaMatriculadaDivisistDTO.getGrupo() != null &&
                materiaMatriculadaDivisistRepository.existsById(materiaMatriculadaId)) {
            // Actualizar materia matriculada existente
            materiaMatriculada = materiaMatriculadaDivisistRepository.findById(materiaMatriculadaId).orElse(null);
            if (materiaMatriculada != null) {
                // Si no se proporciona semestre, calcular automáticamente
                if (materiaMatriculadaDivisistDTO.getSemestre() == null
                        || materiaMatriculadaDivisistDTO.getSemestre().isEmpty()) {
                    materiaMatriculada.setSemestre(calcularSemestre(new Date()));
                } else {
                    materiaMatriculada.setSemestre(materiaMatriculadaDivisistDTO.getSemestre());
                }
                materiaMatriculada.setCiclo(materiaMatriculadaDivisistDTO.getCiclo());
                materiaMatriculada.setActivo(materiaMatriculadaDivisistDTO.getActivo());
            }
        } else {
            // Crear nueva materia matriculada
            materiaMatriculada = new MateriaMatriculadaDivisist();
            materiaMatriculada.setCodCarrera(materiaMatriculadaDivisistDTO.getCodCarrera());
            materiaMatriculada.setCodAlumno(materiaMatriculadaDivisistDTO.getCodigo()); // usando codAlumno
            materiaMatriculada.setCodCarMat(materiaMatriculadaDivisistDTO.getCodCarrera()); // usando codCarMat
            materiaMatriculada.setCodMatMat(materiaMatriculadaDivisistDTO.getCodMateria()); // usando codMatMat
            materiaMatriculada.setCodMateria(materiaMatriculadaDivisistDTO.getCodMateria());
            materiaMatriculada.setGrupo(materiaMatriculadaDivisistDTO.getGrupo());
            materiaMatriculada.setCiclo(materiaMatriculadaDivisistDTO.getCiclo());

            // Si no se proporciona semestre, calcular automáticamente basado en la fecha
            // actual
            if (materiaMatriculadaDivisistDTO.getSemestre() == null
                    || materiaMatriculadaDivisistDTO.getSemestre().isEmpty()) {
                materiaMatriculada.setSemestre(calcularSemestre(new Date()));
            } else {
                materiaMatriculada.setSemestre(materiaMatriculadaDivisistDTO.getSemestre());
            }

            materiaMatriculada.setActivo(materiaMatriculadaDivisistDTO.getActivo());
        }

        if (materiaMatriculada != null) {
            materiaMatriculada = materiaMatriculadaDivisistRepository.save(materiaMatriculada);
        }

        MateriaMatriculadaDivisistDTO responseDTO = new MateriaMatriculadaDivisistDTO();
        if (materiaMatriculada != null) {
            responseDTO.setCodigo(materiaMatriculada.getCodAlumno()); // usando codAlumno
            responseDTO.setCodMateria(materiaMatriculada.getCodMateria());
            responseDTO.setCodCarrera(materiaMatriculada.getCodCarrera());
            responseDTO.setGrupo(materiaMatriculada.getGrupo());
            responseDTO.setCiclo(materiaMatriculada.getCiclo());
            responseDTO.setSemestre(materiaMatriculada.getSemestre());
            responseDTO.setActivo(materiaMatriculada.getActivo());
        }
        return responseDTO;
    }

    public NotaDivisistDTO crearOActualizarNota(NotaDivisistDTO notaDivisistDTO) {
        NotaDivisist nota = null;

        // Buscar nota existente por los campos únicos (no por ID ya que es
        // autogenerado)
        if (notaDivisistDTO.getCodigo() != null &&
                notaDivisistDTO.getCodMateria() != null &&
                notaDivisistDTO.getCodCarrera() != null &&
                notaDivisistDTO.getGrupo() != null &&
                notaDivisistDTO.getCiclo() != null &&
                notaDivisistDTO.getSemestre() != null) {

            // Buscar por combinación única de campos
            Optional<NotaDivisist> notaExistente = notaDivisistRepository
                    .findByEstudianteGrupoSemestre(
                            notaDivisistDTO.getCodigo(),
                            notaDivisistDTO.getCodCarrera(),
                            notaDivisistDTO.getCodMateria(),
                            notaDivisistDTO.getGrupo(),
                            notaDivisistDTO.getCiclo(),
                            notaDivisistDTO.getSemestre());

            if (notaExistente.isPresent()) {
                // Actualizar nota existente
                nota = notaExistente.get();
                nota.setP1(notaDivisistDTO.getNota1());
                nota.setP2(notaDivisistDTO.getNota2());
                nota.setP3(notaDivisistDTO.getNota3());
                nota.setDef(notaDivisistDTO.getNotaDefinitiva());
                nota.setActivo(notaDivisistDTO.getActivo());
            }
        }

        if (nota == null) {
            // Crear nueva nota
            nota = new NotaDivisist();
            nota.setCodAlumno(notaDivisistDTO.getCodigo());
            nota.setCodMateria(notaDivisistDTO.getCodMateria());
            nota.setCodCarrera(notaDivisistDTO.getCodCarrera());
            nota.setGrupo(notaDivisistDTO.getGrupo());
            nota.setCiclo(notaDivisistDTO.getCiclo());
            nota.setSemestre(notaDivisistDTO.getSemestre());
            nota.setP1(notaDivisistDTO.getNota1());
            nota.setP2(notaDivisistDTO.getNota2());
            nota.setP3(notaDivisistDTO.getNota3());
            nota.setDef(notaDivisistDTO.getNotaDefinitiva());
            nota.setActivo(notaDivisistDTO.getActivo());
        }

        nota = notaDivisistRepository.save(nota);

        NotaDivisistDTO responseDTO = new NotaDivisistDTO();
        responseDTO.setCodigo(nota.getCodAlumno());
        responseDTO.setCodMateria(nota.getCodMateria());
        responseDTO.setCodCarrera(nota.getCodCarrera());
        responseDTO.setGrupo(nota.getGrupo());
        responseDTO.setCiclo(nota.getCiclo());
        responseDTO.setSemestre(nota.getSemestre());
        responseDTO.setNota1(nota.getP1());
        responseDTO.setNota2(nota.getP2());
        responseDTO.setNota3(nota.getP3());
        responseDTO.setNotaDefinitiva(nota.getDef());
        responseDTO.setActivo(nota.getActivo());
        return responseDTO;
    }

    public NotaDivisistDTO actualizarNotaParcial(ActualizarNotaDivisistRequest request) {
        // Buscar la nota existente usando el método del repositorio
        Optional<NotaDivisist> notaOptional = notaDivisistRepository.findByEstudianteGrupoSemestre(
                request.getCodAlumno(),
                request.getCodCarrera(),
                request.getCodMateria(),
                request.getGrupo(),
                request.getCiclo(),
                request.getSemestre());

        NotaDivisist nota;

        if (!notaOptional.isPresent()) {
            // Si no existe la nota, crear una nueva
            nota = new NotaDivisist();
            nota.setCodAlumno(request.getCodAlumno());
            nota.setCodMateria(request.getCodMateria());
            nota.setCodCarrera(request.getCodCarrera());
            nota.setGrupo(request.getGrupo());
            nota.setCiclo(request.getCiclo());
            nota.setSemestre(request.getSemestre());
            nota.setActivo(true);
            // Inicializar notas en 0.0
            // nota.setP1(0.0);
            // nota.setP2(0.0);
            // nota.setP3(0.0);
            // nota.setDef(0.0);
        } else {
            nota = notaOptional.get();
        }

        // Solo actualizar los campos que vienen en el Map
        Map<String, Object> camposActualizar = request.getCamposActualizar();
        if (camposActualizar != null) {
            for (Map.Entry<String, Object> entry : camposActualizar.entrySet()) {
                String campo = entry.getKey();
                Object valor = entry.getValue();

                switch (campo) {
                    case "nota1":
                    case "p1":
                        if (valor != null) {
                            nota.setP1(((Number) valor).doubleValue());
                        }
                        break;
                    case "nota2":
                    case "p2":
                        if (valor != null) {
                            nota.setP2(((Number) valor).doubleValue());
                        }
                        break;
                    case "nota3":
                    case "p3":
                        if (valor != null) {
                            nota.setP3(((Number) valor).doubleValue());
                        }
                        break;
                    case "ex":
                        if (valor != null) {
                            nota.setEx(((Number) valor).doubleValue());
                        }
                        break;
                    case "hab":
                        if (valor != null) {
                            nota.setHab(((Number) valor).doubleValue());
                        }
                        break;
                    case "notaDefinitiva":
                    case "def":
                        if (valor != null) {
                            nota.setDef(((Number) valor).doubleValue());
                        }
                        break;
                    case "estadoNota":
                        if (valor != null) {
                            nota.setEstadoNota((String) valor);
                        }
                        break;
                    case "activo":
                        if (valor != null) {
                            nota.setActivo((Boolean) valor);
                        }
                        break;
                    default:
                        // Ignorar campos no reconocidos
                        break;
                }
            }
        }

        nota = notaDivisistRepository.save(nota);

        // Crear la respuesta DTO
        NotaDivisistDTO responseDTO = new NotaDivisistDTO();
        responseDTO.setCodigo(nota.getCodAlumno());
        responseDTO.setCodMateria(nota.getCodMateria());
        responseDTO.setCodCarrera(nota.getCodCarrera());
        responseDTO.setGrupo(nota.getGrupo());
        responseDTO.setCiclo(nota.getCiclo());
        responseDTO.setSemestre(nota.getSemestre());
        responseDTO.setNota1(nota.getP1());
        responseDTO.setNota2(nota.getP2());
        responseDTO.setNota3(nota.getP3());
        responseDTO.setNotaDefinitiva(nota.getDef());
        responseDTO.setActivo(nota.getActivo());
        return responseDTO;
    }

    @Override
    public MateriaDivisistDTO buscarMateriaPorCarreraYCodigo(String codCarrera, String codMateria) {
        // Crear el ID compuesto para buscar la materia
        MateriaDivisistId materiaId = new MateriaDivisistId(codCarrera, codMateria);

        // Buscar la materia en el repositorio
        Optional<MateriaDivisist> materiaOpt = materiaDivisistRepository.findById(materiaId);

        if (!materiaOpt.isPresent()) {
            return null; // O lanzar una excepción personalizada si prefieres
        }

        MateriaDivisist materia = materiaOpt.get();

        // Mapear la entidad al DTO
        MateriaDivisistDTO responseDTO = new MateriaDivisistDTO();
        responseDTO.setCodCarrera(materia.getCodCarrera());
        responseDTO.setCodMateria(materia.getCodMateria());
        responseDTO.setNomMateria(materia.getNombre());
        responseDTO.setCreditos(materia.getCreditos());
        responseDTO.setSemestre(materia.getSemestre());
        responseDTO.setMoodleId(materia.getMoodleId());
        responseDTO.setActivo(materia.getActivo());
        responseDTO.setPensum(materia.getPensumId() != null ? materia.getPensumId().getId() : null);

        return responseDTO;
    }

    @Override
    public GrupoDivisistDTO buscarGrupoPorCarreraMateriaYGrupo(String codCarrera, String codMateria, String grupo) {
        // Buscar grupos que coincidan con los parámetros (puede haber múltiples ciclos)
        List<GrupoDivisist> grupos = grupoDivisistRepository.findByCodCarreraAndCodMateria(codCarrera, codMateria);

        // Filtrar por grupo específico
        Optional<GrupoDivisist> grupoOpt = grupos.stream()
                .filter(g -> g.getGrupo().equals(grupo))
                .findFirst();

        if (!grupoOpt.isPresent()) {
            return null; // Retornar null para que el controlador maneje el objeto vacío
        }

        GrupoDivisist grupoEntity = grupoOpt.get();

        // Mapear la entidad al DTO
        GrupoDivisistDTO responseDTO = new GrupoDivisistDTO();
        responseDTO.setCodCarrera(grupoEntity.getCodCarrera());
        responseDTO.setCodMateria(grupoEntity.getCodMateria());
        responseDTO.setGrupo(grupoEntity.getGrupo());
        responseDTO.setCiclo(grupoEntity.getCiclo());
        responseDTO.setCodProfesor(grupoEntity.getCodProfesor());
        responseDTO.setSemestre(grupoEntity.getSemestre());
        responseDTO.setNumAlumMatriculados(grupoEntity.getNumAlumMatriculados());
        responseDTO.setNumMaxAlumnos(grupoEntity.getNumMaxAlumnos());
        responseDTO.setMoodleId(grupoEntity.getMoodleId());
        responseDTO.setActivo(grupoEntity.getActivo());

        return responseDTO;
    }

    @Override
    public ProfesorDivisistDTO buscarProfesorPorCodigo(String codProfesor) {
        // Buscar el profesor en el repositorio
        Optional<ProfesorDivisist> profesorOpt = profesorDivisistRepository.findById(codProfesor);

        if (!profesorOpt.isPresent()) {
            return null; // Retornar null para que el controlador maneje el objeto vacío
        }

        ProfesorDivisist profesor = profesorOpt.get();

        // Mapear la entidad al DTO
        ProfesorDivisistDTO responseDTO = new ProfesorDivisistDTO();
        responseDTO.setCodProfesor(profesor.getCodProfesor());
        responseDTO.setDocumento(profesor.getDocumento());
        responseDTO.setTipoDocumento(profesor.getTipoDocumento());
        responseDTO.setNombre1(profesor.getNombre1());
        responseDTO.setNombre2(profesor.getNombre2());
        responseDTO.setApellido1(profesor.getApellido1());
        responseDTO.setApellido2(profesor.getApellido2());
        responseDTO.setEmail(profesor.getEmail());
        responseDTO.setMoodleId(profesor.getMoodleId());
        responseDTO.setActivo(profesor.getActivo());

        return responseDTO;
    }

    @Override
    public EstudianteDivisistDTO buscarEstudiantePorCodigo(String codigo) {
        // Buscar el estudiante en el repositorio
        Optional<EstudianteDivisist> estudianteOpt = estudianteDivisistRepository.findById(codigo);

        if (!estudianteOpt.isPresent()) {
            return null; // Retornar null para que el controlador maneje el objeto vacío
        }

        EstudianteDivisist estudiante = estudianteOpt.get();

        // Mapear la entidad al DTO
        EstudianteDivisistDTO responseDTO = new EstudianteDivisistDTO();
        responseDTO.setCodigo(estudiante.getCodigo());
        responseDTO.setNomCarrera(estudiante.getNomCarrera());
        responseDTO.setPrimerNombre(estudiante.getPrimerNombre());
        responseDTO.setSegundoNombre(estudiante.getSegundoNombre());
        responseDTO.setPrimerApellido(estudiante.getPrimerApellido());
        responseDTO.setSegundoApellido(estudiante.getSegundoApellido());
        responseDTO.setDocumento(estudiante.getDocumento());
        responseDTO.setTipoDocumento(estudiante.getTipoDocumento());
        responseDTO.setFechaNacimiento(estudiante.getFechaNacimiento());
        responseDTO.setTMatriculado(estudiante.getTMatriculado());
        responseDTO.setDescTipoCar(estudiante.getDescTipoCar());
        responseDTO.setEmail(estudiante.getEmail());
        responseDTO.setMoodleId(estudiante.getMoodleId());
        responseDTO.setActivo(estudiante.getActivo());

        return responseDTO;
    }

    @Override
    public NotaDivisistDTO buscarNotaPorAlumnoCarreraMateriaGrupo(String codAlumno, String codCarrera,
                                                                  String codMateria, String grupo) {
        // Buscar las notas que coincidan con los parámetros
        List<NotaDivisist> notas = notaDivisistRepository.findByAlumnoCarreraMateriaGrupo(codAlumno, codCarrera,
                codMateria, grupo);

        if (notas.isEmpty()) {
            return null; // Retornar null para que el controlador maneje el objeto vacío
        }

        // Si hay múltiples registros, tomar el más reciente
        NotaDivisist nota = notas.get(0);
        if (notas.size() > 1) {
            nota = notas.stream()
                    .max((n1, n2) -> n1.getFechaUltimaActualizacion().compareTo(n2.getFechaUltimaActualizacion()))
                    .orElse(nota);
        }

        // Mapear la entidad al DTO
        NotaDivisistDTO responseDTO = new NotaDivisistDTO();
        responseDTO.setCodigo(nota.getCodAlumno());
        responseDTO.setCodMateria(nota.getCodMateria());
        responseDTO.setCodCarrera(nota.getCodCarrera());
        responseDTO.setGrupo(nota.getGrupo());
        responseDTO.setCiclo(nota.getCiclo());
        responseDTO.setSemestre(nota.getSemestre());
        responseDTO.setNota1(nota.getP1());
        responseDTO.setNota2(nota.getP2());
        responseDTO.setNota3(nota.getP3());
        responseDTO.setNotaDefinitiva(nota.getDef());
        responseDTO.setActivo(nota.getActivo());

        return responseDTO;
    }

    @Override
    public List<NotasDivisistResponse> listarNotasPorEstudianteSemestreActual(String codAlumno) {
        // Calcular el semestre actual basado en la fecha actual
        String semestreActual = calcularSemestre(new Date());

        // Buscar las notas del estudiante para el semestre calculado
        List<NotaDivisist> notas = notaDivisistRepository.findBySemestreAndCodAlumno(semestreActual, codAlumno);

        // Convertir las entidades a responses
        return notas.stream()
                .map(this::convertirNotaAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotasDivisistResponse> listarNotasPorGrupoSemestreActual(String codCarrera, String codMateria, String grupo) {
        // Calcular el semestre actual basado en la fecha actual
        String semestreActual = calcularSemestre(new Date());

        // Buscar las notas del grupo para el semestre calculado
        List<NotaDivisist> notas = notaDivisistRepository.findByGrupoCompletoConSemestre(codCarrera, codMateria, grupo, semestreActual);

        // Convertir las entidades a responses
        return notas.stream()
                .map(this::convertirNotaAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotasDivisistResponse> listarNotasPorSemestreActual() {
        // Calcular el semestre actual basado en la fecha actual
        String semestreActual = calcularSemestre(new Date());

        // Buscar todas las notas del semestre calculado
        List<NotaDivisist> notas = notaDivisistRepository.findBySemestre(semestreActual);

        // Convertir las entidades a responses
        return notas.stream()
                .map(this::convertirNotaAResponse)
                .collect(Collectors.toList());
    }

    private NotasDivisistResponse convertirNotaAResponse(NotaDivisist nota) {
        NotasDivisistResponse response = new NotasDivisistResponse();

        // Campos básicos de la nota
        response.setCodAlumno(nota.getCodAlumno());
        response.setCodCarrera(nota.getCodCarrera());
        response.setCodMateria(nota.getCodMateria());
        response.setGrupo(nota.getGrupo());
        response.setCiclo(nota.getCiclo());
        response.setSemestre(nota.getSemestre());
        response.setNota1(nota.getP1());
        response.setNota2(nota.getP2());
        response.setNota3(nota.getP3());
        response.setExamen(nota.getEx());
        response.setHabilitacion(nota.getHab());
        response.setNotaDefinitiva(nota.getDef());
        response.setEstadoNota(nota.getEstadoNota());
        response.setActivo(nota.getActivo());

        // Formatear fecha de última actualización
        if (nota.getFechaUltimaActualizacion() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            response.setFechaUltimaActualizacion(nota.getFechaUltimaActualizacion().format(formatter));
        }

        // Buscar información adicional de la materia
        try {
            MateriaDivisistId materiaId = new MateriaDivisistId(nota.getCodCarrera(), nota.getCodMateria());
            Optional<MateriaDivisist> materiaOpt = materiaDivisistRepository.findById(materiaId);
            if (materiaOpt.isPresent()) {
                MateriaDivisist materia = materiaOpt.get();
                response.setNombreMateria(materia.getNombre());
                response.setCreditos(materia.getCreditos());
            }
        } catch (Exception e) {
            // Si no se encuentra la materia, dejar campos en null
            response.setNombreMateria("Materia no encontrada");
            response.setCreditos(0);
        }

        // Buscar información del profesor del grupo
        try {
            List<GrupoDivisist> grupos = grupoDivisistRepository.findByCodCarreraAndCodMateria(
                    nota.getCodCarrera(), nota.getCodMateria());

            Optional<GrupoDivisist> grupoOpt = grupos.stream()
                    .filter(g -> g.getGrupo().equals(nota.getGrupo()) &&
                            g.getCiclo().equals(nota.getCiclo()))
                    .findFirst();

            if (grupoOpt.isPresent()) {
                GrupoDivisist grupo = grupoOpt.get();
                response.setCodProfesor(grupo.getCodProfesor());

                // Buscar nombre del profesor
                if (grupo.getCodProfesor() != null) {
                    Optional<ProfesorDivisist> profesorOpt = profesorDivisistRepository
                            .findById(grupo.getCodProfesor());
                    if (profesorOpt.isPresent()) {
                        ProfesorDivisist profesor = profesorOpt.get();
                        String nombreCompleto = (profesor.getNombre1() != null ? profesor.getNombre1() : "") + " " +
                                (profesor.getNombre2() != null ? profesor.getNombre2() : "") + " " +
                                (profesor.getApellido1() != null ? profesor.getApellido1() : "") + " " +
                                (profesor.getApellido2() != null ? profesor.getApellido2() : "");
                        response.setNombreProfesor(nombreCompleto.trim());
                    }
                }
            }
        } catch (Exception e) {
            // Si no se encuentra el grupo o profesor, dejar campos en null
            response.setCodProfesor("No asignado");
            response.setNombreProfesor("Profesor no encontrado");
        }

        return response;
    }

    private String calcularSemestre(Date fechaMatriculacion) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaMatriculacion);

        int mes = cal.get(Calendar.MONTH) + 1; // Enero = 0
        int anio = cal.get(Calendar.YEAR);

        return anio + "-" + (mes <= 6 ? "I" : "II");
    }
}
