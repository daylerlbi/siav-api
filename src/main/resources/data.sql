-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 10-06-2025 a las 04:14:44
-- Versión del servidor: 8.0.31
-- Versión de PHP: 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `edu_virtual_ufps`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `admins`
--

DROP TABLE IF EXISTS `admins`;
CREATE TABLE IF NOT EXISTS `admins` (
  `id` int NOT NULL AUTO_INCREMENT,
  `primer_nombre` varchar(255) NOT NULL,
  `segundo_nombre` varchar(255) DEFAULT NULL,
  `primer_apellido` varchar(255) NOT NULL,
  `segundo_apellido` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `es_super_admin` tinyint(1) NOT NULL,
  `activo` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `admins`
--

INSERT INTO `admins` (`id`, `primer_nombre`, `segundo_nombre`, `primer_apellido`, `segundo_apellido`, `email`, `password`, `es_super_admin`, `activo`) VALUES
(1, 'Unidad', ' ', 'Virtual', 'UFPS', 'uvirtual@ufps.edu.co', '$2a$10$ddkeQ8RKhqKAT6XTQe2iTuW5vBjWbOUAM0l4EUKVjK.rsJCnHHqZu', 1, 1),
(2, 'SIAV', ' ', 'UFPS', '', 'siavufps@gmail.com', '$2a$10$ddkeQ8RKhqKAT6XTQe2iTuW5vBjWbOUAM0l4EUKVjK.rsJCnHHqZu', 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cambio_estado_matriculas`
--

DROP TABLE IF EXISTS `cambio_estado_matriculas`;
CREATE TABLE IF NOT EXISTS `cambio_estado_matriculas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fechaCambioEstado` datetime(6) DEFAULT NULL,
  `semestre` varchar(255) DEFAULT NULL,
  `usuarioCambioEstado` varchar(255) DEFAULT NULL,
  `estado_matricula_id` int DEFAULT NULL,
  `matricula_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjjbm01neg39qni139c1axto1y` (`matricula_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cohortes`
--

DROP TABLE IF EXISTS `cohortes`;
CREATE TABLE IF NOT EXISTS `cohortes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fechaCreacion` datetime(6) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cohorte_grupos`
--

DROP TABLE IF EXISTS `cohorte_grupos`;
CREATE TABLE IF NOT EXISTS `cohorte_grupos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `cohorte_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdrd5vdak6x2ga4m5cqsamajg8` (`cohorte_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `coloquio`
--

DROP TABLE IF EXISTS `coloquio`;
CREATE TABLE IF NOT EXISTS `coloquio` (
  `id` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `hora` time(6) DEFAULT NULL,
  `lugar` varchar(255) DEFAULT NULL,
  `grupo_cohorte_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5jmvpw2wwiwge072fid1b81no` (`grupo_cohorte_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `coloquio_estudiante`
--

DROP TABLE IF EXISTS `coloquio_estudiante`;
CREATE TABLE IF NOT EXISTS `coloquio_estudiante` (
  `idColoquio` int NOT NULL,
  `idDocumento` int NOT NULL,
  `idEstudiante` int NOT NULL,
  PRIMARY KEY (`idColoquio`,`idDocumento`,`idEstudiante`),
  KEY `FK20vgd3r4y5e3th3pgkoofw62o` (`idDocumento`),
  KEY `FKebaoerr1mtt1amc4e2hf3nvyw` (`idEstudiante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contraprestaciones`
--

DROP TABLE IF EXISTS `contraprestaciones`;
CREATE TABLE IF NOT EXISTS `contraprestaciones` (
  `id` int NOT NULL AUTO_INCREMENT,
  `actividades` varchar(255) DEFAULT NULL,
  `aprobada` bit(1) DEFAULT NULL,
  `certificadoGenerado` bit(1) DEFAULT NULL,
  `fechaCertificado` datetime(6) DEFAULT NULL,
  `fechaCreacion` datetime(6) DEFAULT NULL,
  `fechaFin` datetime(6) DEFAULT NULL,
  `fechaInicio` datetime(6) DEFAULT NULL,
  `semestre` varchar(255) DEFAULT NULL,
  `certificado_id` int DEFAULT NULL,
  `estudiante_id` int DEFAULT NULL,
  `soporte_id` int DEFAULT NULL,
  `tipo_contraprestacion_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjd584w3u6crdusm74a4b8slqx` (`certificado_id`),
  KEY `FKcpysn3djl678e2u9jgpfstit4` (`estudiante_id`),
  KEY `FKq5ht4riyljuoivi35hij362n3` (`soporte_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `criterio_evaluacion`
--

DROP TABLE IF EXISTS `criterio_evaluacion`;
CREATE TABLE IF NOT EXISTS `criterio_evaluacion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) DEFAULT NULL,
  `id_sustentacion` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgl1agfk66ve69rrf2plam02o2` (`id_sustentacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `definitiva`
--

DROP TABLE IF EXISTS `definitiva`;
CREATE TABLE IF NOT EXISTS `definitiva` (
  `id` int NOT NULL AUTO_INCREMENT,
  `calificacion` double DEFAULT NULL,
  `honores` bit(1) DEFAULT NULL,
  `id_proyecto` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr18ea5chf1alcg4iod403nq4y` (`id_proyecto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `documento`
--

DROP TABLE IF EXISTS `documento`;
CREATE TABLE IF NOT EXISTS `documento` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `peso` varchar(255) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `tipoArchivo` varchar(255) DEFAULT NULL,
  `tipoDocumento` enum('ACTAAPROBACION','ACTABORRADOR','ACTAORIGINAL','ACTASOLICITUD','ACTAVB','ANTEPROYECTO','ARTICULO','COLOQUIO','EVALUACION','REQUISITOS','TESIS') DEFAULT NULL,
  `id_proyecto` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlr4ah2kub1pgpof5gr8l2pnm1` (`id_proyecto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estados_estudiantes`
--

DROP TABLE IF EXISTS `estados_estudiantes`;
CREATE TABLE IF NOT EXISTS `estados_estudiantes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `estados_estudiantes`
--

INSERT INTO `estados_estudiantes` (`id`, `nombre`) VALUES
(1, 'En curso'),
(2, 'Inactivo'),
(3, 'Egresado');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estados_matriculas`
--

DROP TABLE IF EXISTS `estados_matriculas`;
CREATE TABLE IF NOT EXISTS `estados_matriculas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `estados_matriculas`
--

INSERT INTO `estados_matriculas` (`id`, `nombre`) VALUES
(1, 'Aprobada'),
(2, 'En curso'),
(3, 'Cancelada'),
(4, 'Reprobada'),
(5, 'Anulada'),
(6, 'Correo enviado');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estudiantes`
--

DROP TABLE IF EXISTS `estudiantes`;
CREATE TABLE IF NOT EXISTS `estudiantes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apellido` varchar(255) NOT NULL,
  `apellido2` varchar(255) DEFAULT NULL,
  `cedula` varchar(255) DEFAULT NULL,
  `codigo` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `esPosgrado` bit(1) DEFAULT NULL,
  `fechaIngreso` datetime(6) DEFAULT NULL,
  `fechaNacimiento` datetime(6) DEFAULT NULL,
  `migrado` bit(1) DEFAULT NULL,
  `moodleId` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `nombre2` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `cohorte_id` int DEFAULT NULL,
  `estado_estudiante_id` int DEFAULT NULL,
  `pensum_id` int DEFAULT NULL,
  `programa_id` int DEFAULT NULL,
  `usuario_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKesxuxp7iba25rm2f8p7u97np1` (`codigo`),
  UNIQUE KEY `UKks74get5br3cnt3bugfama4b4` (`email`),
  UNIQUE KEY `UK1gjxhvkenxh2bs8lp358yipkq` (`moodleId`),
  UNIQUE KEY `UKfje3n18j10lxwrl73c0tjg6gx` (`usuario_id`),
  KEY `FKt6yb39ot7jxi9p05j7s1l34jf` (`cohorte_id`),
  KEY `FKq2l5np2wch62731b5vryjm3l1` (`pensum_id`),
  KEY `FK1tr60pb4abi55vg66fxo0oonh` (`programa_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estudiantes_divisist`
--

DROP TABLE IF EXISTS `estudiantes_divisist`;
CREATE TABLE IF NOT EXISTS `estudiantes_divisist` (
  `codigo` varchar(20) NOT NULL,
  `activo` bit(1) DEFAULT NULL,
  `desc_tipo_car` varchar(100) DEFAULT NULL,
  `documento` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `fecha_sincronizacion` datetime(6) DEFAULT NULL,
  `moodle_id` varchar(20) DEFAULT NULL,
  `nom_carrera` varchar(200) DEFAULT NULL,
  `primer_apellido` varchar(50) DEFAULT NULL,
  `primer_nombre` varchar(50) DEFAULT NULL,
  `segundo_apellido` varchar(50) DEFAULT NULL,
  `segundo_nombre` varchar(50) DEFAULT NULL,
  `t_matriculado` varchar(50) DEFAULT NULL,
  `tipo_documento` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupos`
--

DROP TABLE IF EXISTS `grupos`;
CREATE TABLE IF NOT EXISTS `grupos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `activo` bit(1) DEFAULT NULL,
  `codigo` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `materia_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKprqndi87s35v1y3vs2ig2bpf2` (`materia_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupos_cohortes`
--

DROP TABLE IF EXISTS `grupos_cohortes`;
CREATE TABLE IF NOT EXISTS `grupos_cohortes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fechaCreacion` datetime(6) DEFAULT NULL,
  `moodleId` varchar(255) DEFAULT NULL,
  `semestre` varchar(255) DEFAULT NULL,
  `semestreTerminado` bit(1) NOT NULL,
  `cohorte_grupo_id` int DEFAULT NULL,
  `cohorte_id` int DEFAULT NULL,
  `docente_id` int DEFAULT NULL,
  `grupo_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkyd9mv1414yo6ywv3t9fscjlp` (`cohorte_grupo_id`),
  KEY `FKdk9tt8m1lpdj9fnuqc9jqi2to` (`cohorte_id`),
  KEY `FKeod14s9fis884wmseahfy4ill` (`docente_id`),
  KEY `FKlxwgymn1ypo59u0ehug67612d` (`grupo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupos_divisist`
--

DROP TABLE IF EXISTS `grupos_divisist`;
CREATE TABLE IF NOT EXISTS `grupos_divisist` (
  `ciclo` varchar(10) NOT NULL,
  `cod_carrera` varchar(10) NOT NULL,
  `cod_materia` varchar(10) NOT NULL,
  `grupo` varchar(5) NOT NULL,
  `activo` bit(1) DEFAULT NULL,
  `cedido` varchar(1) DEFAULT NULL,
  `cod_profesor` varchar(20) DEFAULT NULL,
  `dirigido` varchar(1) DEFAULT NULL,
  `fecha_sincronizacion` datetime(6) DEFAULT NULL,
  `moodle_id` varchar(20) DEFAULT NULL,
  `notas_procesadas` varchar(1) DEFAULT NULL,
  `num_alum_matriculados` int DEFAULT NULL,
  `num_max_alumnos` int DEFAULT NULL,
  `seccional` varchar(10) DEFAULT NULL,
  `semestre` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`ciclo`,`cod_carrera`,`cod_materia`,`grupo`),
  KEY `FK21a0emqu8qqpoyahys56cipvp` (`cod_carrera`,`cod_materia`),
  KEY `FKaymod3uflvgb3ewnhwy2w590y` (`cod_profesor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupo_investigacion`
--

DROP TABLE IF EXISTS `grupo_investigacion`;
CREATE TABLE IF NOT EXISTS `grupo_investigacion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `id_programa` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK87vuupotqufc02ciyvotmd7fi` (`id_programa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historial_cierre_notas`
--

DROP TABLE IF EXISTS `historial_cierre_notas`;
CREATE TABLE IF NOT EXISTS `historial_cierre_notas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_cierre` datetime(6) NOT NULL,
  `grupo_cohorte_id` bigint NOT NULL,
  `matricula_id` bigint NOT NULL,
  `realizado_por` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historico_grupos`
--

DROP TABLE IF EXISTS `historico_grupos`;
CREATE TABLE IF NOT EXISTS `historico_grupos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_creacion` datetime(6) DEFAULT NULL,
  `moodle_curso_historico_id` varchar(255) DEFAULT NULL,
  `moodle_curso_original_id` varchar(255) DEFAULT NULL,
  `grupo_cohorte_id` bigint NOT NULL,
  `historico_semestre_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3bp80f1c95bry92xj0c3c4vuo` (`grupo_cohorte_id`),
  KEY `FKkabxu1933qgg6mx9jgr3ffrxg` (`historico_semestre_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `historico_semestres`
--

DROP TABLE IF EXISTS `historico_semestres`;
CREATE TABLE IF NOT EXISTS `historico_semestres` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha_fin` datetime(6) DEFAULT NULL,
  `fecha_inicio` datetime(6) DEFAULT NULL,
  `moodle_categoria_id` varchar(255) DEFAULT NULL,
  `semestre` varchar(10) NOT NULL,
  `programa_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKceowba9lu9yddvfubdn2o0kf0` (`programa_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `linea_investigacion`
--

DROP TABLE IF EXISTS `linea_investigacion`;
CREATE TABLE IF NOT EXISTS `linea_investigacion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `id_grupo_investigacion` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3yqjrx7rty5g8yvgcgl59tcvh` (`id_grupo_investigacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `materias`
--

DROP TABLE IF EXISTS `materias`;
CREATE TABLE IF NOT EXISTS `materias` (
  `id` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(255) DEFAULT NULL,
  `creditos` varchar(255) DEFAULT NULL,
  `moodleId` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `semestre` varchar(255) DEFAULT NULL,
  `pensum_id` int DEFAULT NULL,
  `semestre_pensum_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjtjj5cfbjnh7df2fcj4jvkckc` (`pensum_id`),
  KEY `FK8837s7018prw7m4xnjci1mytj` (`semestre_pensum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `materias_divisist`
--

DROP TABLE IF EXISTS `materias_divisist`;
CREATE TABLE IF NOT EXISTS `materias_divisist` (
  `cod_carrera` varchar(10) NOT NULL,
  `cod_materia` varchar(10) NOT NULL,
  `activa` varchar(1) DEFAULT NULL,
  `activo` bit(1) DEFAULT NULL,
  `cod_dpto` varchar(10) DEFAULT NULL,
  `creditos` int DEFAULT NULL,
  `fecha_sincronizacion` datetime(6) DEFAULT NULL,
  `hasa` int DEFAULT NULL,
  `hasl` int DEFAULT NULL,
  `hp` int DEFAULT NULL,
  `ht` int DEFAULT NULL,
  `hti` int DEFAULT NULL,
  `id_micro` int DEFAULT NULL,
  `modulo_acu_012` varchar(10) DEFAULT NULL,
  `moodle_id` varchar(20) DEFAULT NULL,
  `multi_p` varchar(1) DEFAULT NULL,
  `nbc` varchar(10) DEFAULT NULL,
  `nombre` varchar(200) DEFAULT NULL,
  `semestre` varchar(10) DEFAULT NULL,
  `tipo_materia` varchar(50) DEFAULT NULL,
  `unica_nota` varchar(1) DEFAULT NULL,
  `pensum_id` int DEFAULT NULL,
  `semestre_pensum_id` int DEFAULT NULL,
  PRIMARY KEY (`cod_carrera`,`cod_materia`),
  KEY `FK6hhihrr7penytsc9hsf9pnko7` (`pensum_id`),
  KEY `FK3qukrdbru9h0pdjfe8o9ob9y8` (`semestre_pensum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `materias_matriculadas_divisist`
--

DROP TABLE IF EXISTS `materias_matriculadas_divisist`;
CREATE TABLE IF NOT EXISTS `materias_matriculadas_divisist` (
  `cod_alumno` varchar(20) NOT NULL,
  `cod_car_mat` varchar(10) NOT NULL,
  `cod_carrera` varchar(10) NOT NULL,
  `cod_mat_mat` varchar(10) NOT NULL,
  `grupo` varchar(5) NOT NULL,
  `activo` bit(1) DEFAULT NULL,
  `ciclo` varchar(10) DEFAULT NULL,
  `cod_materia` varchar(10) DEFAULT NULL,
  `estado` varchar(20) DEFAULT NULL,
  `fecha_sincronizacion` datetime(6) DEFAULT NULL,
  `seccional` varchar(10) DEFAULT NULL,
  `semestre` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`cod_alumno`,`cod_car_mat`,`cod_carrera`,`cod_mat_mat`,`grupo`),
  KEY `FK2u058mrbhivl5n75hwc8vsn5k` (`ciclo`,`cod_carrera`,`cod_materia`,`grupo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `matriculas`
--

DROP TABLE IF EXISTS `matriculas`;
CREATE TABLE IF NOT EXISTS `matriculas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `correoEnviado` bit(1) NOT NULL,
  `fechaCorreoEnviado` datetime(6) DEFAULT NULL,
  `fechaMatriculacion` datetime(6) DEFAULT NULL,
  `fechaNota` datetime(6) DEFAULT NULL,
  `nota` double DEFAULT NULL,
  `notaAbierta` bit(1) DEFAULT NULL,
  `nuevaMatricula` bit(1) NOT NULL,
  `semestre` varchar(255) DEFAULT NULL,
  `estado_matricula_id` int DEFAULT NULL,
  `estudiante_id` int DEFAULT NULL,
  `grupo_cohorte_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKik1axtq6kxfa4tiba11rvwfgs` (`estudiante_id`),
  KEY `FK4o9xswge9hl9we98ehimvpehk` (`grupo_cohorte_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `meta_ods`
--

DROP TABLE IF EXISTS `meta_ods`;
CREATE TABLE IF NOT EXISTS `meta_ods` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `meta_ods`
--

INSERT INTO `meta_ods` (`id`, `nombre`) VALUES
(1, 'FIN DE LA POBREZA'),
(2, 'HAMBRE CERO'),
(3, 'SALUD Y BIENESTAR'),
(4, 'EDUCACIÓN DE CALIDAD'),
(5, 'IGUALDAD DE GÉNERO'),
(6, 'AGUA LIMPIA Y SANEAMIENTO'),
(7, 'ENERGÍA ASEQUIBLE Y NO CONTAMINANTE'),
(8, 'TRABAJO DECENTE Y CRECIMIENTO ECONÓMICO'),
(9, 'INDUSTRIA, INNIVACIÓN E INFRAESTRUCTURA'),
(10, 'REDUCCIÓN DE LAS DESIGUALDADES'),
(11, 'CIUDADES Y COMUNIDADES SOSTENIBLES'),
(12, 'PRODUCCIÓN Y CONSUMO RESPONSABLES'),
(13, 'ACCIÓN POR EL CLIMA'),
(14, 'VIDA SUBMARINA'),
(15, 'VIDA DE ECOSISTEMAS TERRESTRES'),
(16, 'PAZ, JUSTICIA E INSTITUCIONES SÓLIDAS'),
(17, 'ALIANZAS PARA LOGRAR LOS OBJETIVOS');


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notas_divisist`
--

DROP TABLE IF EXISTS `notas_divisist`;
CREATE TABLE IF NOT EXISTS `notas_divisist` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activo` bit(1) DEFAULT NULL,
  `ciclo` varchar(10) NOT NULL,
  `cod_alumno` varchar(20) NOT NULL,
  `cod_carrera` varchar(10) NOT NULL,
  `cod_materia` varchar(10) NOT NULL,
  `def` double DEFAULT NULL,
  `estado_nota` varchar(20) DEFAULT NULL,
  `ex` double DEFAULT NULL,
  `fecha_sincronizacion` datetime(6) DEFAULT NULL,
  `fecha_ultima_actualizacion` datetime(6) DEFAULT NULL,
  `grupo` varchar(5) NOT NULL,
  `hab` double DEFAULT NULL,
  `p1` double DEFAULT NULL,
  `p2` double DEFAULT NULL,
  `p3` double DEFAULT NULL,
  `semestre` varchar(10) NOT NULL,
  `cod_car_mat` varchar(10) DEFAULT NULL,
  `cod_mat_mat` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfwupjaqlc1hdk44gbtp30drmf` (`cod_alumno`,`cod_carrera`,`cod_materia`,`grupo`,`ciclo`,`semestre`),
  KEY `FKcexa2iali4t0jvahn7net6h7k` (`ciclo`,`cod_carrera`,`cod_materia`,`grupo`),
  KEY `FKrip0jllp1gk1k3pf6mgtrxjb0` (`cod_alumno`,`cod_car_mat`,`cod_carrera`,`cod_mat_mat`,`grupo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notas_posgrado`
--

DROP TABLE IF EXISTS `notas_posgrado`;
CREATE TABLE IF NOT EXISTS `notas_posgrado` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fechaNota` datetime(6) DEFAULT NULL,
  `nota` double DEFAULT NULL,
  `realizadoPor` varchar(255) DEFAULT NULL,
  `matricula_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4tnth8sca6hi6hhwbjfxh20y` (`matricula_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notas_pregrado`
--

DROP TABLE IF EXISTS `notas_pregrado`;
CREATE TABLE IF NOT EXISTS `notas_pregrado` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `es_modificable` bit(1) DEFAULT NULL,
  `estudiante_codigo` varchar(255) DEFAULT NULL,
  `examen_final` double DEFAULT NULL,
  `fecha_modificacion` datetime(6) DEFAULT NULL,
  `fecha_registro` datetime(6) DEFAULT NULL,
  `habilitacion` double DEFAULT NULL,
  `modificado_por` varchar(255) DEFAULT NULL,
  `moodle_course_id` varchar(255) DEFAULT NULL,
  `moodle_last_sync` datetime(6) DEFAULT NULL,
  `moodle_student_id` varchar(255) DEFAULT NULL,
  `moodle_sync_status` bit(1) DEFAULT NULL,
  `nota_definitiva` double DEFAULT NULL,
  `observaciones` varchar(255) DEFAULT NULL,
  `oracle_ciclo` varchar(255) DEFAULT NULL,
  `oracle_cod_alumno` varchar(255) DEFAULT NULL,
  `oracle_cod_carrera` varchar(255) DEFAULT NULL,
  `oracle_cod_materia` varchar(255) DEFAULT NULL,
  `oracle_grupo` varchar(255) DEFAULT NULL,
  `primer_previo` double DEFAULT NULL,
  `realizado_por` varchar(255) DEFAULT NULL,
  `segundo_previo` double DEFAULT NULL,
  `tercera_nota` double DEFAULT NULL,
  `grupo_cohorte_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb1d9g06065q9md9nk6xa7japb` (`grupo_cohorte_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `objetivo_especifico`
--

DROP TABLE IF EXISTS `objetivo_especifico`;
CREATE TABLE IF NOT EXISTS `objetivo_especifico` (
  `id` int NOT NULL AUTO_INCREMENT,
  `avanceReal` int DEFAULT NULL,
  `avanceReportado` int DEFAULT NULL,
  `descripcion` varchar(3000) DEFAULT NULL,
  `codirector` bit(1) DEFAULT NULL,
  `director` bit(1) DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `numeroOrden` int DEFAULT NULL,
  `id_proyecto` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK136u33psin025awffoifv9suv` (`id_proyecto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pensums`
--

DROP TABLE IF EXISTS `pensums`;
CREATE TABLE IF NOT EXISTS `pensums` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cantidadSemestres` int DEFAULT NULL,
  `moodleId` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `programa_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf9vyd99sqj31jd25du30vnxpk` (`programa_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `profesores_divisist`
--

DROP TABLE IF EXISTS `profesores_divisist`;
CREATE TABLE IF NOT EXISTS `profesores_divisist` (
  `cod_profesor` varchar(20) NOT NULL,
  `activo` bit(1) DEFAULT NULL,
  `apellido1` varchar(50) DEFAULT NULL,
  `apellido2` varchar(50) DEFAULT NULL,
  `documento` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `fecha_sincronizacion` datetime(6) DEFAULT NULL,
  `moodle_id` varchar(20) DEFAULT NULL,
  `nombre1` varchar(50) DEFAULT NULL,
  `nombre2` varchar(50) DEFAULT NULL,
  `tipo_documento` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`cod_profesor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `programas`
--

DROP TABLE IF EXISTS `programas`;
CREATE TABLE IF NOT EXISTS `programas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(255) NOT NULL,
  `es_posgrado` bit(1) NOT NULL,
  `historicoMoodleId` varchar(255) DEFAULT NULL,
  `moodleId` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `semestre_actual` varchar(255) DEFAULT NULL,
  `tipo_programa_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlksox5h0m37o0u2uchhwq607v` (`codigo`),
  UNIQUE KEY `UKbqnxq1gual5r1q1nv04nttlk8` (`moodleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proyecto`
--

DROP TABLE IF EXISTS `proyecto`;
CREATE TABLE IF NOT EXISTS `proyecto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `createdAt` date DEFAULT NULL,
  `estadoActual` enum('FASE_0','FASE_1','FASE_2','FASE_3','FASE_4','FASE_5','FASE_6','FASE_7','FASE_8','FASE_9') DEFAULT NULL,
  `estadoRevision` enum('SIN_REVISAR','EN_REVISION','ACEPTADA','RECHAZADA') DEFAULT NULL,
  `comentarioRevision` varchar(3000) DEFAULT NULL,
  `objetivoGeneral` varchar(3000) DEFAULT NULL,
  `pregunta` varchar(3000) DEFAULT NULL,
  `problema` varchar(3000) DEFAULT NULL,
  `recomendacionDirectores` varchar(255) DEFAULT NULL,
  `titulo` varchar(3000) DEFAULT NULL,
  `updatedAt` date DEFAULT NULL,
  `id_linea_investigacion` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKj1fcqjinrq9qnwq3gkf1y3pp5` (`id_linea_investigacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proyecto_meta_ods`
--

DROP TABLE IF EXISTS `proyecto_meta_ods`;
CREATE TABLE IF NOT EXISTS `proyecto_meta_ods` (
  `proyecto_id` int NOT NULL,
  `meta_ods_id` int NOT NULL,
  KEY `FKnvu5m3m9ndnasqkei9qn8c7u1` (`meta_ods_id`),
  KEY `FKlxhrk627m5rgv07mh4c2obdp2` (`proyecto_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `retroalimentacion`
--

DROP TABLE IF EXISTS `retroalimentacion`;
CREATE TABLE IF NOT EXISTS `retroalimentacion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(3000) DEFAULT NULL,
  `idDocumento` int DEFAULT NULL,
  `idUsuario` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1d27onnyva9tnqoubdk2temym` (`idDocumento`),
  KEY `FK2nk6b3iwgsbmvso8f7s9qd84v` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `nombre`) VALUES
(1, 'Estudiante'),
(2, 'Docente'),
(3, 'Director'),
(4, 'Jurado'),
(5, 'Codirector');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `semestres`
--

DROP TABLE IF EXISTS `semestres`;
CREATE TABLE IF NOT EXISTS `semestres` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `numero` int NOT NULL,
  `numeroRomano` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `semestres`
--

INSERT INTO `semestres` (`id`, `nombre`, `numero`, `numeroRomano`) VALUES
(1, 'Semestre I', 1, 'I'),
(2, 'Semestre II', 2, 'II'),
(3, 'Semestre III', 3, 'III'),
(4, 'Semestre IV', 4, 'IV'),
(5, 'Semestre V', 5, 'V'),
(6, 'Semestre VI', 6, 'VI'),
(7, 'Semestre VII', 7, 'VII'),
(8, 'Semestre VIII', 8, 'VIII'),
(9, 'Semestre IX', 9, 'IX'),
(10, 'Semestre X', 10, 'X');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `semestres_pensums`
--

DROP TABLE IF EXISTS `semestres_pensums`;
CREATE TABLE IF NOT EXISTS `semestres_pensums` (
  `id` int NOT NULL AUTO_INCREMENT,
  `moodleId` varchar(255) DEFAULT NULL,
  `pensum_id` int DEFAULT NULL,
  `programa_id` int DEFAULT NULL,
  `semestre_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcr3s0sfdkvqfnar642t5xghkw` (`pensum_id`),
  KEY `FK4ut2fukpq9kjtfwcmlclogh1x` (`programa_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `semestres_programas`
--

DROP TABLE IF EXISTS `semestres_programas`;
CREATE TABLE IF NOT EXISTS `semestres_programas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `moodleId` varchar(255) DEFAULT NULL,
  `programa_id` int DEFAULT NULL,
  `semestre_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKajj1j5egkmhxcixuysljotiwc` (`programa_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sesiones_activas`
--

DROP TABLE IF EXISTS `sesiones_activas`;
CREATE TABLE IF NOT EXISTS `sesiones_activas` (
  `correoUsuario` varchar(100) NOT NULL,
  `fecha_expiracion` datetime(6) DEFAULT NULL,
  `token` text,
  `ultima_actividad` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`correoUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `solicitudes`
--

DROP TABLE IF EXISTS `solicitudes`;
CREATE TABLE IF NOT EXISTS `solicitudes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) DEFAULT NULL,
  `estaAprobada` bit(1) DEFAULT NULL,
  `fechaAprobacion` datetime(6) DEFAULT NULL,
  `fechaCreacion` datetime(6) DEFAULT NULL,
  `estudiante_id` int DEFAULT NULL,
  `matricula_id` bigint DEFAULT NULL,
  `solicitud_aplazamiento_id` bigint DEFAULT NULL,
  `soporte_id` int DEFAULT NULL,
  `tipo_solicitud_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlcv62dpe1h73vooboux7ysjf4` (`estudiante_id`),
  KEY `FKmdikte5qjm9srim72xuqd1swi` (`matricula_id`),
  KEY `FKqwcrwtpqebbct53rfl7jueagj` (`solicitud_aplazamiento_id`),
  KEY `FKaogdt2sios04cxdunxpx8hxo9` (`soporte_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `soportes`
--

DROP TABLE IF EXISTS `soportes`;
CREATE TABLE IF NOT EXISTS `soportes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `extension` varchar(255) DEFAULT NULL,
  `fecha_subida` datetime(6) DEFAULT NULL,
  `mime_type` varchar(255) DEFAULT NULL,
  `nombre_archivo` varchar(255) DEFAULT NULL,
  `peso` varchar(255) DEFAULT NULL,
  `ruta` varchar(255) DEFAULT NULL,
  `tamano_bytes` bigint DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `url_s3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sustentacion`
--

DROP TABLE IF EXISTS `sustentacion`;
CREATE TABLE IF NOT EXISTS `sustentacion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `asistenciaConfirmada` bit(1) DEFAULT NULL,
  `descripcion` varchar(3000) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `hora` time(6) DEFAULT NULL,
  `horaFin` time(6) DEFAULT NULL,
  `lugar` varchar(3000) DEFAULT NULL,
  `sustentacionExterna` bit(1) DEFAULT NULL,
  `sustentacionRealizada` bit(1) DEFAULT NULL,
  `tipoSustentacion` enum('ANTEPROYECTO','IDEA','TESIS') DEFAULT NULL,
  `id_proyecto` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK774t49mvv48vuw6qorgx8vt11` (`id_proyecto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sustentacion_documento`
--

DROP TABLE IF EXISTS `sustentacion_documento`;
CREATE TABLE IF NOT EXISTS `sustentacion_documento` (
  `idDocumento` int NOT NULL,
  `idSustentacion` int NOT NULL,
  PRIMARY KEY (`idDocumento`,`idSustentacion`),
  KEY `FKrjjyaquhadiapn53a7x6c8kc6` (`idSustentacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sustentacion_evaluador`
--

DROP TABLE IF EXISTS `sustentacion_evaluador`;
CREATE TABLE IF NOT EXISTS `sustentacion_evaluador` (
  `idSustentacion` int NOT NULL,
  `idUsuario` int NOT NULL,
  `juradoExterno` bit(1) NOT NULL,
  `nota` double DEFAULT NULL,
  `observaciones` varchar(3000) DEFAULT NULL,
  PRIMARY KEY (`idSustentacion`,`idUsuario`),
  KEY `FKi1a30rnmpwd84qn3822ltymjc` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipos_contraprestaciones`
--

DROP TABLE IF EXISTS `tipos_contraprestaciones`;
CREATE TABLE IF NOT EXISTS `tipos_contraprestaciones` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `porcentaje` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `tipos_contraprestaciones`
--

INSERT INTO `tipos_contraprestaciones` (`id`, `nombre`, `porcentaje`) VALUES
(1, 'Tesis Laureada', '50%'),
(2, 'Tesis Meritoria', '30%'),
(3, 'Medalla en Plata', '30%'),
(4, 'Matricula de Honor', '50%');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipos_programas`
--

DROP TABLE IF EXISTS `tipos_programas`;
CREATE TABLE IF NOT EXISTS `tipos_programas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `moodle_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `tipos_programas`
--

INSERT INTO `tipos_programas` (`id`, `nombre`, `moodle_id`) VALUES
(1, 'Tecnologia', '307'),
(2, 'Maestria', '306');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipos_solicitudes`
--

DROP TABLE IF EXISTS `tipos_solicitudes`;
CREATE TABLE IF NOT EXISTS `tipos_solicitudes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `tipos_solicitudes`
--

INSERT INTO `tipos_solicitudes` (`id`, `nombre`) VALUES
(1, 'Cancelacion de materias'),
(2, 'Aplazamiento de semestre'),
(3, 'Reintegro');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cedula` varchar(12) DEFAULT NULL,
  `codigo` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `foto_url` varchar(255) DEFAULT NULL,
  `google_id` varchar(255) DEFAULT NULL,
  `moodleId` varchar(255) DEFAULT NULL,
  `nombreCompleto` varchar(255) NOT NULL,
  `primerApellido` varchar(255) DEFAULT NULL,
  `primerNombre` varchar(255) DEFAULT NULL,
  `segundoApellido` varchar(255) DEFAULT NULL,
  `segundoNombre` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `rol_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkfsp0s1tflm1cwlj8idhqsad0` (`email`),
  UNIQUE KEY `UKefovjjo5q5jlsa0f9eoptdjly` (`cedula`),
  UNIQUE KEY `UKosc6lskg34pth4l3fpf86rnry` (`codigo`),
  UNIQUE KEY `UK69mcqeeg7pulu0ouige5ytybm` (`google_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario_proyecto`
--

DROP TABLE IF EXISTS `usuario_proyecto`;
CREATE TABLE IF NOT EXISTS `usuario_proyecto` (
  `idProyecto` int NOT NULL,
  `idUsuario` int NOT NULL,
  `rol_id` int DEFAULT NULL,
  PRIMARY KEY (`idProyecto`,`idUsuario`),
  KEY `FK1rbioys7c0khefrkieycds1ud` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cambio_estado_matriculas`
--
ALTER TABLE `cambio_estado_matriculas`
  ADD CONSTRAINT `FKjjbm01neg39qni139c1axto1y` FOREIGN KEY (`matricula_id`) REFERENCES `matriculas` (`id`);

--
-- Filtros para la tabla `cohorte_grupos`
--
ALTER TABLE `cohorte_grupos`
  ADD CONSTRAINT `FKdrd5vdak6x2ga4m5cqsamajg8` FOREIGN KEY (`cohorte_id`) REFERENCES `cohortes` (`id`);

--
-- Filtros para la tabla `coloquio`
--
ALTER TABLE `coloquio`
  ADD CONSTRAINT `FK5jmvpw2wwiwge072fid1b81no` FOREIGN KEY (`grupo_cohorte_id`) REFERENCES `grupos_cohortes` (`id`);

--
-- Filtros para la tabla `coloquio_estudiante`
--
ALTER TABLE `coloquio_estudiante`
  ADD CONSTRAINT `FK20vgd3r4y5e3th3pgkoofw62o` FOREIGN KEY (`idDocumento`) REFERENCES `documento` (`id`),
  ADD CONSTRAINT `FK3m4fvr7baskpdhm1rr9u0gpid` FOREIGN KEY (`idColoquio`) REFERENCES `coloquio` (`id`),
  ADD CONSTRAINT `FKebaoerr1mtt1amc4e2hf3nvyw` FOREIGN KEY (`idEstudiante`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `contraprestaciones`
--
ALTER TABLE `contraprestaciones`
  ADD CONSTRAINT `FKcpysn3djl678e2u9jgpfstit4` FOREIGN KEY (`estudiante_id`) REFERENCES `estudiantes` (`id`),
  ADD CONSTRAINT `FKjd584w3u6crdusm74a4b8slqx` FOREIGN KEY (`certificado_id`) REFERENCES `soportes` (`id`),
  ADD CONSTRAINT `FKq5ht4riyljuoivi35hij362n3` FOREIGN KEY (`soporte_id`) REFERENCES `soportes` (`id`);

--
-- Filtros para la tabla `criterio_evaluacion`
--
ALTER TABLE `criterio_evaluacion`
  ADD CONSTRAINT `FKgl1agfk66ve69rrf2plam02o2` FOREIGN KEY (`id_sustentacion`) REFERENCES `sustentacion` (`id`);

--
-- Filtros para la tabla `definitiva`
--
ALTER TABLE `definitiva`
  ADD CONSTRAINT `FKf4kkqnny9n6tspqjcs5x394ys` FOREIGN KEY (`id_proyecto`) REFERENCES `proyecto` (`id`);

--
-- Filtros para la tabla `documento`
--
ALTER TABLE `documento`
  ADD CONSTRAINT `FKlr4ah2kub1pgpof5gr8l2pnm1` FOREIGN KEY (`id_proyecto`) REFERENCES `proyecto` (`id`);

--
-- Filtros para la tabla `estudiantes`
--
ALTER TABLE `estudiantes`
  ADD CONSTRAINT `FK1tr60pb4abi55vg66fxo0oonh` FOREIGN KEY (`programa_id`) REFERENCES `programas` (`id`),
  ADD CONSTRAINT `FKiqye1ytd68ta1pibrpbtav3bc` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FKq2l5np2wch62731b5vryjm3l1` FOREIGN KEY (`pensum_id`) REFERENCES `pensums` (`id`),
  ADD CONSTRAINT `FKt6yb39ot7jxi9p05j7s1l34jf` FOREIGN KEY (`cohorte_id`) REFERENCES `cohorte_grupos` (`id`);

--
-- Filtros para la tabla `grupos`
--
ALTER TABLE `grupos`
  ADD CONSTRAINT `FKprqndi87s35v1y3vs2ig2bpf2` FOREIGN KEY (`materia_id`) REFERENCES `materias` (`id`);

--
-- Filtros para la tabla `grupos_cohortes`
--
ALTER TABLE `grupos_cohortes`
  ADD CONSTRAINT `FKdk9tt8m1lpdj9fnuqc9jqi2to` FOREIGN KEY (`cohorte_id`) REFERENCES `cohortes` (`id`),
  ADD CONSTRAINT `FKeod14s9fis884wmseahfy4ill` FOREIGN KEY (`docente_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FKkyd9mv1414yo6ywv3t9fscjlp` FOREIGN KEY (`cohorte_grupo_id`) REFERENCES `cohorte_grupos` (`id`),
  ADD CONSTRAINT `FKlxwgymn1ypo59u0ehug67612d` FOREIGN KEY (`grupo_id`) REFERENCES `grupos` (`id`);

--
-- Filtros para la tabla `grupos_divisist`
--
ALTER TABLE `grupos_divisist`
  ADD CONSTRAINT `FK21a0emqu8qqpoyahys56cipvp` FOREIGN KEY (`cod_carrera`,`cod_materia`) REFERENCES `materias_divisist` (`cod_carrera`, `cod_materia`),
  ADD CONSTRAINT `FKaymod3uflvgb3ewnhwy2w590y` FOREIGN KEY (`cod_profesor`) REFERENCES `profesores_divisist` (`cod_profesor`);

--
-- Filtros para la tabla `grupo_investigacion`
--
ALTER TABLE `grupo_investigacion`
  ADD CONSTRAINT `FK87vuupotqufc02ciyvotmd7fi` FOREIGN KEY (`id_programa`) REFERENCES `programas` (`id`);

--
-- Filtros para la tabla `historico_grupos`
--
ALTER TABLE `historico_grupos`
  ADD CONSTRAINT `FK3bp80f1c95bry92xj0c3c4vuo` FOREIGN KEY (`grupo_cohorte_id`) REFERENCES `grupos_cohortes` (`id`),
  ADD CONSTRAINT `FKkabxu1933qgg6mx9jgr3ffrxg` FOREIGN KEY (`historico_semestre_id`) REFERENCES `historico_semestres` (`id`);

--
-- Filtros para la tabla `historico_semestres`
--
ALTER TABLE `historico_semestres`
  ADD CONSTRAINT `FKceowba9lu9yddvfubdn2o0kf0` FOREIGN KEY (`programa_id`) REFERENCES `programas` (`id`);

--
-- Filtros para la tabla `linea_investigacion`
--
ALTER TABLE `linea_investigacion`
  ADD CONSTRAINT `FK3yqjrx7rty5g8yvgcgl59tcvh` FOREIGN KEY (`id_grupo_investigacion`) REFERENCES `grupo_investigacion` (`id`);

--
-- Filtros para la tabla `materias`
--
ALTER TABLE `materias`
  ADD CONSTRAINT `FK8837s7018prw7m4xnjci1mytj` FOREIGN KEY (`semestre_pensum_id`) REFERENCES `semestres_pensums` (`id`),
  ADD CONSTRAINT `FKjtjj5cfbjnh7df2fcj4jvkckc` FOREIGN KEY (`pensum_id`) REFERENCES `pensums` (`id`);

--
-- Filtros para la tabla `materias_divisist`
--
ALTER TABLE `materias_divisist`
  ADD CONSTRAINT `FK3qukrdbru9h0pdjfe8o9ob9y8` FOREIGN KEY (`semestre_pensum_id`) REFERENCES `semestres_pensums` (`id`),
  ADD CONSTRAINT `FK6hhihrr7penytsc9hsf9pnko7` FOREIGN KEY (`pensum_id`) REFERENCES `pensums` (`id`);

--
-- Filtros para la tabla `materias_matriculadas_divisist`
--
ALTER TABLE `materias_matriculadas_divisist`
  ADD CONSTRAINT `FK2u058mrbhivl5n75hwc8vsn5k` FOREIGN KEY (`ciclo`,`cod_carrera`,`cod_materia`,`grupo`) REFERENCES `grupos_divisist` (`ciclo`, `cod_carrera`, `cod_materia`, `grupo`),
  ADD CONSTRAINT `FKemyes239kdtaj84hxq1rhvfjx` FOREIGN KEY (`cod_alumno`) REFERENCES `estudiantes_divisist` (`codigo`);

--
-- Filtros para la tabla `matriculas`
--
ALTER TABLE `matriculas`
  ADD CONSTRAINT `FK4o9xswge9hl9we98ehimvpehk` FOREIGN KEY (`grupo_cohorte_id`) REFERENCES `grupos_cohortes` (`id`),
  ADD CONSTRAINT `FKik1axtq6kxfa4tiba11rvwfgs` FOREIGN KEY (`estudiante_id`) REFERENCES `estudiantes` (`id`);

--
-- Filtros para la tabla `notas_divisist`
--
ALTER TABLE `notas_divisist`
  ADD CONSTRAINT `FKadl7gfnfpafwd3jfs1dxon832` FOREIGN KEY (`cod_alumno`) REFERENCES `estudiantes_divisist` (`codigo`),
  ADD CONSTRAINT `FKcexa2iali4t0jvahn7net6h7k` FOREIGN KEY (`ciclo`,`cod_carrera`,`cod_materia`,`grupo`) REFERENCES `grupos_divisist` (`ciclo`, `cod_carrera`, `cod_materia`, `grupo`),
  ADD CONSTRAINT `FKrip0jllp1gk1k3pf6mgtrxjb0` FOREIGN KEY (`cod_alumno`,`cod_car_mat`,`cod_carrera`,`cod_mat_mat`,`grupo`) REFERENCES `materias_matriculadas_divisist` (`cod_alumno`, `cod_car_mat`, `cod_carrera`, `cod_mat_mat`, `grupo`);

--
-- Filtros para la tabla `notas_posgrado`
--
ALTER TABLE `notas_posgrado`
  ADD CONSTRAINT `FK4tnth8sca6hi6hhwbjfxh20y` FOREIGN KEY (`matricula_id`) REFERENCES `matriculas` (`id`);

--
-- Filtros para la tabla `notas_pregrado`
--
ALTER TABLE `notas_pregrado`
  ADD CONSTRAINT `FKb1d9g06065q9md9nk6xa7japb` FOREIGN KEY (`grupo_cohorte_id`) REFERENCES `grupos_cohortes` (`id`);

--
-- Filtros para la tabla `objetivo_especifico`
--
ALTER TABLE `objetivo_especifico`
  ADD CONSTRAINT `FK136u33psin025awffoifv9suv` FOREIGN KEY (`id_proyecto`) REFERENCES `proyecto` (`id`);

--
-- Filtros para la tabla `pensums`
--
ALTER TABLE `pensums`
  ADD CONSTRAINT `FKf9vyd99sqj31jd25du30vnxpk` FOREIGN KEY (`programa_id`) REFERENCES `programas` (`id`);

--
-- Filtros para la tabla `proyecto`
--
ALTER TABLE `proyecto`
  ADD CONSTRAINT `FKj1fcqjinrq9qnwq3gkf1y3pp5` FOREIGN KEY (`id_linea_investigacion`) REFERENCES `linea_investigacion` (`id`);

--
-- Filtros para la tabla `proyecto_meta_ods`
--
ALTER TABLE `proyecto_meta_ods`
  ADD CONSTRAINT `FKlxhrk627m5rgv07mh4c2obdp2` FOREIGN KEY (`proyecto_id`) REFERENCES `proyecto` (`id`),
  ADD CONSTRAINT `FKnvu5m3m9ndnasqkei9qn8c7u1` FOREIGN KEY (`meta_ods_id`) REFERENCES `meta_ods` (`id`);

--
-- Filtros para la tabla `retroalimentacion`
--
ALTER TABLE `retroalimentacion`
  ADD CONSTRAINT `FK1d27onnyva9tnqoubdk2temym` FOREIGN KEY (`idDocumento`) REFERENCES `documento` (`id`),
  ADD CONSTRAINT `FK2nk6b3iwgsbmvso8f7s9qd84v` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `semestres_pensums`
--
ALTER TABLE `semestres_pensums`
  ADD CONSTRAINT `FK4ut2fukpq9kjtfwcmlclogh1x` FOREIGN KEY (`programa_id`) REFERENCES `programas` (`id`),
  ADD CONSTRAINT `FKcr3s0sfdkvqfnar642t5xghkw` FOREIGN KEY (`pensum_id`) REFERENCES `pensums` (`id`);

--
-- Filtros para la tabla `semestres_programas`
--
ALTER TABLE `semestres_programas`
  ADD CONSTRAINT `FKajj1j5egkmhxcixuysljotiwc` FOREIGN KEY (`programa_id`) REFERENCES `programas` (`id`);

--
-- Filtros para la tabla `solicitudes`
--
ALTER TABLE `solicitudes`
  ADD CONSTRAINT `FKaogdt2sios04cxdunxpx8hxo9` FOREIGN KEY (`soporte_id`) REFERENCES `soportes` (`id`),
  ADD CONSTRAINT `FKlcv62dpe1h73vooboux7ysjf4` FOREIGN KEY (`estudiante_id`) REFERENCES `estudiantes` (`id`),
  ADD CONSTRAINT `FKmdikte5qjm9srim72xuqd1swi` FOREIGN KEY (`matricula_id`) REFERENCES `matriculas` (`id`),
  ADD CONSTRAINT `FKqwcrwtpqebbct53rfl7jueagj` FOREIGN KEY (`solicitud_aplazamiento_id`) REFERENCES `solicitudes` (`id`);

--
-- Filtros para la tabla `sustentacion`
--
ALTER TABLE `sustentacion`
  ADD CONSTRAINT `FK774t49mvv48vuw6qorgx8vt11` FOREIGN KEY (`id_proyecto`) REFERENCES `proyecto` (`id`);

--
-- Filtros para la tabla `sustentacion_documento`
--
ALTER TABLE `sustentacion_documento`
  ADD CONSTRAINT `FKqisuas7c1s5xg9tpgleur0a95` FOREIGN KEY (`idDocumento`) REFERENCES `documento` (`id`),
  ADD CONSTRAINT `FKrjjyaquhadiapn53a7x6c8kc6` FOREIGN KEY (`idSustentacion`) REFERENCES `sustentacion` (`id`);

--
-- Filtros para la tabla `sustentacion_evaluador`
--
ALTER TABLE `sustentacion_evaluador`
  ADD CONSTRAINT `FKakv9ltp37q6mbjpje0fbpf85d` FOREIGN KEY (`idSustentacion`) REFERENCES `sustentacion` (`id`),
  ADD CONSTRAINT `FKi1a30rnmpwd84qn3822ltymjc` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `usuario_proyecto`
--
ALTER TABLE `usuario_proyecto`
  ADD CONSTRAINT `FK1rbioys7c0khefrkieycds1ud` FOREIGN KEY (`idUsuario`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FK3t84f3vbm9olxt8pkaowaivax` FOREIGN KEY (`idProyecto`) REFERENCES `proyecto` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
