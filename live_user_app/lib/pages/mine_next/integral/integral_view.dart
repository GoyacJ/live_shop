import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/services/user_info_ctl.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';
import '../../../widgets/jh_text.dart';

import 'integral_logic.dart';

class IntegralPage extends BaseStatefulWidget<IntegralLogic> {
  const IntegralPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '积分';
  }

  @override
  List<Widget>? appBarActionWidget() {
    return [
      InkWell(
        onTap: () {
          Get.toNamed(AppPages.integralGoodsPage);
        },
        child: Container(
          alignment: Alignment.center,
          padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 8.w),
          child: JhText.cText("积分商城", ColorUtil.primaryColor, 16.0, fontWeight: FontWeight.bold),
        ),
      )
    ];
  }

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(
        child: SmartRefresher(
      enablePullDown: true,
      enablePullUp: true,
      header: WaterDropHeader(
        refresh: SizedBox(
          width: 20.0,
          height: 20.0,
          child:  CircularProgressIndicator(color: ColorUtil.primaryColor,strokeWidth: 2.0),
        ),
        complete: Center(child: JhText.cText("刷新成功", ColorUtil.black, 14.0)),
      ),
      footer: CustomFooter(
        builder: (context, mode) {
          Widget body;
          if (mode == LoadStatus.idle) {
            body = JhText.cText("上拉加载", ColorUtil.black, 14.0);
          } else if (mode == LoadStatus.loading) {
            body = const CupertinoActivityIndicator();
          } else if (mode == LoadStatus.failed) {
            body = JhText.cText("加载失败！点击重试！", ColorUtil.black, 14.0);
          } else if (mode == LoadStatus.canLoading) {
            body = JhText.cText("松手,加载更多!", ColorUtil.black, 14.0);
          } else {
            body = JhText.cText("没有更多数据了!", ColorUtil.black, 14.0);
          }
          return SizedBox(
            height: 55.0,
            child: Center(child: body),
          );
        },
      ),
      controller: controller.refreshController,
      onRefresh: controller.onRefresh,
      onLoading: controller.onLoading,
      child: _buildView(),
    ));
  }

  // 主视图
  Widget _buildView() {
    return ListView.builder(
        padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 12.w),
        itemCount: controller.list.isEmpty ? 1 : controller.list.length,
        itemBuilder: (context, index) {
          if (index == 0) {
            return _buildStat();
          }

          if (controller.list.isEmpty) {
            return _buildNull();
          }
          return Container(
            decoration: const BoxDecoration(
              border: Border(
                  bottom:
                      BorderSide(width: 1, color: ColorUtil.backgroundColor)),
            ),
            child: ListTile(
              title: JhText.cText(controller.list[index].integralSource??'',
                  ColorUtil.black, 18.0,
                  fontWeight: FontWeight.bold),
              subtitle: JhText.cText(controller.list[index].operationTime??'',
                  ColorUtil.black45, 15.0),
              trailing: JhText.cText(
                  setText(index),
                  ColorUtil.primaryColor, 16.0,
                  fontWeight: FontWeight.bold),
            ),
          );
        });
  }

  // 暂无记录
  Widget _buildNull() {
    return SizedBox(
      width: double.infinity,
      height: 200.w,
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

  Widget _buildStat() {
    return Container(
      margin: const EdgeInsets.only(bottom: 10.0),
      padding: const EdgeInsets.symmetric(vertical: 40.0),
      decoration: const BoxDecoration(
          color: ColorUtil.white,
          borderRadius: BorderRadius.all(Radius.circular(6.0))),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const SizedBox(width: 12.0),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.start,
              children: [
                JhText.cText("可用积分",
                    ColorUtil.black, 16.0),
                Obx(() => JhText.cText('${UserInfoCtl.to.integer.value}',
                    ColorUtil.primaryColor, 24.0,
                    fontWeight: FontWeight.bold)),
                const SizedBox(height: 16.0),
                InkWell(
                  onTap: controller.onWithdrawal,
                  child: Container(
                    width: 90.w,
                    height: 30.0,
                    decoration: BoxDecoration(
                      color: ColorUtil.white,
                      borderRadius: const BorderRadius.all(Radius.circular(15.0)),
                      border:
                          Border.all(color: ColorUtil.primaryColor, width: 1.0),
                    ),
                    child: Center(
                      child: JhText.cText("立即兑换",
                          ColorUtil.primaryColor, 16.0),
                    ),
                  ),
                )
              ],
            ),
          ),
          const SizedBox(width: 10.0),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.start,
              children: [
               JhText.cText(
                  "冻结积分",
                  ColorUtil.black, 16.0,
                ),
                Obx(() => JhText.cText(
                    UserInfoCtl.to.profile.frozenIntegral.toStringAsFixed(2),
                    ColorUtil.primaryColor, 24.0,
                    fontWeight: FontWeight.bold)),
              ],
            ),
          ),
          const SizedBox(width: 12.0),
        ],
      ),
    );
  }

  setText(index){
    if(controller.list[index].changeType==null){
      return "";
    }else {
      if (controller.list[index].changeType == '1') {
        return "+ ${controller.list[index].integral}";
      }else if (controller.list[index].changeType == '2') {
        return '- ${controller.list[index].integral}';
      }else{
        return '${controller.list[index].integral}';
      }
    }
  }
}
