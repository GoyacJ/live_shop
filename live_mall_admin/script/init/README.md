# 数据库初始化脚本

## 为什么有这份东西

项目原本只有三份不完整的 SQL：`script/snowy.sql`（2025-02-24 的 Navicat 快照，32 张表）、
`_sql/snowy_mysql.sql`（Snowy-Lite 上游基础库，24 张表、全大写表名、没有任何业务表）、
`script/update/update-2025-04-09.sql`（只补菜单和字典）。

而代码里 `@Table` 声明了 **79 张表**——缺的 47 张（短剧、直播、商城订单、分销、积分、任务）
从来没有建表语句，完整结构只存在于那台线上库。

> 注意：`script/snowy.sql` 是带业务数据的线上快照，公开仓库不包含该文件；
> 当前 schema 以 `01_schema.sql` 为准，历史数据不要随源码发布。

这里按实体类把结构反推出来，加上从旧快照里捞回来的基础数据，做成一套能从零建库的脚本。

## 文件

| 文件 | 内容 | 什么时候用 |
| --- | --- | --- |
| `01_schema.sql` | 79 张表全量结构，1157 列 | 每次新建库 |
| `02_base_data.sql` | 脱敏后的系统基础数据：机构、角色、演示用户、菜单、按钮、字典、配置、模块 | 每次新建库 |
| `03_business_data.sql` | 脱敏后的业务演示数据：商品、轮播、公告、积分、测试用户 | 联调要，生产不要 |
| `04_patch_2025-04-09.sql` | 4 月增量：积分商城菜单、订单状态字典、营销活动字段 | 每次新建库 |
| `05_demo_mobile_data.sql` | 移动端完整演示数据：轮播、商品、专题、APP 版本、直播、积分商品、任务、短剧 | 联调要，生产不要 |
| `99_verify.sql` | 建完自检：表数、主键、字符集、关键行数、超管账号 | 建完跑一次 |
| `run_init.sh` | 把上面几步串起来，一把跑完 | 推荐入口 |
| `tools/gen_schema.py` | 生成器，从实体类重新生成 | 改了实体之后 |
| `REPORT.md` | 生成报告：表清单、推导项、待复核项 | 上线前过一遍 |

## 怎么跑

### 一把梭（推荐）

```bash
MYSQL_HOST=127.0.0.1 MYSQL_PORT=3306 \
MYSQL_USER=root MYSQL_PASSWORD='yourpass' MYSQL_DB=live \
./script/init/run_init.sh

# 生产环境，不要演示数据
./script/init/run_init.sh --no-demo
```

脚本会先 `CREATE DATABASE IF NOT EXISTS live DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci`，
然后按顺序导 4 个文件，最后跑自检。

### 手工

```bash
mysql -h127.0.0.1 -uroot -p --default-character-set=utf8mb4 -e \
  "CREATE DATABASE IF NOT EXISTS live DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"

cd script/init
for f in 01_schema.sql 02_base_data.sql 03_business_data.sql 04_patch_2025-04-09.sql; do
  mysql -h127.0.0.1 -uroot -p --default-character-set=utf8mb4 live < "$f"
done

mysql -h127.0.0.1 -uroot -p --default-character-set=utf8mb4 -t live < 99_verify.sql
```

## 跑完该看到什么

`99_verify.sql` 里几处硬指标：

- 表数量 `79`
- 无主键表：空
- 字符集异常表：空
- `sys_user` 3 行，`superAdmin` 状态 `ENABLE`
- `superAdmin` 挂在 `超级管理员`、`业务管理员` 两个角色上
- 一级菜单 15 个
- `sys_resource` 176 行、`mobile_resource` 18 行、`dev_dict` 198 行、`gen_config` 119 行

对不上说明中途有语句失败，看导入时的报错。

## 移动端联调注意

`05_demo_mobile_data.sql` 里的图片使用 `picsum.photos` 固定 seed，加载图片需要外网；
离线环境请把 `IMAGE` / `COVER` / `HOST_IMAGE` 等字段换成自己的静态资源地址。

移动端接口签名还有一条硬约束：

- 后端 `app-dev.yml` 的 `mobile.request.sign-secret`
- Flutter `lib/init/config.dart` 的 `apiSecret`

