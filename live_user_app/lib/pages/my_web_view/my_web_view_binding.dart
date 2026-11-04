import 'package:get/get.dart';

import 'my_web_view_logic.dart';

class MyWebViewBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => MyWebViewLogic());
  }
}
