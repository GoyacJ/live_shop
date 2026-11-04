import 'package:get/get.dart';

import 'update_avatar_logic.dart';

class UpdateAvatarBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => UpdateAvatarLogic());
  }
}
