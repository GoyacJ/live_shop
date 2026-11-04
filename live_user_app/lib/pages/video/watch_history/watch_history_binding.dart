import 'package:get/get.dart';

import 'watch_history_logic.dart';

class WatchHistoryBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => WatchHistoryLogic());
  }
}
