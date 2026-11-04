import 'package:get/get.dart';

import 'integral_logic.dart';

class IntegralBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => IntegralLogic());
  }
}
