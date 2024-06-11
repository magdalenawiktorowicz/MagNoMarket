-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: magno
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `idCategoria` int NOT NULL AUTO_INCREMENT,
  `nombreCategoria` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`idCategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES (1,'Legumbres'),(2,'Bebidas'),(3,'Postres'),(4,'Platos Preparados'),(5,'Panaderia'),(6,'Marisco y Pescado'),(7,'Huevos y Lacteos'),(8,'Fruta y Verdura'),(9,'Carnes');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comprasmagno`
--

DROP TABLE IF EXISTS `comprasmagno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comprasmagno` (
  `idCompraMagno` int NOT NULL AUTO_INCREMENT,
  `fechaCompraMagno` date DEFAULT NULL,
  `totalCompraMagno` decimal(8,2) DEFAULT NULL,
  `idUsuarioFK` int DEFAULT NULL,
  PRIMARY KEY (`idCompraMagno`),
  KEY `idUsuarioFK` (`idUsuarioFK`),
  CONSTRAINT `comprasmagno_ibfk_1` FOREIGN KEY (`idUsuarioFK`) REFERENCES `usuarios` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comprasmagno`
--

LOCK TABLES `comprasmagno` WRITE;
/*!40000 ALTER TABLE `comprasmagno` DISABLE KEYS */;
/*!40000 ALTER TABLE `comprasmagno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comprasproductomagno`
--

DROP TABLE IF EXISTS `comprasproductomagno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comprasproductomagno` (
  `idComprasProductoMagno` int NOT NULL AUTO_INCREMENT,
  `idProductoMagnoFK` int DEFAULT NULL,
  `idCompraMagnoFK` int DEFAULT NULL,
  PRIMARY KEY (`idComprasProductoMagno`),
  KEY `idProductoMagnoFK` (`idProductoMagnoFK`),
  KEY `idCompraMagnoFK` (`idCompraMagnoFK`),
  CONSTRAINT `comprasproductomagno_ibfk_1` FOREIGN KEY (`idProductoMagnoFK`) REFERENCES `productosmagno` (`idProductoMagno`),
  CONSTRAINT `comprasproductomagno_ibfk_2` FOREIGN KEY (`idCompraMagnoFK`) REFERENCES `comprasmagno` (`idCompraMagno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comprasproductomagno`
--

LOCK TABLES `comprasproductomagno` WRITE;
/*!40000 ALTER TABLE `comprasproductomagno` DISABLE KEYS */;
/*!40000 ALTER TABLE `comprasproductomagno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `listadecompra`
--

DROP TABLE IF EXISTS `listadecompra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `listadecompra` (
  `idListaDeCompra` int NOT NULL AUTO_INCREMENT,
  `nombreProducto` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `cantidadProducto` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `unidadProducto` int DEFAULT NULL,
  `comentarioProducto` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`idListaDeCompra`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listadecompra`
--

LOCK TABLES `listadecompra` WRITE;
/*!40000 ALTER TABLE `listadecompra` DISABLE KEYS */;
/*!40000 ALTER TABLE `listadecompra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `listadecompraproductosdespensa`
--

DROP TABLE IF EXISTS `listadecompraproductosdespensa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `listadecompraproductosdespensa` (
  `idListaDeCompraProductoDespensa` int NOT NULL AUTO_INCREMENT,
  `idProductoFK` int DEFAULT NULL,
  `idListaDeCompraFK` int DEFAULT NULL,
  PRIMARY KEY (`idListaDeCompraProductoDespensa`),
  KEY `idProductoFK` (`idProductoFK`),
  KEY `idListaDeCompraFK` (`idListaDeCompraFK`),
  CONSTRAINT `listadecompraproductosdespensa_ibfk_1` FOREIGN KEY (`idProductoFK`) REFERENCES `productosdespensa` (`idProductoDespensa`),
  CONSTRAINT `listadecompraproductosdespensa_ibfk_2` FOREIGN KEY (`idListaDeCompraFK`) REFERENCES `listadecompra` (`idListaDeCompra`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listadecompraproductosdespensa`
--

LOCK TABLES `listadecompraproductosdespensa` WRITE;
/*!40000 ALTER TABLE `listadecompraproductosdespensa` DISABLE KEYS */;
/*!40000 ALTER TABLE `listadecompraproductosdespensa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificaciones`
--

DROP TABLE IF EXISTS `notificaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificaciones` (
  `idNotificacion` int NOT NULL AUTO_INCREMENT,
  `tipoNotificacion` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`idNotificacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificaciones`
--

LOCK TABLES `notificaciones` WRITE;
/*!40000 ALTER TABLE `notificaciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificacionesproductosdespensa`
--

DROP TABLE IF EXISTS `notificacionesproductosdespensa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificacionesproductosdespensa` (
  `idNotificacionesProductosDespensa` int NOT NULL AUTO_INCREMENT,
  `idProductoDespensaFK` int DEFAULT NULL,
  `idNotificacionFK` int DEFAULT NULL,
  PRIMARY KEY (`idNotificacionesProductosDespensa`),
  KEY `idProductoDespensaFK` (`idProductoDespensaFK`),
  KEY `idNotificacionFK` (`idNotificacionFK`),
  CONSTRAINT `notificacionesproductosdespensa_ibfk_1` FOREIGN KEY (`idProductoDespensaFK`) REFERENCES `productosdespensa` (`idProductoDespensa`),
  CONSTRAINT `notificacionesproductosdespensa_ibfk_2` FOREIGN KEY (`idNotificacionFK`) REFERENCES `notificaciones` (`idNotificacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificacionesproductosdespensa`
--

LOCK TABLES `notificacionesproductosdespensa` WRITE;
/*!40000 ALTER TABLE `notificacionesproductosdespensa` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificacionesproductosdespensa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productosdespensa`
--

DROP TABLE IF EXISTS `productosdespensa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productosdespensa` (
  `idProductoDespensa` int NOT NULL AUTO_INCREMENT,
  `nombreProductoDespensa` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `imagenProductoDespensa` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `fechaCaducidadProductoDespensa` date DEFAULT NULL,
  `cantidadProductoDespensa` int DEFAULT NULL,
  `unidadProductoDespensa` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `autoanadirAListaCompraDespensa` tinyint DEFAULT NULL,
  `cantidadMinParaAnadirDespensa` int DEFAULT NULL,
  `tiendaProductoDespensa` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `idCategoriaFK` int DEFAULT NULL,
  `idUsuarioFK` int DEFAULT NULL,
  PRIMARY KEY (`idProductoDespensa`),
  KEY `idCategoriaFK_idx` (`idCategoriaFK`),
  KEY `idUsuarioFK_idx` (`idUsuarioFK`),
  CONSTRAINT `productosdespensa_ibfk_1` FOREIGN KEY (`idCategoriaFK`) REFERENCES `categorias` (`idCategoria`),
  CONSTRAINT `productosdespensa_ibfk_2` FOREIGN KEY (`idUsuarioFK`) REFERENCES `usuarios` (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productosdespensa`
--

LOCK TABLES `productosdespensa` WRITE;
/*!40000 ALTER TABLE `productosdespensa` DISABLE KEYS */;
INSERT INTO `productosdespensa` VALUES (39,'Manzanas','content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F27/ORIGINAL/NONE/image%2Fjpeg/743956310','2024-06-05',3,'kg',0,0,'',8,2),(42,'Gnocchi','content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F35/ORIGINAL/NONE/image%2Fjpeg/612389697','2024-06-28',1,'unidad',0,1,'',4,2),(44,'RedBull','content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F36/ORIGINAL/NONE/image%2Fjpeg/1007750495','2024-09-26',2,'unidad',0,1,'',2,2),(54,'Manzanas','content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F27/ORIGINAL/NONE/image%2Fjpeg/303537859','2024-06-20',3,'kg',0,1,'',8,1),(55,'Garbanzos','content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F26/ORIGINAL/NONE/image%2Fjpeg/2036385718','2024-06-21',1,'unidad',0,1,'',1,1),(56,'Gnocchi','content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F35/ORIGINAL/NONE/image%2Fjpeg/2116888380','2024-06-11',1,'unidad',0,1,'',4,1),(57,'Chocolate blanco','content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F34/ORIGINAL/NONE/image%2Fjpeg/2090661001','2024-08-21',2,'unidad',0,1,'',3,1),(61,'RedBull','content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F36/ORIGINAL/NONE/image%2Fjpeg/136337338','2024-10-29',2,'unidad',0,1,'',2,1);
/*!40000 ALTER TABLE `productosdespensa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productosmagno`
--

DROP TABLE IF EXISTS `productosmagno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productosmagno` (
  `idProductoMagno` int NOT NULL AUTO_INCREMENT,
  `nombreProductoMagno` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `imagenProductoMagno` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `precioProductoMagno` decimal(8,2) DEFAULT NULL,
  `marcaProductoMagno` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `idCategoriaFK` int DEFAULT NULL,
  PRIMARY KEY (`idProductoMagno`),
  KEY `idCategoriaFK_idx` (`idCategoriaFK`),
  CONSTRAINT `idCategoriaFK` FOREIGN KEY (`idCategoriaFK`) REFERENCES `categorias` (`idCategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productosmagno`
--

LOCK TABLES `productosmagno` WRITE;
/*!40000 ALTER TABLE `productosmagno` DISABLE KEYS */;
INSERT INTO `productosmagno` VALUES (6,'Arroz Blanco','https://avunto.com/34436-large_default/sos-arroz-clasico-500-gr.jpg?w=100&h=100',1.85,'SOS',1),(7,'Arroz Vaporizado','https://prod-mercadona.imgix.net/images/acbc70377b3c46d3334fd2a103fe2a15.jpg?fit=crop&h=600&w=600',1.69,'Brillante',1),(8,'Garbanzos Cocidos','https://a2.soysuper.com/3229ef5149746bc35bde67108f047439.1500.0.0.0.wmark.c7d266d5.jpg',1.35,'Luengo',1),(9,'Lentejas Cocidas','https://distribucionesplata.com/tienda/20315-thickbox_default/lenteja-luengo-cocida-bote-570-gr.jpg',0.95,'Luengo',1),(10,'Alubias Cocidas','https://vegetalia.com/wp-content/uploads/VGT_alubias_blancas_cocidas_ca.jpg',0.98,'Vegetalia',1),(11,'Alubia Blanca','https://llenatudespensa.com/alubia-pochas-cocidas-marzo-bote-vidrio-_Id-10543.jpg',1.25,'Marzo',1),(12,'Lentejas Rojas','https://vegetalia.com/wp-content/uploads/VGT_lentejas_ca-768x768.jpg',1.15,'Vegetalia',1),(13,'Garbanzos Cocidos','https://www.ferreteriayhosteleria.com/30597-large_default/la-pedriza-garbanzos-cocidos-e-tra-bote-560-grs.jpg',0.95,'La Pedriza',1),(14,'Garbanzos al natural','https://www.biosano.es/cdnassets/products/large/garbanzos-cocidos-ecologicos-vegetalia-bote-vidrio.png',0.85,'Vegetalia',1),(15,'Quinoa','https://www.brillante.es/wp-content/uploads/2017/04/Brillante-Vasitos-Quinoa-321x225.jpg',1.25,'Brillante',1),(16,'Agua 1L','https://prod-mercadona.imgix.net/images/0d30af63ff94f5d094bbdc47f6fdb432.jpg?fit=crop&h=600&w=600',0.60,'Natura',2),(17,'Agua Mineral 6L','https://prod-mercadona.imgix.net/images/e4da46382ed0093fd242f1f3e2e9a4b3.jpg?fit=crop&h=600&w=600',2.49,'Natura',2),(18,'Coca-Cola 2L','https://prod-mercadona.imgix.net/images/8e4664bbdc20e75e8c2510a8456d987e.jpg?fit=crop&h=600&w=600',1.99,'CocaCola',2),(19,'Pepsi','https://prod-mercadona.imgix.net/images/cc539810afa487821582115b981014e5.jpg?fit=crop&h=600&w=600',1.69,'Pepsi',2),(20,'Fanta Limon','https://prod-mercadona.imgix.net/images/16536842382c2b37b48aabc6695c17af.jpg?fit=crop&h=600&w=600',1.54,'Fanta',2),(21,'Fanta Naranja','https://prod-mercadona.imgix.net/images/ba6a9420f28bf6f56a13e033210a4a7d.jpg?fit=crop&h=600&w=600',1.54,'Fanta',2),(22,'Zumo Naranja','https://vivonium.es/wp-content/uploads/2020/05/Zumo-de-naranja-juver.jpg',0.56,'Juver',2),(23,'Cerveza','https://i0.wp.com/www.sanchez-garrido.com/wp-content/uploads/2020/03/botella1l.jpg?fit=600%2C600&ssl=1',1.05,'Cruzcampo',2),(24,'Vino Blanco','https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/201710/10/00118769600562____9__600x600.jpg',1.45,'Mar de Frades',2),(25,'Vino Tinto','https://www.vinosderiojaycestasdenavidad.com/755-large_default/-beronia-tinto-crianza-botella-75-cl.jpg',2.54,'Beronia',2),(26,'ColaCao','https://supermercado.galuresa.com/319-large_default/cacao-soluble-cola-cao-original-bote-390-grs.jpg',1.58,'ColaCao',3),(27,'Cafe soluble','https://www.supermercadoseljamon.com/documents/10180/892067/20001102_G.jpg',0.89,'Nescafe',3),(28,'Flan Vainilla','https://www.yoguresnestle.es/img/productos/la_lechera/flanes/flanby-flan-vainilla-caramelo.jpg',1.25,'Flanby',3),(29,'Yogures','https://www.dia.es/product_images/204979/204979_ISO_0_ES.jpg?imwidth=392',1.32,'Vitalinea',3),(30,'Yogures','https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/202201/12/00118822900280____8__600x600.jpg',1.35,'Danone',3),(31,'Gelatina','https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/202005/13/00118821303700____8__600x600.jpg',0.95,'Reina',3),(32,'Chocolate negro','https://www.valor.es/wp-content/uploads/2018/04/92.png',1.25,'Valor',3),(33,'Chocolate con leche','https://www.supermercadosmas.com/media/catalog/product/cache/d91bc430dbe2e3d899436802c7aa5233/a/e/aecoc_07613035268388_1679779753_07613035268388_a1n1.jpg',1.85,'Nestle',3),(34,'Chocolate blanco','https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/202401/11/00120642901787____5__600x600.jpg',1.25,'Milka',3),(35,'Pizza','https://m.media-amazon.com/images/I/9169JHumqjL.jpg',3.35,'Tarradellas',4),(36,'Pizza','https://www.dia.es/product_images/39962/39962_ISO_0_ES.jpg',3.45,'Tarradellas',4),(37,'Tortilla','https://www.dia.es/product_images/239160/239160_ISO_0_ES.jpg',3.25,'Los Palacios',4),(38,'Ensaladilla','https://www.productoslapescaderia.com/wp-content/uploads/2015/04/Ensaladilla-Rusa-La-Pescaderia-Bandeja-400gr-1.png',2.56,'La Pescaderia',4),(39,'Lasaña verduras','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSi8zOFt5OoS0YS-xB_To1E4pRLKpyoJg4fYiKMV4chMA&s',3.15,'La Cocinera',4),(40,'Lasaña carne','https://m.media-amazon.com/images/I/81uxjNLhLmL._AC_UF894,1000_QL80_DpWeblab_.jpg',3.85,'La Cocinera',4),(41,'Gazpacho','https://www.alvalle.com/images/librariesprovider4/productos/pck_original_540.png?sfvrsn=19abecb8_8',2.45,'Alvalle',4),(42,'Empanada ','https://www.lasantina.es/wp-content/uploads/2018/03/empanadaatun.png',3.19,'La Santiña',4),(43,'Sopa cebolla','https://www.unileverfoodsolutions.es/dam/global-ufs/mcos/SPAIN/calcmenu/products/ES-products/packshots/knorr/knorr-sopa-de-cebolla-deshidratada-bote-450g/SOPA-CEBOLLA-HERO.jpg',1.48,'Knorr',4),(44,'Sopa verduras','https://cdn-consum.aktiosdigitalservices.com/tol/consum/media/product/img/300x300/7072721_001.jpg?t=20220723040002',1.25,'Knorr',4),(45,'Pan','https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Assorted_bread.jpg/640px-Assorted_bread.jpg',0.15,'null',5),(46,'Donut','https://yourspanishcorner.com/13847-large_default/donuts-bombon-bimbo-x4.jpg',1.45,'Donut',5),(47,'Magdalenas','https://dulcesol.com/infografias/2023/02/09684.jpg',2.00,'Dulcesol',5),(48,'Bizcochos','https://supermercado.eroski.es/images/358135.jpg',3.12,'Fontaneda',5),(49,'Pan de molde','https://www.bimbo.es/uploads/bimbocorp/BIMBO_50_50.png?7',2.95,'Bimbo',5),(50,'Harina de trigo','https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/201709/14/00118037900018____1__600x600.jpg',1.45,'Gallo',5),(51,'Harina de maiz','https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/202404/02/00118038300028____3__600x600.jpg',1.85,'PAN',5),(52,'Levadura','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT61CoUjYpy_lWYayrFl2hmB1sHzjj3fXHjzTPn9hXXjA&s',0.95,'Royal',5),(53,'Picos','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwndaVpLJd6dfkjPuUPSpI6I6AEqc10bMyfHxFrO6vmg&s',0.45,'Obando',5),(54,'Langostinos 1kg','https://www.aquamargold.com/wp-content/uploads/2021/06/AdobeStock_32391707-2048x1360.jpeg',5.56,'null',6),(55,'Gambas 1kg','https://olemarisco.es/cdn/shop/articles/curiosidades-sobre-las-gambas.jpg?v=1616414929',4.52,'null',6),(56,'Mejillones','https://mordestefoods.com/blog/wp-content/uploads/2022/07/mejillones-gallegos-moluscos-mordeste.jpg',3.45,'null',6),(57,'Almejas','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSLaq2UpXOSlOLxoYCfyaEizaX70Gc9zymcczhSdvawDQ&s',5.19,'null',6),(58,'Salmon','https://opercebeiro.com/wp-content/uploads/nc/catalog/rodaja-salmon-opercebeiro-800x800.png',4.78,'null',6),(59,'Dorada','https://mordestefoods.com/images/producto/2022/05/11/96/full_dorada-fresca-pescado-mordeste.jpg',4.89,'null',6),(60,'Sardinas 1kg','https://steemitimages.com/640x0/https://cdn.steemitimages.com/DQmRPtbrm5XRfhbtd1RWH7ui5u1d8UhpTAKjv9w87opvWyh/image.png',2.52,'null',6),(61,'Merluza','https://www.hiperxel.com/wp-content/uploads/2022/05/merluza-del-cabo-congelada-imprescindible-en-nuestros-congeladores-2022-05-12-merluza-del-cabo-congelada-imprescindible-en-nuestros-congeladores-1024x1024.png',4.52,'null',6),(62,'Bacalao','https://www.lapondala.com/wp-content/uploads/2017/04/bacalao-web-1200x600.jpg',5.85,'null',6),(63,'Huevos 12und','https://content21.sabervivirtv.com/medio/2023/11/03/huevos_fe2ba96b_231103092853_1280x720.jpg',2.45,'null',7),(64,'Leche entera 1L','https://www.supermercadoseljamon.com/documents/10180/892067/17004100_G.jpg',1.24,'Puleva',7),(65,'Leche Semi 1L','https://sgfm.elcorteingles.es/SGFM/dctm/MEDIA03/202204/20/00120912200100____13__600x600.jpg',1.24,'Pascual',7),(66,'Leche desnatada 1L','https://supermercado.galuresa.com/1341-large_default/leche-desnatada-clasturiana-btlla-15l.jpg',1.24,'Asturiana',7),(67,'Avena 1L','https://pamplona.e-leclerc.es/documents/10180/10815/5411188124689_G.jpg',1.45,'Alpro',7),(68,'Bebida de soja 1L','https://bixoto.com/media/catalog/product/cache/2e45ba69d70625e0fc47dbc2d34862e1/8/4/8410128650315_S4602117_P0-v_12.jpg',1.48,'ViveSoy',7),(69,'Batido Chocolate','https://www.supermercadoseljamon.com/documents/10180/892067/17004505_G.jpg',1.24,'Puleva',7),(70,'Leche infantil','https://static.carrefour.es/hd_510x_/img_pim_food/644323_00_2.jpg',1.54,'Puleva',7),(71,'Leche infantil','https://www.arenal.com/medias/N321646OriginalImage-pdpFormat?context=bWFzdGVyfGltYWdlc3wyNTAzNnxpbWFnZS9qcGVnfGltYWdlcy9oYjgvaDFlLzg4MzAwMzUzNjE4MjIuanBnfGIxZTVmOTRjMzU4NjlhOTgxOTRmMDgyZjJlMjI4Y2UzOTA4MGQ2MDJlZjA2YTVjYzA0NWZkZjZiNjdjOGRlMTI',1.42,'Nestle',7),(72,'Queso','https://quesoscamporeal.com/wp-content/uploads/2021/02/TIERNO-TRADICIONAL-CUN%CC%83A.png',2.45,'Campo Real',7),(73,'Platano de Canarias','https://www.frutiban.com/wp-content/uploads/2017/12/banana.jpg',0.99,'null',8),(74,'Uvas','https://phantom-elmundo.unidadeditorial.es/3ce4cca00caea1a116a24f4a20eab6e5/crop/0x55/699x520/resize/414/f/jpg/assets/multimedia/imagenes/2019/12/19/15767544243662.jpg',1.24,'null',8),(75,'Manzanas','https://manzanaswashington.com/wp-content/uploads/2021/02/Nuestras-manzanas-portada1.jpg',1.26,'null',8),(76,'Peras','https://www.lekue.com/storyblok/f/120479/c3bb1cd8e3/pera-beneficios.jpg',1.24,'null',8),(77,'Sandia','https://5aldia.cl/wp-content/uploads/2018/03/sandia.jpg',2.52,'null',8),(78,'Melon','https://img2.rtve.es/im/6952659/?w=900',2.89,'null',8),(79,'Naranjas','https://elpoderdelconsumidor.org/wp-content/uploads/2022/02/naranja-1.jpg',3.15,'null',8),(80,'Limon','https://www.quironsalud.com/idcsalud-client/cm/images?locale=es_ES&idMmedia=3153140',2.11,'null',8),(81,'Aguacate','https://thefoodtech.com/wp-content/uploads/2023/07/aguacate-828x548.jpg',2.54,'null',8),(82,'Lechuga Iceberg','https://www.frutascuevas.es/130-large_default/romanilla-unidad.jpg',1.87,'null',8),(83,'Patatas 3Kg','https://clinicacisem.com/wp-content/uploads/2019/04/patatas.jpg',2.41,'null',8),(84,'Cebolla','https://www.citrusgourmet.com/es/206-thickbox_default/saco-de-cebollas-12-kg.jpg',1.14,'null',8),(85,'Tomates','https://static.abc.es/media/viajar/2017/08/01/tomates-kl8G--1240x698@abc.jpg',1.85,'null',8),(86,'Zanahorias','https://phantom-marca.unidadeditorial.es/631c4b360757ce3355fc636fe8c0cab9/resize/828/f/jpg/assets/multimedia/imagenes/2023/09/05/16939288675740.jpg',1.12,'null',8),(87,'Calabacin','https://metode.es/wp-content/uploads/2015/05/104-85ros.jpg',1.24,'null',8),(88,'Pimientos','https://hortamar.es/wp-content/uploads/pimiento-california-hortamar-1.jpg',1.74,'null',8),(89,'Berenjena','https://www.gastronomiavasca.net/uploads/image/file/5702/w700_berenjena1.jpg',1.99,'null',8),(90,'Pollo entero','https://s1.eestatic.com/2019/04/02/ciencia/nutricion/nutricion_387972895_119439934_1706x960.jpg',5.54,'null',9),(91,'Pavo ','https://www.coubet.com/foto/mitja/106/_0605.jpg',5.89,'null',9),(92,'Cerdo','https://carneentucasa.com/archivos/image/tienda_productos/medias/37_atr-97-comprarcarne-de-cerdolomo-cana-de-cerdo-fileteado-bandeja-de-2-kg-.jpg',4.54,'null',9),(93,'Ternera','https://s1.eestatic.com/2020/12/23/actualidad/545707261_168225414_1706x960.jpg',5.54,'null',9),(94,'Alitas de pollo','https://i0.wp.com/meatlovers.es/wp-content/uploads/alitas-de-pollo-bandeja-1-kg_860002.jpg?fit=2000%2C1353&ssl=1',4.12,'null',9),(95,'Hamburguesas','https://www.shutterstock.com/image-photo/raw-fresh-beef-burgers-plastic-600nw-758252548.jpg',3.25,'null',9),(96,'Chistorras','https://viper.storekais.com/wp-content/uploads/86004_0.jpg',3.56,'null',9),(97,'Fresas de Huelva','https://belenfruteria.es/wp-content/uploads/2022/01/fresones-huelva.jpg',2.79,'null',8);
/*!40000 ALTER TABLE `productosmagno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `nombreUsuario` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `apellidosUsuario` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `tipoUsuario` tinyint DEFAULT NULL,
  `direccionUsuario` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `emailUsuario` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `claveUsuario` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Magda','Wiktorowicz',0,'Antioquia 2, 5B','magdalenawiktorowicz@op.pl','31e1df59b390aea997c354a3c06b126dc6317849a92505341d081b2eecf569fd'),(2,'a','a',0,'a','a','ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb'),(3,'Noe','Munoz',0,'Monesterio','noeiffel@gmail.com','Studium2024'),(18,'admin2','lopez',0,'plaza d','admin2@gmail.com','abu'),(19,'manuel','addf',1,'sdf','manuel@gmail.com','$2b$10$/8KKYIByWnVXsIPtWAWVLuNXV//k/cP6lWMyI/282VWnkqXcdluR2');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-11 12:15:07
