-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: cps_test
-- ------------------------------------------------------
-- Server version	5.7.20-log

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
-- Table structure for table `car_transportation`
--

DROP TABLE IF EXISTS `car_transportation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `car_transportation` (
  `customer_id` int(10) NOT NULL,
  `car_id` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  `auth_type` int(10) NOT NULL,
  `auth_id` int(10) NOT NULL,
  `lot_id` int(10) NOT NULL,
  `inserted_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `removed_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`customer_id`,`car_id`,`inserted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_transportation`
--

LOCK TABLES `car_transportation` WRITE;
/*!40000 ALTER TABLE `car_transportation` DISABLE KEYS */;
/*!40000 ALTER TABLE `car_transportation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `complaint`
--

DROP TABLE IF EXISTS `complaint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `complaint` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `customer_id` int(10) NOT NULL,
  `employee_id` int(10) DEFAULT NULL,
  `status` int(10) NOT NULL DEFAULT '1',
  `description` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `resolved_at` timestamp NULL DEFAULT NULL,
  `refund_amount` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `complaint`
--

LOCK TABLES `complaint` WRITE;
/*!40000 ALTER TABLE `complaint` DISABLE KEYS */;
/*!40000 ALTER TABLE `complaint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `debit` float NOT NULL DEFAULT '0',
  `credit` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `daily_statistics`
--

DROP TABLE IF EXISTS `daily_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `daily_statistics` (
  `day` date NOT NULL,
  `lot_id` int(10) NOT NULL,
  `realized_orders` int(10) DEFAULT '0',
  `canceled_orders` int(10) DEFAULT '0',
  `late_arrivals` int(10) DEFAULT '0',
  `inactive_slots` int(10) DEFAULT '0',
  PRIMARY KEY (`day`,`lot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daily_statistics`
--

LOCK TABLES `daily_statistics` WRITE;
/*!40000 ALTER TABLE `daily_statistics` DISABLE KEYS */;
/*!40000 ALTER TABLE `daily_statistics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `id` int(10) NOT NULL,
  `username` varchar(45) CHARACTER SET latin1 NOT NULL,
  `password` varchar(45) CHARACTER SET latin1 NOT NULL,
  `dept_type` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `lot_id` int(10) DEFAULT NULL,
  `permissions` varchar(30) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `onetime_service`
--

DROP TABLE IF EXISTS `onetime_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `onetime_service` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `parking_type` int(10) NOT NULL,
  `customer_id` int(10) NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `car_id` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lot_id` int(10) NOT NULL,
  `planned_start_time` datetime NOT NULL,
  `planned_end_time` datetime NOT NULL,
  `canceled` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `onetime_service`
--

