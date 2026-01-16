/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: biblioteca_db
-- ------------------------------------------------------
-- Server version	12.1.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `autores`
--

DROP TABLE IF EXISTS `autores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `autores` (
  `id_autor` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(80) NOT NULL,
  `apellidos` varchar(120) NOT NULL,
  `seudonimo` varchar(120) DEFAULT NULL,
  `nacionalidad` varchar(80) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `biografia` text DEFAULT NULL,
  PRIMARY KEY (`id_autor`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autores`
--

LOCK TABLES `autores` WRITE;
/*!40000 ALTER TABLE `autores` DISABLE KEYS */;
INSERT INTO `autores` VALUES
(1,'Isaac','Asimov',NULL,'Rusa','1920-01-02',1,NULL),
(2,'George','Orwell',NULL,'Británica','1903-06-25',1,NULL),
(3,'J.R.R.','Tolkien',NULL,'Británica','1892-01-03',1,NULL),
(4,'Aldous','Huxley',NULL,'Británica','1894-07-26',1,NULL),
(5,'Ray','Bradbury',NULL,'Estadounidense','1920-08-22',1,NULL),
(6,'Margaret','Atwood',NULL,'Canadiense','1939-11-18',1,NULL),
(7,'Umberto','Eco',NULL,'Italiana','1932-01-05',1,NULL),
(8,'Albert','Camus',NULL,'Francesa','1913-11-07',1,NULL),
(9,'Hannah','Arendt',NULL,'Alemana','1906-10-14',1,NULL),
(10,'Michel','Foucault',NULL,'Francesa','1926-10-15',1,NULL),
(11,'Zygmunt','Bauman',NULL,'Polaca','1925-11-19',1,NULL),
(12,'Yuval Noah','Harari',NULL,'Israelí','1976-02-24',1,NULL);
/*!40000 ALTER TABLE `autores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `id_categoria` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(120) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `activa` tinyint(1) NOT NULL DEFAULT 1,
  `prioridad` int(11) NOT NULL DEFAULT 0,
  `fecha_creacion` date NOT NULL DEFAULT curdate(),
  `color` varchar(20) DEFAULT NULL,
  `codigo` varchar(20) DEFAULT NULL,
  `id_categoria_padre` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id_categoria`),
  UNIQUE KEY `uq_categorias_nombre` (`nombre`),
  KEY `fk_categorias_padre` (`id_categoria_padre`),
  CONSTRAINT `fk_categorias_padre` FOREIGN KEY (`id_categoria_padre`) REFERENCES `categorias` (`id_categoria`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES
(1,'Narrativa','Libros de ficción narrativa',1,1,'2026-01-08',NULL,NULL,NULL),
(2,'Ciencia ficción','Subgénero de narrativa',1,2,'2026-01-08',NULL,NULL,1),
(3,'Ensayo','Libros de reflexión y análisis',1,1,'2026-01-08',NULL,NULL,NULL),
(4,'Novela','Novela contemporánea y clásica',1,1,'2026-01-08',NULL,NULL,NULL),
(5,'Fantasía','Literatura fantástica',1,2,'2026-01-08',NULL,NULL,1),
(6,'Distopía','Narrativa distópica',1,2,'2026-01-08',NULL,NULL,2),
(7,'Historia','Ensayo histórico',1,1,'2026-01-08',NULL,NULL,NULL),
(8,'Biografía','Relatos biográficos',1,1,'2026-01-08',NULL,NULL,NULL),
(9,'Filosofía','Ensayos filosóficos',1,1,'2026-01-08',NULL,NULL,NULL),
(10,'Ciencia','Divulgación científica',1,1,'2026-01-08',NULL,NULL,NULL),
(11,'Política','Análisis político',1,1,'2026-01-08',NULL,NULL,NULL),
(12,'Psicología','Divulgación psicológica',1,1,'2026-01-08',NULL,NULL,NULL),
(13,'Sociología','Estudios sociales',1,1,'2026-01-08',NULL,NULL,NULL);
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `editoriales`
--

DROP TABLE IF EXISTS `editoriales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `editoriales` (
  `id_editorial` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) NOT NULL,
  `activa` tinyint(1) NOT NULL DEFAULT 1,
  `ciudad` varchar(100) NOT NULL,
  `pais` varchar(100) NOT NULL,
  `email_contacto` varchar(150) NOT NULL,
  `fecha_fundacion` date DEFAULT NULL,
  PRIMARY KEY (`id_editorial`),
  UNIQUE KEY `uq_editoriales_nombre` (`nombre`),
  UNIQUE KEY `uq_editoriales_email` (`email_contacto`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `editoriales`
--

LOCK TABLES `editoriales` WRITE;
/*!40000 ALTER TABLE `editoriales` DISABLE KEYS */;
INSERT INTO `editoriales` VALUES
(1,'Planeta',1,'Barcelona','España','contacto@planeta.es','1949-01-01'),
(2,'Anagrama',1,'Barcelona','España','info@anagrama-ed.es','1969-01-01'),
(3,'Alianza',1,'Madrid','España','contacto@alianza.es','1966-01-01'),
(4,'Debolsillo',1,'Barcelona','España','info@debolsillo.es','1957-01-01'),
(5,'Minotauro',1,'Barcelona','España','contacto@minotauro.es','1955-01-01'),
(6,'Siruela',1,'Madrid','España','info@siruela.com','1982-01-01'),
(7,'Destino',1,'Barcelona','España','contacto@destino.es','1942-01-01'),
(8,'Tusquets',1,'Barcelona','España','info@tusquets.es','1969-01-01'),
(9,'Salamandra',1,'Barcelona','España','contacto@salamandra.info','1989-01-01'),
(10,'RBA',1,'Barcelona','España','info@rba.es','1981-01-01'),
(11,'Akal',1,'Madrid','España','contacto@akal.com','1972-01-01'),
(12,'Cátedra',1,'Madrid','España','info@catedra.com','1973-01-01');
/*!40000 ALTER TABLE `editoriales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `libro_autor`
--

DROP TABLE IF EXISTS `libro_autor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `libro_autor` (
  `id_libro` int(10) unsigned NOT NULL,
  `id_autor` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id_libro`,`id_autor`),
  KEY `fk_libroautor_autores` (`id_autor`),
  CONSTRAINT `fk_libroautor_autores` FOREIGN KEY (`id_autor`) REFERENCES `autores` (`id_autor`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_libroautor_libros` FOREIGN KEY (`id_libro`) REFERENCES `libros` (`id_libro`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libro_autor`
--

LOCK TABLES `libro_autor` WRITE;
/*!40000 ALTER TABLE `libro_autor` DISABLE KEYS */;
INSERT INTO `libro_autor` VALUES
(2,1),
(1,2),
(5,3),
(3,4),
(4,5),
(6,6),
(10,6),
(11,7),
(7,8),
(12,10),
(8,12),
(9,12);
/*!40000 ALTER TABLE `libro_autor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `libros`
--

DROP TABLE IF EXISTS `libros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `libros` (
  `id_libro` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `titulo` varchar(200) NOT NULL,
  `sinopsis` text DEFAULT NULL,
  `fecha_publicacion` date DEFAULT NULL,
  `idioma` enum('ES','EN','FR','DE','IT','PT','OTRO') NOT NULL DEFAULT 'ES',
  `numero_paginas` int(11) NOT NULL DEFAULT 0,
  `isbn` varchar(20) NOT NULL,
  `disponible` tinyint(1) NOT NULL DEFAULT 1,
  `id_editorial` int(10) unsigned NOT NULL,
  `id_categoria` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id_libro`),
  UNIQUE KEY `uq_libros_isbn` (`isbn`),
  KEY `fk_libros_editoriales` (`id_editorial`),
  KEY `fk_libros_categorias` (`id_categoria`),
  CONSTRAINT `fk_libros_categorias` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`) ON UPDATE CASCADE,
  CONSTRAINT `fk_libros_editoriales` FOREIGN KEY (`id_editorial`) REFERENCES `editoriales` (`id_editorial`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libros`
--

LOCK TABLES `libros` WRITE;
/*!40000 ALTER TABLE `libros` DISABLE KEYS */;
INSERT INTO `libros` VALUES
(1,'1984',NULL,'1949-06-08','EN',328,'9780451524935',1,2,1),
(2,'Fundación',NULL,'1951-01-01','ES',255,'9788499890944',1,1,2),
(3,'Un mundo feliz',NULL,'1932-01-01','ES',288,'9788499890951',1,4,6),
(4,'Fahrenheit 451',NULL,'1953-01-01','ES',249,'9788499890952',1,3,6),
(5,'El señor de los anillos',NULL,'1954-01-01','ES',1200,'9788499890953',1,6,5),
(6,'Ensayo sobre la ceguera',NULL,'1995-01-01','ES',352,'9788499890954',1,6,1),
(7,'La peste',NULL,'1947-01-01','ES',308,'9788499890955',1,7,1),
(8,'Sapiens',NULL,'2011-01-01','ES',496,'9788499890956',1,10,7),
(9,'Homo Deus',NULL,'2015-01-01','ES',528,'9788499890957',1,10,7),
(10,'1985',NULL,'1985-01-01','ES',400,'9788499890958',1,2,3),
(11,'El nombre de la rosa',NULL,'1980-01-01','ES',512,'9788499890959',1,6,1),
(12,'Vigilar y castigar',NULL,'1975-01-01','ES',384,'9788499890960',1,9,8),
(13,'Aitana',NULL,'2025-12-29','ES',123,'ABIW688',1,3,1);
/*!40000 ALTER TABLE `libros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prestamos`
--

DROP TABLE IF EXISTS `prestamos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `prestamos` (
  `id_prestamo` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `estado` enum('ABIERTO','DEVUELTO','ATRASADO') NOT NULL DEFAULT 'ABIERTO',
  `fecha_prestamo` date NOT NULL DEFAULT curdate(),
  `fecha_prevista_devolucion` date NOT NULL,
  `fecha_devolucion` date DEFAULT NULL,
  `penalizacion_euros` decimal(8,2) NOT NULL DEFAULT 0.00,
  `observaciones` text DEFAULT NULL,
  `origen` varchar(50) NOT NULL DEFAULT 'MOSTRADOR',
  `id_socio` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id_prestamo`),
  KEY `fk_prestamos_socios` (`id_socio`),
  CONSTRAINT `fk_prestamos_socios` FOREIGN KEY (`id_socio`) REFERENCES `socios` (`id_socio`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prestamos`
--

LOCK TABLES `prestamos` WRITE;
/*!40000 ALTER TABLE `prestamos` DISABLE KEYS */;
INSERT INTO `prestamos` VALUES
(1,'ABIERTO','2026-01-08','2026-02-01',NULL,0.00,NULL,'MOSTRADOR',1),
(2,'ABIERTO','2026-01-08','2026-02-05',NULL,0.00,NULL,'MOSTRADOR',2),
(3,'ABIERTO','2026-01-08','2026-02-10',NULL,0.00,NULL,'MOSTRADOR',3),
(4,'ABIERTO','2026-01-08','2026-02-12',NULL,0.00,NULL,'MOSTRADOR',4),
(5,'ABIERTO','2026-01-08','2026-02-15',NULL,0.00,NULL,'MOSTRADOR',5),
(6,'ABIERTO','2026-01-08','2026-02-18',NULL,0.00,NULL,'MOSTRADOR',6),
(7,'ABIERTO','2026-01-08','2026-02-20',NULL,0.00,NULL,'MOSTRADOR',7),
(8,'ABIERTO','2026-01-08','2026-02-22',NULL,0.00,NULL,'MOSTRADOR',8),
(9,'ABIERTO','2026-01-08','2026-02-25',NULL,0.00,NULL,'MOSTRADOR',9),
(10,'ABIERTO','2026-01-08','2026-02-28',NULL,0.00,NULL,'MOSTRADOR',10),
(11,'ABIERTO','2026-01-08','2026-03-02',NULL,0.00,NULL,'MOSTRADOR',11),
(12,'ABIERTO','2026-01-08','2026-03-05',NULL,0.00,NULL,'MOSTRADOR',12),
(13,'ABIERTO','2026-01-13','2026-01-28',NULL,0.00,'El libro está en mal estado','MOSTRADOR',2),
(16,'ABIERTO','2024-03-06','2024-03-31','2024-03-15',0.00,'','MOSTRADOR',12);
/*!40000 ALTER TABLE `prestamos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prestamos_libro`
--

DROP TABLE IF EXISTS `prestamos_libro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `prestamos_libro` (
  `id_prestamo` int(10) unsigned NOT NULL,
  `id_libro` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id_prestamo`,`id_libro`),
  KEY `fk_prestamoslibro_libros` (`id_libro`),
  CONSTRAINT `fk_prestamoslibro_libros` FOREIGN KEY (`id_libro`) REFERENCES `libros` (`id_libro`) ON UPDATE CASCADE,
  CONSTRAINT `fk_prestamoslibro_prestamos` FOREIGN KEY (`id_prestamo`) REFERENCES `prestamos` (`id_prestamo`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prestamos_libro`
--

LOCK TABLES `prestamos_libro` WRITE;
/*!40000 ALTER TABLE `prestamos_libro` DISABLE KEYS */;
INSERT INTO `prestamos_libro` VALUES
(1,1),
(2,2),
(3,3),
(4,4),
(13,4),
(5,5),
(6,6),
(7,7),
(8,8),
(9,9),
(10,10),
(11,11),
(16,11),
(12,12);
/*!40000 ALTER TABLE `prestamos_libro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `socios`
--

DROP TABLE IF EXISTS `socios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `socios` (
  `id_socio` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(80) NOT NULL,
  `apellidos` varchar(120) NOT NULL,
  `email` varchar(150) NOT NULL,
  `dni` varchar(12) NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `telefono` bigint(20) unsigned DEFAULT NULL,
  `fecha_alta` date NOT NULL DEFAULT curdate(),
  `direccion` varchar(200) NOT NULL DEFAULT 'SIN ESPECIFICAR',
  `codigo_postal` varchar(10) NOT NULL DEFAULT '00000',
  PRIMARY KEY (`id_socio`),
  UNIQUE KEY `uq_socios_email` (`email`),
  UNIQUE KEY `uq_socios_dni` (`dni`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `socios`
--

LOCK TABLES `socios` WRITE;
/*!40000 ALTER TABLE `socios` DISABLE KEYS */;
INSERT INTO `socios` VALUES
(1,'Juan','Pérez Gómez','juan.perez@email.com','12345678A',1,600123123,'2026-01-08','SIN ESPECIFICAR','00000'),
(2,'María','López Ruiz','maria.lopez@email.com','87654321B',1,600321321,'2026-01-08','SIN ESPECIFICAR','00000'),
(3,'Carlos','Martín López','carlos.martin@email.com','11111111C',1,600111111,'2026-01-08','SIN ESPECIFICAR','00000'),
(4,'Laura','Sánchez Pérez','laura.sanchez@email.com','22222222D',1,600222222,'2026-01-08','SIN ESPECIFICAR','00000'),
(5,'Pedro','Gómez Ruiz','pedro.gomez@email.com','33333333E',1,600333333,'2026-01-08','SIN ESPECIFICAR','00000'),
(6,'Ana','Torres Gil','ana.torres@email.com','44444444F',1,600444444,'2026-01-08','SIN ESPECIFICAR','00000'),
(7,'Luis','Navarro Díaz','luis.navarro@email.com','55555555G',1,600555555,'2026-01-08','SIN ESPECIFICAR','00000'),
(8,'Marta','Hernández Soto','marta.hernandez@email.com','66666666J',1,600666666,'2026-01-08','SIN ESPECIFICAR','00000'),
(9,'Jorge','Iglesias Mora','jorge.iglesias@email.com','77777777J',1,600777777,'2026-01-08','SIN ESPECIFICAR','00000'),
(10,'Elena','Vega Castro','elena.vega@email.com','88888888K',1,600888888,'2026-01-08','SIN ESPECIFICAR','00000'),
(11,'Raúl','Romero Peña','raul.romero@email.com','99999999L',1,600999999,'2026-01-08','SIN ESPECIFICAR','00000'),
(12,'Lucía','Campos León','lucia.campos@email.com','10101010M',1,601010101,'2026-01-08','SIN ESPECIFICAR','00000');
/*!40000 ALTER TABLE `socios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `vw_libros_detalle`
--

DROP TABLE IF EXISTS `vw_libros_detalle`;
/*!50001 DROP VIEW IF EXISTS `vw_libros_detalle`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `vw_libros_detalle` AS SELECT
 1 AS `id_libro`,
  1 AS `titulo`,
  1 AS `isbn`,
  1 AS `idioma`,
  1 AS `fecha_publicacion`,
  1 AS `numero_paginas`,
  1 AS `disponible`,
  1 AS `editorial`,
  1 AS `categoria` */;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'biblioteca_db'
