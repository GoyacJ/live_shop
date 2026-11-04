import 'package:get/get.dart';

import 'address_edit_logic.dart';

class AddressEditBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => AddressEditLogic());
  }
}
