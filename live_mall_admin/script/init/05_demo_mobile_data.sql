-- =============================================================
-- live_mall_admin 数据库初始化 —— 05_demo_mobile_data.sql
-- 移动端（Flutter / H5）联调所需的完整演示数据
--
-- 覆盖：首页轮播、商城商品、商品专题、APP 版本、直播类型/主播/场次、
--       积分商城、任务中心、短剧
--
-- 与 03_business_data.sql 的区别：
--   03 是 2025 年初的历史联调快照，字段取值不规范（如 STATUS='男'）、
--   图片指向未部署的 MinIO，且缺少专题/直播/版本等移动端必需数据。
--   本文件按实体枚举契约重新构造，可重复执行（先删后插）。
--
-- 图片使用 picsum.photos 的固定 seed，保证同一份数据每次拿到同一张图。
-- 本地不需要对象存储；离线环境可把图片换成自己的静态资源地址。
--
-- 生产环境不要执行本文件（run_init.sh --no-demo 会自动跳过）。
-- =============================================================

SET NAMES utf8mb4;

-- -------------------------------------------------------------
-- 1. 首页轮播（dev_slideshow）
--    移动端只读取 PLACE 包含 "CLIENT_MOBILE" 且 STATUS=ENABLE 的记录，
--    并按 SORT_CODE 升序展示。
-- -------------------------------------------------------------
DELETE FROM `dev_slideshow` WHERE `ID` IN (
  '9900000000000000121', '9900000000000000122', '9900000000000000123'
);

INSERT INTO `dev_slideshow`
  (`ID`, `TITLE`, `PLACE`, `IMAGE`, `PATH_DETAILS`, `STATUS`, `SORT_CODE`, `EXT_JSON`, `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`)
