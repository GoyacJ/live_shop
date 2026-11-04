import 'package:get/get.dart';

import 'integral_order_list_logic.dart';

class IntegralOrderListBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => IntegralOrderListLogic());
  }
}