--
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP FUNCTION IF EXISTS `fn_autores_de_libro` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `fn_autores_de_libro`(p_id_libro INT) RETURNS varchar(1000) CHARSET utf8mb4 COLLATE utf8mb4_spanish_ci
BEGIN
  DECLARE v_existe INT DEFAULT 0;
  DECLARE v_autores VARCHAR(1000);

  -- Comprobación previa: ¿existe el libro?
  SELECT COUNT(*)
  INTO v_existe
  FROM libros
  WHERE id_libro = p_id_libro;
  
  IF v_existe = 0 THEN
    RETURN NULL;
  END IF;
  
  -- Consulta principal: autores del libro
  SELECT GROUP_CONCAT(CONCAT(a.nombre, ' ', a.apellidos) SEPARATOR ', ')
  INTO v_autores 
  FROM libro_autor la 
  JOIN autores a ON a.id_autor = la.id_autor 
  WHERE la.id_libro = p_id_libro;
  
  RETURN v_autores;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP FUNCTION IF EXISTS `fn_libros_prestados_socio` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `fn_libros_prestados_socio`(p_id_socio INT) RETURNS int(11)
BEGIN
  DECLARE v_existe INT DEFAULT 0;
  DECLARE v_total  INT DEFAULT 0;

  -- Comprobación previa: ¿existe el socio?
  SELECT COUNT(*)
  INTO v_existe
  FROM socios
  WHERE id_socio = p_id_socio;

  IF v_existe = 0 THEN
    RETURN -1;
  END IF;

  -- Consulta: préstamos abiertos del socio + libros asociados
  SELECT COUNT(*)
  INTO v_total
  FROM prestamos p
  JOIN prestamos_libro pl ON pl.id_prestamo = p.id_prestamo
  WHERE p.id_socio = p_id_socio
  AND p.fecha_devolucion IS NULL;

  RETURN v_total;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `vw_libros_detalle`
--

/*!50001 DROP VIEW IF EXISTS `vw_libros_detalle`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_uca1400_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_libros_detalle` AS select `l`.`id_libro` AS `id_libro`,`l`.`titulo` AS `titulo`,`l`.`isbn` AS `isbn`,`l`.`idioma` AS `idioma`,`l`.`fecha_publicacion` AS `fecha_publicacion`,`l`.`numero_paginas` AS `numero_paginas`,`l`.`disponible` AS `disponible`,`e`.`nombre` AS `editorial`,`c`.`nombre` AS `categoria` from ((`libros` `l` join `editoriales` `e` on(`e`.`id_editorial` = `l`.`id_editorial`)) join `categorias` `c` on(`c`.`id_categoria` = `l`.`id_categoria`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2026-01-15 13:47:47
