/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : activiti

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 21/02/2019 22:20:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auditprocess
-- ----------------------------
DROP TABLE IF EXISTS `auditprocess`;
CREATE TABLE `auditprocess`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `leaveDay` int(11) NULL DEFAULT NULL,
  `cause` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `leaveTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `createTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `createUserId` int(11) NULL DEFAULT NULL,
  `processInstanceId` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auditprocess
-- ----------------------------
INSERT INTO `auditprocess` VALUES (1, '葛林', 152, 'xdf', 'dfgdfg', 'dfgdfg', 3, '2019-02-19 23:56:32', 1, '20001');

-- ----------------------------
-- Table structure for auditrecords
-- ----------------------------
DROP TABLE IF EXISTS `auditrecords`;
CREATE TABLE `auditrecords`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auditProcessId` int(11) NULL DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `examineUserId` int(11) NULL DEFAULT NULL,
  `examineStatus` int(11) NULL DEFAULT NULL,
  `examineTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auditrecords
-- ----------------------------
INSERT INTO `auditrecords` VALUES (1, 1, 'dgdfgdfg', 3, 1, NULL);
INSERT INTO `auditrecords` VALUES (2, 1, 'sdfsdfsdf', 4, 1, NULL);

-- ----------------------------
-- Table structure for firstplan
-- ----------------------------
DROP TABLE IF EXISTS `firstplan`;
CREATE TABLE `firstplan`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `processInstanceId` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `siteName` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `chanceName` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `detailedDescription` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `arrange` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `createUserId` int(11) NULL DEFAULT NULL,
  `createDateTime` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of firstplan
-- ----------------------------
INSERT INTO `firstplan` VALUES (1, '27513', '士大夫十分', '手动阀手动阀手动阀', '士大夫山豆根山豆根', NULL, 1, '2019-02-21 21:47:23', 1);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
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
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '1', '1', '葛林');
INSERT INTO `user` VALUES (2, '2', '2', '宋旭明');
INSERT INTO `user` VALUES (3, '3', '3', '胡成荣');
INSERT INTO `user` VALUES (4, '4', '4', '王鹏');
INSERT INTO `user` VALUES (5, '5', '5', '张三');
INSERT INTO `user` VALUES (6, '6', '6', '李四');
INSERT INTO `user` VALUES (7, '7', '7', '王五');
INSERT INTO `user` VALUES (8, '8', '8', '赵六');
INSERT INTO `user` VALUES (9, '9', '9', '李刚');
INSERT INTO `user` VALUES (10, '10', '10', '习大大');
INSERT INTO `user` VALUES (11, '11', '11', '葛大大');

-- ----------------------------
-- Table structure for userrolerelation
-- ----------------------------
DROP TABLE IF EXISTS `userrolerelation`;
CREATE TABLE `userrolerelation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NULL DEFAULT NULL,
  `roleId` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of userrolerelation
-- ----------------------------
INSERT INTO `userrolerelation` VALUES (1, 1, 1);
INSERT INTO `userrolerelation` VALUES (2, 2, 1);
INSERT INTO `userrolerelation` VALUES (3, 3, 2);
INSERT INTO `userrolerelation` VALUES (4, 4, 3);
INSERT INTO `userrolerelation` VALUES (5, 5, 1);
INSERT INTO `userrolerelation` VALUES (6, 6, 1);
INSERT INTO `userrolerelation` VALUES (7, 7, 1);
INSERT INTO `userrolerelation` VALUES (8, 8, 4);
INSERT INTO `userrolerelation` VALUES (9, 9, 4);
INSERT INTO `userrolerelation` VALUES (10, 10, 5);
INSERT INTO `userrolerelation` VALUES (11, 11, 5);

SET FOREIGN_KEY_CHECKS = 1;
