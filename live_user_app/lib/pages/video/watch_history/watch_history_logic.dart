import 'package:flutter/material.dart';
import 'package:live_app/base/controller/base_controller.dart';

class WatchHistoryLogic extends BaseController {
  late TabController tabController;
  final List<Map<String, dynamic>> records = [
    {
      "title": "明月苍茫",
      "status": "追剧",
      "progress": "观看至1集",
      "cover": "https://example.com/cover1.jpg",
      "followed": false
    },
    {
      "title": "明月苍茫",
      "status": "已追剧",
      "progress": "观看至1集",
      "cover": "https://example.com/cover2.jpg",
      "followed": true
    }
  ];
  @override
  initData() async {
    // TODO: implement initData
    tabController = TabController(length: 2, vsync: this);
  }
}
