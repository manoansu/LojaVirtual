CREATE DATABASE  IF NOT EXISTS `android_webapp` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `android_webapp`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: android_webapp
-- ------------------------------------------------------
-- Server version	5.7.11-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_pagamento`
--

DROP TABLE IF EXISTS `tb_pagamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_pagamento` (
  `id_pagamento` int(11) NOT NULL AUTO_INCREMENT,
  `pagtoPayPalId` text NOT NULL,
  `valor` decimal(6,2) NOT NULL,
  `moeda` varchar(3) DEFAULT NULL,
  `idUsuario` int(11) NOT NULL,
  `estado` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id_pagamento`),
  KEY `fk_tb_pagamento_tb_usuario1_idx` (`idUsuario`),
  CONSTRAINT `fk_tb_pagamento_tb_usuario1` FOREIGN KEY (`idUsuario`) REFERENCES `tb_usuario` (`ID_USUARIO`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_pagamento`
--

LOCK TABLES `tb_pagamento` WRITE;
/*!40000 ALTER TABLE `tb_pagamento` DISABLE KEYS */;
INSERT INTO `tb_pagamento` VALUES (1,'PAY-1D5795313A945994NK6BFZGY',126.99,'BRL',1,'completed');
/*!40000 ALTER TABLE `tb_pagamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_produto`
--

DROP TABLE IF EXISTS `tb_produto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_produto` (
  `id_produto` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) NOT NULL,
  `descricao` varchar(450) DEFAULT NULL,
  `valor` float DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  `sku` text,
  PRIMARY KEY (`id_produto`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_produto`
--

LOCK TABLES `tb_produto` WRITE;
/*!40000 ALTER TABLE `tb_produto` DISABLE KEYS */;
INSERT INTO `tb_produto` VALUES (4,'Caderno de Desenho','Caderno artesanal, com folhas especiais para Desenho',56.78,'img/produtos/caderno-de-desenho-quadrinhos.jpg',NULL),(5,'Coleção lápis de cor','Coleção lápis de cor 36 cores',126.99,'img/produtos/colecao_lapis_cor.jpg',NULL),(6,'Mochila M Infantil ','Mochila M Infantil Mickey 15Y 63404',260.99,'img/produtos/mochila-infantil-m-mickey.jpg',NULL),(7,'Caderno de Desenho','Caderno artesanal, com folhas especiais para Desenho',56.78,'img/produtos/caderno-de-desenho-quadrinhos.jpg',NULL),(8,'Coleção lápis de cor','Coleção lápis de cor 36 cores',126.99,'img/produtos/colecao_lapis_cor.jpg',NULL),(9,'Mochila M Infantil ','Mochila M Infantil Mickey 15Y 63404',260.99,'img/produtos/mochila-infantil-m-mickey.jpg',NULL);
/*!40000 ALTER TABLE `tb_produto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_usuario`
--

DROP TABLE IF EXISTS `tb_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_usuario` (
  `ID_USUARIO` int(11) NOT NULL AUTO_INCREMENT,
  `USUARIO` varchar(45) NOT NULL,
  `SENHA` varchar(45) NOT NULL,
  `TOKEN_GCM` varchar(650) DEFAULT NULL,
  PRIMARY KEY (`ID_USUARIO`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_usuario`
--

LOCK TABLES `tb_usuario` WRITE;
/*!40000 ALTER TABLE `tb_usuario` DISABLE KEYS */;
INSERT INTO `tb_usuario` VALUES (1,'admin','21232f297a57a5a743894a0e4a801fc3','cWncx_lTp8g:APA91bEGC4hrTW2o1IrCNnjwIrwJ5_ymr4X1SnzMXF3SI9jCUlH7pi09Hl5fXyNbNz86GxUucv69wXf_3GiMgm8yCdCqrC2QWpo63jTDHPj27R3BX76wRCiFj16l91gTpce9BwScdV3d'),(2,'diogo','202cb962ac59075b964b07152d234b70','dZemte6CQlM:APA91bHpM-Ap74TJKQP3GCeyKwKWzwrTmtPlDYQSbHuPP4I8R9ObEtkNjkjr1umWIiVxUfw8DOFdJM64NX-u-Me7uwCXEqgbDJ7C8gf_xcIlFG-QbF8gZyUw9LVTu5ht3g9-h3XvJq88');
/*!40000 ALTER TABLE `tb_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_venda`
--

DROP TABLE IF EXISTS `tb_venda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_venda` (
  `id_venda` int(11) NOT NULL AUTO_INCREMENT,
  `preco` decimal(6,2) NOT NULL,
  `estado` varchar(45) NOT NULL,
  `qtde` int(4) DEFAULT NULL,
  `produtoId` int(11) NOT NULL,
  `pagamentoId` int(11) NOT NULL,
  PRIMARY KEY (`id_venda`),
  KEY `fk_tb_venda_tb_produto_idx` (`produtoId`),
  KEY `fk_tb_venda_tb_pagamento1_idx` (`pagamentoId`),
  CONSTRAINT `fk_tb_venda_tb_pagamento1` FOREIGN KEY (`pagamentoId`) REFERENCES `tb_pagamento` (`id_pagamento`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_venda_tb_produto` FOREIGN KEY (`produtoId`) REFERENCES `tb_produto` (`id_produto`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_venda`
--

LOCK TABLES `tb_venda` WRITE;
/*!40000 ALTER TABLE `tb_venda` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_venda` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-07-10 14:54:29
