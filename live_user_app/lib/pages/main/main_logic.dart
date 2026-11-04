import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:live_app/pages/video/video_view.dart';

import '../../base/controller/base_controller.dart';
import '../get_integral/get_integral_view.dart';
import '../home/home_view.dart';
import '../mall/mall_view.dart';
import '../mine/mine_view.dart';

/// MainLogic
/// 主页面的业务逻辑控制器
/// 负责管理底部导航、页面切换等功能
class MainLogic extends BaseController {
  DateTime? lastPopTime;
  /// 当前选中的底部导航索引
  final currentIndex = 0.obs;
  
  /// 页面控制器，用于管理页面切换
  final PageController pageController = PageController();

  /// 主页面包含的所有子页面
  final List<Widget> pages = [
    const MallPage(),
    const GetIntegralPage(),
    const MinePage(),
  ];

  /// 切换底部导航标签
  /// [index] 目标页面索引
  void switchTab(int index) {
    // 如果点击当前页面，不做处理
    if (currentIndex.value == index) return;
    // if (index== 2) return;
    // 更新索引并切换页面
    currentIndex.value = index;
    pageController.jumpToPage(index);
  }

  @override
  void onClose() {
    // 释放页面控制器资源
    pageController.dispose();
    super.onClose();
  }
}