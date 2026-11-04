import 'package:common_utils/common_utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../models/goods_model.dart';
import '../../models/topic_model.dart';
import '../../services/user_info_ctl.dart';
import '../../utils/config_util.dart';
import '../../utils/sp_util.dart';
import '../../widgets/dialog/privacy_pollicy_dialog.dart';

class MallLogic extends BaseController {
  // 添加刷新控制器
  final RefreshController refreshController = RefreshController();
  List<TopicModel> swiperList = [];
  List<TopicModel> topicList = [];
  List<GoodsModel> goodsList = [];
  var current = 1.obs;

  @override
  Future<void> initData() async {
    // 只在初始化时调用一次getSearchList
    await getData();
    Future.delayed(Duration(milliseconds: 500));
    await getSearchList();
  }

  getData() async {
    await getTopicList();
    // 异步执行非关键操作，不阻塞页面显示
    Future.microtask(() async {
      bool isAgreement =
          SpUtil.getData(ConfigUtil.isAgreement, defaultValue: false) ?? false;
      if (!isAgreement) {
        // 在显示弹框前，确保数据加载完成且页面稳定
        await Future.delayed(const Duration(milliseconds: 200));
        showPrivacyDiaLog();
      }
    });
  }

  ///隐私协议弹窗
  showPrivacyDiaLog() async {
    await Get.dialog(
      PopScope(
        canPop: false,
        onPopInvokedWithResult: (didPop, result) async {
          if (!didPop) {
            // 用户按返回键，我们直接退出应用
            SystemNavigator.pop(); // Android 直接退出 App
          }
        },
        child: PrivacyPolicyDialog(
          onAgree: () {
            Get.put<UserInfoCtl>(UserInfoCtl());
          },
        ),
      ),
      barrierDismissible: false,
    );
  }

  getTopicList() async {
    var result = await NetUtil.topicList();
    if (result['code'] == 200) {
      final List<dynamic> dataList = result['data'];
      List<TopicModel> mData = dataList.map((e) => TopicModel.fromJson(e)).toList();

      if (!ObjectUtil.isEmptyList(mData)) {
        // 新的 swiperList / topicList
        List<TopicModel> newSwiperList = [];
        List<TopicModel> newTopicList = [];

        for (int i = 0; i < mData.length; i++) {
          if (mData[i].carousel == 0) {
            newSwiperList.add(mData[i]);
          } else {
            newTopicList.add(mData[i]);
          }
        }

        // 对比是否与原有数据相同（你可以根据 id 或模型实现 == 操作符）
        if (!listEquals(swiperList, newSwiperList) || !listEquals(topicList, newTopicList)) {
          swiperList = newSwiperList;
          topicList = newTopicList;
          update(); // 仅当数据真的变化时更新 UI
        }
      }
    }
  }


  @override
  Future<void> refreshData() async {
    // 重置页码
    current.value = 1;
    // 调用父类的refreshData，它会调用initData
    await super.refreshData();
  }

  Future<void> getSearchList({bool isRefresh = true}) async {
    try {
      Map<String,dynamic>jsonMap = {};
      jsonMap.putIfAbsent('current', ()=>current.value);
      jsonMap.putIfAbsent('size', ()=>10);
      var result = await NetUtil.searchList(data: jsonMap);
      if (result['code'] == 200) {
        if(ObjectUtil.isNotEmpty(result['data'])){
        final List<dynamic> dataList = result['data']['records'];
        List<GoodsModel> newData = dataList.map((e) => GoodsModel.fromJson(e)).toList();
        if (isRefresh) {
          // 刷新操作：清空数据
          current.value = 1;
          goodsList.clear();
          goodsList.addAll(newData);
          refreshController.refreshCompleted();
        } else {
          // 加载更多操作：追加数据
          goodsList.addAll(newData);
          if (ObjectUtil.isEmptyList(newData) || newData.length < 10) {
            refreshController.loadNoData();
          } else {
            refreshController.loadComplete();
          }
        }
        update();
        }
      }
    } catch (e) {

      refreshController.loadFailed();
      refreshController.refreshFailed();
    }
  }
}
