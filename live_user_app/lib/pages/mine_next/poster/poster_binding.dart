import 'package:get/get.dart';

import 'poster_logic.dart';

class PosterBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => PosterLogic());
  }
}