LOCK TABLES `onetime_service` WRITE;
/*!40000 ALTER TABLE `onetime_service` DISABLE KEYS */;
INSERT INTO `onetime_service` VALUES (1,2,0,'fnjufju@gmail.com','TB-173806',1,'2018-01-14 15:32:31','2018-01-15 15:27:31','\0'),(2,2,0,'ggsurnu@gmail.com','BI-334664',1,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(3,2,0,'uujsnuu@gmail.com','TI-383195',1,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(4,2,0,'gunsjun@gmail.com','TB-438658',1,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(5,2,0,'gguulas@gmail.com','TT-608591',1,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(6,2,0,'anufgff@gmail.com','AA-936426',1,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(7,2,0,'jfjjjfs@gmail.com','TT-117204',1,'2018-01-14 15:32:31','2018-01-15 15:27:31','\0'),(8,2,0,'sunugfs@gmail.com','II-988872',1,'2018-01-14 15:32:31','2018-01-15 21:27:31','\0'),(9,2,0,'jnjfnjf@gmail.com','BL-560362',1,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(10,2,0,'jsjjraj@gmail.com','AT-750428',1,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(11,2,0,'jfuunfr@gmail.com','AA-657537',1,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(12,2,0,'lfjsrlf@gmail.com','AT-726309',1,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(13,2,0,'uljjjuu@gmail.com','BI-815622',1,'2018-01-14 15:32:31','2018-01-15 21:27:31','\0'),(14,2,0,'frjfjfj@gmail.com','IB-344379',1,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(15,2,0,'unusnfj@gmail.com','LT-911188',1,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(16,2,0,'jllflnj@gmail.com','TL-280281',1,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(17,2,0,'fjgulfn@gmail.com','LL-898065',1,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(18,2,0,'uuujugs@gmail.com','IB-641438',1,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(19,2,0,'jluauau@gmail.com','TL-402841',1,'2018-01-14 15:32:31','2018-01-14 22:27:31','\0'),(20,2,0,'jffurfr@gmail.com','LL-692389',1,'2018-01-14 15:32:31','2018-01-14 22:27:31','\0'),(21,2,0,'lglfluf@gmail.com','TB-888895',1,'2018-01-14 15:32:31','2018-01-15 15:27:31','\0'),(22,2,0,'rjjnjgf@gmail.com','BA-183049',1,'2018-01-14 15:32:31','2018-01-15 21:27:31','\0'),(23,2,0,'jurasjf@gmail.com','TL-628149',1,'2018-01-14 15:32:31','2018-01-15 21:27:31','\0'),(24,2,0,'rnfjjjj@gmail.com','BL-672323',1,'2018-01-14 15:32:31','2018-01-14 22:27:31','\0'),(25,2,0,'nnnrlnn@gmail.com','TT-121714',1,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(26,2,0,'jjulnlj@gmail.com','BT-389374',1,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(27,2,0,'ggjlujr@gmail.com','IA-641347',1,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(28,2,0,'ujruruu@gmail.com','IL-738957',1,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(29,2,0,'rfgajfj@gmail.com','IL-913541',1,'2018-01-14 15:32:31','2018-01-14 22:27:31','\0'),(30,2,0,'ujgjfnf@gmail.com','IT-671514',1,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(31,2,0,'frjfjfj@gmail.com','AB-070961',1,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(32,2,0,'nfsjfra@gmail.com','BL-274224',1,'2018-01-14 15:32:31','2018-01-15 15:27:31','\0'),(33,2,0,'afufslf@gmail.com','TL-835379',1,'2018-01-14 15:32:31','2018-01-15 15:27:31','\0'),(34,2,0,'jljsjng@gmail.com','TL-206691',1,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(35,2,0,'njafuaa@gmail.com','LB-345623',1,'2018-01-14 15:32:31','2018-01-15 15:27:31','\0'),(36,2,0,'jnauluu@gmail.com','LT-036689',1,'2018-01-14 15:32:31','2018-01-14 22:27:31','\0'),(37,2,0,'rjffsua@gmail.com','II-372171',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(38,2,0,'rffjajs@gmail.com','IB-072845',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(39,2,0,'gugfjff@gmail.com','AA-550254',2,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(40,2,0,'sjuljjf@gmail.com','LB-663960',2,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(41,2,0,'gfuffjj@gmail.com','AB-116709',2,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(42,2,0,'anjjssu@gmail.com','IT-592020',2,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(43,2,0,'jlnjfaj@gmail.com','IB-602559',2,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(44,2,0,'uufujsj@gmail.com','IT-336326',2,'2018-01-14 15:32:31','2018-01-15 15:27:31','\0'),(45,2,0,'fsjafrj@gmail.com','LI-732543',2,'2018-01-14 15:32:31','2018-01-15 09:27:31','\0'),(46,2,0,'saurfuj@gmail.com','LL-116150',2,'2018-01-14 15:32:31','2018-01-15 15:27:31','\0'),(47,2,0,'ujgsujf@gmail.com','TI-268976',2,'2018-01-14 15:32:31','2018-01-14 22:27:31','\0'),(48,2,0,'sjjjjfg@gmail.com','LB-950969',2,'2018-01-14 15:32:31','2018-01-15 21:27:31','\0'),(49,2,0,'fjajrnj@gmail.com','IA-221815',2,'2018-01-14 15:32:31','2018-01-14 22:27:31','\0'),(50,2,0,'julgjuu@gmail.com','II-597400',2,'2018-01-14 15:32:31','2018-01-14 22:27:31','\0'),(51,2,0,'nranfrj@gmail.com','AB-208795',2,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(52,2,0,'jsljjlf@gmail.com','IA-691941',2,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(53,2,0,'lgnaaff@gmail.com','LI-429077',2,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(54,2,0,'rfffggu@gmail.com','AI-786489',2,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(55,2,0,'jlsjljf@gmail.com','BL-901103',2,'2018-01-14 15:32:31','2018-01-15 09:27:31','\0'),(56,2,0,'jjggjgj@gmail.com','BL-487887',2,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(57,2,0,'fagajuj@gmail.com','BL-597357',2,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(58,2,0,'ujjajjg@gmail.com','AL-278997',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(59,2,0,'fjuugrn@gmail.com','LB-496871',2,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(60,2,0,'rfsfgjj@gmail.com','TI-533268',2,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(61,2,0,'lfaujjf@gmail.com','BI-669995',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(62,2,0,'ujugrrj@gmail.com','IL-825488',2,'2018-01-14 15:32:31','2018-01-15 09:27:31','\0'),(63,2,0,'rggfunu@gmail.com','BI-722728',2,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(64,2,0,'sfnsufu@gmail.com','TB-134260',2,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(65,2,0,'nlfugnj@gmail.com','LB-871358',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(66,2,0,'ujuauuf@gmail.com','IT-400330',2,'2018-01-14 15:32:31','2018-01-15 15:27:31','\0'),(67,2,0,'ajffsua@gmail.com','LB-069348',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(68,2,0,'jrrfgrl@gmail.com','BL-486725',2,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(69,2,0,'gnugrug@gmail.com','TL-324718',2,'2018-01-14 15:32:31','2018-01-15 09:27:31','\0'),(70,2,0,'grgjruj@gmail.com','LI-114794',2,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(71,2,0,'launllu@gmail.com','LB-114869',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(72,2,0,'jlujfur@gmail.com','LI-967307',2,'2018-01-14 15:32:31','2018-01-14 22:27:31','\0'),(73,2,0,'ngnjjgr@gmail.com','LI-277351',2,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(74,2,0,'jfufssf@gmail.com','AL-957378',2,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(75,2,0,'ujujunj@gmail.com','TT-541789',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(76,2,0,'jgfujsn@gmail.com','IT-232433',2,'2018-01-14 15:32:31','2018-01-15 15:27:31','\0'),(77,2,0,'fsjjfrs@gmail.com','LI-594707',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(78,2,0,'ufujjja@gmail.com','BT-709657',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(79,2,0,'suffujs@gmail.com','IT-922137',2,'2018-01-14 15:32:31','2018-01-14 22:27:31','\0'),(80,2,0,'ffnnujs@gmail.com','AA-215094',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(81,2,0,'uufjjgu@gmail.com','BI-753186',2,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(82,2,0,'sagslla@gmail.com','TT-201961',2,'2018-01-14 15:32:31','2018-01-14 22:27:31','\0'),(83,2,0,'rufjnuu@gmail.com','TI-217962',2,'2018-01-14 15:32:31','2018-01-14 22:27:31','\0'),(84,2,0,'urjjujg@gmail.com','TA-857346',2,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(85,2,0,'uaujrgu@gmail.com','II-613684',2,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(86,2,0,'ujrrgun@gmail.com','BA-609756',2,'2018-01-14 15:32:31','2018-01-14 17:27:31','\0'),(87,2,0,'rurjrun@gmail.com','AB-829147',2,'2018-01-14 15:32:31','2018-01-14 22:27:31','\0'),(88,2,0,'uffujnu@gmail.com','LA-375147',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(89,2,0,'jjugfju@gmail.com','LL-890982',2,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(90,2,0,'snruffu@gmail.com','TB-020857',2,'2018-01-14 15:32:31','2018-01-15 21:27:31','\0'),(91,2,0,'rujsrjn@gmail.com','LB-107799',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(92,2,0,'urffajs@gmail.com','IL-308743',2,'2018-01-14 15:32:31','2018-01-15 21:27:31','\0'),(93,2,0,'flsfurl@gmail.com','AA-657860',2,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(94,2,0,'usnsgnl@gmail.com','IL-700827',2,'2018-01-14 15:32:31','2018-01-15 04:27:31','\0'),(95,2,0,'ljrffrr@gmail.com','TA-523464',2,'2018-01-14 15:32:31','2018-01-15 09:27:31','\0'),(96,2,0,'gujjfuf@gmail.com','TT-148177',2,'2018-01-14 15:32:31','2018-01-15 09:27:31','\0'),(97,2,0,'fjuujrl@gmail.com','TB-184846',2,'2018-01-14 15:32:31','2018-01-15 15:27:31','\0'),(98,2,0,'ugjgusl@gmail.com','TB-674049',2,'2018-01-14 15:32:31','2018-01-15 09:27:31','\0'),(99,2,0,'jasgnsj@gmail.com','TB-926346',2,'2018-01-14 15:32:31','2018-01-15 15:27:31','\0'),(100,2,0,'sufnujj@gmail.com','AT-646629',2,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(101,2,0,'furrsua@gmail.com','BI-990081',2,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(102,2,0,'jnuajll@gmail.com','II-197631',2,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0'),(103,2,0,'jrjujgr@gmail.com','TI-358595',2,'2018-01-14 15:32:31','2018-01-15 21:27:31','\0'),(104,2,0,'jfljsng@gmail.com','AB-327929',2,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(105,2,0,'lngnglj@gmail.com','LL-375847',2,'2018-01-14 15:32:31','2018-01-15 17:27:31','\0'),(106,2,0,'ujjfnja@gmail.com','IL-618783',2,'2018-01-14 15:32:31','2018-01-15 15:27:31','\0'),(107,2,0,'agljnfn@gmail.com','IB-692806',2,'2018-01-14 15:32:31','2018-01-16 07:27:31','\0');
/*!40000 ALTER TABLE `onetime_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parking_cell`
--

DROP TABLE IF EXISTS `parking_cell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parking_cell` (
  `lot_id` int(10) NOT NULL,
  `i` int(10) NOT NULL,
  `j` int(10) NOT NULL,
  `k` int(10) NOT NULL,
  `car_id` varchar(16) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `planned_end_time` datetime DEFAULT NULL,
  `reserved` bit(1) DEFAULT b'0',
  `disabled` bit(1) DEFAULT b'0',
  PRIMARY KEY (`lot_id`,`i`,`j`,`k`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking_cell`
--

LOCK TABLES `parking_cell` WRITE;
/*!40000 ALTER TABLE `parking_cell` DISABLE KEYS */;
INSERT INTO `parking_cell` VALUES (1,0,0,0,'LT-036689','2018-01-14 22:27:31','\0','\0'),(1,0,0,1,'LL-692389','2018-01-14 22:27:31','\0','\0'),(1,0,0,2,'TL-280281','2018-01-14 17:27:31','\0','\0'),(1,0,1,0,'BL-274224','2018-01-15 15:27:31','\0','\0'),(1,0,1,1,'TL-402841','2018-01-14 22:27:31','\0','\0'),(1,0,1,2,'LT-911188','2018-01-14 17:27:31','\0','\0'),(1,0,2,0,'IL-738957','2018-01-15 17:27:31','\0','\0'),(1,0,2,1,'IB-641438','2018-01-14 17:27:31','\0','\0'),(1,0,2,2,'TI-383195','2018-01-14 17:27:31','\0','\0'),(1,1,0,0,'LB-345623','2018-01-15 15:27:31','\0','\0'),(1,1,0,1,'TB-888895','2018-01-15 15:27:31','\0','\0'),(1,1,0,2,'TT-117204','2018-01-15 15:27:31','\0','\0'),(1,1,1,0,'AB-070961','2018-01-16 07:27:31','\0','\0'),(1,1,1,1,'LL-898065','2018-01-15 04:27:31','\0','\0'),(1,1,1,2,'TB-438658','2018-01-15 04:27:31','\0','\0'),(1,1,2,0,'IA-641347','2018-01-16 07:27:31','\0','\0'),(1,1,2,1,'TB-173806','2018-01-15 15:27:31','\0','\0'),(1,1,2,2,'BL-672323','2018-01-14 22:27:31','\0','\0'),(1,2,0,0,'TL-206691','2018-01-16 07:27:31','\0','\0'),(1,2,0,1,'BA-183049','2018-01-15 21:27:31','\0','\0'),(1,2,0,2,'AA-657537','2018-01-15 17:27:31','\0','\0'),(1,2,1,0,'IT-671514','2018-01-16 07:27:31','\0','\0'),(1,2,1,1,'IB-344379','2018-01-15 17:27:31','\0','\0'),(1,2,1,2,'II-988872','2018-01-15 21:27:31','\0','\0'),(1,2,2,0,'BT-389374','2018-01-15 04:27:31','\0','\0'),(1,2,2,1,'BI-815622','2018-01-15 21:27:31','\0','\0'),(1,2,2,2,'BI-334664','2018-01-15 17:27:31','\0','\0'),(1,3,0,0,'TL-835379','2018-01-15 15:27:31','\0','\0'),(1,3,0,1,'BL-560362','2018-01-15 04:27:31','\0','\0'),(1,3,0,2,'AT-750428','2018-01-16 07:27:31','\0','\0'),(1,3,1,0,'IL-913541','2018-01-14 22:27:31','\0','\0'),(1,3,1,1,'TL-628149','2018-01-15 21:27:31','\0','\0'),(1,3,1,2,'AA-936426','2018-01-16 07:27:31','\0','\0'),(1,3,2,0,'TT-121714','2018-01-15 17:27:31','\0','\0'),(1,3,2,1,'AT-726309','2018-01-16 07:27:31','\0','\0'),(1,3,2,2,'TT-608591','2018-01-16 07:27:31','\0','\0'),(2,0,0,0,NULL,NULL,'\0','\0'),(2,0,0,1,'BL-597357','2018-01-14 17:27:31','\0','\0'),(2,0,0,2,'IA-691941','2018-01-14 17:27:31','\0','\0'),(2,0,1,0,'AT-646629','2018-01-15 17:27:31','\0','\0'),(2,0,1,1,'BL-487887','2018-01-14 17:27:31','\0','\0'),(2,0,1,2,'IB-602559','2018-01-14 17:27:31','\0','\0'),(2,0,2,0,'IL-308743','2018-01-15 21:27:31','\0','\0'),(2,0,2,1,'LI-429077','2018-01-14 17:27:31','\0','\0'),(2,0,2,2,'IT-592020','2018-01-14 17:27:31','\0','\0'),(2,1,0,0,'IB-692806','2018-01-16 07:27:31','\0','\0'),(2,1,0,1,'LI-967307','2018-01-14 22:27:31','\0','\0'),(2,1,0,2,'II-597400','2018-01-14 22:27:31','\0','\0'),(2,1,1,0,'TB-926346','2018-01-15 15:27:31','\0','\0'),(2,1,1,1,'BI-722728','2018-01-14 17:27:31','\0','\0'),(2,1,1,2,'IA-221815','2018-01-14 22:27:31','\0','\0'),(2,1,2,0,'LB-107799','2018-01-15 04:27:31','\0','\0'),(2,1,2,1,'TI-268976','2018-01-14 22:27:31','\0','\0'),(2,1,2,2,'TA-857346','2018-01-14 17:27:31','\0','\0'),(2,2,0,0,'IL-618783','2018-01-15 15:27:31','\0','\0'),(2,2,0,1,'LI-732543','2018-01-15 09:27:31','\0','\0'),(2,2,0,2,'TI-217962','2018-01-14 22:27:31','\0','\0'),(2,2,1,0,'TB-674049','2018-01-15 09:27:31','\0','\0'),(2,2,1,1,'IB-072845','2018-01-15 04:27:31','\0','\0'),(2,2,1,2,'TT-201961','2018-01-14 22:27:31','\0','\0'),(2,2,2,0,'TB-020857','2018-01-15 21:27:31','\0','\0'),(2,2,2,1,'II-372171','2018-01-15 04:27:31','\0','\0'),(2,2,2,2,'IT-922137','2018-01-14 22:27:31','\0','\0'),(2,3,0,0,'LL-375847','2018-01-15 17:27:31','\0','\0'),(2,3,0,1,'LB-069348','2018-01-15 04:27:31','\0','\0'),(2,3,0,2,'IL-825488','2018-01-15 09:27:31','\0','\0'),(2,3,1,0,'TB-184846','2018-01-15 15:27:31','\0','\0'),(2,3,1,1,'LI-594707','2018-01-15 04:27:31','\0','\0'),(2,3,1,2,'LB-114869','2018-01-15 04:27:31','\0','\0'),(2,3,2,0,'LL-890982','2018-01-15 17:27:31','\0','\0'),(2,3,2,1,'BI-669995','2018-01-15 04:27:31','\0','\0'),(2,3,2,2,'LB-496871','2018-01-14 17:27:31','\0','\0'),(2,4,0,0,'AB-327929','2018-01-15 17:27:31','\0','\0'),(2,4,0,1,'IT-232433','2018-01-15 15:27:31','\0','\0'),(2,4,0,2,'BL-901103','2018-01-15 09:27:31','\0','\0'),(2,4,1,0,'TT-148177','2018-01-15 09:27:31','\0','\0'),(2,4,1,1,'IT-336326','2018-01-15 15:27:31','\0','\0'),(2,4,1,2,'TL-324718','2018-01-15 09:27:31','\0','\0'),(2,4,2,0,'LA-375147','2018-01-15 04:27:31','\0','\0'),(2,4,2,1,'AL-278997','2018-01-15 04:27:31','\0','\0'),(2,4,2,2,'IT-400330','2018-01-15 15:27:31','\0','\0'),(2,5,0,0,'TI-358595','2018-01-15 21:27:31','\0','\0'),(2,5,0,1,'BL-486725','2018-01-15 17:27:31','\0','\0'),(2,5,0,2,'LI-277351','2018-01-15 17:27:31','\0','\0'),(2,5,1,0,'TA-523464','2018-01-15 09:27:31','\0','\0'),(2,5,1,1,'AA-550254','2018-01-15 17:27:31','\0','\0'),(2,5,1,2,'BT-709657','2018-01-15 04:27:31','\0','\0'),(2,5,2,0,'AB-829147','2018-01-14 22:27:31','\0','\0'),(2,5,2,1,'AB-208795','2018-01-15 17:27:31','\0','\0'),(2,5,2,2,'AA-215094','2018-01-15 04:27:31','\0','\0'),(2,6,0,0,'II-197631','2018-01-16 07:27:31','\0','\0'),(2,6,0,1,'AI-786489','2018-01-15 17:27:31','\0','\0'),(2,6,0,2,'TI-533268','2018-01-16 07:27:31','\0','\0'),(2,6,1,0,'IL-700827','2018-01-15 04:27:31','\0','\0'),(2,6,1,1,'AB-116709','2018-01-16 07:27:31','\0','\0'),(2,6,1,2,'LB-950969','2018-01-15 21:27:31','\0','\0'),(2,6,2,0,'BA-609756','2018-01-14 17:27:31','\0','\0'),(2,6,2,1,'TB-134260','2018-01-15 17:27:31','\0','\0'),(2,6,2,2,'TT-541789','2018-01-15 04:27:31','\0','\0'),(2,7,0,0,'BI-990081','2018-01-16 07:27:31','\0','\0'),(2,7,0,1,'LL-116150','2018-01-15 15:27:31','\0','\0'),(2,7,0,2,'BI-753186','2018-01-16 07:27:31','\0','\0'),(2,7,1,0,'AA-657860','2018-01-15 17:27:31','\0','\0'),(2,7,1,1,'AL-957378','2018-01-15 17:27:31','\0','\0'),(2,7,1,2,'LB-663960','2018-01-16 07:27:31','\0','\0'),(2,7,2,0,'II-613684','2018-01-16 07:27:31','\0','\0'),(2,7,2,1,'LB-871358','2018-01-15 04:27:31','\0','\0'),(2,7,2,2,'LI-114794','2018-01-16 07:27:31','\0','\0'),(3,0,0,0,NULL,NULL,'\0','\0'),(3,0,0,1,NULL,NULL,'\0','\0'),(3,0,0,2,NULL,NULL,'\0','\0'),(3,0,1,0,NULL,NULL,'\0','\0'),(3,0,1,1,NULL,NULL,'\0','\0'),(3,0,1,2,NULL,NULL,'\0','\0'),(3,0,2,0,NULL,NULL,'\0','\0'),(3,0,2,1,NULL,NULL,'\0','\0'),(3,0,2,2,NULL,NULL,'\0','\0'),(3,1,0,0,NULL,NULL,'\0','\0'),(3,1,0,1,NULL,NULL,'\0','\0'),(3,1,0,2,NULL,NULL,'\0','\0'),(3,1,1,0,NULL,NULL,'\0','\0'),(3,1,1,1,NULL,NULL,'\0','\0'),(3,1,1,2,NULL,NULL,'\0','\0'),(3,1,2,0,NULL,NULL,'\0','\0'),(3,1,2,1,NULL,NULL,'\0','\0'),(3,1,2,2,NULL,NULL,'\0','\0'),(3,2,0,0,NULL,NULL,'\0','\0'),(3,2,0,1,NULL,NULL,'\0','\0'),(3,2,0,2,NULL,NULL,'\0','\0'),(3,2,1,0,NULL,NULL,'\0','\0'),(3,2,1,1,NULL,NULL,'\0','\0'),(3,2,1,2,NULL,NULL,'\0','\0'),(3,2,2,0,NULL,NULL,'\0','\0'),(3,2,2,1,NULL,NULL,'\0','\0'),(3,2,2,2,NULL,NULL,'\0','\0'),(3,3,0,0,NULL,NULL,'\0','\0'),(3,3,0,1,NULL,NULL,'\0','\0'),(3,3,0,2,NULL,NULL,'\0','\0'),(3,3,1,0,NULL,NULL,'\0','\0'),(3,3,1,1,NULL,NULL,'\0','\0'),(3,3,1,2,NULL,NULL,'\0','\0'),(3,3,2,0,NULL,NULL,'\0','\0'),(3,3,2,1,NULL,NULL,'\0','\0'),(3,3,2,2,NULL,NULL,'\0','\0'),(3,4,0,0,NULL,NULL,'\0','\0'),(3,4,0,1,NULL,NULL,'\0','\0'),(3,4,0,2,NULL,NULL,'\0','\0'),(3,4,1,0,NULL,NULL,'\0','\0'),(3,4,1,1,NULL,NULL,'\0','\0'),(3,4,1,2,NULL,NULL,'\0','\0'),(3,4,2,0,NULL,NULL,'\0','\0'),(3,4,2,1,NULL,NULL,'\0','\0'),(3,4,2,2,NULL,NULL,'\0','\0'),(3,5,0,0,NULL,NULL,'\0','\0'),(3,5,0,1,NULL,NULL,'\0','\0'),(3,5,0,2,NULL,NULL,'\0','\0'),(3,5,1,0,NULL,NULL,'\0','\0'),(3,5,1,1,NULL,NULL,'\0','\0'),(3,5,1,2,NULL,NULL,'\0','\0'),(3,5,2,0,NULL,NULL,'\0','\0'),(3,5,2,1,NULL,NULL,'\0','\0'),(3,5,2,2,NULL,NULL,'\0','\0'),(3,6,0,0,NULL,NULL,'\0','\0'),(3,6,0,1,NULL,NULL,'\0','\0'),(3,6,0,2,NULL,NULL,'\0','\0'),(3,6,1,0,NULL,NULL,'\0','\0'),(3,6,1,1,NULL,NULL,'\0','\0'),(3,6,1,2,NULL,NULL,'\0','\0'),(3,6,2,0,NULL,NULL,'\0','\0'),(3,6,2,1,NULL,NULL,'\0','\0'),(3,6,2,2,NULL,NULL,'\0','\0'),(3,7,0,0,NULL,NULL,'\0','\0'),(3,7,0,1,NULL,NULL,'\0','\0'),(3,7,0,2,NULL,NULL,'\0','\0'),(3,7,1,0,NULL,NULL,'\0','\0'),(3,7,1,1,NULL,NULL,'\0','\0'),(3,7,1,2,NULL,NULL,'\0','\0'),(3,7,2,0,NULL,NULL,'\0','\0'),(3,7,2,1,NULL,NULL,'\0','\0'),(3,7,2,2,NULL,NULL,'\0','\0'),(3,8,0,0,NULL,NULL,'\0','\0'),(3,8,0,1,NULL,NULL,'\0','\0'),(3,8,0,2,NULL,NULL,'\0','\0'),(3,8,1,0,NULL,NULL,'\0','\0'),(3,8,1,1,NULL,NULL,'\0','\0'),(3,8,1,2,NULL,NULL,'\0','\0'),(3,8,2,0,NULL,NULL,'\0','\0'),(3,8,2,1,NULL,NULL,'\0','\0'),(3,8,2,2,NULL,NULL,'\0','\0'),(3,9,0,0,NULL,NULL,'\0','\0'),(3,9,0,1,NULL,NULL,'\0','\0'),(3,9,0,2,NULL,NULL,'\0','\0'),(3,9,1,0,NULL,NULL,'\0','\0'),(3,9,1,1,NULL,NULL,'\0','\0'),(3,9,1,2,NULL,NULL,'\0','\0'),(3,9,2,0,NULL,NULL,'\0','\0'),(3,9,2,1,NULL,NULL,'\0','\0'),(3,9,2,2,NULL,NULL,'\0','\0'),(3,10,0,0,NULL,NULL,'\0','\0'),(3,10,0,1,NULL,NULL,'\0','\0'),(3,10,0,2,NULL,NULL,'\0','\0'),(3,10,1,0,NULL,NULL,'\0','\0'),(3,10,1,1,NULL,NULL,'\0','\0'),(3,10,1,2,NULL,NULL,'\0','\0'),(3,10,2,0,NULL,NULL,'\0','\0'),(3,10,2,1,NULL,NULL,'\0','\0'),(3,10,2,2,NULL,NULL,'\0','\0'),(3,11,0,0,NULL,NULL,'\0','\0'),(3,11,0,1,NULL,NULL,'\0','\0'),(3,11,0,2,NULL,NULL,'\0','\0'),(3,11,1,0,NULL,NULL,'\0','\0'),(3,11,1,1,NULL,NULL,'\0','\0'),(3,11,1,2,NULL,NULL,'\0','\0'),(3,11,2,0,NULL,NULL,'\0','\0'),(3,11,2,1,NULL,NULL,'\0','\0'),(3,11,2,2,NULL,NULL,'\0','\0');
/*!40000 ALTER TABLE `parking_cell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parking_lot`
--

DROP TABLE IF EXISTS `parking_lot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parking_lot` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `street_address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `size` int(10) NOT NULL,
  `price1` float NOT NULL DEFAULT '0',
  `price2` float NOT NULL DEFAULT '0',
  `alternative_lots` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `robot_ip` varchar(48) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lot_full` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parking_lot`
--

LOCK TABLES `parking_lot` WRITE;
/*!40000 ALTER TABLE `parking_lot` DISABLE KEYS */;
INSERT INTO `parking_lot` VALUES (1,'Sesam 2',4,5,3,NULL,'12.f.t43','\0'),(2,'Rabin 14',8,5,3,NULL,'13.f.t43','\0'),(3,'Big 16',12,5,3,NULL,'14.f.t43','\0');
/*!40000 ALTER TABLE `parking_lot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription_service`
--

DROP TABLE IF EXISTS `subscription_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscription_service` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `subs_type` int(10) NOT NULL,
  `customer_id` int(10) NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `car_id` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lot_id` int(10) DEFAULT '0',
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `daily_exit_time` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription_service`
--

LOCK TABLES `subscription_service` WRITE;
/*!40000 ALTER TABLE `subscription_service` DISABLE KEYS */;
/*!40000 ALTER TABLE `subscription_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `weekly_statistics`
--

DROP TABLE IF EXISTS `weekly_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weekly_statistics` (
  `start` date NOT NULL,
  `lot_id` int(10) NOT NULL,
  `realized_orders_mean` double NOT NULL DEFAULT '0',
  `canceled_orders_mean` double NOT NULL DEFAULT '0',
  `late_arrivals_mean` double NOT NULL DEFAULT '0',
  `realized_orders_median` double NOT NULL DEFAULT '0',
  `canceled_orders_median` double NOT NULL DEFAULT '0',
  `late_arrivals_median` double NOT NULL DEFAULT '0',
  `realized_orders_dist` varchar(300) NOT NULL,
  `canceled_orders_dist` varchar(300) NOT NULL,
  `late_arrivals_dist` varchar(300) NOT NULL,
  PRIMARY KEY (`start`,`lot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `weekly_statistics`
--

LOCK TABLES `weekly_statistics` WRITE;
/*!40000 ALTER TABLE `weekly_statistics` DISABLE KEYS */;
/*!40000 ALTER TABLE `weekly_statistics` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-14 15:29:40
