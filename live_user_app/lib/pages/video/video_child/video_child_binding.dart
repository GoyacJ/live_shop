import 'package:get/get.dart';

import 'video_child_logic.dart';

class VideoChildBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => VideoChildLogic());
  }
}