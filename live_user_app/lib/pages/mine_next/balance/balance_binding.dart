import 'package:get/get.dart';

import 'balance_logic.dart';

class BalanceBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => BalanceLogic());
  }
}
