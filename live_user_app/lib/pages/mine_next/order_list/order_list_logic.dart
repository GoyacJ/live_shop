import 'package:flutter/material.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:get/get.dart';


class OrderListLogic extends BaseController {
  var selectedIndex = 0.obs;
  TabController? tabController;

  final List orderStatusList = [
    // 订单状态：待付款、待发货、待收货、已完成、售后，需要小图标、标题、各状态订单数量
    {
      'title': '全部',
      'value': -1,
    },
    {
      'title': '待付款',
      'value': 0,
    },
    {
      'title': '待发货',
      'value': 1,
    },
    {
      'title': '待收货',
      'value': 2,
    },
    {
      'title': '已完成',
      'value': 4,
    },
    {
      'title': '售后',
      'value': 5,
    },

  ];


  @override
  initData() async{
    tabController = TabController(vsync: this, length: orderStatusList.length);
    int index = Get.arguments;
    tabController!.animateTo(index); // 切换到第二个 tab
  }

  @override
  void onClose() {
    tabController?.dispose();
    super.onClose();
  }
}
