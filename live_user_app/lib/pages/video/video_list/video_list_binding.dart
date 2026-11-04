import 'package:get/get.dart';

import 'video_list_logic.dart';

class VideoListBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => VideoListLogic());
  }
}
