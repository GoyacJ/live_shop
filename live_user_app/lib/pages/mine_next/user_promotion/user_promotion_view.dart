import 'package:common_utils/common_utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/utils/picker_util.dart';
import 'package:live_app/widgets/clickable.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../base/widget/base_stateful_widget.dart';
import '../../../widgets/jh_text.dart';
import 'user_promotion_logic.dart';

/// 推广页面开发规范：
/// 1. 所有颜色使用 ColorUtil 类中定义的颜色，禁止硬编码颜色值
/// 2. 所有文本组件使用 JhText.cText() 方法创建
/// 3. 布局尺寸使用 ScreenUtil 进行适配 (例如：16.sp, 10.w, 8.w)
/// 4. 列表数据来源使用 controller.list
/// 5. 选项卡索引使用 controller.tabIndex.value 控制
/// 6. 页面内主要分为：选项卡、统计卡片、推广明细标题和列表内容四部分

class UserPromotionPage extends BaseStatefulWidget<UserPromotionLogic> {
  const UserPromotionPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '我的推广';
  }

  @override
  Widget buildContent(BuildContext context) {
    return Column(
      children: [
        _buildTabs(),
        Expanded(
          child: SmartRefresher(
            enablePullDown: true,
            enablePullUp: true,
            header: WaterDropHeader(
              refresh: SizedBox(
                width: 20.0,
                height: 20.0,
                child: CircularProgressIndicator(
                    color: ColorUtil.primaryColor, strokeWidth: 2.0),
              ),
              complete: Center(
                  child: JhText.cText("刷新成功", ColorUtil.textSecondary, 14)),
            ),
            footer: CustomFooter(
              builder: (context, mode) {
                Widget body;
                print(mode);
                if (mode == LoadStatus.idle) {
                  body = JhText.cText("上拉加载", ColorUtil.textSecondary, 14);
                } else if (mode == LoadStatus.loading) {
                  body = const CupertinoActivityIndicator();
                } else if (mode == LoadStatus.failed) {
                  body = JhText.cText("加载失败！点击重试！", ColorUtil.dangerRed, 14);
                } else if (mode == LoadStatus.canLoading) {
                  body = JhText.cText("松手,加载更多!", ColorUtil.textSecondary, 14);
                } else {
                  body = JhText.cText("没有更多数据了!", ColorUtil.textGrey, 14);
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
          ),
        ),
      ],
    );
  }

  // 选项卡
  Widget _buildTabs() {
    return Obx(() => Row(
          children: [
            Expanded(
              child: GestureDetector(
                onTap: () => controller.setTabIndex(0),
                child: Container(
                  height: 50.w,
                  margin: EdgeInsets.only(left: 16.w, right: 10.w),
                  decoration: BoxDecoration(
                    color: controller.tabIndex.value == 0
                        ? ColorUtil.primaryColor
                        : ColorUtil.white,
                    borderRadius: BorderRadius.circular(4.r),
                  ),
                  alignment: Alignment.center,
                  child: JhText.cText(
                    "全部",
                    controller.tabIndex.value == 0
                        ? ColorUtil.white
                        : ColorUtil.black,
                    16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),
            Expanded(
              child: GestureDetector(
                onTap: () => controller.setTabIndex(1),
                child: Container(
                  height: 50.w,
                  decoration: BoxDecoration(
                    color: controller.tabIndex.value == 1
                        ? ColorUtil.primaryColor
                        : ColorUtil.white,
                    borderRadius: BorderRadius.circular(4.r),
                    border: Border.all(color: ColorUtil.grey200),
                  ),
                  alignment: Alignment.center,
                  child: JhText.cText(
                    "今日",
                    controller.tabIndex.value == 1
                        ? ColorUtil.white
                        : ColorUtil.black,
                    16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),
            SizedBox(width: 10.w),
            Expanded(
              child: GestureDetector(
                onTap: () => controller.setTabIndex(2),
                child: Container(
                  height: 50.w,
                  margin: EdgeInsets.only(right: 16.w, left: 10.w),
                  decoration: BoxDecoration(
                    color: controller.tabIndex.value == 2
                        ? ColorUtil.primaryColor
                        : ColorUtil.white,
                    borderRadius: BorderRadius.circular(4.r),
                    border: Border.all(color: ColorUtil.grey200),
                  ),
                  alignment: Alignment.center,
                  child: JhText.cText(
                    "昨日",
                    controller.tabIndex.value == 2
                        ? ColorUtil.white
                        : ColorUtil.black,
                    16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),
          ],
        ));
  }

  // 暂无记录
  Widget _buildNull() {
    return SizedBox(
      width: double.infinity,
      height: 200.w,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Image.asset("assets/images/empty.png",
              height: 100.0, fit: BoxFit.contain),
          JhText.cText("暂无记录", ColorUtil.black38, 16)
        ],
      ),
    );
  }

  Widget _buildStat() {
    return Container(
      margin: EdgeInsets.only(bottom: 10.w),
      padding: EdgeInsets.symmetric(vertical: 30.w),
      decoration: BoxDecoration(
          color: ColorUtil.primaryColor,
          borderRadius: BorderRadius.all(Radius.circular(4.r))),
      child: Row(
        children: [
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                JhText.cText("邀请人数", ColorUtil.white, 16),
                SizedBox(height: 8.w),
                Obx(() => JhText.cText(
                    controller.recCount.toString(), ColorUtil.white, 28,
                    fontWeight: FontWeight.bold)),
              ],
            ),
          ),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                JhText.cText("推广积分", ColorUtil.white, 16),
                SizedBox(height: 8.w),
                Obx(() => JhText.cText(
                    controller.salesAmount.toStringAsFixed(0),
                    ColorUtil.white,
                    28,
                    fontWeight: FontWeight.bold)),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildPromotionDetail() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            JhText.cText("推广明细", ColorUtil.black, 16,
                fontWeight: FontWeight.bold),
            Clickable(
              child: Container(
                padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 6.w),
                decoration: BoxDecoration(
                  color: ColorUtil.white,
                  borderRadius: BorderRadius.circular(20.r),
                  border: Border.all(color: ColorUtil.grey300),
                ),
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Obx(() => JhText.cText(
                          ObjectUtil.isEmptyString(controller.selectYm.value)
                              ? "请选择"
                              : controller.selectYm.value,
                          ColorUtil.black54,
                          14,
                        )),
                    SizedBox(width: 4.w),
                    Icon(Icons.arrow_drop_down,
                        color: ColorUtil.black54, size: 20.sp),
                  ],
                ),
              ),
              onTap: () async {
                controller.tabIndex.value = 3;
                controller.selectYm.value =
                    await PickerUtil.showPickerYM(Get.context!);
              },
            ),
          ],
        ),
        SizedBox(height: 15.w),
      ],
    );
  }

  // 主视图
  Widget _buildView() {
    return ListView.builder(
        padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 12.w),
        itemCount: (controller.list.isEmpty ? 1 : controller.list.length) + 2,
        itemBuilder: (context, index) {
          if (index == 0) {
            return _buildStat();
          }

          if (index == 1) {
            return _buildPromotionDetail();
          }

          if (controller.list.isEmpty) {
            return _buildNull();
          }

          // index-2 因为前面有两个特殊item
          final actualIndex = index - 2;
          return Container(
            padding: EdgeInsets.symmetric(vertical: 15.w),
            decoration: const BoxDecoration(
              border: Border(
                  bottom:
                      BorderSide(width: 1, color: ColorUtil.backgroundColor)),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    Expanded(
                        child: JhText.cText(
                            controller.list[actualIndex].promotionName,
                            ColorUtil.black,
                            15)),
                    JhText.cText(
                        '${controller.list[actualIndex].integral ?? 0}',
                        ColorUtil.primaryColor,
                        15)
                  ],
                ),
                SizedBox(height: 5.w),
                JhText.cText(controller.list[actualIndex].settleTime,
                    ColorUtil.black, 15)
              ],
            ),
          );
        });
  }
}
