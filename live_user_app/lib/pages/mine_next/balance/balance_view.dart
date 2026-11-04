import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/services/user_info_ctl.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

import '../../../widgets/my_list_tile.dart';
import 'balance_logic.dart';

class BalancePage extends BaseStatefulWidget<BalanceLogic> {
  const BalancePage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '账户余额';
  }

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(
      child: _buildView(),
    );
  }

  // 主视图
  Widget _buildView() {
    return Column(
      children: [
        Container(
          width: double.infinity,
          padding: const EdgeInsets.all(20.0),
          decoration: const BoxDecoration(
            color: ColorUtil.white,
          ),
          child: Container(
            height: 160.w,
            // margin: EdgeInsets.symmetric(vertical: 10.0, horizontal: ),
            decoration: const BoxDecoration(
                color: ColorUtil.primaryColor,
                borderRadius: BorderRadius.all(Radius.circular(6.0))
            ),
            child: Row(
              children: [
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Obx(() => Text(
                          UserInfoCtl.to.profile.cash.toStringAsFixed(2), style: const TextStyle(color: ColorUtil.white, fontSize: 24.0, fontWeight: FontWeight.bold))),
                     const Text("可用余额(元)", style: TextStyle(color: ColorUtil.white, fontSize: 16.0),)
                    ],
                  ),
                ),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Obx(() => Text(
                          UserInfoCtl.to.profile.frozenCash.toStringAsFixed(2), style: const TextStyle(color: ColorUtil.white, fontSize: 24.0, fontWeight: FontWeight.bold))),
                      const Text("冻结余额(元)", style: TextStyle(color: ColorUtil.white, fontSize: 16.0),)
                    ],
                  ),
                )
              ],
            ),
          ),
        ),
        const SizedBox(height: 10.0),
        MyListTile(
          leading: const Icon(Icons.account_balance_wallet_outlined, size: 24.0, color: ColorUtil.black),
          title: "提现",
          value: "",
          trailing: const Icon(Icons.arrow_forward_ios, size: 18.0, color: ColorUtil.grey),
          onClick: () {
            Get.toNamed(AppPages.withdrawalPage);
          },
        ),
        const Divider(height: .1, color: ColorUtil.backgroundColor, indent: 12.0),
        MyListTile(
            leading: const Icon(Icons.book_outlined, size: 24.0, color: ColorUtil.black),
            title: "交易明细",
            value: "",
            trailing: const Icon(Icons.arrow_forward_ios, size: 16.0, color: ColorUtil.grey),
            onClick: () {
              Get.toNamed(AppPages.cashLogPage);
            }
        ),
      ],
    );
  }
}
