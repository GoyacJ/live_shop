import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/widgets/clickable.dart';
import 'package:live_app/widgets/jh_text.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../utils/color_util.dart';
import '../../widgets/cache_image.dart';
import '../../widgets/public_view.dart';
import 'mall_logic.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';

class MallPage extends BaseStatefulWidget<MallLogic> {
  const MallPage({super.key});

  @override
  bool showAppBar() {
    // TODO: implement showAppBar
    return false;
  }

  @override
  bool enablePullRefresh() => true;

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(child: bodyView());
  }

  bodyView() {
    return SmartRefresher(
      enablePullDown: false,
      enablePullUp: true,
      header: _buildRefreshHeader(),
      footer: PublicView.buildLoadMoreFooter(),
      controller: controller.refreshController,
      onLoading: () async {
        controller.current++;
        await controller.getSearchList(isRefresh: false);
      },
      child: ListView(
        children: [
          titleView(),
          searchView(),
          _buildSwiper(),
          featuredView(),
          goodsView(),
        ],
      ),
    );
  }

  titleView() {
    return Row(
      children: [
        SizedBox(
          width: 10.w,
        ),
        JhText.cText('颐心相伴官方商城', ColorUtil.black, 18,
            fontWeight: FontWeight.w500),
        const Spacer(),
        InkWell(
          child: Padding(
            padding: EdgeInsets.symmetric(vertical: 10.w, horizontal: 10.w),
            child: Row(
              children: [
                JhText.cText('联系客服', ColorUtil.grey600, 15,
                    fontWeight: FontWeight.w500),
                Image.asset(
                  'assets/images/ic_kf.png',
                  width: 22.w,
                  height: 29.w,
                )
              ],
            ),
          ),
          onTap: () {
            Get.toNamed(AppPages.contactPage);
          },
        )
      ],
    );
  }

  searchView() {
    return InkWell(
      child: Container(
          padding: EdgeInsets.only(left: 10.w),
          margin: EdgeInsets.only(left: 10.w, right: 10.w, bottom: 10.w),
          decoration: BoxDecoration(
              color: ColorUtil.searchBg,
              borderRadius: BorderRadius.circular(16.r)),
          alignment: Alignment.center,
          height: 32.w,
          child: Row(
            children: [
              Image.asset(
                'assets/images/ic_search.png',
                width: 14.w,
                height: 14.w,
              ),
              SizedBox(
                width: 10.w,
              ),
              JhText.cText('您喜欢的商品:木梳子/大米/油', ColorUtil.textGrey, 12)
            ],
          )),
      onTap: () {
        Get.toNamed(AppPages.searchPage);
      },
    );
  }

  // 顶部轮播图
  Widget _buildSwiper() {
    return CarouselSlider(
        items: controller.swiperList.map((item) {
          return Clickable(
            color: ColorUtil.white,
            child: Container(
              margin: EdgeInsets.all(10.w),
              child: ClipRRect(
                  borderRadius: BorderRadius.circular(8.w),
                  child: CacheImage(
                    item.img ?? '',
                    fit: BoxFit.cover,
                  )),
            ),
            onTap: () {
              Get.toNamed(AppPages.topicsPage, arguments: item.specialTopicId);
            },
          );
        }).toList(),
        options: CarouselOptions(
          autoPlay: true,
          viewportFraction: 1.0,
          initialPage: 0,
          aspectRatio: 2.0,
        ));
  }

  // 专题
  featuredView() {
    return Container(
      padding: EdgeInsets.only(left: 20.w, top: 10.w),
      child: Wrap(
        spacing: 25.w,
        runSpacing: 20.w,
        children: controller.topicList.map((item) {
          return InkWell(
            child: Column(
              children: [
                CacheImage(
                  item.img ?? '',
                  width: 60.w,
                  height: 60.w,
                  radius: 8.r,
                ),
                SizedBox(
                  height: 5.w,
                ),
                JhText.cText(item.specialTopicName, ColorUtil.textSecondary, 14)
              ],
            ),
            onTap: () {
              Get.toNamed(AppPages.topicsPage, arguments: {'id':item.specialTopicId,'title':item.specialTopicName});
            },
          );
        }).toList(),
      ),
    );
  }

  goodsView() {
    return MasonryGridView.count(
      crossAxisCount: 2,
      mainAxisSpacing: 10.w,
      crossAxisSpacing: 7.w,
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 20.w),
      itemCount: controller.goodsList.length,
      itemBuilder: (context, index) {
        return InkWell(
          child: itemView(index),
          onTap: () {
            Get.toNamed(AppPages.goodsDetailPage,
                arguments: {'id': controller.goodsList[index].goodsId});
          },
        );
      },
    );
  }

  itemView(index) {
    return Card(
      margin: EdgeInsets.zero,
      child: Column(
        children: [
          CacheImage(
            controller.goodsList[index].goodsImg ?? '',
            height: 170.w,
            radius: 6.r,
          ),
          SizedBox(
            height: 3.w,
          ),
          Padding(
            padding: EdgeInsets.fromLTRB(5.w, 0, 5.w, 10.w),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                JhText.cText(
                    controller.goodsList[index].goodsTitle, ColorUtil.black, 15,
                    maxLines: 2, overflow: TextOverflow.ellipsis),
                SizedBox(
                  height: 3.w,
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

  // 下拉刷新头部
  Widget _buildRefreshHeader() {
    return const WaterDropHeader(
      waterDropColor: ColorUtil.primaryColor,
    );
  }

  // 上拉加载尾部
  Widget _buildLoadMoreFooter() {
    return CustomFooter(
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
    );
  }
}
