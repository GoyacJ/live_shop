-- =============================================================
-- live_mall_admin 数据库初始化 —— 01_schema.sql
-- 由 script/init/tools/gen_schema.py 从实体类 + script/snowy.sql 生成
-- 表数量：79
-- 适用：MySQL 5.7 / 8.0，字符集 utf8mb4
-- 说明：导出中已有的表保留原始列定义（真实建表结果），
--       其余表按实体推导；推导明细见 REPORT.md
-- =============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- auth_third_user  
-- ----------------------------
DROP TABLE IF EXISTS `auth_third_user`;
CREATE TABLE `auth_third_user` (
  `ID` varchar(20) NOT NULL,
  `THIRD_ID` varchar(255) NULL COMMENT '三方用户id',
  `USER_ID` varchar(20) NULL COMMENT '系统用户id',
  `AVATAR` longtext NULL COMMENT '头像',
  `NAME` varchar(255) NULL COMMENT '姓名',
  `NICKNAME` varchar(255) NULL COMMENT '昵称',
  `GENDER` varchar(255) NULL COMMENT '性别',
  `CATEGORY` varchar(255) NULL COMMENT '分类',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_auth_third_user_third_id` (`THIRD_ID`),
  KEY `idx_auth_third_user_user_id` (`USER_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- biz_notice  
-- ----------------------------
DROP TABLE IF EXISTS `biz_notice`;
CREATE TABLE `biz_notice` (
  `ID` varchar(20) NOT NULL COMMENT '主键',
  `TITLE` varchar(200) NULL COMMENT '标题',
  `IMAGE` varchar(200) NULL COMMENT '封面图',
  `CONTENT` text NULL COMMENT '内容',
  `DIGEST` varchar(300) NULL COMMENT '摘要',
  `TYPE` varchar(50) NULL COMMENT '类型',
  `PLACE` varchar(50) NULL COMMENT '发布位置',
  `STATUS` varchar(10) NULL COMMENT '状态',
  `SORT_CODE` int(11) NULL COMMENT '排序',
  `REMARK` varchar(500) NULL COMMENT '备注',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新用户',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- client_relation  
-- ----------------------------
DROP TABLE IF EXISTS `client_relation`;
CREATE TABLE `client_relation` (
  `ID` varchar(20) NOT NULL,
  `OBJECT_ID` varchar(255) NULL COMMENT '对象ID',
  `TARGET_ID` varchar(255) NULL COMMENT '目标ID',
  `CATEGORY` varchar(255) NULL COMMENT '分类',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_client_relation_object_id` (`OBJECT_ID`),
  KEY `idx_client_relation_target_id` (`TARGET_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- client_user  
-- ----------------------------
DROP TABLE IF EXISTS `client_user`;
CREATE TABLE `client_user` (
  `ID` varchar(20) NOT NULL,
  `AVATAR` longtext NULL COMMENT '头像',
  `SIGNATURE` longtext NULL COMMENT '签名',
  `ACCOUNT` varchar(255) NULL COMMENT '账号',
  `PASSWORD` varchar(255) NULL COMMENT '密码',
  `NAME` varchar(255) NULL COMMENT '姓名',
  `NICKNAME` varchar(255) NULL COMMENT '昵称',
  `GENDER` varchar(255) NULL COMMENT '性别',
  `AGE` varchar(255) NULL COMMENT '年龄',
  `BIRTHDAY` varchar(255) NULL COMMENT '出生日期',
  `NATION` varchar(255) NULL COMMENT '民族',
  `NATIVE_PLACE` varchar(255) NULL COMMENT '籍贯',
  `HOME_ADDRESS` text NULL COMMENT '家庭住址',
  `MAILING_ADDRESS` text NULL COMMENT '通信地址',
  `ID_CARD_TYPE` varchar(255) NULL COMMENT '证件类型',
  `ID_CARD_NUMBER` varchar(255) NULL COMMENT '证件号码',
  `CULTURE_LEVEL` varchar(255) NULL COMMENT '文化程度',
  `POLITICAL_OUTLOOK` varchar(255) NULL COMMENT '政治面貌',
  `COLLEGE` varchar(255) NULL COMMENT '毕业院校',
  `EDUCATION` varchar(255) NULL COMMENT '学历',
  `EDU_LENGTH` varchar(255) NULL COMMENT '学制',
  `DEGREE` varchar(255) NULL COMMENT '学位',
  `PHONE` varchar(255) NULL COMMENT '手机',
  `EMAIL` varchar(255) NULL COMMENT '邮箱',
  `HOME_TEL` varchar(255) NULL COMMENT '家庭电话',
  `OFFICE_TEL` varchar(255) NULL COMMENT '办公电话',
  `EMERGENCY_CONTACT` varchar(255) NULL COMMENT '紧急联系人',
  `EMERGENCY_PHONE` varchar(255) NULL COMMENT '紧急联系人电话',
  `EMERGENCY_ADDRESS` text NULL COMMENT '紧急联系人地址',
  `LAST_LOGIN_IP` varchar(255) NULL COMMENT '上次登录ip',
  `LAST_LOGIN_ADDRESS` varchar(255) NULL COMMENT '上次登录地点',
  `LAST_LOGIN_TIME` datetime NULL COMMENT '上次登录时间',
  `LAST_LOGIN_DEVICE` text NULL COMMENT '上次登录设备',
  `LATEST_LOGIN_IP` varchar(255) NULL COMMENT '最新登录ip',
  `LATEST_LOGIN_ADDRESS` varchar(255) NULL COMMENT '最新登录地点',
  `LATEST_LOGIN_TIME` datetime NULL COMMENT '最新登录时间',
  `LATEST_LOGIN_DEVICE` text NULL COMMENT '最新登录设备',
  `USER_STATUS` varchar(255) NULL COMMENT '用户状态',
  `SORT_CODE` int(11) NULL COMMENT '排序码',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_client_user_account` (`ACCOUNT`),
  KEY `idx_client_user_phone` (`PHONE`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- dev_config  
-- ----------------------------
DROP TABLE IF EXISTS `dev_config`;
CREATE TABLE `dev_config` (
  `ID` varchar(20) NOT NULL,
  `CONFIG_KEY` varchar(255) NULL COMMENT '配置键',
  `CONFIG_VALUE` longtext NULL COMMENT '配置值',
  `CATEGORY` varchar(255) NULL COMMENT '分类',
  `REMARK` varchar(255) NULL COMMENT '备注',
  `SORT_CODE` int(11) NULL COMMENT '排序码',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- dev_dict  
-- ----------------------------
DROP TABLE IF EXISTS `dev_dict`;
CREATE TABLE `dev_dict` (
  `ID` varchar(20) NOT NULL,
  `PARENT_ID` varchar(20) NULL COMMENT '父id',
  `DICT_LABEL` varchar(255) NULL COMMENT '字典文字',
  `DICT_VALUE` varchar(255) NULL COMMENT '字典值',
  `CATEGORY` varchar(255) NULL COMMENT '分类',
  `SORT_CODE` int(11) NULL COMMENT '排序码',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_dev_dict_parent_id` (`PARENT_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- dev_email  
-- ----------------------------
DROP TABLE IF EXISTS `dev_email`;
CREATE TABLE `dev_email` (
  `ID` varchar(20) NOT NULL,
  `ENGINE` varchar(255) NULL COMMENT '邮件引擎',
  `SEND_ACCOUNT` varchar(255) NULL COMMENT '发件人邮箱',
  `SEND_USER` varchar(255) NULL COMMENT '发件人昵称',
  `RECEIVE_ACCOUNTS` text NULL COMMENT '接收人',
  `SUBJECT` text NULL COMMENT '邮件主题',
  `CONTENT` longtext NULL COMMENT '邮件正文',
  `TAG_NAME` varchar(255) NULL COMMENT '标签名',
  `TEMPLATE_NAME` text NULL COMMENT '模板名',
  `TEMPLATE_PARAM` text NULL COMMENT '发送参数',
  `RECEIPT_INFO` longtext NULL COMMENT '回执信息',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- dev_file  
-- ----------------------------
DROP TABLE IF EXISTS `dev_file`;
CREATE TABLE `dev_file` (
  `ID` varchar(20) NOT NULL,
  `ENGINE` varchar(255) NULL COMMENT '存储引擎',
  `BUCKET` varchar(255) NULL COMMENT '存储桶',
  `NAME` text NULL COMMENT '文件名称',
  `SUFFIX` varchar(255) NULL COMMENT '文件后缀',
  `SIZE_KB` bigint(20) NULL COMMENT '文件大小kb',
  `SIZE_INFO` varchar(255) NULL COMMENT '文件大小（格式化后）',
  `OBJ_NAME` text NULL COMMENT '文件的对象名（唯一名称）',
  `STORAGE_PATH` text NULL COMMENT '文件存储路径',
  `DOWNLOAD_PATH` text NULL COMMENT '文件下载路径',
  `THUMBNAIL` longtext NULL COMMENT '图片缩略图',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- dev_job  
-- ----------------------------
DROP TABLE IF EXISTS `dev_job`;
CREATE TABLE `dev_job` (
  `ID` varchar(20) NOT NULL,
  `NAME` varchar(255) NULL COMMENT '名称',
  `CODE` varchar(255) NULL COMMENT '编码',
  `CATEGORY` varchar(255) NULL COMMENT '分类',
  `ACTION_CLASS` varchar(255) NULL COMMENT '任务类名',
  `CRON_EXPRESSION` varchar(255) NULL COMMENT 'cron表达式',
  `JOB_STATUS` varchar(255) NULL COMMENT '任务状态',
  `SORT_CODE` int(11) NULL COMMENT '排序码',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_dev_job_code` (`CODE`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- dev_log  
-- ----------------------------
DROP TABLE IF EXISTS `dev_log`;
CREATE TABLE `dev_log` (
  `ID` varchar(20) NOT NULL COMMENT '主键',
  `CATEGORY` varchar(255) NULL COMMENT '日志分类',
  `NAME` varchar(255) NULL COMMENT '日志名称',
  `EXE_STATUS` varchar(255) NULL COMMENT '执行状态',
  `EXE_MESSAGE` longtext NULL COMMENT '具体消息',
  `OP_IP` varchar(255) NULL COMMENT '操作ip',
  `OP_ADDRESS` varchar(255) NULL COMMENT '操作地址',
  `OP_BROWSER` varchar(255) NULL COMMENT '操作浏览器',
  `OP_OS` varchar(255) NULL COMMENT '操作系统',
  `CLASS_NAME` varchar(255) NULL COMMENT '类名称',
  `METHOD_NAME` varchar(255) NULL COMMENT '方法名称',
  `REQ_METHOD` varchar(255) NULL COMMENT '请求方式',
  `REQ_URL` longtext NULL COMMENT '请求地址',
  `PARAM_JSON` longtext NULL COMMENT '请求参数',
  `RESULT_JSON` longtext NULL COMMENT '返回结果',
  `OP_TIME` datetime NULL COMMENT '操作时间',
  `OP_USER` varchar(255) NULL COMMENT '操作人姓名',
  `SIGN_DATA` longtext NULL COMMENT '签名数据',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- dev_message  
-- ----------------------------
DROP TABLE IF EXISTS `dev_message`;
CREATE TABLE `dev_message` (
  `ID` varchar(20) NOT NULL,
  `CATEGORY` varchar(255) NULL COMMENT '分类',
  `SUBJECT` text NULL COMMENT '主题',
  `CONTENT` longtext NULL COMMENT '正文',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- dev_relation  
-- ----------------------------
DROP TABLE IF EXISTS `dev_relation`;
CREATE TABLE `dev_relation` (
  `ID` varchar(20) NOT NULL,
  `OBJECT_ID` varchar(255) NULL COMMENT '对象ID',
  `TARGET_ID` varchar(255) NULL COMMENT '目标ID',
  `CATEGORY` varchar(255) NULL COMMENT '分类',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_dev_relation_object_id` (`OBJECT_ID`),
  KEY `idx_dev_relation_target_id` (`TARGET_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- dev_slideshow  
-- ----------------------------
DROP TABLE IF EXISTS `dev_slideshow`;
CREATE TABLE `dev_slideshow` (
  `ID` varchar(20) NOT NULL COMMENT '主键',
  `TITLE` varchar(200) NULL COMMENT '标题',
  `PLACE` varchar(500) NULL COMMENT '展示位置',
  `IMAGE` varchar(200) NULL COMMENT '图片',
  `PATH_DETAILS` varchar(500) NULL COMMENT '路径详情',
  `STATUS` varchar(10) NULL COMMENT '状态',
  `SORT_CODE` int(11) NULL COMMENT '排序',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新用户',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- dev_sms  
-- ----------------------------
DROP TABLE IF EXISTS `dev_sms`;
CREATE TABLE `dev_sms` (
  `ID` varchar(20) NOT NULL,
  `ENGINE` varchar(255) NULL COMMENT '短信引擎',
  `PHONE_NUMBERS` text NULL COMMENT '手机号',
  `SIGN_NAME` text NULL COMMENT '短信签名',
  `TEMPLATE_CODE` text NULL COMMENT '模板编码',
  `TEMPLATE_PARAM` text NULL COMMENT '发送参数',
  `RECEIPT_INFO` longtext NULL COMMENT '回执信息',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- gen_basic  
-- ----------------------------
DROP TABLE IF EXISTS `gen_basic`;
CREATE TABLE `gen_basic` (
  `ID` varchar(20) NOT NULL COMMENT '主键',
  `DB_TABLE` varchar(255) NULL COMMENT '主表',
  `DB_TABLE_KEY` varchar(255) NULL COMMENT '主表主键',
  `PLUGIN_NAME` varchar(255) NULL COMMENT '插件名',
  `MODULE_NAME` varchar(255) NULL COMMENT '模块名',
  `TABLE_PREFIX` varchar(255) NULL COMMENT '移除表前缀',
  `GENERATE_TYPE` varchar(255) NULL COMMENT '生成方式',
  `MODULE` varchar(255) NULL COMMENT '所属模块',
  `MENU_PID` varchar(255) NULL COMMENT '上级目录',
  `MOBILE_MODULE` varchar(255) NULL COMMENT '移动端所属模块',
  `FUNCTION_NAME` varchar(255) NULL COMMENT '功能名',
  `BUS_NAME` varchar(255) NULL COMMENT '业务名',
  `CLASS_NAME` varchar(255) NULL COMMENT '类名',
  `FORM_LAYOUT` varchar(255) NULL COMMENT '表单布局',
  `GRID_WHETHER` varchar(255) NULL COMMENT '使用栅格',
  `PACKAGE_NAME` varchar(255) NULL COMMENT '包名',
  `AUTHOR_NAME` varchar(255) NULL COMMENT '作者',
  `SORT_CODE` int(11) NULL COMMENT '排序',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- gen_config  
-- ----------------------------
DROP TABLE IF EXISTS `gen_config`;
CREATE TABLE `gen_config` (
  `ID` varchar(20) NOT NULL COMMENT '主键',
  `BASIC_ID` varchar(20) NULL COMMENT '基础ID',
  `IS_TABLE_KEY` varchar(255) NULL COMMENT '是否主键',
  `FIELD_NAME` varchar(255) NULL COMMENT '字段',
  `FIELD_REMARK` varchar(255) NULL COMMENT '名称',
  `FIELD_TYPE` varchar(255) NULL COMMENT '类型',
  `FIELD_JAVA_TYPE` varchar(255) NULL COMMENT '实体类型',
  `EFFECT_TYPE` varchar(255) NULL COMMENT '作用类型',
  `DICT_TYPE_CODE` varchar(255) NULL COMMENT '字典',
  `WHETHER_TABLE` varchar(255) NULL COMMENT '列表显示',
  `WHETHER_RETRACT` varchar(255) NULL COMMENT '列省略',
  `WHETHER_ADD_UPDATE` varchar(255) NULL COMMENT '是否增改',
  `WHETHER_REQUIRED` varchar(255) NULL COMMENT '必填',
  `QUERY_WHETHER` varchar(255) NULL COMMENT '查询',
  `QUERY_TYPE` varchar(255) NULL COMMENT '查询方式',
  `SORT_CODE` int(11) NULL COMMENT '排序',
  `DELETE_FLAG` varchar(20) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_gen_config_basic_id` (`BASIC_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_goods  
-- ----------------------------
DROP TABLE IF EXISTS `mall_goods`;
CREATE TABLE `mall_goods` (
  `ID` varchar(20) NOT NULL,
  `TITLE` varchar(150) NOT NULL DEFAULT '' COMMENT '商品标题',
  `DETAIL` text NULL COMMENT '商品详情',
  `COST_PRICE` decimal(12, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '成本价',
  `PRICE` decimal(12, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '售价',
  `SORT` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序，从大到小',
  `MERCHANT` varchar(255) NOT NULL DEFAULT '' COMMENT '商品所属商家',
  `GIFT_POINTS` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '赠送的积分',
  `SPREAD_GIFT` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '推广奖励积分',
  `DESCRIPTION` varchar(255) NOT NULL DEFAULT '' COMMENT '商品简述',
  `STOCK` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '库存',
  `LOW_STOCK` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '库存预警值',
  `SALES` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '销量',
  `VIRTUAL_SALES` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '虚拟销量',
  `STATUS` char(1) NOT NULL DEFAULT '' COMMENT '状态，1下架，2上架',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  `HOST_IMAGE` varchar(255) NULL COMMENT '主图',
  `CAROUSE_IMAGE` varchar(255) NULL COMMENT '轮播图',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_goods_slide  
-- ----------------------------
DROP TABLE IF EXISTS `mall_goods_slide`;
CREATE TABLE `mall_goods_slide` (
  `ID` varchar(20) NOT NULL,
  `GOODS_ID` varchar(20) NOT NULL DEFAULT '' COMMENT '商品id',
  `URL` varchar(255) NOT NULL DEFAULT '' COMMENT '商品图片链接',
  `TYPE` char(1) NOT NULL DEFAULT '' COMMENT '类型，1=图片/2=视频',
  `SORT` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序，从大到小',
  `STATUS` char(1) NOT NULL DEFAULT '' COMMENT '状态，1=启用/2=禁用',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_mall_goods_slide_goods_id` (`GOODS_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mobile_resource  
-- ----------------------------
DROP TABLE IF EXISTS `mobile_resource`;
CREATE TABLE `mobile_resource` (
  `ID` varchar(20) NOT NULL COMMENT '主键',
  `PARENT_ID` varchar(255) NULL COMMENT '父ID',
  `TITLE` varchar(255) NULL COMMENT '名称',
  `CODE` varchar(255) NULL COMMENT '编码',
  `CATEGORY` varchar(255) NULL COMMENT '分类',
  `MODULE` varchar(255) NULL COMMENT '模块',
  `MENU_TYPE` varchar(255) NULL COMMENT '菜单类型',
  `PATH` varchar(255) NULL COMMENT '路径',
  `ICON` varchar(255) NULL COMMENT '图标',
  `COLOR` varchar(255) NULL COMMENT '颜色',
  `REG_TYPE` varchar(255) NULL COMMENT '规则类型',
  `STATUS` varchar(255) NULL COMMENT '可用状态',
  `SORT_CODE` int(11) NULL COMMENT '排序码',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_mobile_resource_parent_id` (`PARENT_ID`),
  KEY `idx_mobile_resource_code` (`CODE`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- sys_group  
-- ----------------------------
DROP TABLE IF EXISTS `sys_group`;
CREATE TABLE `sys_group` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `NAME` varchar(200) NOT NULL COMMENT '名称',
  `REMARK` varchar(500) NULL COMMENT '备注',
  `SORT_CODE` int(11) NULL COMMENT '排序码',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- sys_org  
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org` (
  `ID` varchar(20) NOT NULL,
  `PARENT_ID` varchar(20) NULL COMMENT '父id',
  `DIRECTOR_ID` varchar(20) NULL COMMENT '主管ID',
  `NAME` varchar(255) NULL COMMENT '名称',
  `CODE` varchar(255) NULL COMMENT '编码',
  `CATEGORY` varchar(255) NULL COMMENT '分类',
  `SORT_CODE` int(11) NULL COMMENT '排序码',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_sys_org_parent_id` (`PARENT_ID`),
  KEY `idx_sys_org_director_id` (`DIRECTOR_ID`),
  KEY `idx_sys_org_code` (`CODE`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- sys_position  
-- ----------------------------
DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position` (
  `ID` varchar(20) NOT NULL,
  `ORG_ID` varchar(20) NULL COMMENT '组织id',
  `NAME` varchar(255) NULL COMMENT '名称',
  `CODE` varchar(255) NULL COMMENT '编码',
  `CATEGORY` varchar(255) NULL COMMENT '分类',
  `SORT_CODE` int(11) NULL COMMENT '排序码',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_sys_position_org_id` (`ORG_ID`),
  KEY `idx_sys_position_code` (`CODE`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- sys_relation  
-- ----------------------------
DROP TABLE IF EXISTS `sys_relation`;
CREATE TABLE `sys_relation` (
  `ID` varchar(20) NOT NULL,
  `OBJECT_ID` varchar(255) NULL COMMENT '对象ID',
  `TARGET_ID` varchar(255) NULL COMMENT '目标ID',
  `CATEGORY` varchar(255) NULL COMMENT '分类',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_sys_relation_object_id` (`OBJECT_ID`),
  KEY `idx_sys_relation_target_id` (`TARGET_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- sys_resource  
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `ID` varchar(20) NOT NULL,
  `PARENT_ID` varchar(255) NULL COMMENT '父id',
  `TITLE` varchar(255) NULL COMMENT '标题',
  `NAME` varchar(255) NULL COMMENT '别名',
  `CODE` varchar(255) NULL COMMENT '编码',
  `CATEGORY` varchar(255) NULL COMMENT '分类',
  `MODULE` varchar(255) NULL COMMENT '模块',
  `MENU_TYPE` varchar(255) NULL COMMENT '菜单类型',
  `PATH` text NULL COMMENT '路径',
  `COMPONENT` varchar(255) NULL COMMENT '组件',
  `ICON` varchar(255) NULL COMMENT '图标',
  `COLOR` varchar(255) NULL COMMENT '颜色',
  `VISIBLE` varchar(255) NULL COMMENT '是否可见',
  `SORT_CODE` int(11) NULL COMMENT '排序码',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_sys_resource_parent_id` (`PARENT_ID`),
  KEY `idx_sys_resource_code` (`CODE`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- sys_role  
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `ID` varchar(20) NOT NULL,
  `ORG_ID` varchar(20) NULL COMMENT '组织id',
  `NAME` varchar(255) NULL COMMENT '名称',
  `CODE` varchar(255) NULL COMMENT '编码',
  `CATEGORY` varchar(255) NULL COMMENT '分类',
  `SORT_CODE` int(11) NULL COMMENT '排序码',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_sys_role_org_id` (`ORG_ID`),
  KEY `idx_sys_role_code` (`CODE`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- sys_user  
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `ID` varchar(20) NOT NULL,
  `AVATAR` longtext NULL COMMENT '头像',
  `SIGNATURE` longtext NULL COMMENT '签名',
  `ACCOUNT` varchar(255) NULL COMMENT '账号',
  `PASSWORD` varchar(255) NULL COMMENT '密码',
  `NAME` varchar(255) NULL COMMENT '姓名',
  `NICKNAME` varchar(255) NULL COMMENT '昵称',
  `GENDER` varchar(255) NULL COMMENT '性别',
  `AGE` varchar(255) NULL COMMENT '年龄',
  `BIRTHDAY` varchar(255) NULL COMMENT '出生日期',
  `NATION` varchar(255) NULL COMMENT '民族',
  `NATIVE_PLACE` varchar(255) NULL COMMENT '籍贯',
  `HOME_ADDRESS` text NULL COMMENT '家庭住址',
  `MAILING_ADDRESS` text NULL COMMENT '通信地址',
  `ID_CARD_TYPE` varchar(255) NULL COMMENT '证件类型',
  `ID_CARD_NUMBER` varchar(255) NULL COMMENT '证件号码',
  `CULTURE_LEVEL` varchar(255) NULL COMMENT '文化程度',
  `POLITICAL_OUTLOOK` varchar(255) NULL COMMENT '政治面貌',
  `COLLEGE` varchar(255) NULL COMMENT '毕业院校',
  `EDUCATION` varchar(255) NULL COMMENT '学历',
  `EDU_LENGTH` varchar(255) NULL COMMENT '学制',
  `DEGREE` varchar(255) NULL COMMENT '学位',
  `PHONE` varchar(255) NULL COMMENT '手机',
  `EMAIL` varchar(255) NULL COMMENT '邮箱',
  `HOME_TEL` varchar(255) NULL COMMENT '家庭电话',
  `OFFICE_TEL` varchar(255) NULL COMMENT '办公电话',
  `EMERGENCY_CONTACT` varchar(255) NULL COMMENT '紧急联系人',
  `EMERGENCY_PHONE` varchar(255) NULL COMMENT '紧急联系人电话',
  `EMERGENCY_ADDRESS` text NULL COMMENT '紧急联系人地址',
  `EMP_NO` varchar(255) NULL COMMENT '员工编号',
  `ENTRY_DATE` varchar(255) NULL COMMENT '入职日期',
  `ORG_ID` varchar(20) NULL COMMENT '机构id',
  `POSITION_ID` varchar(20) NULL COMMENT '职位id',
  `POSITION_LEVEL` varchar(255) NULL COMMENT '职级',
  `DIRECTOR_ID` varchar(20) NULL COMMENT '主管id',
  `POSITION_JSON` longtext NULL COMMENT '兼任信息',
  `LAST_LOGIN_IP` varchar(255) NULL COMMENT '上次登录ip',
  `LAST_LOGIN_ADDRESS` varchar(255) NULL COMMENT '上次登录地点',
  `LAST_LOGIN_TIME` datetime NULL COMMENT '上次登录时间',
  `LAST_LOGIN_DEVICE` text NULL COMMENT '上次登录设备',
  `LATEST_LOGIN_IP` varchar(255) NULL COMMENT '最新登录ip',
  `LATEST_LOGIN_ADDRESS` varchar(255) NULL COMMENT '最新登录地点',
  `LATEST_LOGIN_TIME` datetime NULL COMMENT '最新登录时间',
  `LATEST_LOGIN_DEVICE` text NULL COMMENT '最新登录设备',
  `USER_STATUS` varchar(255) NULL COMMENT '用户状态',
  `SORT_CODE` int(11) NULL COMMENT '排序码',
  `EXT_JSON` longtext NULL COMMENT '扩展信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `uk_sys_user_account` (`ACCOUNT`),
  KEY `idx_sys_user_phone` (`PHONE`),
  KEY `idx_sys_user_org_id` (`ORG_ID`),
  KEY `idx_sys_user_position_id` (`POSITION_ID`),
  KEY `idx_sys_user_director_id` (`DIRECTOR_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- user_address  
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address` (
  `ID` varchar(20) NOT NULL,
  `MOBILE` varchar(11) NOT NULL DEFAULT '' COMMENT '联系方式',
  `NICKNAME` varchar(50) NOT NULL DEFAULT '' COMMENT '收货人昵称',
  `PROVINCE_NAME` varchar(255) NOT NULL DEFAULT '' COMMENT '省份',
  `CITY_NAME` varchar(255) NOT NULL DEFAULT '' COMMENT '市',
  `COUNTIES_NAME` varchar(255) NOT NULL DEFAULT '' COMMENT '区县',
  `ADDRESS` varchar(255) NOT NULL DEFAULT '' COMMENT '详细地址',
  `IS_DEFAULT` char(1) NOT NULL DEFAULT '' COMMENT '是否为默认地址',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '用户id',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `PROVINCE_CODE` varchar(255) NULL COMMENT '省份',
  `CITY_CODE` varchar(255) NULL COMMENT '市',
  `COUNTIES_CODE` varchar(255) NULL COMMENT '区县',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_address_mobile` (`MOBILE`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- user_balance_log  
-- ----------------------------
DROP TABLE IF EXISTS `user_balance_log`;
CREATE TABLE `user_balance_log` (
  `ID` varchar(20) NOT NULL,
  `TYPE` char(1) NOT NULL DEFAULT '' COMMENT '变更类型,1=任务奖励，2=购物奖励3=直播福利,4=直播福袋,5=直播红包,6=积分兑换,7=现金提现,8=积分抵扣',
  `BALANCE_TYPE` char(1) NOT NULL DEFAULT '' COMMENT '余额类型，1=现金,2=冻结现金,3=积分,4=冻结积分',
  `BEFORE_BALANCE` decimal(12, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '变更前',
  `CHANGE_BALANCE` decimal(12, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '变更',
  `AFTER_BALANCE` decimal(12, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '变更后',
  `REASON` varchar(512) NOT NULL DEFAULT '' COMMENT '变更原因',
  `REASON_ID` varchar(20) NOT NULL DEFAULT '' COMMENT '订单id/提现记录id/任务奖励记录id···',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  `USER_ID` varchar(20) NULL,
  `SETTLE_TIME_TYPE` varchar(50) NULL,
  `SETTLE_TIME` datetime NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_balance_log_reason_id` (`REASON_ID`),
  KEY `idx_user_balance_log_user_id` (`USER_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- user_info  
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `ID` varchar(20) NOT NULL,
  `MOBILE` varchar(11) NOT NULL DEFAULT '' COMMENT '手机号',
  `AVATAR` longtext NOT NULL COMMENT '头像',
  `LEVEL_ID` varchar(20) NOT NULL DEFAULT '' COMMENT '等级id',
  `NICKNAME` varchar(255) NOT NULL DEFAULT '' COMMENT '昵称',
  `INTEGRAL` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '积分账户',
  `FROZEN_INTEGRAL` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '冻结积分',
  `CASH` decimal(12, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '现金账户',
  `FROZEN_CASH` decimal(12, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '冻结现金',
  `LAST_LOGIN_IP` varchar(255) NOT NULL DEFAULT '' COMMENT '上次登录ip',
  `LAST_LOGIN_ADDRESS` varchar(255) NOT NULL DEFAULT '' COMMENT '上次登录地点',
  `LAST_LOGIN_TIME` datetime NULL COMMENT '上次登录时间',
  `LAST_LOGIN_DEVICE` text NULL COMMENT '上次登录设备',
  `USER_STATUS` varchar(255) NOT NULL DEFAULT '' COMMENT '用户状态',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  `INTEGRAL_ID` varchar(20) NULL COMMENT '积分账户',
  `RCODE` varchar(255) NULL COMMENT '用户邀请码',
  `P_ID` varchar(20) NULL COMMENT '邀请人id',
  `REGISTER_URL_STATUS` varchar(50) NULL COMMENT '推广注册链接状态',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_info_mobile` (`MOBILE`),
  KEY `idx_user_info_level_id` (`LEVEL_ID`),
  KEY `idx_user_info_integral_id` (`INTEGRAL_ID`),
  KEY `idx_user_info_p_id` (`P_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- user_integral  
-- ----------------------------
DROP TABLE IF EXISTS `user_integral`;
CREATE TABLE `user_integral` (
  `ID` varchar(20) NOT NULL,
  `AMOUNT` decimal(12, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '兑换金额',
  `INTEGRAL` decimal(12, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '兑换积分',
  `REASON` varchar(512) NOT NULL DEFAULT '' COMMENT '审核原因',
  `STATUS` char(1) NOT NULL DEFAULT '' COMMENT '状态，1=待处理,2=已通过,3=拒绝申请',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- user_level  
-- ----------------------------
DROP TABLE IF EXISTS `user_level`;
CREATE TABLE `user_level` (
  `ID` varchar(20) NOT NULL,
  `NAME` varchar(50) NOT NULL DEFAULT '' COMMENT '等级名称',
  `ICON` varchar(255) NOT NULL DEFAULT '' COMMENT '图表',
  `INTEGRAL_CONDITION` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '升级条件-积分',
  `USER_CONDITION` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '升级条件-邀请人数',
  `CONDITION_MODE` char(1) NOT NULL DEFAULT '' COMMENT '升级条件方式',
  `EQUITY_INTRODUCTION` varchar(255) NOT NULL DEFAULT '' COMMENT '权益描述',
  `SORT` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
  `STATUS` char(1) NOT NULL DEFAULT '' COMMENT '状态',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- user_withdrawal_log  
-- ----------------------------
DROP TABLE IF EXISTS `user_withdrawal_log`;
CREATE TABLE `user_withdrawal_log` (
  `ID` varchar(20) NOT NULL,
  `AMOUNT` decimal(12, 2) UNSIGNED NOT NULL DEFAULT 0.00 COMMENT '提现金额',
  `REASON` varchar(512) NOT NULL DEFAULT '' COMMENT '审核原因',
  `STATUS` char(1) NOT NULL DEFAULT '' COMMENT '状态1=待处理,2=已打款,3=拒绝申请',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- live_marketing_campaigns_log  营销活动记录表实体
-- ----------------------------
DROP TABLE IF EXISTS `live_marketing_campaigns_log`;
CREATE TABLE `live_marketing_campaigns_log` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `MARKETING_ID` int NULL COMMENT '活动id',
  `USER_ID` int NULL COMMENT '用户id',
  `MARKETING_TYPE` int NULL COMMENT '营销类型',
  `MARKETING_VALUE` varchar(255) NULL COMMENT '营销价值',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_live_marketing_campaigns_log_marketing_id` (`MARKETING_ID`),
  KEY `idx_live_marketing_campaigns_log_user_id` (`USER_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '营销活动记录表实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- live_spectator_grade  直播观众成长表实体
-- ----------------------------
DROP TABLE IF EXISTS `live_spectator_grade`;
CREATE TABLE `live_spectator_grade` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `SPECTATOR_ID` varchar(20) NULL COMMENT '观众id',
  `ANCHOR_ID` varchar(20) NULL COMMENT '主播id',
  `SPECTATOR_GRADE` int NULL COMMENT '观众等级',
  `GROWTH_VALUE` int NULL COMMENT '成长值',
  `TOTAL_CONSUMPTION` decimal(12,2) NULL COMMENT '总消费',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_live_spectator_grade_spectator_id` (`SPECTATOR_ID`),
  KEY `idx_live_spectator_grade_anchor_id` (`ANCHOR_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '直播观众成长表实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- live_spectator_watching  直播观众观看时间表实体
-- ----------------------------
DROP TABLE IF EXISTS `live_spectator_watching`;
CREATE TABLE `live_spectator_watching` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `USER_ID` varchar(20) NULL COMMENT '观众id',
  `LIVE_ID` varchar(20) NULL COMMENT '直播id',
  `START_TIME` datetime NULL COMMENT '开始时间',
  `END_TIME` datetime NULL COMMENT '结束时间',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_live_spectator_watching_user_id` (`USER_ID`),
  KEY `idx_live_spectator_watching_live_id` (`LIVE_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '直播观众观看时间表实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- live_task_sheet  任务表实体
-- ----------------------------
DROP TABLE IF EXISTS `live_task_sheet`;
CREATE TABLE `live_task_sheet` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `TASK_ID` int NULL COMMENT '任务id',
  `TASK_TYPE` int NULL COMMENT '任务类型',
  `TASK_STATE` int NULL COMMENT '任务状态',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_live_task_sheet_task_id` (`TASK_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务表实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- live_shopping_car  直播间购物车表实体
-- ----------------------------
DROP TABLE IF EXISTS `live_shopping_car`;
CREATE TABLE `live_shopping_car` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `GOODS_ID` varchar(20) NULL,
  `LIVE_ID` varchar(20) NULL COMMENT '直播间id',
  `QUANTITY` int NULL COMMENT '数量',
  `SORT` int NULL COMMENT '排序',
  `YELLOW_BIKE_STATE` int NULL,
  `PRICE` decimal(12,2) NULL COMMENT '价格',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_live_shopping_car_goods_id` (`GOODS_ID`),
  KEY `idx_live_shopping_car_live_id` (`LIVE_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '直播间购物车表实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- live_gift  礼物表实体
-- ----------------------------
DROP TABLE IF EXISTS `live_gift`;
CREATE TABLE `live_gift` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `GIFT_URL` varchar(255) NULL COMMENT '礼物图片url',
  `GIFT_PRICE` decimal(12,2) NULL COMMENT '礼物价格',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '礼物表实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- live_streaming_type  直播类型表实体
-- ----------------------------
DROP TABLE IF EXISTS `live_streaming_type`;
CREATE TABLE `live_streaming_type` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `TITLE` varchar(255) NULL COMMENT '直播类型',
  `DESCRIBE` varchar(255) NULL COMMENT '描述',
  `STATUS` varchar(255) NULL COMMENT '状态',
  `SORT` varchar(255) NULL COMMENT '排序',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '直播类型表实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- live_focus_relationships  关注关系表实体
-- ----------------------------
DROP TABLE IF EXISTS `live_focus_relationships`;
CREATE TABLE `live_focus_relationships` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `FOLLOWED_ID` int NULL COMMENT '被关注者id',
  `FOLLOWERS_ID` int NULL COMMENT '关注者id',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_live_focus_relationships_followed_id` (`FOLLOWED_ID`),
  KEY `idx_live_focus_relationships_followers_id` (`FOLLOWERS_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '关注关系表实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- live_anchor  主播信息表实体
-- ----------------------------
DROP TABLE IF EXISTS `live_anchor`;
CREATE TABLE `live_anchor` (
  `ID` int AUTO_INCREMENT NOT NULL COMMENT 'ID',
  `USERNAME` varchar(255) NULL COMMENT '主播name',
  `REAL_NAME` varchar(255) NULL COMMENT '昵称',
  `ACCOUNT` varchar(100) NULL COMMENT '账号',
  `PASSWORD` varchar(255) NULL COMMENT '密码',
  `PICTURE_URL` varchar(255) NULL COMMENT '头像url',
  `BIO` varchar(255) NULL COMMENT '简介',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_live_anchor_account` (`ACCOUNT`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主播信息表实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- live_marketing_campaigns  营销活动表实体
-- ----------------------------
DROP TABLE IF EXISTS `live_marketing_campaigns`;
CREATE TABLE `live_marketing_campaigns` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `MARKETING_ID` int NULL COMMENT '活动id',
  `LIVE_ID` varchar(20) NULL,
  `MARKETING_TYPE` int NULL COMMENT '营销类型',
  `MARKETING_STATE` int NULL COMMENT '营销状态',
  `MARKETING_VALUE` varchar(255) NULL COMMENT '营销价值',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `NUMBER_WINNERS` int NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_live_marketing_campaigns_marketing_id` (`MARKETING_ID`),
  KEY `idx_live_marketing_campaigns_live_id` (`LIVE_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '营销活动表实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- live_biz  直播列表实体
-- ----------------------------
DROP TABLE IF EXISTS `live_biz`;
CREATE TABLE `live_biz` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `LIVE_BROADCASTS` int NULL COMMENT '直播场次id',
  `ANCHOR_ID` int NULL COMMENT '主播id',
  `SPECTATOR_NUMBER` int NULL COMMENT '观众数量',
  `INGEST_URL` varchar(255) NULL COMMENT '推流url',
  `LIVE_URL` varchar(255) NULL COMMENT '观看url',
  `AVATAR_URL` varchar(255) NULL COMMENT '头像url',
  `COVER_URL` varchar(255) NULL COMMENT '封面url',
  `BACK_URL` varchar(255) NULL COMMENT '回放url',
  `IS_SCREEN_RECORDING` tinyint(1) NULL COMMENT '是否录屏 0录屏 1不录',
  `LIVE_ROOM_STATE` varchar(255) NULL COMMENT '直播间状态 0待开播 1启用 2禁用',
  `MAIN_TITLE` varchar(255) NULL COMMENT '主标题',
  `SUB_TITLE` varchar(255) NULL COMMENT '子标题',
  `LIVE_STATE` varchar(255) NULL COMMENT '直播类型',
  `LIVE_BEGIN_TIME` datetime NULL COMMENT '直播开始时间',
  `LIVE_OVER_TIME` datetime NULL COMMENT '直播结束时间',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_live_biz_anchor_id` (`ANCHOR_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '直播列表实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- task_record  <p>任务</p>
-- ----------------------------
DROP TABLE IF EXISTS `task_record`;
CREATE TABLE `task_record` (
  `ID` varchar(20) NOT NULL,
  `TASK_ID` varchar(20) NULL,
  `OPERATE_TIME` datetime NULL,
  `USER_ID` varchar(20) NULL,
  `TASK_NAME` varchar(255) NULL,
  `TASK_TYPE` varchar(50) NULL,
  `TASK_CATEGORY` varchar(50) NULL,
  `TASK_BUSINESS_CATEGORY` varchar(50) NULL,
  `TRIGGER_CONDITION` varchar(50) NULL,
  `REWARD_INTEGRAL` decimal(12,2) NULL,
  `FREQUENCY_LIMIT` int NULL,
  `REFRESH_TIME` varchar(50) NULL,
  `IS_REFRESH` varchar(50) NULL,
  `IS_LIMIT_TIME` varchar(50) NULL,
  `VALIDITY_PERIOD_START` datetime NULL,
  `VALIDITY_PERIOD_END` datetime NULL,
  `NUMBER_PARMS` int NULL,
  `VALIDITY_PERIOD` datetime NULL,
  `EXECUTE_STATUS` varchar(50) NULL,
  `ERR_REASON` varchar(255) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_task_record_task_id` (`TASK_ID`),
  KEY `idx_task_record_user_id` (`USER_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>任务</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- task_info  <p>任务</p>
-- ----------------------------
DROP TABLE IF EXISTS `task_info`;
CREATE TABLE `task_info` (
  `ID` varchar(20) NOT NULL,
  `SORT` int NULL,
  `ICON` varchar(255) NULL,
  `TASK_NAME` varchar(255) NULL,
  `TASK_INTRODUCTION` varchar(255) NULL,
  `TASK_TYPE` varchar(50) NULL,
  `TASK_CATEGORY` varchar(50) NULL,
  `TASK_BUSINESS_CATEGORY` varchar(50) NULL,
  `TRIGGER_CONDITION` varchar(50) NULL,
  `REWARD_INTEGRAL` decimal(12,2) NULL,
  `FREQUENCY_LIMIT` int NULL,
  `REFRESH_TIME` varchar(50) NULL,
  `IS_REFRESH` varchar(50) NULL,
  `IS_LIMIT_TIME` varchar(50) NULL,
  `VALIDITY_PERIOD_START` datetime NULL,
  `VALIDITY_PERIOD_END` datetime NULL,
  `NUMBER_PARMS` int NULL,
  `VALIDITY_PERIOD` datetime NULL,
  `JUMP_ROUTING` varchar(255) NULL,
  `STATUS` varchar(50) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>任务</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- integral_record  <p>积分规则</p>
-- ----------------------------
DROP TABLE IF EXISTS `integral_record`;
CREATE TABLE `integral_record` (
  `ID` varchar(20) NOT NULL,
  `INTEGRAL_INFO_ID` varchar(20) NULL,
  `USER_ID` varchar(20) NULL,
  `INTEGRAL_SOURCE` varchar(50) NULL,
  `INTEGRAL_SOURCE_ID` varchar(20) NULL,
  `CHANGE_TYPE` varchar(50) NULL,
  `INTEGRAL_CATEGORY` varchar(50) NULL,
  `OPERATION_TIME` datetime NULL,
  `INTEGRAL` decimal(12,2) NULL,
  `OPERATION_SOURCE` varchar(50) NULL,
  `OPERATION_USER_ID` varchar(20) NULL,
  `INTEGRAL_BALANCE` decimal(12,2) NULL,
  `FROZEN_INTEGRAL_BALANCE` decimal(12,2) NULL,
  `EXPIRE_INTEGRAL_BALANCE` decimal(12,2) NULL,
  `CLEAR_EXPIRE_STATUS` varchar(50) NULL,
  `VALIDITY_PERIOD` datetime NULL,
  `REMARK` varchar(500) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_integral_record_integral_info_id` (`INTEGRAL_INFO_ID`),
  KEY `idx_integral_record_user_id` (`USER_ID`),
  KEY `idx_integral_record_integral_source_id` (`INTEGRAL_SOURCE_ID`),
  KEY `idx_integral_record_operation_user_id` (`OPERATION_USER_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>积分规则</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- integral_withdrawal_record  <p>积分提现记录</p>
-- ----------------------------
DROP TABLE IF EXISTS `integral_withdrawal_record`;
CREATE TABLE `integral_withdrawal_record` (
  `ID` varchar(20) NOT NULL,
  `USER_ID` varchar(20) NULL,
  `INTEGRAL` decimal(12,2) NULL,
  `WITHDRAWAL_STATUS` varchar(50) NULL,
  `WITHDRAWAL_TIME` datetime NULL,
  `WITHDRAWAL_ACCOUNT` varchar(255) NULL,
  `INTEGRAL_EXCHANGE_RATE` decimal(12,2) NULL,
  `REMARK` varchar(500) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_integral_withdrawal_record_user_id` (`USER_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>积分提现记录</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- integral_info  <p>积分信息</p>
-- ----------------------------
DROP TABLE IF EXISTS `integral_info`;
CREATE TABLE `integral_info` (
  `ID` varchar(20) NOT NULL,
  `USER_ID` varchar(20) NULL,
  `INTEGRAL` decimal(12,2) NULL,
  `FROZEN_INTEGRAL` decimal(12,2) NULL,
  `EXPIRE_INTEGRAL` decimal(12,2) NULL,
  `LAST_MODIFY_TIME` datetime NULL,
  `LAST_MODIFY_USER_ID` varchar(20) NULL,
  `STATUS` varchar(50) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_integral_info_user_id` (`USER_ID`),
  KEY `idx_integral_info_last_modify_user_id` (`LAST_MODIFY_USER_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>积分信息</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- integral_rules  <p>积分规则</p>
-- ----------------------------
DROP TABLE IF EXISTS `integral_rules`;
CREATE TABLE `integral_rules` (
  `ID` varchar(20) NOT NULL,
  `SORT` int NULL,
  `USER_LIMIT_TYPE` varchar(50) NULL,
  `USER_ID` varchar(20) NULL,
  `VALIDITY_PERIOD` datetime NULL,
  `LIMIT_TIME_TYPE` varchar(50) NULL,
  `LIMIT_INTEGRAL` decimal(12,2) NULL,
  `WITHDRAWAL_TIME_TYPE` varchar(50) NULL,
  `WITHDRAWAL_INTEGRAL` decimal(12,2) NULL,
  `POINTS_TO_CASH_RATIO` decimal(12,2) NULL,
  `STATUS` varchar(50) NULL,
  `REMARK` varchar(500) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_integral_rules_user_id` (`USER_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>积分规则</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- dev_app_version  <p>App版本管理</p>
-- ----------------------------
DROP TABLE IF EXISTS `dev_app_version`;
CREATE TABLE `dev_app_version` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `VERSION_NAME` varchar(255) NULL,
  `VERSION_CODE` varchar(255) NULL,
  `APP_TYPE` varchar(50) NULL,
  `APP_URL` varchar(255) NULL,
  `APP_DESC` varchar(255) NULL,
  `FORCE_UPDATE` tinyint(1) NULL,
  `SORT_CODE` int NULL COMMENT '排序码',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>App版本管理</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- distribution_record  <p>推广记录</p>
-- ----------------------------
DROP TABLE IF EXISTS `distribution_record`;
CREATE TABLE `distribution_record` (
  `ID` varchar(20) NOT NULL,
  `USER_ID` varchar(20) NULL,
  `PROMOTER_ID` varchar(20) NULL,
  `TYPE` varchar(50) NULL,
  `BUSINESS_ID` varchar(20) NULL,
  `OPERATION_TIME` datetime NULL,
  `INFO_ID` varchar(20) NULL,
  `SETTLEMENT_ID` varchar(20) NULL,
  `SETTLEMENT_STATUS` varchar(50) NULL,
  `SETTLEMENT_TIME` datetime NULL,
  `RECOMMEND_COMMISSION` varchar(50) NULL,
  `INTEGRAL` decimal(12,2) NULL,
  `SETTLEMENT_TYPE` varchar(50) NULL,
  `SETTLEMENT_TIME_TYPE` varchar(50) NULL,
  `COMMISSION_TIME_TYPE` varchar(50) NULL,
  `COMMISSION_TIME_STATUS` varchar(50) NULL,
  `COMMISSION_TIME` datetime NULL,
  `REMARK` varchar(500) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_distribution_record_user_id` (`USER_ID`),
  KEY `idx_distribution_record_promoter_id` (`PROMOTER_ID`),
  KEY `idx_distribution_record_business_id` (`BUSINESS_ID`),
  KEY `idx_distribution_record_info_id` (`INFO_ID`),
  KEY `idx_distribution_record_settlement_id` (`SETTLEMENT_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>推广记录</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- distribution_settlement  <p>推广结算</p>
-- ----------------------------
DROP TABLE IF EXISTS `distribution_settlement`;
CREATE TABLE `distribution_settlement` (
  `ID` varchar(20) NOT NULL,
  `TYPE` varchar(50) NULL,
  `INFO_ID` varchar(20) NULL,
  `USER_ID` varchar(20) NULL,
  `PROMOTER_ID` varchar(20) NULL,
  `RECOMMEND_COMMISSION` varchar(50) NULL,
  `COMMISSION_INTEGRAL` decimal(12,2) NULL,
  `SETTLEMENT_TYPE` varchar(50) NULL,
  `SETTLEMENT_STATUS` varchar(50) NULL,
  `SETTLEMENT_TIME_TYPE` varchar(50) NULL,
  `COMMISSION_TIME_TYPE` varchar(50) NULL,
  `COMMISSION_FIX_TIME` varchar(255) NULL,
  `COMMISSION_TIME_STATUS` varchar(50) NULL,
  `COMMISSION_TIME` datetime NULL,
  `SETTLEMENT_TIME` datetime NULL,
  `INTEGRAL` decimal(12,2) NULL,
  `BUSINESS_ID` varchar(20) NULL,
  `REMARK` varchar(500) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_distribution_settlement_info_id` (`INFO_ID`),
  KEY `idx_distribution_settlement_user_id` (`USER_ID`),
  KEY `idx_distribution_settlement_promoter_id` (`PROMOTER_ID`),
  KEY `idx_distribution_settlement_business_id` (`BUSINESS_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>推广结算</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- distribution_info  <p>推广信息</p>
-- ----------------------------
DROP TABLE IF EXISTS `distribution_info`;
CREATE TABLE `distribution_info` (
  `ID` varchar(20) NOT NULL,
  `NAME` varchar(255) NULL,
  `TYPE` varchar(50) NULL,
  `INTRODUCTION` varchar(255) NULL,
  `LOGO` varchar(255) NULL,
  `STATUS` varchar(50) NULL,
  `SORT` int NULL,
  `RECOMMEND_COMMISSION` varchar(50) NULL,
  `COMMISSION_INTEGRAL` decimal(12,2) NULL,
  `SETTLEMENT_TYPE` varchar(50) NULL,
  `SETTLEMENT_TIME_TYPE` varchar(50) NULL,
  `COMMISSION_TIME_TYPE` varchar(50) NULL,
  `COMMISSION_FIX_TIME` varchar(255) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>推广信息</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- user_question_answer  用户问题答案实体
-- ----------------------------
DROP TABLE IF EXISTS `user_question_answer`;
CREATE TABLE `user_question_answer` (
  `ID` varchar(20) NOT NULL COMMENT '主键',
  `QUESTION_CONTENT` varchar(255) NULL COMMENT '问题文本',
  `ANSWER_CONTENT` varchar(255) NULL COMMENT '答案文本',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户问题答案实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mobile_device_info  <p>移动端设备信息</p>
-- ----------------------------
DROP TABLE IF EXISTS `mobile_device_info`;
CREATE TABLE `mobile_device_info` (
  `ID` varchar(20) NOT NULL,
  `CLIENT_ID` varchar(20) NULL,
  `BRAND` varchar(255) NULL,
  `MODEL` varchar(255) NULL,
  `VERSION` varchar(255) NULL,
  `SIMULATOR` int NULL,
  `USER_ID` varchar(20) NULL,
  `FIRST_ACCESS_TIME` datetime NULL,
  `LAST_ACCESS_TIME` datetime NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_mobile_device_info_client_id` (`CLIENT_ID`),
  KEY `idx_mobile_device_info_user_id` (`USER_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>移动端设备信息</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- user_feedback  反馈意见实体
-- ----------------------------
DROP TABLE IF EXISTS `user_feedback`;
CREATE TABLE `user_feedback` (
  `ID` varchar(20) NOT NULL COMMENT '主键',
  `FEEDBACK_TYPE` varchar(255) NULL COMMENT '名称',
  `FEEDBACK_MESSAGE` varchar(255) NULL COMMENT '反馈信息',
  `REPLY_MESSAGE` varchar(255) NULL COMMENT '回复信息',
  `PICTURE` varchar(255) NULL COMMENT '图片',
  `MOBILE` varchar(20) NULL COMMENT '手机号',
  `STATUS` varchar(255) NULL COMMENT '状态',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_user_feedback_mobile` (`MOBILE`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '反馈意见实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- user_account  消息记录实体
-- ----------------------------
DROP TABLE IF EXISTS `user_account`;
CREATE TABLE `user_account` (
  `ID` varchar(20) NOT NULL COMMENT '主键',
  `USER_ID` varchar(20) NULL COMMENT '用户id',
  `ACCOUNT` varchar(100) NULL COMMENT '账号',
  `ACCOUNT_TYPE` varchar(255) NULL COMMENT '账号类别',
  `DEFALUT_STATUS` varchar(255) NULL COMMENT '是否为默认',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_user_account_user_id` (`USER_ID`),
  KEY `idx_user_account_account` (`ACCOUNT`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息记录实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- live_message_log  消息记录实体
-- ----------------------------
DROP TABLE IF EXISTS `live_message_log`;
CREATE TABLE `live_message_log` (
  `ID` varchar(20) NOT NULL COMMENT '主键',
  `MESSAGE_CATEGORY` varchar(255) NULL COMMENT '消息类型',
  `CONTENT` text NULL COMMENT '消息类容',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息记录实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- user_amount_available  消息记录实体
-- ----------------------------
DROP TABLE IF EXISTS `user_amount_available`;
CREATE TABLE `user_amount_available` (
  `ID` varchar(20) NOT NULL COMMENT '主键',
  `AMOUNT` double NULL COMMENT '可选金额',
  `CONDITION` varchar(255) NULL COMMENT '条件',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息记录实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- content_video  <p>视频</p>
-- ----------------------------
DROP TABLE IF EXISTS `content_video`;
CREATE TABLE `content_video` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `TITLE` varchar(255) NULL,
  `CATEGORY` varchar(255) NULL,
  `TAGS` varchar(255) NULL,
  `COVER` varchar(1000) NULL,
  `DESCRIPTION` text NULL,
  `VIDEO_URL` varchar(255) NULL,
  `DURATION` int NULL,
  `STATUS` int NULL,
  `SOURCE` varchar(255) NULL,
  `DOWNLOADABLE` int NULL,
  `SHAREABLE` int NULL,
  `ALLOW_COMMENT` int NULL,
  `SORT` int NULL,
  `GOODS_ID` varchar(20) NULL,
  `START_SHOW_GOODS_SECOND` int NULL,
  `SHOW_GOODS_SECOND` int NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_content_video_goods_id` (`GOODS_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>视频</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- content_watch_progress  <p>观看进度表</p>
-- ----------------------------
DROP TABLE IF EXISTS `content_watch_progress`;
CREATE TABLE `content_watch_progress` (
  `ID` varchar(20) NOT NULL,
  `USER_ID` varchar(20) NULL,
  `CONTENT_ID` varchar(20) NULL,
  `CONTENT_TYPE` varchar(50) NULL,
  `EPISODE_ID` varchar(20) NULL,
  `PROGRESS_SECONDS` int NULL,
  `TOTAL_SECONDS` int NULL,
  `VIEW_PROGRESS` varchar(255) NULL,
  `START_WATCH_TIME` datetime NULL,
  `LAST_WATCH_TIME` datetime NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_content_watch_progress_user_id` (`USER_ID`),
  KEY `idx_content_watch_progress_content_id` (`CONTENT_ID`),
  KEY `idx_content_watch_progress_episode_id` (`EPISODE_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>观看进度表</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- content_watch_record  <p>观看记录表</p>
-- ----------------------------
DROP TABLE IF EXISTS `content_watch_record`;
CREATE TABLE `content_watch_record` (
  `ID` varchar(20) NOT NULL,
  `USER_ID` varchar(20) NULL,
  `CONTENT_ID` varchar(20) NULL,
  `CONTENT_TYPE` varchar(50) NULL,
  `EPISODE_ID` varchar(20) NULL,
  `PROGRESS_SECONDS` int NULL,
  `VIEW_PROGRESS` varchar(255) NULL,
  `START_WATCH_TIME` datetime NULL,
  `LAST_WATCH_TIME` datetime NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_content_watch_record_user_id` (`USER_ID`),
  KEY `idx_content_watch_record_content_id` (`CONTENT_ID`),
  KEY `idx_content_watch_record_episode_id` (`EPISODE_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>观看记录表</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- content_stat_counter  <p>统计信息表</p>
-- ----------------------------
DROP TABLE IF EXISTS `content_stat_counter`;
CREATE TABLE `content_stat_counter` (
  `ID` varchar(20) NOT NULL,
  `CONTENT_ID` varchar(20) NULL,
  `CONTENT_TYPE` varchar(50) NULL,
  `VIEW_COUNT` int NULL,
  `LIKE_COUNT` int NULL,
  `COLLECT_COUNT` int NULL,
  `SHARE_COUNT` int NULL,
  `COMMENT_COUNT` int NULL,
  `FOLLOW_COUNT` int NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_content_stat_counter_content_id` (`CONTENT_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>统计信息表</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- content_user_action  <p>用户行为表</p>
-- ----------------------------
DROP TABLE IF EXISTS `content_user_action`;
CREATE TABLE `content_user_action` (
  `ID` varchar(20) NOT NULL,
  `USER_ID` varchar(20) NULL,
  `VIDEO_ID` varchar(20) NULL,
  `CONTENT_ACTION_TYPE` varchar(50) NULL,
  `ACTION_TIME` datetime NULL,
  `IS_ACTIVE` varchar(50) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_content_user_action_user_id` (`USER_ID`),
  KEY `idx_content_user_action_video_id` (`VIDEO_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>用户行为表</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- content_shop_play_follow  <p>追剧表</p>
-- ----------------------------
DROP TABLE IF EXISTS `content_shop_play_follow`;
CREATE TABLE `content_shop_play_follow` (
  `ID` varchar(20) NOT NULL,
  `USER_ID` varchar(20) NULL,
  `OPERATION_TIME` datetime NULL,
  `SHOP_PLAY_ID` varchar(20) NULL,
  `IS_ACTIVE` varchar(50) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_content_shop_play_follow_user_id` (`USER_ID`),
  KEY `idx_content_shop_play_follow_shop_play_id` (`SHOP_PLAY_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>追剧表</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- content_short_play_episode  <p>短剧集</p>
-- ----------------------------
DROP TABLE IF EXISTS `content_short_play_episode`;
CREATE TABLE `content_short_play_episode` (
  `ID` varchar(20) NOT NULL,
  `SHORT_PLAY_ID` varchar(20) NULL,
  `TITLE` varchar(255) NULL,
  `EPISODE_NUMBER` int NULL,
  `COVER` varchar(1000) NULL,
  `VIDEO_ID` varchar(20) NULL,
  `VIDEO_URL` varchar(255) NULL,
  `DURATION` int NULL,
  `DESCRIPTION` text NULL,
  `IS_FREE` tinyint(1) NULL,
  `SORT` int NULL,
  `GOODS_ID` varchar(20) NULL,
  `START_SHOW_GOODS_SECOND` int NULL,
  `SHOW_GOODS_SECOND` int NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_content_short_play_episode_short_play_id` (`SHORT_PLAY_ID`),
  KEY `idx_content_short_play_episode_video_id` (`VIDEO_ID`),
  KEY `idx_content_short_play_episode_goods_id` (`GOODS_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>短剧集</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- content_short_play  <p></p>
-- ----------------------------
DROP TABLE IF EXISTS `content_short_play`;
CREATE TABLE `content_short_play` (
  `ID` varchar(20) NOT NULL,
  `TITLE` varchar(255) NULL,
  `CATEGORY` varchar(255) NULL,
  `TAGS` varchar(255) NULL,
  `EPISODE_COUNT` int NULL,
  `COVER` varchar(1000) NULL,
  `POSTER` varchar(255) NULL,
  `DESCRIPTION` text NULL,
  `SORT` int NULL,
  `STATUS` int NULL,
  `RECOMMEND_LEVEL` int NULL,
  `PUBLISH_TIME` datetime NULL,
  `ALLOW_COMMENT` tinyint(1) NULL,
  `ALLOW_SHARE` tinyint(1) NULL,
  `IS_FREE` tinyint(1) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p></p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_order  商城-订单实体
-- ----------------------------
DROP TABLE IF EXISTS `mall_order`;
CREATE TABLE `mall_order` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `ORDER_TYPE` varchar(255) NULL COMMENT '订单类型',
  `ORDER_NUMBER` varchar(64) NULL COMMENT '订单编号',
  `ORDER_FEE` decimal(12,2) NULL COMMENT '订单价格',
  `LOGISTICS_NUMBER` varchar(255) NULL COMMENT '物流编号',
  `MOBILE` varchar(20) NULL COMMENT '手机号',
  `NICKNAME` varchar(255) NULL COMMENT '收货人昵称',
  `PROVINCE_NAME` varchar(255) NULL COMMENT '省',
  `CITY_NAME` varchar(255) NULL COMMENT '市',
  `COUNTIES_NAME` varchar(255) NULL COMMENT '县区',
  `ADDRESS` varchar(255) NULL COMMENT '详细地址',
  `STATUS` varchar(255) NULL COMMENT '状态',
  `SETTLEMENT_STATUS` varchar(50) NULL,
  `SETTLE_TIME` datetime NULL,
  `SORT` varchar(255) NULL COMMENT '排序',
  `ATTACH` text NULL,
  `PID` varchar(20) NULL COMMENT '上级用户ID',
  `ORDER_SOURCE_ID` varchar(20) NULL COMMENT '订单来源ID',
  `REMARK` varchar(500) NULL COMMENT '备注',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_mall_order_order_number` (`ORDER_NUMBER`),
  KEY `idx_mall_order_mobile` (`MOBILE`),
  KEY `idx_mall_order_order_source_id` (`ORDER_SOURCE_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商城-订单实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_goods_change  商城-商品实体
-- ----------------------------
DROP TABLE IF EXISTS `mall_goods_change`;
CREATE TABLE `mall_goods_change` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `GOODS_ID` varchar(20) NULL COMMENT '商品id',
  `DELIST_REASON` varchar(255) NULL COMMENT '下架原因',
  `SORT` varchar(255) NULL COMMENT '排序号',
  `STATUS` varchar(255) NULL COMMENT '状态',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_mall_goods_change_goods_id` (`GOODS_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商城-商品实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_goods_evaluate  商城-商品实体
-- ----------------------------
DROP TABLE IF EXISTS `mall_goods_evaluate`;
CREATE TABLE `mall_goods_evaluate` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `USER_ID` varchar(20) NULL COMMENT '用户id',
  `GOODS_ID` varchar(20) NULL COMMENT '商品id',
  `EVALUATE_INFORMATION` varchar(255) NULL COMMENT '评价信息',
  `EVALUATE_TYPE` varchar(255) NULL COMMENT '评价类型',
  `STATUS` varchar(255) NULL COMMENT '评价状态',
  `EVALUATE_ID` varchar(20) NULL COMMENT '评价id',
  `EVALUATE_TIME` datetime NULL COMMENT '评价时间',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_mall_goods_evaluate_user_id` (`USER_ID`),
  KEY `idx_mall_goods_evaluate_goods_id` (`GOODS_ID`),
  KEY `idx_mall_goods_evaluate_evaluate_id` (`EVALUATE_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商城-商品实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_payment_config  商城-支付配置实体
-- ----------------------------
DROP TABLE IF EXISTS `mall_payment_config`;
CREATE TABLE `mall_payment_config` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `PAYMENT_NAME` varchar(255) NULL COMMENT '支付名称',
  `PAYMENT_VALUE` varchar(255) NULL COMMENT '支付标识',
  `MCHID` varchar(255) NULL COMMENT '商户id',
  `APP_ID` varchar(20) NULL COMMENT '应用id',
  `PUBLIC_KEY` varchar(255) NULL COMMENT '支付平台公钥',
  `PRIVATE_KEY` varchar(255) NULL COMMENT '支付平台私钥',
  `NOTIFY_URL` varchar(255) NULL COMMENT '异步回调地址',
  `RETURN_URL` varchar(255) NULL COMMENT '同步回调地址',
  `SELLER` varchar(255) NULL COMMENT '收款账号',
  `SIGN` varchar(255) NULL COMMENT '签名',
  `SIGN_TYPE` varchar(255) NULL COMMENT '签名类型',
  `INPUT_CHARSET` varchar(255) NULL COMMENT '字符类型',
  `MSG_TYPE` varchar(255) NULL COMMENT '消息类型',
  `KEYSTORE_PATH` varchar(255) NULL COMMENT '证书地址',
  `STORE_PASSWORD` varchar(255) NULL COMMENT '证书密码',
  `FIELD_JSON` varchar(255) NULL COMMENT '差异字段',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_mall_payment_config_app_id` (`APP_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商城-支付配置实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_order_goods  商城-订单实体
-- ----------------------------
DROP TABLE IF EXISTS `mall_order_goods`;
CREATE TABLE `mall_order_goods` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `GOODS_ID` varchar(20) NULL COMMENT '订单编号',
  `ORDER_ID` varchar(20) NULL COMMENT '订单价格',
  `GOODS_TITLE` varchar(255) NULL COMMENT '物流编号',
  `GOODS_COVER` varchar(255) NULL COMMENT '商品封面',
  `GOODS_PRICE` decimal(12,2) NULL COMMENT '物流状态',
  `GOODS_COUNT` varchar(255) NULL COMMENT '手机号',
  `GIFT_POINTS` int NULL COMMENT '赠送的积分',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_mall_order_goods_goods_id` (`GOODS_ID`),
  KEY `idx_mall_order_goods_order_id` (`ORDER_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商城-订单实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_after_sales  商城-订单实体
-- ----------------------------
DROP TABLE IF EXISTS `mall_after_sales`;
CREATE TABLE `mall_after_sales` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `ORDER_ID` varchar(20) NULL COMMENT '订单id',
  `AFTER_SALE_TYPE` varchar(255) NULL COMMENT '售后类型',
  `AFTER_SALE_STATUS` varchar(255) NULL COMMENT '处理状态',
  `FEEDBACK_MESSAGE` varchar(255) NULL COMMENT '反馈信息',
  `REPLY_MESSAGE` varchar(255) NULL COMMENT '处理信息',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_mall_after_sales_order_id` (`ORDER_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商城-订单实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_goods_special_topic_detail  <p>商品专题详情</p>
-- ----------------------------
DROP TABLE IF EXISTS `mall_goods_special_topic_detail`;
CREATE TABLE `mall_goods_special_topic_detail` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `SPECIAL_TOPIC_ID` varchar(20) NULL,
  `GOODS_ID` varchar(20) NULL,
  `GOODS_NUM` int NULL,
  `GOODS_PRICE` decimal(12,2) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_mall_goods_special_topic_detail_special_topic_id` (`SPECIAL_TOPIC_ID`),
  KEY `idx_mall_goods_special_topic_detail_goods_id` (`GOODS_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>商品专题详情</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_goods_special_topic  <p>商品专题表/p>
-- ----------------------------
DROP TABLE IF EXISTS `mall_goods_special_topic`;
CREATE TABLE `mall_goods_special_topic` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `NAME` varchar(255) NULL,
  `GOODS_NUM` int NULL,
  `SORT` int NULL COMMENT '排序',
  `CAROUSEL` int NULL,
  `BACKGROUND` varchar(255) NULL,
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>商品专题表/p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_third_payment  商城-支付实体
-- ----------------------------
DROP TABLE IF EXISTS `mall_third_payment`;
CREATE TABLE `mall_third_payment` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `ORDER_ID` varchar(20) NULL COMMENT '订单id',
  `APPID` varchar(255) NULL COMMENT '调用接口提交的公众号id',
  `MCHID` varchar(255) NULL COMMENT '分配的商户id',
  `OUT_ORDER_NO` varchar(255) NULL COMMENT '调用接口时提交的商户服务订单号',
  `TOTAL_FEE` decimal(12,2) NULL COMMENT '支付金额',
  `PAY_TYPE` varchar(255) NULL COMMENT '支付类型',
  `STATUS` varchar(255) NULL COMMENT '支付状态',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `idx_mall_third_payment_order_id` (`ORDER_ID`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商城-支付实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_order_logistics  <p>订单物流信息</p>
-- ----------------------------
DROP TABLE IF EXISTS `mall_order_logistics`;
CREATE TABLE `mall_order_logistics` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `ORDER_CODE` varchar(255) NULL COMMENT '订单编号',
  `LOGISTIC_CODE` varchar(255) NULL COMMENT '物流编号',
  `SHIPPER` varchar(50) NULL COMMENT '物流公司',
  `CUSTOMER_NAME` varchar(255) NULL COMMENT '寄件人or收件人 手机号后四位',
  `STATE` varchar(50) NULL COMMENT '物流状态',
  `STATE_EX` varchar(50) NULL,
  `LOCATION` varchar(255) NULL COMMENT '当前所在城市',
  `NEXT_CITY` varchar(255) NULL COMMENT '下一站城市',
  `DELIVERY_MAN` varchar(255) NULL COMMENT '配送员',
  `DELIVERY_MAN_TEL` varchar(255) NULL COMMENT '配送员电话',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '<p>订单物流信息</p>' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_integral_goods  积分商城实体
-- ----------------------------
DROP TABLE IF EXISTS `mall_integral_goods`;
CREATE TABLE `mall_integral_goods` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `TITLE` varchar(255) NULL COMMENT '积分商品标题',
  `COVER` varchar(1000) NULL COMMENT '封面',
  `MARKET_PRICE` varchar(255) NULL COMMENT '市场价',
  `INTEGRAL` int NULL COMMENT '兑换所需积分',
  `DETAIL` text NULL COMMENT '商品描述，纯文本',
  `STOCK` int NULL COMMENT '库存',
  `USER_LIMIT` int NULL COMMENT '每人限购数量，0不限购，大于0为限购数量',
  `STATUS` varchar(255) NULL COMMENT '状态，0禁用，1启用',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建用户',
  `UPDATE_TIME` datetime NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '修改用户',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '积分商城实体' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- mall_sales_type  商城-订单实体
-- ----------------------------
DROP TABLE IF EXISTS `mall_sales_type`;
CREATE TABLE `mall_sales_type` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `TITLE` varchar(255) NULL,
  `SORT` varchar(255) NULL COMMENT '排序',
  `DELETE_FLAG` varchar(255) NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL COMMENT '更新时间',
  `UPDATE_USER` varchar(20) NULL COMMENT '更新人',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商城-订单实体' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
