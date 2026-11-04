import 'package:get/get.dart';

import 'address_form_logic.dart';

class AddressFormBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => AddressFormLogic());
  }
}
