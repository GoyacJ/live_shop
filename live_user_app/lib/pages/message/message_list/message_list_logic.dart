import 'package:live_app/base/controller/base_controller.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../models/cash_log.dart';

class MessageListLogic extends BaseController {

  RxString title = ''.obs;
  RxString type = ''.obs;
  RxList<CashLogEntity> list = <CashLogEntity>[].obs;
  int _page = 1;
  int _total = -1;

  RefreshController refreshController = RefreshController(initialRefresh: false);


  @override
  initData() async {
    // TODO: implement initData
    title.value = Get.arguments['title'];
    type.value = Get.arguments['type'];
    getData();
  }

  getData() async {
    if (_total>-1 && _page>_total) return;
    list.value = [];
    return;
  }

  void onRefresh() async{
    print("下拉刷新");
    _page = 1;
    await getData();
    // if failed,use refreshFailed()
    refreshController.refreshCompleted();
  }

  void onLoading() async{
    print("上拉加载");
    await getData();
    refreshController.loadComplete();
  }

  @override
  void onClose() {
    refreshController.dispose();
    super.onClose();
  }
}
