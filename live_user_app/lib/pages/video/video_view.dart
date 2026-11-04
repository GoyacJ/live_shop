import 'package:aliplayer_widget/utils/log_util.dart';
import 'package:custom_refresh_indicator/custom_refresh_indicator.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:lottie/lottie.dart';
import 'package:preload_page_view/preload_page_view.dart';

import '../../utils/color_util.dart';
import '../../widgets/video/video_scroll_physics.dart';
import 'my_better_player.dart';
import 'video_logic.dart';

class VideoPage extends BaseStatefulWidget<VideoLogic> {
  @override
  String? title() {
    return '短剧专场';
  }

  @override
  bool showAppBar() {
    return false;
  }

  @override
  bool showBackButton() {
    return false;
  }

  @override
  bool showTopBar() {
    return true;
  }

  const VideoPage({super.key});

  @override
  Widget buildContent(BuildContext context) {
    return CustomMaterialIndicator(
        indicatorBuilder: (context, controller) {
          return Lottie.asset('assets/lotties/tiktok-loader.json', width: 50);
        },
        onRefresh: () async {
          controller.current.value = 1;
          await controller.initData();
        },
        child: Column(
          children: [
            Expanded(
                child: PreloadPageView.builder(
                    key: controller.uniqueKey,
                    scrollDirection: Axis.vertical,
                    controller: controller.pageController,
                    physics: const VideoScrollPhysics(),
                    preloadPagesCount: 2,
                    onPageChanged: (value) {
                      controller.index = value;
                      if(MyBetterPlayer.currentPlayerState?.isProductCardVisible==true){
                        MyBetterPlayer.currentPlayerState?.hideProductCard();
                      }
                      controller.submitVideoViewDuration();
                    },
                    itemCount: controller.mData.length,
                    itemBuilder: (context, index) {
                      return MyBetterPlayer(
                        episodeList: controller.videoData,
                        index: index,
                        type: 0,
                        count: 0,
                        changeStateData: changeStateData,
                        onPosition: onPosition,
                        livePoster: controller.livePoster,
                        showProductCardPerVideo: false,
                      );
                    })),
            Obx(() => Visibility(
                  visible: controller.isLoading.value,
                  child: Lottie.asset(
                    width: 50,
                    'assets/lotties/tiktok-loader.json',
                    fit: BoxFit.cover,
                  ),
                ))
          ],
        ));
  }

  void onPosition(Duration position) {
    // LogUtils.e('打印position${position.toString()}');
    controller.position = position.toString();
    controller.progressSeconds = position.inSeconds;
    LogUtil.e('打印观看时长${controller.position}');
    LogUtil.e('打印观看时长${controller.progressSeconds}');
  }

  void changeStateData(
    int index, {
    bool? isLike,
    bool? isShare,
    int? likeCount,
    int? commentCount,
    int? shareCount,
    bool? isCollect,
  }) {
    if (isLike != null) {
      if (isLike) {
        controller.videoData[index].like = true;
        controller.videoData[index].likeCount = (controller.videoData[index].likeCount ?? 0) + 1;
        controller.mData[index].userAction?.isLike = true;
        controller.mData[index].contentStats?.likeCount = (controller.mData[index].contentStats?.likeCount ?? 0) + 1;
      } else {
        controller.mData[index].userAction?.isLike = false;
        controller.videoData[index].like = false;
        var count = controller.mData[index].contentStats?.likeCount ?? 0;
        if (count > 0) {
          controller.mData[index].contentStats?.likeCount = count - 1;
          controller.videoData[index].likeCount = count - 1;
        }
      }
      controller.update();
      controller.submitData(type: 0);
      return;
    }

    if (isCollect != null) {
      if (isCollect) {

        controller.videoData[index].collect = true;
        controller.videoData[index].collectCount = (controller.videoData[index].collectCount ?? 0) + 1;

        controller.mData[index].userAction?.isCollect = true;
        controller.mData[index].contentStats?.collectCount =
            (controller.mData[index].contentStats?.collectCount ?? 0) + 1;
      } else {
        controller.videoData[index].collect = false;
        controller.mData[index].userAction?.isCollect = false;
        var count = controller.mData[index].contentStats?.collectCount ?? 0;
        if (count > 0) {
          controller.mData[index].contentStats?.collectCount = count - 1;
          controller.videoData[index].collectCount = count - 1;

        }
      }
      controller.update();
      controller.submitData(type: 1);
      return;
    }
    if (isShare != null) {
      if (isShare) {
        controller.submitData(type: 2);
      }
      return;
    }
  }

  @override
  Color backgroundColor() {
    // TODO: implement backgroundColor
    return ColorUtil.black;
  }

  @override
  Color appBarColor() {
    return ColorUtil.black;
  }

  @override
  Color appBarTitleColor() {
    return ColorUtil.black;
  }

  @override
  int statusBarStyle() {
    return 0;
  }
}
