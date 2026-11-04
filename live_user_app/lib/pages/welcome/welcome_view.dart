/// 欢迎页面开发规范：
/// 1. 所有颜色使用 ColorUtil 类中定义的颜色，禁止硬编码颜色值
/// 2. 所有文本组件使用 JhText.cText() 方法创建
/// 3. 布局尺寸使用 ScreenUtil 进行适配 (例如：16.sp, 10.w, 8.w)
/// 4. 状态管理使用 GetX 框架
/// 5. 页面跳转使用 Get.offAllNamed() 清除路由栈
/// 6. 启动屏广告使用 SplashAdView 组件

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:flutter/foundation.dart';
import 'package:live_app/routes/app_pages.dart';

import '../../init/config.dart';
import 'welcome_logic.dart';

/// 欢迎页面 - 应用启动页
/// 
/// 功能说明：
/// - 显示启动屏广告
/// - 处理广告加载、显示、点击、关闭等事件
/// - 广告结束后跳转至主页面
/// - 支持 Android 和 iOS 平台不同的广告 ID
class WelcomePage extends BaseStatefulWidget<WelcomeLogic> {
  const WelcomePage({super.key});

  /// 重写基类方法：隐藏返回按钮
  /// 启动页不需要返回按钮
  @override
  bool showBackButton() {
    return false;
  }

  /// 重写基类方法：隐藏应用栏
  /// 启动页需要全屏显示广告
  @override
  bool showAppBar() {
    // TODO: implement showAppBar
    return false;
  }

  /// 重写基类方法：显示顶部状态栏
  /// 保留系统状态栏显示
  @override
  bool showTopBar() {
    // TODO: implement showTopBar
    return true;
  }

  /// 构建页面主要内容
  /// 
  /// 功能：
  /// 1. 获取屏幕尺寸用于广告显示
  /// 2. 根据平台选择合适的广告 ID
  /// 3. 初始化并显示启动屏广告
  /// 4. 处理广告各种状态回调
  @override
  Widget buildContent(BuildContext context) {
    // 获取屏幕尺寸 - 用于广告适配
    final width = MediaQuery.of(context).size.width; //宽度 411.4
    final height = MediaQuery.of(context).size.height; //高度 683.4

    // 根据平台设置不同的广告 ID
    // var adId = 'sjmad_test002'; // Android 默认测试广告 ID (sjmad_test009 sjmad_test002)
    // if (defaultTargetPlatform == TargetPlatform.iOS) {
    //   adId = "SJM_S001"; // iOS 平台广告 ID
    // }

    // 返回启动屏广告组件
    return Image.asset('assets/images/iv_splash_bg.jpg', width: width, height: height, fit: BoxFit.cover);
  }

}
