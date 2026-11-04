import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../../models/order_list.dart';
import '../../../widgets/cache_image.dart';
import '../../../widgets/loading_dialog.dart';
import 'integral_order_list_logic.dart';

class IntegralOrderListPage extends BaseStatefulWidget<IntegralOrderListLogic> {
  const IntegralOrderListPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '兑换记录';
  }

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(
        child: SmartRefresher(
          enablePullDown: true,
          // enablePullUp: controller.list.isEmpty ? false : true,
          enablePullUp: false,
          header: WaterDropHeader(
            refresh: SizedBox(
              width: 20.0,
              height: 20.0,
              child: CircularProgressIndicator(color: ColorUtil.primaryColor,strokeWidth: 2.0),
            ),
            complete: Center(child: JhText.cText("刷新成功", ColorUtil.black, 14.0)),
          ),
          footer: CustomFooter(
            builder: (context, mode){
              Widget body ;
              if(mode==LoadStatus.idle){
                body = JhText.cText("上拉加载", ColorUtil.black, 14.0);
              }
              else if(mode==LoadStatus.loading){
                body = const CupertinoActivityIndicator();
              }
              else if(mode == LoadStatus.failed){
                body = JhText.cText("加载失败！点击重试！", ColorUtil.black, 14.0);
              }
              else if(mode == LoadStatus.canLoading){
                body = JhText.cText("松手,加载更多!", ColorUtil.black, 14.0);
              }
              else{
                body = JhText.cText("没有更多数据了!", ColorUtil.black, 14.0);
              }
              return SizedBox(
                height: 55.0,
                child: Center(child:body),
              );
            },
          ),
          controller: controller.refreshController,
          onRefresh: controller.onRefresh,
          // onLoading: controller.onLoading,
          child: _buildView(),
        )
    );
  }


  // 主视图
  Widget _buildView() {
    return ListView.builder(
        itemCount: controller.list.isEmpty ? 1 : controller.list.length,
        itemBuilder: (context, index) {
          if (controller.list.isEmpty) {
            return _buildNull();
          }

          return InkWell(
            onTap: () {
              Get.toNamed(AppPages.orderDetailPage, arguments: controller.list[index].id)?.then((result) {
                print("回传：$result");
                if (result != null) {
                  print("开始刷新");
                  controller.onRefresh();
                }
              });
            },
            child: _buildOrderItem(controller.list[index]),
          );
        }
    );
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
          Image.asset("assets/images/empty.png", height: 100.0, fit: BoxFit.contain),
          JhText.cText("暂无记录", ColorUtil.black38, 16.0)
        ],
      ),
    );
  }

  // 订单
  Widget _buildOrderItem(OrderListEntity item) {
    return Container(
      margin: EdgeInsets.symmetric(horizontal: 10.w, vertical: 5.w),
      padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 6.w),
      decoration: const BoxDecoration(
          color: ColorUtil.white,
          borderRadius: BorderRadius.all(Radius.circular(10.0)),
          boxShadow: [
            BoxShadow(
              color: ColorUtil.black12,
              offset: Offset(0.0, 0.0), //阴影xy轴偏移量
              blurRadius: 2.0, //阴影模糊程度
              spreadRadius: 1.0, //阴影扩散程度
            )
          ]
      ),
      child: Column(
          children: [
            Row(
              children: [
                JhText.cText(item.orderNumber, ColorUtil.black, 18.0),
                const Spacer(),
                JhText.cText(controller.statusList[item.status], ColorUtil.primaryColor, 16.0)
              ],
            ),
            SizedBox(height: 6.w),
            const Divider(height: 1.0, color: ColorUtil.backgroundColor),
            SizedBox(height: 6.w),

            for (var item in item.goods)
              Row(
                children: [
                  CacheImage(
                    item.goodsCover,
                    width: 80.w,
                    height: 80.w,
                    ),
                  SizedBox(width: 6.w),
                  Expanded(
                    child: SizedBox(
                      height: 80.w,
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          JhText.cText(item.goodsTitle, ColorUtil.black, 16.0, maxLines: 3, overflow: TextOverflow.ellipsis),
                        ],
                      ),
                    ),
                  ),
                  Column(
                    children: [
                      JhText.cText("${item.goodsPrice.floor()}积分", ColorUtil.primaryColor, 16.0),
                      JhText.cText("${item.goodsCount}件", ColorUtil.primaryColor, 16.0),
                    ],
                  )
                ],
              )
          ]
      ),
    );
  }

}
