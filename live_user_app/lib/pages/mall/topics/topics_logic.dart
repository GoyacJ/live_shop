import 'package:common_utils/common_utils.dart';
import 'package:get/get.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../models/goods_model.dart';

class TopicsLogic extends BaseController {
  // 添加刷新控制器
  final RefreshController refreshController = RefreshController(initialRefresh: true);
  int selectedIndex = 0; // 0: 综合, 1: 销量, 2: 价格
  bool isAsc = false; // 仅销量和价格使用，true 升序，false 降序
  final List<String> tabs = ['综合', '销量', '价格'];
  String title = '';

  // setter
  var currentPage = 1.obs;
  final int pageSize = 10;
  List<GoodsModel> goodsList = [];

  @override
  Future<void> initData() async {
    // TODO: implement initData
    title = Get.arguments['title'];
    // getData();
  }

  Future<void> getData() async {
    try {
      Map<String, dynamic> jsonMap = {};
      switch (selectedIndex) {
        case 1:
          jsonMap.putIfAbsent('sales', () => isAsc ? 'ascend' : 'descend');
          break;
        case 2:
          jsonMap.putIfAbsent('price', () => isAsc ? 'ascend' : 'descend');
          break;
        case 0:
        default:
          jsonMap.putIfAbsent('sort', () => 'descend');
          break;
      }
      jsonMap.putIfAbsent('current', () => currentPage.value);
      jsonMap.putIfAbsent('size', () => pageSize);
      jsonMap.putIfAbsent('specialTopicId', () => Get.arguments['id']);
      var result = await NetUtil.topicGoodsList(jsonMap);
      if (result['code'] == 200) {
        if(ObjectUtil.isNotEmpty(result['data'])){
        final List<dynamic> dataList = result['data']['records'];
        List<GoodsModel> newData = dataList.map((e) => GoodsModel.fromJson(e)).toList();
        if (currentPage.value == 1) {
          goodsList.clear();
        }
        if (ObjectUtil.isEmptyList(newData)) {
          refreshController.loadNoData();
        } else {
          goodsList.addAll(newData);
          if (newData.length < pageSize) {
            refreshController.loadNoData();
          }
        }
        refreshController.loadComplete();
        refreshController.refreshCompleted();
        update();
        }
      }
    } catch (e) {
      refreshController.loadFailed();
      refreshController.refreshFailed();
    }
  }

  onRefresh() async {
    currentPage.value = 1;
    await getData();
  }

  onLoading() async {
    currentPage.value++;
    await getData();
  }

  @override
  void onClose() {
    refreshController.dispose();
    super.onClose();
  }
}
