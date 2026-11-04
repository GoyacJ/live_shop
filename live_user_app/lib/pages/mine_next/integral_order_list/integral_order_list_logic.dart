import 'package:common_utils/common_utils.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../models/order_list.dart';

class IntegralOrderListLogic extends BaseController {

  RxInt currentTab = 0.obs;
  RxList<OrderListEntity> list = <OrderListEntity>[].obs;
  RxBool isLoading = true.obs;

  int _page = 1;
  int _total = -1;
  List<String> statusList = [
    '待付款',
    '待发货',
    '待收货',
    '已签收',
    '已完成',
    '售后',
    '已取消'
  ];

  RefreshController refreshController = RefreshController(initialRefresh: false);

  @override
  initData()async{
   await _initData();
  }

  _initData() async {
    if (_total>-1 && _page>_total) return;

    final Map<String, dynamic> result = await NetUtil.getIntegralOrderList(_page);
    isLoading.value = false;
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      if(ObjectUtil.isNotEmpty(result['data'])){
        final resultList = result['data']['records'].map<OrderListEntity>((item) {
          return OrderListEntity.fromJson(item);
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

    print("长度：${list.length}");

    update();
  }

  void onRefresh() async{
    print("下拉刷新");
    _page = 1;
    await _initData();
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
