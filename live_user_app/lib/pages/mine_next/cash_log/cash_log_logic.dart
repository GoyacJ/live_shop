import 'package:common_utils/common_utils.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../models/cash_log.dart';

class CashLogLogic extends BaseController {
  RxList<CashLogEntity> list = <CashLogEntity>[].obs;
  int _page = 1;
  int _total = -1;

  RefreshController refreshController = RefreshController(initialRefresh: false);


  _initData() async {
    if (_total>-1 && _page>_total) return;

    final Map<String, dynamic> result = await NetUtil.getCashLog(_page);
    print("余额日志：$result");
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      if(ObjectUtil.isNotEmpty(result['data'])){
      final resultList = result['data']['records'].map<CashLogEntity>((item) {
        return CashLogEntity.fromJson(item);
      }).toList();

      if (_page == 1) {
        list.value = resultList;
      } else {
        list.addAll(resultList);
      }
      _page ++;
      _total = list.isEmpty ? -1 : result['data']['pages'];
      }
    }

    update();
  }

  @override
  initData() async{
    _initData();
  }

  void onRefresh() async{
    print("下拉刷新");
    _page = 1;
    await _initData();
    // if failed,use refreshFailed()
    refreshController.refreshCompleted();
  }

  void onLoading() async{
    print("上拉加载");
    await _initData();
    refreshController.loadComplete();
  }

  @override
  void onClose() {
    refreshController.dispose();
    super.onClose();
  }
}
