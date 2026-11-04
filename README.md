# live_shop monorepo

本目录统一维护直播商城的三个客户端/服务端项目：

- `live_mall_admin/`：Java/Solon 后台服务、管理端前端和数据库脚本
- `live_user_app/`：Flutter 用户端，包含 Android、iOS、桌面端和 Web 适配
- `live_h5/`：uni-app H5 页面

目标远端仓库：<https://github.com/GoyacJ/live_shop>

## 当前迁移状态

三个项目原本各自带有 Git 仓库。现在由根目录 Git 仓库统一管理，三个旧 `.git` 已备份到工作区外（见 [`COMMIT_MANIFEST.md`](COMMIT_MANIFEST.md)），不会进入公开历史。目标远端仓库为 <https://github.com/GoyacJ/live_shop>。

提交前请先阅读 [`COMMIT_MANIFEST.md`](COMMIT_MANIFEST.md)。其中列出的支付证书、环境配置、签名配置、源码密钥和线上数据库快照不能直接进入公开仓库。

## 本地运行入口

后台服务和管理端的说明见 [`live_mall_admin/README.md`](live_mall_admin/README.md)，Flutter 用户端见 [`live_user_app/README.md`](live_user_app/README.md)。H5 项目是 uni-app 工程，构建产物 `unpackage/` 被根级 `.gitignore` 排除，应从源码重新构建。

数据库初始化脚本位于 [`live_mall_admin/script/init/`](live_mall_admin/script/init/)，其中 schema 和校验脚本可以作为迁移基础；演示数据与线上快照需要先脱敏和拆分。

## 本地敏感配置

公开仓库只保留 `*.example` 模板。首次克隆后按需复制并填入本地值：

- `live_mall_admin/snowy-web-app/src/main/resources/app.example.yml` → `app.yml`
- `live_mall_admin/snowy-web-app/src/main/resources/app-dev.example.yml` → `app-dev.yml`
- `live_mall_admin/snowy-web-app/src/main/resources/pay/wx/wxpay_v3.example.properties` → `wxpay_v3.properties`
- `live_mall_admin/snowy-admin-web/.env.example` → `.env.development` 或 `.env.production`
- `live_user_app/lib/init/config.example.dart` → `config.dart`
- `live_user_app/android/key.properties.example` → `key.properties`

微信支付私钥、平台证书、数据库密码、签名库和线上数据快照都不进入 Git；部署时由环境变量或服务器本地文件提供。
