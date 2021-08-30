DROP TABLE IF EXISTS `tb_course`;
CREATE TABLE `tb_course`  (
  `id` int(0) NOT NULL,
  `class_hour` decimal(8, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;

INSERT INTO `tb_course` VALUES (1, 10.00);
INSERT INTO `tb_course` VALUES (2, 20.00);
INSERT INTO `tb_course` VALUES (3, 66.00);
INSERT INTO `tb_course` VALUES (4, 99.00);