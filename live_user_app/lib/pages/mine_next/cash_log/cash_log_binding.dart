import 'package:get/get.dart';

import 'cash_log_logic.dart';

class CashLogBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => CashLogLogic());
  }
}