VALUES
  ('9900000000000000121', '颐心相伴 · 新客礼遇', '["CLIENT_MOBILE"]',
   'https://picsum.photos/seed/live-banner-1/1200/600',
   '[{"key":"CLIENT_MOBILE","whetherToClick":"1","skipMode":"1","url":"/pages/mall/mall"}]',
   'ENABLE', 1, NULL, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000122', '好物严选 · 产地直发', '["CLIENT_MOBILE"]',
   'https://picsum.photos/seed/live-banner-2/1200/600',
   '[{"key":"CLIENT_MOBILE","whetherToClick":"1","skipMode":"1","url":"/pages/mall/mall"}]',
   'ENABLE', 2, NULL, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000123', '积分兑好礼', '["CLIENT_MOBILE"]',
   'https://picsum.photos/seed/live-banner-3/1200/600',
   '[{"key":"CLIENT_MOBILE","whetherToClick":"1","skipMode":"1","url":"/pages/mine_next/integral_goods/integral_goods"}]',
   'ENABLE', 3, NULL, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001');

-- -------------------------------------------------------------
-- 2. 商城商品（mall_goods）
--    STATUS：'1' 正常 / '2' 下架
-- -------------------------------------------------------------
DELETE FROM `mall_goods` WHERE `ID` BETWEEN '9900000000000000011' AND '9900000000000000016';

INSERT INTO `mall_goods`
  (`ID`, `TITLE`, `DETAIL`, `COST_PRICE`, `PRICE`, `SORT`, `MERCHANT`, `GIFT_POINTS`, `SPREAD_GIFT`,
   `DESCRIPTION`, `STOCK`, `LOW_STOCK`, `SALES`, `VIRTUAL_SALES`, `STATUS`, `DELETE_FLAG`,
   `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`, `HOST_IMAGE`, `CAROUSE_IMAGE`)
VALUES
  ('9900000000000000011', '天然檀木梳 · 顺发养发', '<p>精选天然檀木，齿圆润不伤发，适合日常梳理与按摩。</p>',
   39.00, 69.00, 6, '颐心相伴自营', 20, 5, '檀木顺发梳', 300, 20, 128, 460, '1', 'NOT_DELETE',
   NOW(), '9900000000000000001', NOW(), '9900000000000000001',
   'https://picsum.photos/seed/live-goods-1/800/800',
   'https://picsum.photos/seed/live-goods-1a/800/800,https://picsum.photos/seed/live-goods-1b/800/800'),
  ('9900000000000000012', '东北五常大米 5kg', '<p>当季新米，颗粒饱满，米香浓郁。</p>',
   45.00, 79.00, 5, '颐心相伴自营', 30, 8, '五常大米', 500, 30, 236, 890, '1', 'NOT_DELETE',
   NOW(), '9900000000000000001', NOW(), '9900000000000000001',
   'https://picsum.photos/seed/live-goods-2/800/800',
   'https://picsum.photos/seed/live-goods-2a/800/800'),
  ('9900000000000000013', '物理压榨山茶油 1L', '<p>低温物理压榨，保留天然营养成分。</p>',
   68.00, 118.00, 4, '颐心相伴自营', 40, 10, '山茶油', 200, 20, 96, 320, '1', 'NOT_DELETE',
   NOW(), '9900000000000000001', NOW(), '9900000000000000001',
   'https://picsum.photos/seed/live-goods-3/800/800',
   'https://picsum.photos/seed/live-goods-3a/800/800'),
  ('9900000000000000014', '手工红薯粉条 500g', '<p>传统手工工艺，久煮不糊。</p>',
   18.00, 32.00, 3, '颐心相伴自营', 10, 3, '红薯粉条', 800, 50, 412, 1200, '1', 'NOT_DELETE',
   NOW(), '9900000000000000001', NOW(), '9900000000000000001',
   'https://picsum.photos/seed/live-goods-4/800/800',
   'https://picsum.photos/seed/live-goods-4a/800/800'),
  ('9900000000000000015', '高原土蜂蜜 500g', '<p>高原蜜源，自然成熟，口感醇厚。</p>',
   52.00, 98.00, 2, '颐心相伴自营', 35, 6, '高原土蜂蜜', 150, 15, 74, 210, '1', 'NOT_DELETE',
   NOW(), '9900000000000000001', NOW(), '9900000000000000001',
   'https://picsum.photos/seed/live-goods-5/800/800',
   'https://picsum.photos/seed/live-goods-5a/800/800'),
  ('9900000000000000016', '有机黑芝麻丸 300g', '<p>九蒸九晒，独立小包装。</p>',
   29.00, 55.00, 1, '颐心相伴自营', 15, 4, '黑芝麻丸', 400, 30, 187, 530, '1', 'NOT_DELETE',
   NOW(), '9900000000000000001', NOW(), '9900000000000000001',
   'https://picsum.photos/seed/live-goods-6/800/800',
   'https://picsum.photos/seed/live-goods-6a/800/800');

-- 历史样例商品的 STATUS 取值不规范（'男' / NULL），统一为正常在售
UPDATE `mall_goods` SET `STATUS` = '1'
WHERE `ID` IN ('250100277657608192', '250461674224513024', '250461783771344896')
  AND (`STATUS` IS NULL OR `STATUS` NOT IN ('1', '2'));

-- -------------------------------------------------------------
-- 3. 商品图集（mall_goods_slide）
-- -------------------------------------------------------------
DELETE FROM `mall_goods_slide` WHERE `ID` BETWEEN '9900000000000000021' AND '9900000000000000026';

INSERT INTO `mall_goods_slide`
  (`ID`, `GOODS_ID`, `URL`, `TYPE`, `SORT`, `STATUS`, `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`)
VALUES
  ('9900000000000000021', '9900000000000000011', 'https://picsum.photos/seed/live-goods-1a/800/800', '1', 1, '1', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000022', '9900000000000000011', 'https://picsum.photos/seed/live-goods-1b/800/800', '1', 2, '1', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000023', '9900000000000000012', 'https://picsum.photos/seed/live-goods-2a/800/800', '1', 1, '1', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000024', '9900000000000000013', 'https://picsum.photos/seed/live-goods-3a/800/800', '1', 1, '1', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000025', '9900000000000000014', 'https://picsum.photos/seed/live-goods-4a/800/800', '1', 1, '1', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000026', '9900000000000000015', 'https://picsum.photos/seed/live-goods-5a/800/800', '1', 1, '1', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001');

-- -------------------------------------------------------------
-- 4. 商品专题（mall_goods_special_topic）
--    CAROUSEL：0 = 顶部轮播位，其他值 = 专题列表
-- -------------------------------------------------------------
DELETE FROM `mall_goods_special_topic` WHERE `ID` BETWEEN '9900000000000000031' AND '9900000000000000033';

INSERT INTO `mall_goods_special_topic`
  (`ID`, `NAME`, `GOODS_NUM`, `SORT`, `CAROUSEL`, `BACKGROUND`, `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`)
VALUES
  ('9900000000000000031', '秋季滋补专场', 3, 1, 0,
   'https://picsum.photos/seed/live-topic-1/1200/600', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000032', '厨房好物', 2, 2, 1,
   'https://picsum.photos/seed/live-topic-2/800/800', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000033', '每日养生', 2, 3, 1,
   'https://picsum.photos/seed/live-topic-3/800/800', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001');

DELETE FROM `mall_goods_special_topic_detail` WHERE `ID` BETWEEN '9900000000000000041' AND '9900000000000000046';

INSERT INTO `mall_goods_special_topic_detail`
  (`ID`, `SPECIAL_TOPIC_ID`, `GOODS_ID`, `GOODS_NUM`, `GOODS_PRICE`, `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`)
VALUES
  ('9900000000000000041', '9900000000000000031', '9900000000000000013', 50, 108.00, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000042', '9900000000000000031', '9900000000000000015', 40, 88.00, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000043', '9900000000000000031', '9900000000000000016', 60, 49.00, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000044', '9900000000000000032', '9900000000000000012', 80, 75.00, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000045', '9900000000000000032', '9900000000000000014', 100, 29.00, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000046', '9900000000000000033', '9900000000000000011', 70, 65.00, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001');

-- -------------------------------------------------------------
-- 5. APP 版本（dev_app_version）
--    APP_TYPE 取枚举名：Android / iOS
-- -------------------------------------------------------------
DELETE FROM `dev_app_version` WHERE `ID` IN ('9900000000000000051', '9900000000000000052');

INSERT INTO `dev_app_version`
  (`ID`, `VERSION_NAME`, `VERSION_CODE`, `APP_TYPE`, `APP_URL`, `APP_DESC`, `FORCE_UPDATE`, `SORT_CODE`, `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`)
VALUES
  ('9900000000000000051', '1.0.0', '1', 'Android',
   'http://127.0.0.1:8081/debug/apk/app-debug.apk', '本地联调基线版本', 0, 1, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000052', '1.0.0', '1', 'iOS',
   'https://apps.apple.com/', '本地联调基线版本', 0, 1, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001');

-- -------------------------------------------------------------
-- 6. 直播类型 / 主播 / 直播场次
-- -------------------------------------------------------------
DELETE FROM `live_streaming_type` WHERE `ID` BETWEEN '9900000000000000061' AND '9900000000000000063';

INSERT INTO `live_streaming_type`
  (`ID`, `TITLE`, `DESCRIBE`, `STATUS`, `SORT`, `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`)
VALUES
  ('9900000000000000061', '好物推荐', '主播现场讲解商品', 'ENABLE', '1', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000062', '健康养生', '养生知识与答疑', 'ENABLE', '2', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000063', '助农专场', '产地直发助农直播', 'ENABLE', '3', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001');

DELETE FROM `live_anchor` WHERE `ID` IN (9001, 9002);

INSERT INTO `live_anchor` (`ID`, `USERNAME`, `REAL_NAME`, `ACCOUNT`, `PASSWORD`, `PICTURE_URL`, `BIO`)
VALUES
  (9001, 'demo_anchor_1', '演示主播 · 小颐', 'demo_anchor_1', '',
   'https://picsum.photos/seed/live-anchor-1/200/200', '好物推荐主播'),
  (9002, 'demo_anchor_2', '演示主播 · 小伴', 'demo_anchor_2', '',
   'https://picsum.photos/seed/live-anchor-2/200/200', '健康养生主播');

DELETE FROM `live_biz` WHERE `ID` BETWEEN '9900000000000000071' AND '9900000000000000073';

INSERT INTO `live_biz`
  (`ID`, `LIVE_BROADCASTS`, `ANCHOR_ID`, `SPECTATOR_NUMBER`, `INGEST_URL`, `LIVE_URL`, `AVATAR_URL`, `COVER_URL`,
   `BACK_URL`, `IS_SCREEN_RECORDING`, `LIVE_ROOM_STATE`, `MAIN_TITLE`, `SUB_TITLE`, `LIVE_STATE`,
   `LIVE_BEGIN_TIME`, `LIVE_OVER_TIME`, `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`)
VALUES
  ('9900000000000000071', 900101, 9001, 1280, '', '',
   'https://picsum.photos/seed/live-anchor-1/200/200', 'https://picsum.photos/seed/live-live-1/1200/700',
   '', 1, '1', '秋季滋补好物专场', '山茶油 / 土蜂蜜 / 黑芝麻丸', '9900000000000000061',
   NOW(), DATE_ADD(NOW(), INTERVAL 2 HOUR), 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000072', 900102, 9002, 860, '', '',
   'https://picsum.photos/seed/live-anchor-2/200/200', 'https://picsum.photos/seed/live-live-2/1200/700',
   '', 1, '1', '养生答疑直播', '日常饮食与作息建议', '9900000000000000062',
   NOW(), DATE_ADD(NOW(), INTERVAL 3 HOUR), 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000073', 900103, 9001, 0, '', '',
   'https://picsum.photos/seed/live-anchor-1/200/200', 'https://picsum.photos/seed/live-live-3/1200/700',
   '', 1, '0', '产地直发助农预告', '明晚八点开播', '9900000000000000063',
   DATE_ADD(NOW(), INTERVAL 1 DAY), DATE_ADD(DATE_ADD(NOW(), INTERVAL 1 DAY), INTERVAL 2 HOUR), 'NOT_DELETE',
   NOW(), '9900000000000000001', NOW(), '9900000000000000001');

-- -------------------------------------------------------------
-- 7. 积分商城（mall_integral_goods）
--    STATUS：'1' 启用 / '0' 禁用
-- -------------------------------------------------------------
DELETE FROM `mall_integral_goods` WHERE `ID` BETWEEN '9900000000000000081' AND '9900000000000000083';

INSERT INTO `mall_integral_goods`
  (`ID`, `TITLE`, `COVER`, `MARKET_PRICE`, `INTEGRAL`, `DETAIL`, `STOCK`, `USER_LIMIT`, `STATUS`,
   `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`)
VALUES
  ('9900000000000000081', '颐心定制帆布袋', 'https://picsum.photos/seed/live-integral-1/800/800',
   '29.00', 500, '加厚帆布，日常通勤可用。', 200, 1, '1', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000082', '保温杯 400ml', 'https://picsum.photos/seed/live-integral-2/800/800',
   '89.00', 1200, '304 不锈钢内胆，保温 12 小时。', 120, 1, '1', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000083', '养生茶礼盒', 'https://picsum.photos/seed/live-integral-3/800/800',
   '139.00', 2000, '六种花草茶组合装。', 80, 2, '1', 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001');

-- -------------------------------------------------------------
-- 8. 任务中心（task_info）
--    枚举落库值：TASK_TYPE 1=日常 2=永久；TASK_CATEGORY 1=系统 2=签到 3=商城 4=直播 5=短视频
--    TASK_BUSINESS_CATEGORY 201=每日签到 301=商品浏览 401=直播观看
--    TRIGGER_CONDITION 0=自动；REFRESH_TIME 1=每日；IS_REFRESH/IS_LIMIT_TIME/STATUS 0=启用
-- -------------------------------------------------------------
DELETE FROM `task_info` WHERE `ID` BETWEEN '9900000000000000091' AND '9900000000000000094';

INSERT INTO `task_info`
  (`ID`, `SORT`, `ICON`, `TASK_NAME`, `TASK_INTRODUCTION`, `TASK_TYPE`, `TASK_CATEGORY`,
   `TASK_BUSINESS_CATEGORY`, `TRIGGER_CONDITION`, `REWARD_INTEGRAL`, `FREQUENCY_LIMIT`, `REFRESH_TIME`,
   `IS_REFRESH`, `IS_LIMIT_TIME`, `VALIDITY_PERIOD_START`, `VALIDITY_PERIOD_END`, `NUMBER_PARMS`,
   `VALIDITY_PERIOD`, `JUMP_ROUTING`, `STATUS`, `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`)
VALUES
  ('9900000000000000091', 1, 'https://picsum.photos/seed/live-task-1/120/120', '每日签到',
   '每天签到可领取积分', '1', '2', '201', '0', 10.00, 1, '1', '0', '1',
   NULL, NULL, NULL, NULL, '/pages/mine_next/sign_in/sign_in', '0', 'NOT_DELETE',
   NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000092', 2, 'https://picsum.photos/seed/live-task-2/120/120', '浏览商品',
   '浏览任意商品详情可获得积分', '1', '3', '301', '0', 5.00, 5, '1', '0', '1',
   NULL, NULL, NULL, NULL, '/pages/mall/mall', '0', 'NOT_DELETE',
   NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000093', 3, 'https://picsum.photos/seed/live-task-3/120/120', '观看直播',
   '观看直播满 5 分钟可获得积分', '1', '4', '401', '0', 20.00, 3, '1', '0', '1',
   NULL, NULL, NULL, NULL, '/pages/home/home', '0', 'NOT_DELETE',
   NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000094', 4, 'https://picsum.photos/seed/live-task-4/120/120', '完善资料',
   '完善个人资料一次性奖励', '2', '1', '101', '0', 50.00, 1, '5', '0', '1',
   NULL, NULL, NULL, NULL, '/pages/mine_next/my_setting/my_setting', '0', 'NOT_DELETE',
   NOW(), '9900000000000000001', NOW(), '9900000000000000001');

-- -------------------------------------------------------------
-- 9. 短剧（content_short_play / content_short_play_episode）
--    STATUS：1=上架
-- -------------------------------------------------------------
DELETE FROM `content_short_play_episode` WHERE `ID` BETWEEN '9900000000000000111' AND '9900000000000000113';
DELETE FROM `content_short_play` WHERE `ID` BETWEEN '9900000000000000101' AND '9900000000000000103';

INSERT INTO `content_short_play`
  (`ID`, `TITLE`, `CATEGORY`, `TAGS`, `EPISODE_COUNT`, `COVER`, `POSTER`, `DESCRIPTION`, `SORT`,
   `STATUS`, `RECOMMEND_LEVEL`, `PUBLISH_TIME`, `ALLOW_COMMENT`, `ALLOW_SHARE`, `IS_FREE`,
   `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`)
VALUES
  ('9900000000000000101', '颐心小剧场 · 第一季', '生活', '生活,温情', 2,
   'https://picsum.photos/seed/live-play-1/720/1280', 'https://picsum.photos/seed/live-play-1p/720/1280',
   '演示短剧：记录社区里的日常温暖。', 3, 1, 10, NOW(), 1, 1, 1, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000102', '养生小知识 · 一分钟', '科普', '养生,科普', 1,
   'https://picsum.photos/seed/live-play-2/720/1280', 'https://picsum.photos/seed/live-play-2p/720/1280',
   '演示短剧：一分钟讲清一个养生常识。', 2, 1, 8, NOW(), 1, 1, 1, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000103', '厨房里的家乡味', '美食', '美食,家常', 1,
   'https://picsum.photos/seed/live-play-3/720/1280', 'https://picsum.photos/seed/live-play-3p/720/1280',
   '演示短剧：一道家常菜的完整做法。', 1, 1, 6, NOW(), 1, 1, 1, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001');

INSERT INTO `content_short_play_episode`
  (`ID`, `SHORT_PLAY_ID`, `TITLE`, `EPISODE_NUMBER`, `COVER`, `VIDEO_ID`, `VIDEO_URL`, `DURATION`,
   `DESCRIPTION`, `IS_FREE`, `SORT`, `GOODS_ID`, `START_SHOW_GOODS_SECOND`, `SHOW_GOODS_SECOND`,
   `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`)
VALUES
  ('9900000000000000111', '9900000000000000101', '第 1 集', 1,
   'https://picsum.photos/seed/live-play-1e1/720/1280', NULL, '', 60, '演示剧集', 1, 1,
   '9900000000000000011', 10, 20, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000112', '9900000000000000101', '第 2 集', 2,
   'https://picsum.photos/seed/live-play-1e2/720/1280', NULL, '', 60, '演示剧集', 1, 2,
   NULL, NULL, NULL, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001'),
  ('9900000000000000113', '9900000000000000102', '第 1 集', 1,
   'https://picsum.photos/seed/live-play-2e1/720/1280', NULL, '', 60, '演示剧集', 1, 1,
   NULL, NULL, NULL, 'NOT_DELETE', NOW(), '9900000000000000001', NOW(), '9900000000000000001');
