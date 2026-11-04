
import 'package:common_utils/common_utils.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../models/integral_goods.dart';
import '../../../utils/config_util.dart';

class IntegralGoodsLogic extends BaseController {
  RxList<IntegralGoodsEntity> list = <IntegralGoodsEntity>[].obs;
  int _page = 1;
  int _total = -1;
  List<String> userInfoStr = [];
  RefreshController refreshController = RefreshController(initialRefresh: false);

  @override
 initData() async {
   await getData();
  }

  getData()async{
   await getPurchaseUser();
   await _initData();
  }

  _initData() async {
    if (_total>-1 && _page>_total) return;
    final Map<String, dynamic> result = await NetUtil.getIntegralGoodsList(_page);
    print("积分商品：$result");
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      if(ObjectUtil.isNotEmpty(result['data'])) {
        final resultList = result['data']['records'].map<IntegralGoodsEntity>((
            item) {
          return IntegralGoodsEntity.fromJson(item);
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

  getPurchaseUser(){
    NetUtil.getPurchaseUser().then((value) {
      List<dynamic> dataList = value['data'];
      // 转成 List<String>
      userInfoStr = List<String>.from(dataList);
      update();
      print('成功返回：$userInfoStr');
    }, onError: (e) {
      print('出错了：$e');
    });
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
