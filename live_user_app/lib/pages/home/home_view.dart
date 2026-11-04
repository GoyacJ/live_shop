import 'package:cached_network_image/cached_network_image.dart';
import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/pages/main/main_logic.dart';
import 'package:live_app/widgets/clickable.dart';
import 'package:live_app/widgets/jh_text.dart';
import 'package:live_app/widgets/public_view.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';
import '../../base/widget/base_stateful_widget.dart';
import '../../routes/app_pages.dart';
import '../../utils/color_util.dart';
import '../../utils/config_util.dart';
import '../../widgets/cache_image.dart';
import 'home_logic.dart';

class HomePage extends BaseStatefulWidget<HomeLogic> {
  const HomePage({super.key});

  @override
  String? title() => ConfigUtil.slogan;

  // @override
  // Color appBarColor() => ColorUtil.primaryColor;

  // @override
  // Color appBarTitleColor() => ColorUtil.white;

  @override
  int statusBarStyle() => 1;

  @override
  showBackButton() {
    return false;
  }

  @override
  bool showAppBar() {
    // TODO: implement showAppBar
    return true;
  }

  @override
  bool enablePullRefresh() => true;

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(
      child: SmartRefresher(
        enablePullDown: false,
        enablePullUp: true,
        // header: _buildRefreshHeader(),
        footer: PublicView.buildLoadMoreFooter(),
        controller: controller.refreshController,
        onLoading: () async {
          controller.current++;
          await controller.getHomeSkitList(isRefresh: false);
        },
        child: _buildView(),
      ),
    );
  }

  // 主视图
  Widget _buildView() {
    return ListView(
      padding: EdgeInsets.zero,
      children: [
        // 轮播图
        _buildSlider(),
        // featuredView(),
        // 直播预告
        buildLivePreview(),
        // _buildLivePreview(),
        videoView(),
        // 直播类型
        // _buildLiveType(),
        // 直播列表
        // _buildLiveList(),

        const SizedBox(height: 30.0),
      ],
    );
  }

  // 顶部轮播图
  Widget _buildSlider() {
    return Obx(() => Stack(
          children: [
            CarouselSlider(
                items: controller.bannerList.map((item) {
                  return Clickable(
                    color: ColorUtil.white,
                    child: Container(
                      margin: EdgeInsets.all(10.w),
                      child: CacheImage(
                        item.image ?? '',
                        fit: BoxFit.cover,
                        radius: 8.r,
                      ),
                    ),
                    onTap: () {
                      if (item.skipMode == 'SHOP') {
                        String id = item.url;
                        Get.toNamed(AppPages.goodsDetailPage,
                            arguments: {'id': id});
                      } else if (item.skipMode == 'INTEGRAL') {
                        MainLogic logic = Get.find();
                        logic.switchTab(3);
                      } else if (item.skipMode == 'LIVE') {
                        String id = item.url;
                        Get.toNamed(AppPages.liveRoom, arguments: {'id': id});
                      } else if (item.skipMode == 'TOPIC') {
                        String id = item.url;
                        Get.toNamed(AppPages.topicsPage, arguments: id);
                      } else if (item.skipMode == 'CONTACT_CUSTOMER_SERVICE') {
                        Get.toNamed(AppPages.contactPage);
                      }
                    },
                  );
                }).toList(),
                options: CarouselOptions(
                  autoPlay: true,
                  viewportFraction: 1.0,
                  initialPage: 0,
                  aspectRatio: 2.0,
                  onPageChanged: controller.onSliderChanged,
                )),
            Positioned(
              left: 0,
              right: 0,
              bottom: 18.0,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: controller.bannerList.map((url) {
                  int index = controller.bannerList.indexOf(url);
                  return Container(
                    width: 6.w,
                    height: 6.w,
                    margin: EdgeInsets.symmetric(horizontal: 2.w),
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: controller.currSliderIndex.value == index
                          ? ColorUtil.primaryColor
                          : ColorUtil.grey,
                    ),
                  );
                }).toList(),
              ),
            )
          ],
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
              Get.toNamed(AppPages.topicsPage, arguments: item.specialTopicId);
            },
          );
        }).toList(),
      ),
    );
  }

  buildLivePreview() {
    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 10.w),
      child: Column(
        children: [
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 10.w),
            child: Row(
              children: [
                JhText.cText('直播预告', ColorUtil.textDarkGrey, 18,
                    fontWeight: FontWeight.bold),
                const Spacer(),
                InkWell(
                  child: Padding(
                    padding: EdgeInsets.all(5.w),
                    child: Image.asset(
                      'assets/images/ic_left.png',
                      width: 5.w,
                      height: 9.w,
                    ),
                  ),
                  onTap: () {
                    controller.carouselController.previousPage();
                  },
                ),
                SizedBox(
                  width: 10.w,
                  child: Obx(() => JhText.cText(
                      '${controller.currLiveIndex.value + 1}',
                      ColorUtil.textDarkGrey,
                      12,
                      fontWeight: FontWeight.bold)),
                ),
                Padding(
                  padding: EdgeInsets.symmetric(horizontal: 3.w),
                  child: JhText.cText('/', ColorUtil.textDarkGrey, 12,
                      fontWeight: FontWeight.bold),
                ),
                SizedBox(
                  width: 10.w,
                  child: JhText.cText('${controller.livePreviewList.length}',
                      ColorUtil.textDarkGrey, 12,
                      fontWeight: FontWeight.bold),
                ),
                InkWell(
                  child: Padding(
                    padding: EdgeInsets.all(5.w),
                    child: Image.asset(
                      'assets/images/ic_right.png',
                      width: 5.w,
                      height: 9.w,
                    ),
                  ),
                  onTap: () {
                    controller.carouselController.nextPage();
                  },
                ),
              ],
            ),
          ),
          _buildSwiper(),
        ],
      ),
    );
  }

  // 直播预告轮播图
  Widget _buildSwiper() {
    return CarouselSlider(
        carouselController: controller.liveController,
        items: controller.livePreviewList.map((item) {
          return InkWell(
            child: Container(
              margin: EdgeInsets.all(10.w),
              child: CacheImage(
                item.image ?? '',
                fit: BoxFit.cover,
                radius: 8.r,
              ),
            ),
            onTap: () {
              Get.toNamed(AppPages.liveRoom, arguments: {'id': item.id});
            },
          );
        }).toList(),
        options: CarouselOptions(
          autoPlay: true,
          viewportFraction: 1.0,
          initialPage: 0,
          aspectRatio: 2.0,
          autoPlayInterval: const Duration(seconds: 3),
          onPageChanged: controller.onLiveChanged,
        ));
  }

  videoView() {
    return Container(
      margin: EdgeInsets.symmetric(horizontal: 10),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Padding(
            padding:
                const EdgeInsets.symmetric(horizontal: 10.0, vertical: 10.0),
            child: Row(
              children: [
                JhText.cText("热门短剧",
                    ColorUtil.textDarkGrey, 18.0,
                    fontWeight: FontWeight.bold),
                const Spacer(),
                Clickable(
                  child: JhText.cText('更多', ColorUtil.textDarkGrey, 14),
                  onTap: () {
                    Get.toNamed(AppPages.videoListPage);
                  },
                ),
                const Icon(Icons.arrow_forward_ios,
                    size: 12.0, color: ColorUtil.grey),
              ],
            ),
          ),
          GridView.builder(
              gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2,
                mainAxisSpacing: 10.w,
                crossAxisSpacing: 7.w,
                childAspectRatio: 0.7,
              ),
              shrinkWrap: true,
              cacheExtent: 500,
              physics: const NeverScrollableScrollPhysics(),
              itemCount: controller.mData.length,
              itemBuilder: (context, index) {
                return InkWell(
                  child: itemView(index),
                  onTap: () {
                    Get.toNamed(AppPages.videoChildPage, arguments: {
                      'type': 1,
                      'id': controller.mData[index].id,
                    });
                  },
                );
              })
        ],
      ),
    );
  }

  itemView(index) {
    return CacheImage(
      controller.mData[index].cover ?? '',
      height: 190.w,
      radius: 8.r,
    );
  }
}
