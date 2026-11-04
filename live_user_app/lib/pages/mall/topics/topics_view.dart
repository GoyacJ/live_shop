import 'package:common_utils/common_utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:get/get.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../routes/app_pages.dart';
import '../../../utils/color_util.dart';
import '../../../widgets/cache_image.dart';
import '../../../widgets/jh_text.dart';
import '../../../widgets/public_view.dart';
import 'topics_logic.dart';

class TopicsPage extends BaseStatefulWidget<TopicsLogic> {
  const TopicsPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return controller.title;
  }

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(
        child: Column(
      children: [
        buildTabBar(),
        Expanded(
          child: SmartRefresher(
            enablePullDown: true,
            enablePullUp: true,
            header: PublicView.buildRefreshHeader(),
            footer: PublicView.buildLoadMoreFooter(),
            controller: controller.refreshController,
            onRefresh: controller.onRefresh,
            onLoading: controller.onLoading,
            child: ObjectUtil.isEmptyList(controller.goodsList)?_buildNull():_buildProductGrid(),
          ),
        ),
      ],
    ));
  }

  // 构建顶部标签栏
  Widget buildTabBar() {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
      child: Row(
        children: List.generate(controller.tabs.length, (index) {
          bool isSelected = controller.selectedIndex == index;
          Icon arrowIcon;

          if (index == 0) {
            // 综合：始终显示朝下箭头（透明或红色）
            arrowIcon = Icon(
              Icons.arrow_drop_down,
              size: 20,
              color: isSelected ? ColorUtil.red : ColorUtil.transparent,
            );
          } else {
            // 销量 / 价格：升/降序切换箭头方向
            arrowIcon = Icon(
              controller.isAsc ? Icons.arrow_drop_up : Icons.arrow_drop_down,
              size: 20,
              color: isSelected ? ColorUtil.red : ColorUtil.transparent,
            );
          }

          return GestureDetector(
            onTap: () async{
              if (controller.selectedIndex == index) {
                if (index != 0) {
                  controller.isAsc = !controller.isAsc;
                }
              } else {
                controller.selectedIndex = index;
                controller.isAsc = false; // 切换到其他时默认降序
              }
              controller.currentPage.value = 1;
              controller.refreshController.requestRefresh();
            },
            child: Padding(
              padding: const EdgeInsets.only(right: 20),
              child: Row(
                children: [
                  JhText.cText(
                    controller.tabs[index],
                    isSelected ? ColorUtil.red : ColorUtil.black87, 16.0,
                    fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
                  ),
                  // 始终预留箭头位置，避免闪烁
                  arrowIcon,
                ],
              ),
            ),
          );
        }),
      ),
    );
  }

  // 构建商品网格

  _buildProductGrid() {
    return MasonryGridView.count(
        crossAxisCount: 2,
        mainAxisSpacing: 10.w,
        crossAxisSpacing: 7.w,
        shrinkWrap: true,
        padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 20.w),
        itemCount: controller.goodsList.length,
        itemBuilder: (context, index) {
          return InkWell(
            child: _buildProductItem(index),
            onTap: (){
              Get.toNamed(AppPages.goodsDetailPage,
                  arguments: {'id':controller.goodsList[index].goodsId});
            },
          );
        });
  }

  // 构建单个商品项
  Widget _buildProductItem(index) {
    return Card(
      margin: EdgeInsets.zero,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          CacheImage(
            controller.goodsList[index].goodsImg??'',
            height: 170.w,
            radius: 6.r,
          ),
          SizedBox(
            height: 3.w,
          ),
          Padding(
            padding: EdgeInsets.fromLTRB(5.w, 0, 5.w, 10.w),
            child: Column(
              children: [
                JhText.cText(
                    controller.goodsList[index].goodsTitle, ColorUtil.black, 15,
                    maxLines: 2, overflow: TextOverflow.ellipsis),
                SizedBox(
                  height: 5.w,
                ),
                Row(
                  children: [
                    JhText.cText('￥${controller.goodsList[index].goodsPrice}',
                        ColorUtil.priceRed, 16,
                        fontWeight: FontWeight.bold),
                    SizedBox(width: 8.w),
                    // 用横线横串数字
                    JhText.cText(
                      controller.goodsList[index].costPrice == null
                          ? ''
                          : "原价：${controller.goodsList[index].costPrice}",
                      ColorUtil.black54, 16.0,
                    ),
                  ],
                )
              ],
            ),
          )
        ],
      ),
    );
  }


  // 暂无记录
  Widget _buildNull() {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Image.asset("assets/images/empty.png",
            height: 100.0, fit: BoxFit.contain),
        JhText.cText("暂无数据",
            ColorUtil.black38, 16.0)
      ],
    );
  }
}
