import 'package:get/get.dart';

import 'my_setting_logic.dart';

class MySettingBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => MySettingLogic());
  }
}
