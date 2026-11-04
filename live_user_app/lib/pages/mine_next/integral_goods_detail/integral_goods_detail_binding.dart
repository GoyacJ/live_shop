import 'package:get/get.dart';

import 'integral_goods_detail_logic.dart';

class IntegralGoodsDetailBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => IntegralGoodsDetailLogic());
  }
}
