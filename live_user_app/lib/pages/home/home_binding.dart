import 'package:get/get.dart';

import '../home/home_logic.dart';

class HomeBinding extends Bindings {
  @override
  void dependencies() {
    // 主页面控制器
    Get.lazyPut(() => HomeLogic());
  }
}
