import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../models/feedback.dart';

class FeedbackListLogic extends BaseController {
  RxList<FeedbackEntity> list = <FeedbackEntity>[].obs;
  RefreshController refreshController = RefreshController(initialRefresh: false);

  @override
  initData()async {
    _initData();
  }

  _initData() async {
    final Map<String, dynamic> result = await NetUtil.getFeedbackList();
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      list.value = result['data'].map<FeedbackEntity>((item) {
        return FeedbackEntity.fromJson(item);
      }).toList();
      print(list[2].replyMessage);
      update();
    }

  }

  void onRefresh() async{
    print("下拉刷新");
    await _initData();
    // if failed,use refreshFailed()
    refreshController.refreshCompleted();
  }

  @override
  void onClose() {
    refreshController.dispose();
    super.onClose();
  }
}
