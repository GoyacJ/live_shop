import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../../base/widget/base_stateful_widget.dart';
import '../../../utils/color_util.dart';
import '../../../widgets/my_list_tile.dart';
import 'withdrawal_logic.dart';

class WithdrawalPage extends BaseStatefulWidget<WithdrawalLogic> {
  const WithdrawalPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '提现';
  }

  @override
  List<Widget>? appBarActionWidget() {
    // TODO: implement appBarActionWidget
    return [
      Container(
        alignment: Alignment.center,
        padding: const EdgeInsets.only(right: 16.0),
        child: InkWell(
          onTap: () {},
          splashColor: ColorUtil.transparent,
          highlightColor: ColorUtil.transparent,
          hoverColor: ColorUtil.transparent,
          focusColor: ColorUtil.transparent,
          child: JhText.cText("提现说明", ColorUtil.black, 16.0),
        ),
      )
    ];
  }

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(
      child: _buildView(),
    );
  }

  // 主视图
  Widget _buildView() {
    return SingleChildScrollView(
      child: Column(
        children: [
          // 余额
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 8.0, vertical: 10.0),
            color: ColorUtil.white,
            child: Row(
              children: [
                 JhText.cText("余额：", ColorUtil.black, 18.0),
                JhText.cText("0.00", ColorUtil.primaryColor, 24.0, fontWeight: FontWeight.bold),
                Padding(
                  padding:  EdgeInsets.only(top: 4.0, left: 2.0),
                  child: JhText.cText("元", ColorUtil.primaryColor, 16.0),)
              ],
            ),
          ),
          // 选择提现金额
          Container(
            width: 500.w,
            margin: const EdgeInsets.only(top: 10.0),
            padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 16.w),
            color: ColorUtil.white,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                JhText.cText("选择提现金额", ColorUtil.black, 18.0, fontWeight: FontWeight.bold),
                Padding(
                  padding: EdgeInsets.only(left: 10.w, top: 16.w),
                  child: Wrap(
                      spacing: 10.w,
                      runSpacing: 10.w,
                      children: List.generate(
                          controller.amountList.length,
                              (index) => _amountItem(controller.amountList[index].condition, "${controller.amountList[index].amount}元")
                      )
                  ),
                )
              ],
            ),
          ),
          const SizedBox(height: 10.0),
          // 选择提现方式
          ..._withdrawalWidgetList(),
          const SizedBox(height: 50.0),
          TextButton(
            onPressed: () {

            },
            style: TextButton.styleFrom(
                backgroundColor: ColorUtil.red,
                padding: const EdgeInsets.symmetric(horizontal: 80.0, vertical: 12.0)
            ),
            child: JhText.cText("确认提现", ColorUtil.white, 18.0),
          ),
        ],
      ),
    );
  }

  // 提现方式
  List<Widget> _withdrawalWidgetList() {
    List<Widget> children = [];
    for (int i = 0; i < controller.withdrawalMethod.length; i++) {
      if (i > 0) {
        children.add(const Divider(height: .1, color: ColorUtil.backgroundColor, indent: 15.0));
      }

      children.add(Obx(() => MyListTile(
        leading: ClipRRect(
          borderRadius: const BorderRadius.all(Radius.circular(4.0)),
          child: Image.asset(controller.withdrawalMethod[i]['icon'], width: 25.w, height: 25.w, fit: BoxFit.cover),
        ),
        title: "提现至 ${controller.withdrawalMethod[i]['title']}",
        value: controller.withdrawalMethod[i]['account_id']>0
            ? controller.withdrawalMethod[i]['account'] : "未绑定，去绑定",
        trailing: controller.selectedMethod.value==i
            ? const Icon(Icons.check_circle_rounded, size: 22.0, color: ColorUtil.primaryColor)
            : const Icon(Icons.circle_outlined, size: 22.0, color: ColorUtil.grey),
        onClick: () {
          // 展示底部微信弹框
          controller.onSelectedMethod(i);
        },
      )));
    }

    return children;
  }

  // 提现金额
  Widget _amountItem(String title, String value) {
    return InkWell(
        onTap: () {},
        child: Container(
          width: 105.w,
          height: 70.w,
          decoration: const BoxDecoration(
              color: ColorUtil.backgroundColor,
              borderRadius: BorderRadius.all(Radius.circular(4.0))
          ),
          child: title=="" ? Center(
            child: JhText.cText(value, ColorUtil.black, 20.0),
          ) : Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              JhText.cText(value, ColorUtil.black, 20.0),
              const SizedBox(width: 4.0),
              JhText.cText(title, ColorUtil.black45, 14.0),
            ],
          ),
        )
    );
  }
}