import 'package:get/get.dart';

import 'integral_goods_logic.dart';

class IntegralGoodsBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => IntegralGoodsLogic());
  }
}
