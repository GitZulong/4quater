/*
Navicat MySQL Data Transfer

Source Server         : 117.50.96.111
Source Server Version : 50727
Source Host           : 117.50.96.111:3306
Source Database       : weeklyplan

Target Server Type    : MYSQL
Target Server Version : 50727
File Encoding         : 65001

Date: 2019-09-18 11:33:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `ID` varchar(32) NOT NULL COMMENT '主键',
  `ORGAN_ID` varchar(32) NOT NULL COMMENT '机构号',
  `DEPT_ID` varchar(32) NOT NULL COMMENT '部门',
  `USERNAME` varchar(200) NOT NULL COMMENT '登录名',
  `PASSWORD` varchar(200) NOT NULL COMMENT '密码',
  `NO` varchar(200) DEFAULT NULL COMMENT '工号',
  `NAME` varchar(200) NOT NULL COMMENT '姓名',
  `EMAIL` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `PHONE` varchar(200) DEFAULT NULL COMMENT '电话',
  `MOBILE` varchar(200) DEFAULT NULL COMMENT '手机',
  `USER_TYPE` varchar(1) DEFAULT NULL COMMENT '用户类型',
  `LOGIN_IP` varchar(200) DEFAULT NULL COMMENT '登录IP',
  `LOGIN_DATE` datetime DEFAULT NULL COMMENT '登录日期',
  `CREATE_BY` varchar(32) DEFAULT NULL COMMENT '创建人',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` varchar(32) DEFAULT NULL COMMENT '最近修改人',
  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '最近修改时间',
  `REMARKS` varchar(200) DEFAULT NULL COMMENT '描述',
  `DEL_FLAG` varchar(1) DEFAULT '0' COMMENT '逻辑删除标记(0.正常，1.删除)',
  `STATUS` varchar(1) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '999999', '6', 'admin', '1f82c942befda29b6ed487a51da199f78fce7f05', '1', '超级管理员', '', '', '18612790818', '1', '0:0:0:0:0:0:0:1', '2019-06-18 17:30:00', '1', '2017-12-02 21:49:00', '1', '2019-07-04 13:56:00', '管理员', '0', '0');
INSERT INTO `sys_user` VALUES ('99999901', '999999', '6', 'BaiKaiShui', '1f82c942befda29b6ed487a51da199f78fce7f05', '2', '陈威', '', '', '', '2', '', null, '1', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999902', '999999', '6', 'WanZhang', '1f82c942befda29b6ed487a51da199f78fce7f05', '3', '樊献泽', '', '', '17665443877', '2', '', null, '1', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999903', '999999', '6', 'FuHao', '1f82c942befda29b6ed487a51da199f78fce7f05', '4', '付豪', '', '', '17601266609', '2', '', null, '1', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999904', '999999', '6', 'JiDanQing', '1f82c942befda29b6ed487a51da199f78fce7f05', '5', '付卫红', '', '', '', '2', '', null, '1', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999905', '999999', '6', 'AnDong', '1f82c942befda29b6ed487a51da199f78fce7f05', '6', '葛安东', '', '', '18551719468', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999906', '999999', '6', 'GuoLiLi', '1f82c942befda29b6ed487a51da199f78fce7f05', '7', '郭丽利', '', '', '15389710283', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999907', '999999', '6', 'HaoYue', '1f82c942befda29b6ed487a51da199f78fce7f05', '8', '郝越', '', '', '15771381991', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999908', '999999', '6', 'HuangWanJie', '1f82c942befda29b6ed487a51da199f78fce7f05', '9', '黄琬洁', '', '', '13770650059', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999909', '999999', '6', '44', '1f82c942befda29b6ed487a51da199f78fce7f05', '10', 'jack1', '', '', '13986086999', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999910', '999999', '6', 'JiaGuiLin', '1f82c942befda29b6ed487a51da199f78fce7f05', '11', '贾桂林', '', '', '15847108608', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999911', '999999', '6', 'Liangh', '1f82c942befda29b6ed487a51da199f78fce7f05', '12', '梁hong', '', '', '13611119303', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999912', '999999', '6', 'YiJinTianDeMingYi', '1f82c942befda29b6ed487a51da199f78fce7f05', '13', '梁正', '', '', '13910128415', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999913', '999999', '6', 'LiGuangWei', '1f82c942befda29b6ed487a51da199f78fce7f05', '14', '李广威', '', '', '15910611584', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999914', '999999', '6', 'LiJia', '1f82c942befda29b6ed487a51da199f78fce7f05', '15', '李佳', '', '', '13404813106', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999915', '999999', '6', 'XingYun', '1f82c942befda29b6ed487a51da199f78fce7f05', '16', '凌云', '', '', '13051860888', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999916', '999999', '6', 'jax', '1f82c942befda29b6ed487a51da199f78fce7f05', '17', '刘宾', '', '', '15639208022', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999917', '999999', '6', 'wind', '1f82c942befda29b6ed487a51da199f78fce7f05', '18', '刘维彪', '', '', '18566278383', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999918', '999999', '6', 'chris', '1f82c942befda29b6ed487a51da199f78fce7f05', '19', '刘兴菊', '', '', '17701295520', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999919', '999999', '6', 'LiuYan', '1f82c942befda29b6ed487a51da199f78fce7f05', '20', '刘岩', '', '', '13911713361', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999920', '999999', '6', 'LiYaNan', '1f82c942befda29b6ed487a51da199f78fce7f05', '21', '李亚楠', '', '', '13936568067', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999921', '999999', '6', 'MaKeBoCai', '1f82c942befda29b6ed487a51da199f78fce7f05', '22', '马可菠菜', '', '', '', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999922', '999999', '6', 'MeiMei', '1f82c942befda29b6ed487a51da199f78fce7f05', '23', '梅梅', '', '', '18600696734', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999923', '999999', '6', 'kevinDianqu', '1f82c942befda29b6ed487a51da199f78fce7f05', '24', '曲文凯', '', '', '', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999924', '999999', '6', 'ireancezhang', '1f82c942befda29b6ed487a51da199f78fce7f05', '25', '上海-张轶', '', '', '13564108897', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999925', '999999', '6', 'WuYu', '1f82c942befda29b6ed487a51da199f78fce7f05', '26', '施文昌', '', '', '', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999926', '999999', '6', 'SuDaJie', '1f82c942befda29b6ed487a51da199f78fce7f05', '27', '苏大洁', '', '', '', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999927', '999999', '6', 'ManMuShanHe', '1f82c942befda29b6ed487a51da199f78fce7f05', '28', '孙博虎', '', '', '17671455329', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999928', '999999', '6', 'TangXiaoDou', '1f82c942befda29b6ed487a51da199f78fce7f05', '29', '唐玮', '', '', '17751450837', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999929', '999999', '6', 'test', '1f82c942befda29b6ed487a51da199f78fce7f05', '30', 'test', '', '', '18612513229', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999930', '999999', '6', 'mr.tWang', '1f82c942befda29b6ed487a51da199f78fce7f05', '31', '王超', '', '', '18393919016', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999931', '999999', '6', 'EnXin', '1f82c942befda29b6ed487a51da199f78fce7f05', '32', '汪琪', '', '', '', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999932', '999999', '6', 'SiBaDaKeSi', '1f82c942befda29b6ed487a51da199f78fce7f05', '33', '汪伟', '', '', '15951916842', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999933', '999999', '6', 'cd471dcd5f33864e51ea230f37437568', '1f82c942befda29b6ed487a51da199f78fce7f05', '34', '王勇', '', '', '', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999934', '999999', '6', 'WuDianHui', '1f82c942befda29b6ed487a51da199f78fce7f05', '35', '武殿辉', '', '', '13104342520', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999935', '999999', '6', 'WuErLang', '1f82c942befda29b6ed487a51da199f78fce7f05', '36', '武文袈', '', '', '', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999936', '999999', '6', 'MiaoXiaoJie', '1f82c942befda29b6ed487a51da199f78fce7f05', '37', '谢婷婷', '', '', '18811462260', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999937', '999999', '6', 'XingWei', '1f82c942befda29b6ed487a51da199f78fce7f05', '38', '邢威', '', '', '18604715235', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999938', '999999', '6', 'Dong', '1f82c942befda29b6ed487a51da199f78fce7f05', '39', '新疆_岳东', '', '', '18129206393', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999939', '999999', '6', 'XuJunWei', '1f82c942befda29b6ed487a51da199f78fce7f05', '40', '许俊伟', '', '', '18240967815', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999940', '999999', '6', 'XuSheng', '1f82c942befda29b6ed487a51da199f78fce7f05', '41', '许升', '', '', '13818499058', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999941', '999999', '6', 'YangZhenNing', '1f82c942befda29b6ed487a51da199f78fce7f05', '42', '杨振宁', '', '', '13507655291', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999942', '999999', '6', 'YaoXu', '1f82c942befda29b6ed487a51da199f78fce7f05', '43', '姚旭', '', '', '18001355716', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999943', '999999', '6', 'YuePengFei', '1f82c942befda29b6ed487a51da199f78fce7f05', '44', '岳鹏飞', '', '', '17316209389', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999944', '999999', '6', 'HaiQiang', '1f82c942befda29b6ed487a51da199f78fce7f05', '45', '张海强', '', '', '13322449884', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999945', '999999', '6', 'ZhangHui', '1f82c942befda29b6ed487a51da199f78fce7f05', '46', '张慧', '', '', '13311271378', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999946', '999999', '6', '4b9d4f159091ba93c538ee6e771b6d89', '1f82c942befda29b6ed487a51da199f78fce7f05', '47', '张学红', '', '', '13693010401', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999947', '999999', '6', 'ZhaoErXi', '1f82c942befda29b6ed487a51da199f78fce7f05', '48', '赵二喜', '', '', '18515590957', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999948', '999999', '6', 'ZhaoQingXin', '1f82c942befda29b6ed487a51da199f78fce7f05', '49', '赵庆新', '', '', '', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999949', '999999', '6', 'KeDaduck', '1f82c942befda29b6ed487a51da199f78fce7f05', '50', '赵晓瑞', '', '', '13770322303', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999950', '999999', '6', 'ZhaoZheng', '1f82c942befda29b6ed487a51da199f78fce7f05', '51', '赵正', '', '', '17551012117', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999951', '999999', '6', 'ZhaoZuLong', '1f82c942befda29b6ed487a51da199f78fce7f05', '52', '赵祖龙', '', '', '18612790818', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999952', '999999', '6', 'Xiao', '1f82c942befda29b6ed487a51da199f78fce7f05', '53', '陈丽娥', '', '', '15805170693', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999953', '999999', '6', 'FengE', '1f82c942befda29b6ed487a51da199f78fce7f05', '54', '葛风娥', '', '', '18970388380', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999954', '999999', '6', 'LiuFei', '1f82c942befda29b6ed487a51da199f78fce7f05', '55', '刘飞', '', '', '18379133956', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999955', '999999', '6', 'bechalin', '1f82c942befda29b6ed487a51da199f78fce7f05', '56', '许春林', '', '', '13960850689', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999956', '999999', '6', 'YangYang', '1f82c942befda29b6ed487a51da199f78fce7f05', '57', '杨华', '', '', '15179168525', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999957', '999999', '6', 'ZhouSanGe', '1f82c942befda29b6ed487a51da199f78fce7f05', '58', '周志勇', '', '', '18907968151', '2', '', null, '', '2019-09-03 11:38:00', '', '2019-09-03 11:38:00', '微信用户账号', '0', '1');
INSERT INTO `sys_user` VALUES ('99999958', '999999', '6', 'DeYing', '1f82c942befda29b6ed487a51da199f78fce7f05', '58', '得英', null, null, '18600113210', null, null, null, null, null, null, null, null, '0', null);
