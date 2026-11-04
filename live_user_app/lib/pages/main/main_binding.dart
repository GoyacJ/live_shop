import 'package:get/get.dart';

import '../get_integral/get_integral_logic.dart';
import '../home/home_logic.dart';
import '../mall/mall_logic.dart';
import '../mine/mine_logic.dart';
import '../video/video_logic.dart';
import 'main_logic.dart';

/// MainBinding
/// 主页面的依赖注入绑定
/// 负责初始化主页面及其子页面所需的所有控制器
class MainBinding extends Bindings {
  @override
  void dependencies() {
    // 主页面控制器
    Get.lazyPut(() => MainLogic());
    
    // 子页面控制器 - 懒加载方式注入
    Get.lazyPut(() => MallLogic());
    Get.lazyPut(() => GetIntegralLogic());
    Get.lazyPut(() => MineLogic());
  }
}
