import 'package:get/get.dart';

import 'user_promotion_logic.dart';

class UserPromotionBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => UserPromotionLogic());
  }
}
