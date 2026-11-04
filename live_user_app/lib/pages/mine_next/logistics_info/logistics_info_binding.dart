import 'package:get/get.dart';

import 'logistics_info_logic.dart';

class LogisticsInfoBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => LogisticsInfoLogic());
  }
}
