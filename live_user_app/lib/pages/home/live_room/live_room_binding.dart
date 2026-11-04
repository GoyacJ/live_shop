import 'package:get/get.dart';

import 'live_room_logic.dart';

class LiveRoomBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => LiveRoomLogic());
  }
}
