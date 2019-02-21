/*
 Navicat Premium Data Transfer

 Source Server         : localhost_mysql
 Source Server Type    : MySQL
 Source Server Version : 80014
 Source Host           : localhost:3306
 Source Schema         : activiti

 Target Server Type    : MySQL
 Target Server Version : 80014
 File Encoding         : 65001

 Date: 21/02/2019 17:48:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auditrecords
-- ----------------------------
DROP TABLE IF EXISTS `auditrecords`;
CREATE TABLE `auditrecords`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auditProcessId` int(11) DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `examineUserId` int(11) DEFAULT NULL,
  `examineStatus` int(11) DEFAULT NULL,
  `examineTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auditrecords
-- ----------------------------
INSERT INTO `auditrecords` VALUES (1, 1, '的规定翻跟斗', 3, 1, '2019-02-20 10:18:31');
INSERT INTO `auditrecords` VALUES (2, 1, '豆腐干豆腐干', 4, 1, '2019-02-20 12:34:35');
INSERT INTO `auditrecords` VALUES (3, 2, 'fcfgh', 3, 2, '2019-02-20 12:36:15');
INSERT INTO `auditrecords` VALUES (4, 3, 'dgsdfsdf', 3, 1, '2019-02-20 12:50:15');
INSERT INTO `auditrecords` VALUES (5, 4, 'dfgdfg', 3, 1, '2019-02-20 13:41:38');
INSERT INTO `auditrecords` VALUES (6, 4, '似的士大夫', 4, 1, '2019-02-20 14:14:39');
INSERT INTO `auditrecords` VALUES (7, 5, 'fghfhfh', 3, 1, '2019-02-20 14:51:46');
INSERT INTO `auditrecords` VALUES (8, 7, 'sdfsfsf', 3, 1, '2019-02-20 15:38:12');
INSERT INTO `auditrecords` VALUES (9, 7, '的热点覆盖', 4, 1, '2019-02-20 15:51:09');
INSERT INTO `auditrecords` VALUES (10, 6, '涤瑕荡垢', 3, 1, '2019-02-20 15:51:39');
INSERT INTO `auditrecords` VALUES (11, 8, 'fgdfg', 3, 1, '2019-02-20 15:57:32');
INSERT INTO `auditrecords` VALUES (12, 9, 'dfgdfg', 3, 1, '2019-02-20 16:00:57');

-- ----------------------------
-- Table structure for firstplan
-- ----------------------------
DROP TABLE IF EXISTS `firstplan`;
CREATE TABLE `firstplan`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `processInstanceId` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `siteName` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `chanceName` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `detailedDescription` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `arrange` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `createUserId` int(11) DEFAULT NULL,
  `createDateTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '基本员工');
INSERT INTO `role` VALUES (2, '部门经理');
INSERT INTO `role` VALUES (3, '总经理');
INSERT INTO `role` VALUES (4, '作业区领导');
INSERT INTO `role` VALUES (5, '计量监督站领导');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NUL