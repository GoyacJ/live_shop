# Monorepo 提交清单

更新日期：2026-11-04

目标仓库：<https://github.com/GoyacJ/live_shop>（当前为空、公开仓库）

## 结论

三个项目原本各自带有独立 `.git`，根目录已经补上统一 `.gitignore`；本次迁移不导入旧历史，公开仓库从全新初始提交开始。

本次将创建根仓库初始提交并推送到目标仓库；三个旧 `.git` 已备份到工作区外，不随公开仓库发布。

## 本次已落地

- 根目录新增 `.gitignore`，统一排除环境文件、证书/私钥、签名配置、构建产物、缓存和嵌套 `.git`。
- `live_h5/.gitignore` 新增，保留 `unpackage/dist/Dockerfile` 和 `nginx.conf`，排除 `build/` 与 `cache/`。
- `live_mall_admin/snowy-admin-web/.gitignore` 不再忽略 `package-lock.json`，便于固定前端依赖。
- `live_user_app/android/.gitignore` 的签名配置匹配修正为 `/key.properties`。
- 新增脱敏模板：`app-dev.example.yml`、`wxpay_v3.example.properties`、`config.example.dart`、`key.properties.example`、`.env.example`。
- `app.yml` 中的快递鸟账号已迁到本地 `app-dev.yml`；`app.yml` 只保留空占位。
- 根目录已初始化本地 Git 仓库（分支 `main`），三个旧 `.git` 已备份到工作区外；初始提交包含三个项目源码、脱敏示例和数据库初始化脚本。

三个子目录的 `.git` 已备份到工作区外，根仓库现在可以直接 `git add` 源码；公开历史从这次初始提交开始，不包含旧仓库中的密钥和线上数据。

本次采用的备份位置与原始 HEAD：

| 项目 | 原始分支 | 原始 HEAD | 备份目录 |
| --- | --- | --- | --- |
| `live_mall_admin` | `dev` | `8d5efb0f3c0e304dbfb874f2a2a62e359c8308f1` | `/Users/goya/Work/live_shop_git_backup_2026-11-04/live_mall_admin.git` |
| `live_user_app` | `dev-jcy-h5` | `862e5c93af0f938369e3dd2ff88599c874f8b260` | `/Users/goya/Work/live_shop_git_backup_2026-11-04/live_user_app.git` |
| `live_h5` | `master` | `d87323c6a5247827362a105ffd2bdaf31a4f3b08` | `/Users/goya/Work/live_shop_git_backup_2026-11-04/live_h5.git` |

这些目录是完整的原 `.git` 数据，恢复时移回对应项目根目录即可。原始远端仍在阿里云 Codeup，未做任何删除。

数据库初始化演示账号为 `superAdmin` / `ChangeMe_2026!`，首次登录后必须修改。

## 敏感项处理状态（提交前复核）

- [ ] **后台敏感配置**：`live_mall_admin/snowy-web-app/src/main/resources/app.yml` 和 `app-dev.yml` 含数据库、Redis、OpenIM、直播签名、快递鸟、微信支付等配置。当前已提供 `app.example.yml` 与 `app-dev.example.yml`，真实文件被忽略；仍需在生产环境轮换这些凭据。
- [ ] **微信支付材料**：`live_mall_admin/snowy-web-app/src/main/resources/pay/wx/` 下的 `apiclient_key.pem`、`apiclient_cert.pem`、`apiclient_cert.p12`、平台证书和 `wxpay_v3.properties` 已排除在公开提交之外；旧历史已备份到工作区外。仍需轮换相关商户密钥/证书。
- [ ] **Flutter 源码密钥**：`live_user_app/lib/init/config.dart` 已忽略，并提供 `config.example.dart`；本地文件和旧历史仍含真实值。移动端包内密钥不能视为服务端秘密，轮换后应改为服务端签名或短期令牌机制。
- [ ] **Android 签名材料**：`live_user_app/android/key.properties` 已忽略，并提供 `key.properties.example`；旧历史仍含签名密码。建议轮换签名口令/密钥库，正式文件只放在发布环境。
- [x] **后台前端环境文件**：已改为 `.env.example`，真实 `.env.development` / `.env.production` 被忽略，不进入公开仓库。
- [x] **数据库快照和种子数据**：`script/snowy.sql` 是线上快照，继续排除；`script/init/02_base_data.sql` 和 `03_business_data.sql` 已替换默认密码、账号联系方式、手机号、地址和内网资源地址为演示占位数据，随初始化脚本提交。
- [x] **历史清理**：三个旧 `.git` 已备份到工作区外，公开仓库使用全新初始提交，不包含旧历史中的密钥和线上数据。

## 建议纳入首个 monorepo 提交

### 后台 `live_mall_admin/`

- Java/Solon 源码、Maven `pom.xml`、资源模板和管理端源码
- `script/init/01_schema.sql`、`02_base_data.sql`、`03_business_data.sql`、`04_patch_2025-04-09.sql`、`99_verify.sql`、`run_init.sh`、`tools/gen_schema.py`、`README.md`、`REPORT.md`
- 当前已完成但需一并复核的改动：根 `.gitignore`、根 `README.md`、`README.md` 初始化说明、`snowy-plugin/snowy-plugin-shop/pom.xml` 依赖删除
- 脱敏后的配置示例和部署说明

### 用户端 `live_user_app/`

- Flutter `lib/`、`assets/`、平台工程、`pubspec.yaml` 和正式 `pubspec.lock`
- 当前 H5 支付改动及新增的 `lib/utils/web_pay_platform*.dart`
- `third_plugin/` 中应用运行所需的源码和必要二进制依赖；逐项确认第三方 SDK 的公开分发许可
- 排除 `build/`、`.dart_tool/`、`android/.gradle/`、本地 `local.properties`、签名配置、`pubspec.lock.before_flutter_347`

### H5 `live_h5/`

- `App.vue`、`pages/`、`static/`、`main.js`、`pages.json`、`manifest.json`、`uni.scss`
- `unpackage/dist/Dockerfile` 与 `nginx.conf` 作为部署模板保留
- 新增 `package.json` / 锁文件（如果后续补齐 uni-app 构建工程）
- 排除 `unpackage/dist/build/` 构建产物、`unpackage/dist/cache/` 缓存和 `.idea/`
- H5 当前没有可复现的构建脚本；首个提交不包含构建产物，部署前需要先由 HBuilderX 或后续补齐的构建流程生成 `build/web/`

## 当前各项目工作树摘要

- `live_mall_admin`：已修改 `.gitignore`、`README.md`、`snowy-plugin/snowy-plugin-shop/pom.xml`，并新增未跟踪的 `script/init/`。
- `live_user_app`：有 Android/Flutter/Web 支付相关改动和 3 个新平台适配文件；`android/key.properties` 虽然是敏感文件但已存在于旧 Git 跟踪中。
- `live_h5`：工作树干净，但旧仓库已跟踪 `unpackage/` 构建输出和 `.idea/` 文件；根仓库迁移时应从首个提交中排除。

## 建议的实际提交顺序

1. 轮换已暴露的数据库、Redis、OpenIM、微信支付、快递鸟、签名和 API 密钥。
2. 生成脱敏示例配置，清理公开提交历史中的敏感路径和线上数据。
3. 在根目录执行一次 `git add -n`，人工核对没有证书、私钥、环境文件、构建产物和真实业务数据。
4. 创建 monorepo 初始提交：`chore: initialize live shop monorepo`。
5. 本地完成构建/静态检查后，再单独确认远端仓库分支、保护规则和推送方案。

本清单不代表敏感项已经修复；它记录的是当前发现结果和提交前门槛。
