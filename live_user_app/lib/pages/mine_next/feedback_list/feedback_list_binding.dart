import 'package:get/get.dart';

import 'feedback_list_logic.dart';

class FeedbackListBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => FeedbackListLogic());
  }
}
