import 'package:common_utils/common_utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../models/promotion_user.dart';

class UserPromotionLogic extends BaseController {
  RxInt recCount = 0.obs;
  RxDouble salesAmount = 0.00.obs;
  RxInt tabIndex = 0.obs; // 选项卡索引：0-全部，1-今日，2-昨日 3自定义时间
  RxString selectYm = ''.obs;

  RxList<PromotionUserEntity> list = <PromotionUserEntity>[].obs;
  int _page = 1;
  int _total = -1;

  final TextEditingController integralTextController = TextEditingController();
  RefreshController refreshController = RefreshController(initialRefresh: false);


  @override
  initData() async{
    _initData();
  }

  // 设置选项卡索引并刷新数据
  void setTabIndex(int index) {
    if (tabIndex.value == index) return;
    tabIndex.value = index;
    _page = 1;
    _initData();
  }

  _initData() async {
    if (_total>-1 && _page>_total) return;
    Map<String,dynamic>jsonMap={};
    jsonMap.putIfAbsent('current', ()=>_page);
    jsonMap.putIfAbsent('size', ()=>10);

    if(tabIndex.value==1){
     var date = ConfigUtil.getTodayStartAndEnd();
     jsonMap.putIfAbsent('startTime', ()=>date['startTime']);
     jsonMap.putIfAbsent('endTime', ()=>date['endTime']);
    }

    if(tabIndex.value==2){
      var date = ConfigUtil.getYesterdayStartAndEnd();
      jsonMap.putIfAbsent('startTime', ()=>date['startTime']);
      jsonMap.putIfAbsent('endTime', ()=>date['endTime']);
    }

    if(tabIndex.value==3){
      var date = ConfigUtil.getMonthFirstAndLastDay(selectYm.value);
      jsonMap.putIfAbsent('startTime', ()=>date['startTime']);
      jsonMap.putIfAbsent('endTime', ()=>date['endTime']);
    }

    final Map<String, dynamic> result = await NetUtil.getUserPromotion(jsonMap);
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      final resultList = result['data']['promotionDetails']['records'].map<PromotionUserEntity>((item) {
        return PromotionUserEntity.fromJson(item);
      }).toList();

      if (_page == 1) {
        list.value = resultList;
        salesAmount.value = result['data']['promotionDetails']['inviteNum']??0;
      } else {
        list.addAll(resultList);
      }
      _total = list.isEmpty ? -1 : result['data']['promotionDetails']['pages'];
      recCount.value = NumUtil.getIntByValueStr(result['data']['promotionDetails']['total'].toString()) ?? 0;
    }

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
    _page ++;
    await _initData();
    refreshController.loadComplete();
    if (_total>-1 && _page>_total) {
      refreshController.loadNoData();
    }
  }

  @override
  void onClose() {
    refreshController.dispose();
    super.onClose();
  }
}
