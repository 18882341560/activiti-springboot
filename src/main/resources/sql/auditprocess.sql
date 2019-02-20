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

 Date: 19/02/2019 18:29:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auditprocess
-- ----------------------------
DROP TABLE IF EXISTS `auditprocess`;
CREATE TABLE `auditprocess`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `leaveDay` int(11) DEFAULT NULL,
  `cause` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `leaveTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `createTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `createUserId` int(11) DEFAULT NULL,
  `processInstanceId` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auditprocess
-- ----------------------------
INSERT INTO `auditprocess` VALUES (1, '大范甘迪发', 5, '婚假', '士大夫士大夫', '2019-2-25', 2, '2019-02-19 09:07:40', 1, '40001');
INSERT INTO `auditprocess` VALUES (2, '葛林', 3, 'fgh', 'dffg', '6151', 2, '2019-02-19 15:43:10', 1, '45001');

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '基本员工');
INSERT INTO `role` VALUES (2, '部门经理');
INSERT INTO `role` VALUES (3, '总经理');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '1', '1', '葛林');
INSERT INTO `user` VALUES (2, '2', '2', '宋旭明');
INSERT INTO `user` VALUES (3, '3', '3', '胡成荣');
INSERT INTO `user` VALUES (4, '4', '4', '王鹏');

-- ----------------------------
-- Table structure for userrolerelation
-- ----------------------------
DROP TABLE IF EXISTS `userrolerelation`;
CREATE TABLE `userrolerelation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `roleId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of userrolerelation
-- ----------------------------
INSERT INTO `userrolerelation` VALUES (1, 1, 1);
INSERT INTO `userrolerelation` VALUES (2, 2, 1);
INSERT INTO `userrolerelation` VALUES (3, 3, 2);
INSERT INTO `userrolerelation` VALUES (4, 4, 3);

SET FOREIGN_KEY_CHECKS = 1;



CREATE TABLE `firstplan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `processInstanceId` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `siteName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `chanceName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `detailedDescription` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `arrange` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `createUserId` int(11) DEFAULT NULL,
  `createDateTime` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
