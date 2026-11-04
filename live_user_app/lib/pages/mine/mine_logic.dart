import 'package:flutter/material.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/services/user_info_ctl.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:get/get.dart';

import '../../utils/color_util.dart';
import '../../utils/config_util.dart';

class MineLogic extends BaseController {
  RxList list = [].obs;

  final List orderStatusList = [
    // 订单状态：待付款、待发货、待收货、已完成、售后，需要小图标、标题、各状态订单数量
    {
      'title': '待付款',
      'value': 0,
      'icon': Icons.payment,
    },
    {
      'title': '待发货',
      'value': 1,
      'icon': Icons.local_shipping,
    },
    {
      'title': '待收货',
      'value': 2,
      'icon': Icons.email_outlined,
    },
    {
      'title': '已完成',
      'value': 4,
      'icon': Icons.check_circle,
    },
    {
      'title': '售后',
      'value': 5,
      'icon': Icons.history,
    },

  ];

  final List serviceList = [
    {
      'title': '邀请好友',
      'icon': Icons.share_outlined,
      'color': ColorUtil.successGreen,
      'nav': AppPages.posterPage
    },
    {
      'title': '我的推广',
      'icon': Icons.card_giftcard,
      'color': ColorUtil.warningYellow,
      'nav': AppPages.userPromotionPage
    },
    {
      'title': '收货地址',
      'icon': Icons.location_on,
      'color': ColorUtil.infoBlue,
      'nav': AppPages.addressListPage
    },
    {
      'title': '联系客服',
      'icon': Icons.contact_support_outlined,
      'color': ColorUtil.dangerRed,
      'nav': AppPages.contactPage
    },
    // {
    //   'title': '我的消息',
    //   'icon': Icons.notifications,
    //   'color': ColorUtil.warningYellow,
    //   'nav': ''
    // },
    // {
    //   'title': '常见问题',
    //   'icon': Icons.help,
    //   'color': ColorUtil.infoBlue,
    //   'nav': '/live_test'
    // },
    {
      'title': '意见反馈',
      'icon': Icons.help,
      'color': ColorUtil.infoBlue,
      'nav': AppPages.feedbackPage
    },
    {
      'title': '我的设置',
      'icon': Icons.settings,
      'color': ColorUtil.successGreen,
      'nav': AppPages.mySettingPage
    }
  ];

  onEditNickname(String name) async {
    var result = await NetUtil.updateUserInfo({
      "nickname": name
    });

    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      final user = UserInfoCtl.to.profile;
      user.nickname = name;
      UserInfoCtl.to.saveProfile(user.toJson());
      ToastUtil.showToast("修改成功");

      Get.back();
    }
  }

  @override
  initData() {
    // TODO: implement initData
    UserInfoCtl.to.getProfile();
  }
}
