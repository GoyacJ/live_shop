# 颐心相伴直播

一款基于 Flutter 开发的直播应用，集成了直播、社交、电商等功能。

## 项目介绍

本项目是一个功能完整的直播应用，主要面向用户提供直播观看、互动社交、商品购买等服务。

### 主要功能

- 直播功能
  - 直播间观看
  - 实时互动聊天
  - 礼物打赏系统
  - 直播间商品展示
- 社交功能
  - 即时通讯
  - 用户关注
  - 消息通知
- 商城系统
  - 商品浏览购买
  - 订单管理
  - 支付功能
- 用户中心
  - 个人信息管理
  - 收货地址管理
  - 反馈系统

### 技术特点

- 基于 Flutter 跨平台开发
- 使用 GetX 进行状态管理
- 集成 AliPlayer 视频播放器
- 整合 OpenIM 即时通讯功能
- 支持多平台支付功能

## 开发环境

- Flutter 3.0+
- Dart 2.17+
- Android Studio / VS Code
- Android SDK
- iOS SDK (用于 iOS 开发)

## 项目结构

## 注意事项
- 使用flutter_openim_sdk插件需要在项目中找到该插件在android目录中的build.gradle文件中增加 namespace = "io.openim.flutter_openim_sdk"
