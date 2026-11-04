import 'package:common_utils/common_utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:get/get.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/utils/sp_util.dart';

import '../../models/goods_model.dart';
import '../../network/net_util.dart';
import '../../widgets/refresh_list.dart';

class SearchLogic extends BaseController {
  static const String _searchHistoryKey = 'search_history';
  List<GoodsModel> goodsList = [];
  List searchHistory = [];
  final TextEditingController searchController = TextEditingController();
  CustomListViewController listViewController = CustomListViewController();
  var current = 1.obs;
  @override
  Future<void> initData() async {
    // 初始化时加载数据
    // await handleRefresh();
    searchHistory = SpUtil.getData<List<String>>(_searchHistoryKey, defaultValue: []) ?? [];
  }

  Future<void> handleRefresh({bool isRefresh = true}) async {
    try {
      showEasyLoading();
      Map<String,dynamic>jsonMap = {};
      jsonMap.putIfAbsent('current', ()=>current.value);
      jsonMap.putIfAbsent('size', ()=>10);
      if(ObjectUtil.isNotEmpty(searchController.text.trim())){
        saveSearchRecord(searchController.text.trim());
        jsonMap.putIfAbsent('title', ()=>searchController.text.trim());
       }
      var result = await NetUtil.searchList(data: jsonMap);
      if (result['code'] == 200) {
        dismissEasyLoading();
        if(ObjectUtil.isNotEmpty(result['data'])){
          final List<dynamic> dataList = result['data']['records'];
          List<GoodsModel> newData = dataList.map((e) => GoodsModel.fromJson(e)).toList();
          if (isRefresh) {
            // 刷新操作：清空数据
            current.value = 1;
            goodsList.clear();
            goodsList.addAll(newData);
          } else {
            // 加载更多操作：追加数据
            goodsList.addAll(newData);
            if (ObjectUtil.isEmptyList(newData) || newData.length < 10) {
              listViewController.loadNoData();
            }
          }
          update();
        }
      }
    } catch (e) {
      dismissEasyLoading();
      print('刷新数据失败: $e');
    }
  }

  /// 保存搜索记录（会自动去重并保持最新记录在前）
  Future<void> saveSearchRecord(String keyword) async {
    List<String> history = SpUtil.getData<List<String>>(_searchHistoryKey, defaultValue: []) ?? [];
    // 去重：如果存在，先移除
    history.remove(keyword);
    history.insert(0, keyword); // 插入到最前面
    // 可限制最大记录数（例如最多保存10条）
    if (history.length > 10) {
      history = history.sublist(0, 10);
    }
    await SpUtil.setData<List<String>>(_searchHistoryKey, history);
    searchHistory = history;
    update();
  }

  cleanHistory(){
    searchHistory.clear();
    SpUtil.remove(_searchHistoryKey);
    update();
    Get.back();
  }

  Future<void> removeSearchRecord(String keyword) async {
    List<String> history = SpUtil.getData<List<String>>(_searchHistoryKey, defaultValue: []) ?? [];
    history.remove(keyword);
    await SpUtil.setData<List<String>>(_searchHistoryKey, history);
  }
}
