-- =============================================================
-- live_mall_admin 数据库初始化 —— 99_verify.sql
-- 初始化完跑一遍，全部对上就说明库是好的
-- 用法：mysql -h.. -u.. -p live < 99_verify.sql
-- =============================================================

-- ------------------------------------------------------------
-- 1. 表数量，应为 79
-- ------------------------------------------------------------
SELECT COUNT(*) AS 表数量_应为79
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = DATABASE();

-- ------------------------------------------------------------
-- 2. 没有主键的表，应为空
-- ------------------------------------------------------------
SELECT t.TABLE_NAME AS 无主键表
FROM information_schema.TABLES t
LEFT JOIN information_schema.TABLE_CONSTRAINTS c
       ON c.TABLE_SCHEMA = t.TABLE_SCHEMA
      AND c.TABLE_NAME = t.TABLE_NAME
      AND c.CONSTRAINT_TYPE = 'PRIMARY KEY'
WHERE t.TABLE_SCHEMA = DATABASE()
  AND t.TABLE_TYPE = 'BASE TABLE'
  AND c.TABLE_NAME IS NULL;

-- ------------------------------------------------------------
-- 3. 字符集不对的表，应为空
-- ------------------------------------------------------------
SELECT TABLE_NAME AS 字符集异常表, TABLE_COLLATION
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_COLLATION <> 'utf8mb4_general_ci';

-- ------------------------------------------------------------
-- 4. 关键表行数，括号内是期望值
-- ------------------------------------------------------------
SELECT 'sys_user' AS 表名, COUNT(*) AS 行数 FROM sys_user            -- 3
UNION ALL SELECT 'sys_role', COUNT(*) FROM sys_role                   -- 2
UNION ALL SELECT 'sys_org', COUNT(*) FROM sys_org                     -- 10
UNION ALL SELECT 'sys_resource', COUNT(*) FROM sys_resource           -- 154 + 补丁
UNION ALL SELECT 'mobile_resource', COUNT(*) FROM mobile_resource     -- 14 + 补丁
UNION ALL SELECT 'dev_dict', COUNT(*) FROM dev_dict                   -- 185 + 补丁
UNION ALL SELECT 'dev_config', COUNT(*) FROM dev_config               -- 51
UNION ALL SELECT 'gen_config', COUNT(*) FROM gen_config               -- 119
UNION ALL SELECT 'mall_goods', COUNT(*) FROM mall_goods;              -- 3

-- ------------------------------------------------------------
-- 5. 超级管理员在不在，USER_STATUS 应为 ENABLE
-- ------------------------------------------------------------
SELECT ACCOUNT AS 账号, NAME AS 姓名, USER_STATUS AS 状态
FROM sys_user
WHERE ACCOUNT = 'superAdmin';

-- ------------------------------------------------------------
-- 6. 角色关联有没有断（超级管理员应挂到 superAdmin 角色）
-- ------------------------------------------------------------
SELECT u.ACCOUNT AS 账号, r.NAME AS 角色
FROM sys_relation rel
JOIN sys_user u ON u.ID = rel.OBJECT_ID
JOIN sys_role r ON r.ID = rel.TARGET_ID
WHERE rel.CATEGORY = 'SYS_USER_HAS_ROLE'
  AND u.ACCOUNT = 'superAdmin';

-- ------------------------------------------------------------
-- 7. 菜单树根节点，应 >= 1
-- ------------------------------------------------------------
SELECT COUNT(*) AS 一级菜单数
FROM sys_resource
WHERE PARENT_ID = '0' AND CATEGORY = 'MENU';

-- ------------------------------------------------------------
-- 8. 数据明显不对的表：行数少得离谱的业务表（用来判断要不要跑 03_business_data.sql）
-- ------------------------------------------------------------
SELECT 'live_biz' AS 表名, COUNT(*) AS 行数 FROM live_biz
UNION ALL SELECT 'live_gift', COUNT(*) FROM live_gift
UNION ALL SELECT 'mall_order', COUNT(*) FROM mall_order
UNION ALL SELECT 'content_short_play', COUNT(*) FROM content_short_play
UNION ALL SELECT 'task_info', COUNT(*) FROM task_info;

-- ------------------------------------------------------------
-- 8.1 移动端联调必需数据（跑了 05_demo_mobile_data.sql 才非 0）
--     期望：轮播 3 / 商品 >= 6 / 专题 3 / 版本 2 / 直播场次 3 / 积分商品 3 / 任务 4 / 短剧 3
-- ------------------------------------------------------------
SELECT '首页轮播(CLIENT_MOBILE)' AS 表名, COUNT(*) AS 行数 FROM dev_slideshow WHERE PLACE LIKE '%CLIENT_MOBILE%' AND STATUS = 'ENABLE'
UNION ALL SELECT 'mall_goods', COUNT(*) FROM mall_goods
UNION ALL SELECT 'mall_goods_special_topic', COUNT(*) FROM mall_goods_special_topic
UNION ALL SELECT 'mall_goods_special_topic_detail', COUNT(*) FROM mall_goods_special_topic_detail
UNION ALL SELECT 'dev_app_version', COUNT(*) FROM dev_app_version
UNION ALL SELECT 'live_streaming_type', COUNT(*) FROM live_streaming_type
UNION ALL SELECT 'live_biz', COUNT(*) FROM live_biz
UNION ALL SELECT 'mall_integral_goods', COUNT(*) FROM mall_integral_goods
UNION ALL SELECT 'task_info', COUNT(*) FROM task_info
UNION ALL SELECT 'content_short_play', COUNT(*) FROM content_short_play;

-- ------------------------------------------------------------
-- 9. 总判：表数 79 + 无无主键表 = PASS
-- ------------------------------------------------------------
SELECT CASE
         WHEN (SELECT COUNT(*) FROM information_schema.TABLES
                WHERE TABLE_SCHEMA = DATABASE()) = 79
          AND NOT EXISTS (
                SELECT 1
                FROM information_schema.TABLES t
                LEFT JOIN information_schema.TABLE_CONSTRAINTS c
                       ON c.TABLE_SCHEMA = t.TABLE_SCHEMA
                      AND c.TABLE_NAME = t.TABLE_NAME
                      AND c.CONSTRAINT_TYPE = 'PRIMARY KEY'
                WHERE t.TABLE_SCHEMA = DATABASE()
                  AND t.TABLE_TYPE = 'BASE TABLE'
                  AND c.TABLE_NAME IS NULL)
         THEN 'PASS'
         ELSE 'FAIL —— 对一下上面几项输出'
       END AS 初始化结果;
