import 'package:get/get.dart';

import 'address_list_logic.dart';

class AddressListBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => AddressListLogic());
  }
}
