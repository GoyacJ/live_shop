import 'dart:async';

import 'package:common_utils/common_utils.dart';
import 'package:flutter/foundation.dart';
import 'package:get/get.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/services/user_info_ctl.dart';

import '../../init/config.dart';
import '../../models/task_model.dart';
import '../../routes/app_pages.dart';
import '../../utils/config_util.dart';
import '../../utils/log_utils.dart';
import '../../utils/toast_util.dart';
import '../main/main_logic.dart';

class GetIntegralLogic extends BaseController {
  List<TaskModel> mData = [];
  bool isUpdateUserInfo = false;

  @override
  initData() async {
    isUpdateUserInfo = false;
    await getTaskList();
  }

  getTaskList() async {
    var result = await NetUtil.getTaskList();
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      final List<dynamic> dataList = result['data'];
      mData = dataList.map((e) => TaskModel.fromJson(e)).toList();
    }
    if(isUpdateUserInfo){
      UserInfoCtl.to.getProfile();
    }
    update();
  }

  signIn() async {
    try {
      showEasyLoading();
      var result = await NetUtil.signIn();
      if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
        isUpdateUserInfo = true;
        getTaskList();
        dismissEasyLoading();
      } else {
        dismissEasyLoading();
      }
    } catch (e) {
      LogUtils.e(e);
      dismissEasyLoading();
    }
  }

  integralAd(taskId) async {
    LogUtils.e('广告观看完成');
    NetUtil.integralAd(taskId).then((v) {
      isUpdateUserInfo = true;
      getTaskList();
    });
  }

  customerWechat() async {
    NetUtil.customerWechat().then((v) {
      isUpdateUserInfo = true;
      getTaskList();
    });
  }

  // 根据类型跳转相应的页面
  goPageByType(int index) {
    TaskModel item = mData[index];
    if (item.isComplete != 1) return; // 如果任务未完成，直接返回

    switch (item.taskBusinessCategory) {
      case '101':
        // 注册
        if (!UserInfoCtl.to.isLogin) {
          Get.toNamed(AppPages.login, arguments: 'isShare')?.then((v) {
            if (v != null) {
              isUpdateUserInfo = true;
              getTaskList();
            }
          });
        }

        break;
      case '102':
        // 添加客服微信
        Get.toNamed(AppPages.contactPage, arguments: {'isShare': true})
            ?.then((v) {
          if (v != null) {
            customerWechat();
          }
        });
        break;
      case '201':
      case '202':
        // 每日签到/连续签到
        if (UserInfoCtl.to.isLogin) {
          signIn();
        } else {
          ToastUtil.showToast('请登录');
        }
        break;
      case '301':
      case '302':
      case '303':
      case '304':
        // 商品浏览/购买/分享/评论
        if (!ObjectUtil.isEmptyString(item.jumpRouting)) {
          String id = item.jumpRouting ?? '';
          Get.toNamed(AppPages.goodsDetailPage,
              arguments: {'id': id, 'isShare': true})?.then((v) {
            if (v != null) {
              isUpdateUserInfo = true;
              getTaskList();
            }
          });
        } else {
          MainLogic logic = Get.find();
          logic.switchTab(1);
        }
        break;
      // 直播浏览/购买/分享/评论
      case '401':
      case '402':
      case '403':
      case '404':
      case '405':
      case '406':
      case '407':
      case '408':
        if(!UserInfoCtl.to.isLogin){
          ToastUtil.showToast('请登录');
          return;
        }
        if(kIsWeb){
          ToastUtil.showToast('请在app中打开');
          return;
        }
        if (!ObjectUtil.isEmptyString(item.jumpRouting)) {
          String id = item.jumpRouting ?? '';
          Get.toNamed(AppPages.liveRoom, arguments: {'id': id, 'isShare': true})
              ?.then((v) {
            if (v != null) {
              isUpdateUserInfo = true;
              getTaskList();
            }
          });
        } else {
          MainLogic logic = Get.find();
          logic.switchTab(0);
        }
        break;
      // 视频浏览/购买/分享/评论
      case '501':
      case '502':
      case '503':
      case '504':
      case '505':
      if(!UserInfoCtl.to.isLogin){
        ToastUtil.showToast('请登录');
        return;
      }
      if(kIsWeb){
        ToastUtil.showToast('请在app中打开');
        return;
      }
        if (!ObjectUtil.isEmptyString(item.jumpRouting)) {
          String id = item.jumpRouting ?? '';
          Get.toNamed(AppPages.videoChildPage,
              arguments: {'type': 1, 'id': id, 'isShare': true});
        } else {
          MainLogic logic = Get.find();
          logic.switchTab(2);
        }
        break;
      case '551':
        if(kIsWeb){
          ToastUtil.showToast('请在app中打开');
          return;
        }
        // 短剧
        if (!ObjectUtil.isEmptyString(item.jumpRouting)) {
          String id = item.jumpRouting ?? '';
          Get.toNamed(AppPages.videoChildPage,
              arguments: {'type': 1, 'id': id, 'isShare': true});
        } else {
          Get.toNamed(AppPages.videoListPage);
        }
        break;
      case '601':
        if(!UserInfoCtl.to.isLogin){
          ToastUtil.showToast('请登录');
          return;
        }
        if(kIsWeb){
          ToastUtil.showToast('请在app中打开');
          return;
        }
        // 看广告
        if (!UserInfoCtl.to.isLogin) {
          ToastUtil.showToast('请登录');
          return;
        }
        break;
    }
  }
}