两者必须完全一致，否则所有 `/mapi/**` 请求都会返回 `参数校验失败`。
本地演示值统一为 `local_demo_sign_secret`。

Android 模拟器访问宿主机时，二选一：

- 执行 `adb reverse tcp:8081 tcp:8081`，`baseUrl` 保持 `http://127.0.0.1:8081`
- 不建反向代理时，把 `baseUrl` 改成 `http://10.0.2.2:8081`

## 演示账号

初始化脚本创建的演示后台账号为 `superAdmin`，默认密码 `ChangeMe_2026!`。
这个密码只用于本地联调和界面演示，**首次登录后立即修改**；生产环境不要直接使用种子账号和默认密码。

## 数据可信度

| 来源 | 表数 | 说明 |
| --- | --- | --- |
| `script/snowy.sql` 原样沿用列定义 | 32 | 真实建表结果，最可信 |
| 实体类推导 | 47 | 推断，`REPORT.md` 逐列列出 |
| 合计 | 79 / 1157 列 | |

需要人眼过一遍的两类东西，都在 `REPORT.md`：

1. **53 处类型不一致**——导出里是 `text`/`longtext`、实体里是 `String` 这种，脚本保留导出的宽类型，
   没有风险。真正要留意的只有 4 处：`dev_file.SIZE_KB`（bigint / String）、
   `mall_goods.SORT`、`mall_goods_slide.SORT`、`user_level.SORT`（tinyint / String），
   代码按字符串读写数字列，MySQL 会自动转，能用但不好看。
2. **62 处枚举列**——Java 枚举按 `varchar(50)` 存。MyBatis-Flex 默认写枚举的 `name()`；
   如果某个枚举用了 `@EnumValue` 落的是别的值（数字、中文），对应列要改。

4 张导出表在 2 月之后加过列，脚本已经补上：

- `mall_goods`：`HOST_IMAGE`、`CAROUSE_IMAGE`
- `user_address`：`PROVINCE_CODE`、`CITY_CODE`、`COUNTIES_CODE`、`UPDATE_USER`
- `user_balance_log`：`USER_ID`、`SETTLE_TIME_TYPE`、`SETTLE_TIME`
- `user_info`：`INTEGRAL_ID`、`RCODE`、`P_ID`、`REGISTER_URL_STATUS`

## 改了实体怎么更新

```bash
python3 script/init/tools/gen_schema.py
```

生成器读的是仓库里所有带 `@Table` 的类 + `CommonEntity`，重新产出
`01_schema.sql` / `02_base_data.sql` / `03_business_data.sql` / `04_patch_2025-04-09.sql` / `REPORT.md`。
重复执行输出一致。

加了新表、新字段之后跑一次，然后 `REPORT.md` 里新增的推导列过一眼。

## 注意

- **表名小写、列名大写**。MySQL 的列名大小写不敏感，表名在小写模式下才敏感，所以脚本统一表名小写。
  旧的 `_sql/snowy_mysql.sql` 表名全大写，在 Linux 上（`lower_case_table_names=0`）会全表找不到，别再用那份。
- **没有外键约束**，跟 Snowy 保持一致，关联靠代码。
- **`live_anchor.ID` 是 `int AUTO_INCREMENT`**，全库唯一一个数字主键，代码里 `save()` 直接不设 id。
- **`dev_log` 没导**（265 行操作日志，纯噪音）。
- **`03_business_data.sql` 是联调数据**，里面有几条测试商品、公告、用户，生产环境用 `--no-demo` 跳过。
- MySQL 8 导入时刷 `Code 1051`（DROP 不存在的表）、`Code 1681`（int 显示宽度废弃）两类
  提示是正常的，前者来自 `DROP TABLE IF EXISTS`，后者来自导出里的 `int(11)`。
- 旧的三份 SQL 保留没删，但已经被这套取代，别再单独执行。

## 已验证

在 MySQL 8.4（`lower_case_table_names=1`）上完整跑通：79 张表建成、四份数据全量导入无报错、
自检项全部对齐。MySQL 5.7 未实测，脚本没有用到 8.0 才有的语法，理论上兼容。
