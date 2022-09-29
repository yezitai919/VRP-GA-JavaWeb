/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : vrp_db

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 29/09/2022 19:43:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for delivery
-- ----------------------------
DROP TABLE IF EXISTS `delivery`;
CREATE TABLE `delivery`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `truck` int NOT NULL,
  `loading` double(4, 1) NOT NULL,
  `speed` double(4, 1) NOT NULL,
  `cost` double(4, 1) NOT NULL,
  `punish` double(4, 1) NOT NULL,
  `deport_x` double(4, 1) NOT NULL,
  `deport_y` double(4, 1) NOT NULL,
  `price_t` double(4, 1) NOT NULL,
  `price_km` double(4, 1) NOT NULL,
  `admin_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `admin_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `registration_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of delivery
-- ----------------------------
INSERT INTO `delivery` VALUES (1, 5, 8.0, 5.0, 2.0, 2.0, 14.5, 13.5, 3.1, 2.2, '公子', '17158964752', 'zhongli');

-- ----------------------------
-- Table structure for genetic
-- ----------------------------
DROP TABLE IF EXISTS `genetic`;
CREATE TABLE `genetic`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `popu_num` int NOT NULL,
  `gene_num` int NOT NULL,
  `cros_rate` double(4, 1) NOT NULL,
  `muta_rate` double(5, 2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of genetic
-- ----------------------------
INSERT INTO `genetic` VALUES (1, 800, 100, 0.6, 0.01);

-- ----------------------------
-- Table structure for needs
-- ----------------------------
DROP TABLE IF EXISTS `needs`;
CREATE TABLE `needs`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `cli_needs` double(4, 1) NOT NULL,
  `cli_x` double(4, 1) NOT NULL,
  `cli_y` double(4, 1) NOT NULL,
  `cli_start` double(4, 1) NOT NULL,
  `cli_end` double(4, 1) NOT NULL,
  `user_id` int NOT NULL,
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `phone_num` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `remark` varchar(140) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '无',
  `price` double(4, 1) NOT NULL,
  `arrival_time` double NULL DEFAULT NULL,
  `state` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '等待发货',
  `compensation` double NULL DEFAULT NULL,
  `evaluation` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '暂无',
  `driver_id` int NULL DEFAULT NULL,
  `driver_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `driver_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `rate1` int NULL DEFAULT NULL,
  `rate2` int NULL DEFAULT NULL,
  `rate3` int NULL DEFAULT NULL,
  `path_id` int NULL DEFAULT NULL,
  `total_dist` double(4, 1) NULL DEFAULT NULL,
  `curr_dist` double(4, 1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of needs
-- ----------------------------
INSERT INTO `needs` VALUES (1, 0.1, 12.8, 8.5, 4.8, 10.4, 1, '张三', '17156489534', '无', 12.0, 2.3, '等待发货', 4.9, '暂无', 15, '夜兰', '12547896547', NULL, NULL, NULL, 6, 11.7, 33.4);
INSERT INTO `needs` VALUES (2, 0.4, 18.4, 3.4, 1.5, 7.0, 3, '赵公明', '18954752365', '无', 23.0, 3.9, '等待发货', 0, '暂无', 15, '夜兰', '12547896547', NULL, NULL, NULL, 6, 19.3, 13.3);
INSERT INTO `needs` VALUES (4, 1.2, 15.4, 16.6, 4.7, 10.2, 4, '李四', '15878465843', '无', 14.0, 0.6, '等待发货', 8.1, '暂无', 15, '夜兰', '12547896547', NULL, NULL, NULL, 6, 3.2, 9.3);
INSERT INTO `needs` VALUES (5, 1.5, 18.9, 15.2, 5.2, 9.5, 4, '李四', '15878465843', '无', 15.0, 7.6, '运输中', 0, '暂无', 3, '万叶', '15878924586', NULL, NULL, NULL, 1, 38.2, 16.9);
INSERT INTO `needs` VALUES (6, 0.8, 15.5, 11.6, 3.7, 8.8, 3, '赵公明', '18954752365', '无', 36.0, 6.9, '等待发货', 0, '暂无', 15, '夜兰', '12547896547', NULL, NULL, NULL, 6, 34.5, 29.3);
INSERT INTO `needs` VALUES (7, 1.3, 3.9, 10.6, 6.7, 12.3, 1, '张三', '17156489534', '无', 36.0, 2.8, '已送达', 7.8, '暂无', 3, '万叶', '15878924586', NULL, NULL, NULL, 1, 14.1, 0.0);
INSERT INTO `needs` VALUES (8, 1.7, 10.6, 7.6, 7.9, 12.9, 2, '王富贵', '18125468757', '无', 15.0, 5.6, '等待发货', 4.5, '小伙子人不错', 15, '夜兰', '12547896547', 5, 5, 5, 6, 28.2, 35.8);
INSERT INTO `needs` VALUES (12, 0.6, 8.6, 8.4, 0.6, 5.7, 4, '李四', '15878465843', '无', 15.0, 5.2, '运输中', 0, '暂无', 3, '万叶', '15878924586', NULL, NULL, NULL, 1, 25.9, 4.6);
INSERT INTO `needs` VALUES (13, 1.2, 12.5, 2.1, 2.6, 6.8, 2, '王富贵', '18125468757', '无', 15.0, 2.3, '等待发货', 0.6, '暂无', 9, '顶针', '18457925684', 5, 4, 3, 5, 11.6, 0.0);
INSERT INTO `needs` VALUES (14, 0.4, 13.8, 5.2, 2.5, 8.1, 4, '李四', '15878465843', '无', 25.0, 8.3, '等待发货', 0.4, '暂无', 9, '顶针', '18457925684', NULL, NULL, NULL, 5, 41.6, 8.3);
INSERT INTO `needs` VALUES (15, 0.9, 6.7, 16.9, 4.1, 10.1, 1, '张三', '17156489534', '无', 25.0, 2.3, '等待发货', 3.5, '暂无', 5, '荒天帝', '14758964527', NULL, NULL, NULL, 2, 11.7, 34.6);
INSERT INTO `needs` VALUES (17, 1.3, 14.8, 2.6, 3.4, 8.1, 2, '王富贵', '18125468757', '无', 25.0, 7.8, '等待发货', 0, '很快啊', 9, '顶针', '18457925684', 5, 5, 5, 5, 38.8, 17.0);
INSERT INTO `needs` VALUES (20, 1.9, 17.1, 11.0, 5.3, 10.3, 4, '李四', '15878465843', '无', 15.0, 8.6, '运输中', 0, '暂无', 3, '万叶', '15878924586', NULL, NULL, NULL, 1, 42.8, 21.5);
INSERT INTO `needs` VALUES (21, 1.7, 7.4, 1.0, 2.1, 6.3, 1, '张三', '17156489534', '无', 15.0, 3.4, '等待发货', 0, '暂无', 9, '顶针', '18457925684', NULL, NULL, NULL, 5, 16.8, 0.0);
INSERT INTO `needs` VALUES (22, 1.1, 0.2, 2.8, 6.8, 11.0, 2, '王富贵', '18125468757', '无', 15.0, 4.8, '等待发货', 3.9, '一支穿云箭', 9, '顶针', '18457925684', 5, 5, 5, 5, 24.2, 0.0);
INSERT INTO `needs` VALUES (28, 2.0, 13.0, 14.0, 2.0, 21.0, 2, '比利', '12324254567', 'Oh yeah', 15.0, 0.3, '等待发货', 3.4, '暂无', 8, '老八', '15748523658', NULL, NULL, NULL, 3, 1.6, 0.0);
INSERT INTO `needs` VALUES (29, 2.0, 3.0, 23.0, 2.0, 9.0, 2, '元首', '12323465684', '我到河北省来', 15.0, 3.8, '等待发货', 0, '暂无', 5, '荒天帝', '14758964527', NULL, NULL, NULL, 2, 18.9, 0.0);
INSERT INTO `needs` VALUES (30, 2.0, 5.0, 16.0, 2.0, 8.0, 2, '田所浩二', '12345679765', '沈阳大街卖果汁', 49.5, 2, '等待发货', 0.1, '暂无', 5, '荒天帝', '14758964527', NULL, NULL, NULL, 2, 9.8, 0.0);
INSERT INTO `needs` VALUES (31, 1.0, 4.0, 16.0, 2.0, 8.0, 2, '万叶', '18503117049', '往返自然', 27.0, 5.2, '等待发货', 0, '暂无', 5, '荒天帝', '14758964527', NULL, NULL, NULL, 2, 26.0, 31.7);
INSERT INTO `needs` VALUES (32, 2.0, 7.0, 13.0, 2.0, 3.0, 2, '钟离', '15745875264', '天理尝蛐', 22.7, 1.5, '已送达', 1, '暂无', 3, '万叶', '15878924586', NULL, NULL, NULL, 1, 7.5, 0.0);
INSERT INTO `needs` VALUES (33, 2.0, 4.0, 14.0, 2.0, 8.0, 2, '公子', '18524687592', '烧冻鸡翅', 29.4, 2.1, '已送达', 0, '速度很快，包装干净。', 3, '万叶', '15878924586', 5, 5, 5, 1, 10.7, 0.0);
INSERT INTO `needs` VALUES (35, 0.5, 1.5, 6.8, 2.0, 12.0, 2, '李四', '18524687592', 'gkd', 33.7, 3.7, '已送达', 0, '很快啊', 3, '万叶', '15878924586', 5, 5, 5, 1, 18.6, 0.0);

-- ----------------------------
-- Table structure for path
-- ----------------------------
DROP TABLE IF EXISTS `path`;
CREATE TABLE `path`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `route` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `plate_num` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `truck_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `truck_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `driver_id` int NULL DEFAULT NULL,
  `driver_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `driver_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `total_dist` double(4, 1) NULL DEFAULT NULL,
  `curr_dist` double(4, 1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of path
-- ----------------------------
INSERT INTO `path` VALUES (1, '  0 → 32 → 33 → 7 → 35 → 12 → 5 → 20 → 0  ', '粤12355', 'cybertruck', 'truck', 3, '万叶', '15878924586', 46.4, 21.3);
INSERT INTO `path` VALUES (2, '  0 → 30 → 15 → 29 → 31 → 0  ', '宁15784', 'cybertruck', 'turck', 5, '荒天帝', '14758964527', 36.7, 0.0);
INSERT INTO `path` VALUES (3, '  0 → 28 → 0  ', '晋25874', 'cybertruck', 'truck', 8, '老八', '15748523658', 3.2, NULL);
INSERT INTO `path` VALUES (5, '  0 → 13 → 21 → 22 → 17 → 14 → 0  ', '桂57486', 'cybertruck', 'truck', 9, '顶针', '18457925684', 49.9, NULL);
INSERT INTO `path` VALUES (6, '  0 → 4 → 1 → 2 → 8 → 6 → 0  ', '桂15784', 'truck', 'suv', 15, '夜兰', '12547896547', 36.6, 54.9);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `net_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `phone_numb` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_name`(`user_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'zhangsan', '123', '管理员', '公子', '17158964752');
INSERT INTO `user` VALUES (2, 'lisi', '456', '客户', '李四', '18524687592');
INSERT INTO `user` VALUES (3, 'wangwu', '789', '配送员', '万叶', '15878924586');
INSERT INTO `user` VALUES (4, 'zhaoliu', '159', '客户', '钟离', '17748236458');
INSERT INTO `user` VALUES (5, 'yidou', '1258963', '配送员', '荒天帝', '14758964527');
INSERT INTO `user` VALUES (6, 'asda', 'sdweqwe', '管理员', '北斗', '18642456456');
INSERT INTO `user` VALUES (8, 'asdaqw', 'sdweqwe', '配送员', '老八', '15748523658');
INSERT INTO `user` VALUES (9, 'asdqw', 'asd', '配送员', '顶针', '18457925684');
INSERT INTO `user` VALUES (11, 'asd', 'asd', '客户', '李四', '15478265925');
INSERT INTO `user` VALUES (13, 'asdqwasd', 'asd', '客户', '王富贵', '14725487695');
INSERT INTO `user` VALUES (14, 'qwrxsa', 'asd', '客户', '赵公明', '15436258546');
INSERT INTO `user` VALUES (15, 'yelan', '145236', '配送员', '夜兰', '12547896547');

SET FOREIGN_KEY_CHECKS = 1;
