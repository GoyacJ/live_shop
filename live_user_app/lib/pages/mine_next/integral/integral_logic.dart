import 'dart:convert';

import 'package:common_utils/common_utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/services/user_info_ctl.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../models/cash_log.dart';
import '../../../models/integral_record_model.dart';
import '../../../utils/config_util.dart';

class IntegralLogic extends BaseController {
  int _page = 1;
  int _total = -1;
  RxList<IntegralRecordModel> list = <IntegralRecordModel>[].obs;
  final TextEditingController integralTextController = TextEditingController();
  RefreshController refreshController = RefreshController(initialRefresh: false);

  @override
  initData() async{
    // TODO: implement initData
    _initData();
  }

  _initData() async {
    if (_total>-1 && _page>_total) return;

    final Map<String, dynamic> result = await NetUtil.getIntegralChange(_page);
    var jsonStr = jsonEncode(result);
    if(ObjectUtil.isNotEmpty(result['data'])) {
      if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
        final resultList = result['data']['records'].map<IntegralRecordModel>((
            item) {
          return IntegralRecordModel.fromJson(item);
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

  @override
  void onClose() {
    refreshController.dispose();
    super.onClose();
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

  // 兑换积分弹框
  void onWithdrawal() async {
    if (UserInfoCtl.to.profile.integral != 0) {
      Get.dialog(
          barrierDismissible: false,
          PopScope(
            canPop: false,
            child: AlertDialog(
              backgroundColor: ColorUtil.white,
              title: Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text('积分兑换现金', style: TextStyle(fontSize: 18.0, color: ColorUtil.black, fontWeight: FontWeight.bold)),
                  const Spacer(),
                  InkWell(
                    onTap: () {
                      Get.back();
                    },
                    child: const Icon(Icons.close, color: ColorUtil.grey, size: 24.0),
                  ),
                ],
              ),
              content: Container(
                width: 350.w,
                height: 200.0,
                margin: const EdgeInsets.only(top: 10.0),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    TextField(
                      keyboardType: TextInputType.number,
                      controller: integralTextController,
                      style: const TextStyle(color: ColorUtil.black, fontSize: 18.0),
                      cursorColor: ColorUtil.black26,
                      decoration: InputDecoration(
                        hintText: '请输入积分数量',
                        fillColor: ColorUtil.grey[200],
                        filled: true,
                        hintStyle: const TextStyle(color: ColorUtil.black26),
                        contentPadding: const EdgeInsets.symmetric(horizontal: 12.0),
                        border: const OutlineInputBorder(borderSide: BorderSide.none),
                      ),
                    ),
                    const SizedBox(height: 10.0),
                    Text('积分余额: ${UserInfoCtl.to.profile.integral}', style: TextStyle(fontSize: 16.0, color: ColorUtil.primaryColor)),
                    const SizedBox(height: 30.0),
                    const Center(
                      child: Text("不足100积分的将原路返回", style: TextStyle(fontSize: 14.0, color: ColorUtil.grey)),
                    ),
                    const SizedBox(height: 10.0),
                    InkWell(
                      onTap: onExchange,
                      child: Container(
                          width: double.infinity,
                          height: 40.0,
                          decoration: BoxDecoration(
                            color: ColorUtil.primaryColor,
                            borderRadius: BorderRadius.circular(4.0),
                          ),
                          child: const Center(
                            child: Text('立即兑换', style: TextStyle(fontSize: 16.0, color: ColorUtil.white)),
                          )
                      ),
                    )
                  ],
                ),
              ),
            ),
          )
      );
    } else {
      ToastUtil.showToast("暂无积分可兑换");
    }
  }

  // 兑换积分
  void onExchange() async {
    if (integralTextController.text.isEmpty) {
      ToastUtil.showToast("请输入积分数量");
      return;
    }
    if (UserInfoCtl.to.profile.integral < (NumUtil.getIntByValueStr(integralTextController.text) ?? 0)) {
      ToastUtil.showToast("剩余积分不足");
      return;
    }
    if (UserInfoCtl.to.profile.integral < 100) {
      ToastUtil.showToast("积分不足兑换最低限制数量");
      return;
    }
  }
}
