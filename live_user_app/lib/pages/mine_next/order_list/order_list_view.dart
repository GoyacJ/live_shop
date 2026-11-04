import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'order_child/order_child_view.dart';
import 'order_list_logic.dart';

class OrderListPage extends BaseStatefulWidget<OrderListLogic> {
  const OrderListPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '订单列表';
  }

  @override
  Widget buildContent(BuildContext context) {
    return Column(
      children: [
        tabView(),
        Expanded(child: tabBarView()),
      ],
    );
  }

  tabView() {
    return Container(
      alignment: Alignment.centerLeft,
      child: TabBar(
        onTap: (index) {
          controller.selectedIndex.value = index;
        },
        isScrollable: true,
        // 设置isScrollable为true
        unselectedLabelColor: ColorUtil.black,
        unselectedLabelStyle:
            TextStyle(fontSize: 18.sp, fontWeight: FontWeight.w400),
        labelColor: ColorUtil.primaryColor,
        labelStyle: TextStyle(fontSize: 18.sp, fontWeight: FontWeight.w400),
        indicatorSize: TabBarIndicatorSize.label,
        indicator: BoxDecoration(),
        tabs: controller.orderStatusList.map((item) {
          return Tab(text: item['title']);
        }).toList(),
        controller: controller.tabController,
      ),
    );
  }

  tabBarView() {
    return TabBarView(
        physics: const NeverScrollableScrollPhysics(),
        controller: controller.tabController,
        children: controller.orderStatusList.map((item) {
          return OrderChildPage(item['value']);
        }).toList());
  }
}
