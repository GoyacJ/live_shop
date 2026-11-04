import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:live_app/widgets/jh_text.dart';

import 'coupon_logic.dart';

class CouponPage extends BaseStatefulWidget<CouponLogic> {
  const CouponPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '我的优惠券';
  }

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(child: _buildView());
  }

  // 暂无记录
  Widget _buildNull() {
    return SizedBox(
      width: double.infinity,
      height: 300.w,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.end,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Image.asset("assets/images/empty.png",
              height: 100.0, fit: BoxFit.contain),
          JhText.cText("暂无记录",
              ColorUtil.black38, 16.0)
        ],
      ),
    );
  }

  // 显示事件范围
  Widget _buildTimePeriod() {
    return Padding(
        padding: const EdgeInsets.symmetric(vertical: 12.0),
        child: Center(
            child: JhText.cText("仅展示三十天内的记录",
                ColorUtil.black45, 16.0)));
  }

  // 主视图
  Widget _buildView() {
    return controller.list.isEmpty
        ? _buildNull()
        : ListView.builder(
            itemCount: controller.list.length * 2 + 1,
            itemBuilder: (context, index) {
              if (index == controller.list.length * 2) {
                return _buildTimePeriod();
              }

              if (index.isEven) {
                return ListTile(
                  title: JhText.cText("积分兑换余额",
                      ColorUtil.black, 18.0, fontWeight: FontWeight.bold),
                  subtitle: JhText.cText("2025-03-06",
                      ColorUtil.black45, 15.0),
                  trailing: JhText.cText("+ 0.05",
                      ColorUtil.primaryColor, 16.0, fontWeight: FontWeight.bold),
                );
              } else {
                return const Divider(
                    height: 1,
                    thickness: 1,
                    color: ColorUtil.backgroundColor,
                    indent: 16.0,
                    endIndent: 16.0);
              }
            });
  }
}
