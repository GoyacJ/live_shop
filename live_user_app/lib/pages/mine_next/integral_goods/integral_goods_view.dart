import 'package:common_utils/common_utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/services/user_info_ctl.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/widgets/clickable.dart';
import 'package:marquee/marquee.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../../widgets/cache_image.dart';
import 'integral_goods_logic.dart';

class IntegralGoodsPage extends BaseStatefulWidget<IntegralGoodsLogic> {
  const IntegralGoodsPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '兑换商城';
  }

  @override
  List<Widget>? appBarActionWidget() {
    // TODO: implement appBarActionWidget
    return [
      Clickable(
        color: ColorUtil.white,
        onTap: () {
          Get.toNamed(AppPages.integralOrderListPage);
        },
        child:Container(
          alignment: Alignment.center,
          child: JhText.cText("兑换记录",
              ColorUtil.black, 16.0),
        ),
      ),
      const SizedBox(width: 8.0),
      Container(
        alignment: Alignment.center,
        child: JhText.cText("|", ColorUtil.black, 16.0),
      ),
      const SizedBox(width: 8.0),
      Clickable(
        color: ColorUtil.white,
        onTap: () {},
        child:Container(
          alignment: Alignment.center,
          child: JhText.cText("规则",
              ColorUtil.black, 16.0),
        ),
      ),
      const SizedBox(width: 10.0),
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
          child: CircularProgressIndicator(color: ColorUtil.primaryColor,strokeWidth: 2.0),
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
    return SingleChildScrollView(
      child: Column(
        children: [
          marqueeView(),
          _buildTop(),
          _buildList()
        ],
      ),
    );
  }

  marqueeView(){
    if(ObjectUtil.isEmptyList(controller.userInfoStr)){
      return Container();
    }else{
      String combinedText = controller.userInfoStr.join('   '); // 加一点小图标或空格隔开
      return Container(
        height: 50,
        color: ColorUtil.grey[200],
        child: Marquee(
          text: combinedText,
          style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
          scrollAxis: Axis.horizontal,
          blankSpace: 60.0,
          velocity: 40.0,
          pauseAfterRound: Duration(seconds: 1),
          startPadding: 10.0,
        ),
      );
    }
  }


  Widget _buildTop() {
    return Container(
      height: 100.0,
      decoration: const BoxDecoration(
          gradient: LinearGradient(
        begin: Alignment.bottomCenter,
        end: Alignment.topCenter,
        colors: [ColorUtil.gradientPink, ColorUtil.gradientPurple],
      )),
      child: Center(
        child: Container(
          width: 180.0,
          height: 40.0,
          alignment: Alignment.center,
          margin: const EdgeInsets.only(top: 10.0),
          decoration: const BoxDecoration(
            color: Color(0x1FFFFFFF),
            borderRadius: BorderRadius.all(Radius.circular(40.0)),
          ),
          child: JhText.cText("我的积分 ${UserInfoCtl.to.profile.integral}",
              ColorUtil.white, 18.0),
        ),
      ),
    );
  }

  Widget _buildList() {
    return Wrap(
      children: [
        for (int i = 0; i < controller.list.length; i++)
          InkWell(
            onTap: () {
              Get.toNamed(AppPages.integralGoodsDetailPage,
                  arguments: controller.list[i].id);
            },
            child: Container(
              width: 171.w,
              margin: EdgeInsets.only(top: 10.w, left: i % 2 == 0 ? 0 : 9.w),
              decoration: const BoxDecoration(
                color: ColorUtil.white,
                borderRadius: BorderRadius.all(Radius.circular(8.0)),
              ),
              child: Column(
                children: [
                  ClipRRect(
                    borderRadius: BorderRadius.only(
                        topLeft: Radius.circular(8.0),
                        topRight: Radius.circular(8.0)),
                    child: CacheImage(
                      controller.list[i].cover,
                      width: 172.w,
                      height: 172.w,
                    ),
                  ),
                  Padding(
                    padding:
                        EdgeInsets.symmetric(horizontal: 6.w, vertical: 8.w),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        JhText.cText(controller.list[i].title,
                            ColorUtil.black, 16.0, maxLines: 1, overflow: TextOverflow.ellipsis),
                        const SizedBox(height: 4.0),
                        JhText.cText("积分 ${controller.list[i].integral}",
                            ColorUtil.primaryColor, 16.0, fontWeight: FontWeight.bold),
                        const SizedBox(height: 4.0),
                        Row(
                          children: [
                            JhText.cText("兑换省${controller.list[i].marketPrice}元",
                                Colors.deepOrangeAccent, 13.0, fontWeight: FontWeight.bold),
                            const Spacer(),
                            InkWell(
                              onTap: () {},
                              child: Container(
                                padding: EdgeInsets.symmetric(
                                    horizontal: 8.w, vertical: 2.w),
                                decoration: const BoxDecoration(
                                  color: ColorUtil.primaryColor,
                                  borderRadius:
                                      BorderRadius.all(Radius.circular(4.0)),
                                ),
                                child: JhText.cText("速抢",
                                    ColorUtil.white, 15.0),
                              ),
                            ),
                          ],
                        )
                      ],
                    ),
                  )
                ],
              ),
            ),
          )
      ],
    );
  }
}
