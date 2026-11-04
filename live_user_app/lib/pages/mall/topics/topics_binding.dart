import 'package:get/get.dart';

import 'topics_logic.dart';

class TopicsBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => TopicsLogic());
  }
}
