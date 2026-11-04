import 'package:get/get.dart';

import 'get_integral_logic.dart';

class GetIntegralBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => GetIntegralLogic());
  }
}
