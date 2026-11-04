import 'package:custom_refresh_indicator/custom_refresh_indicator.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:get/get_core/src/get_main.dart';
import 'package:get/get_state_manager/src/rx_flutter/rx_obx_widget.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:lottie/lottie.dart';
import 'package:preload_page_view/preload_page_view.dart';

import '../../../utils/color_util.dart';
import '../../../utils/log_utils.dart';
import '../../../widgets/video/video_scroll_physics.dart';
import '../my_better_player.dart';
import 'video_child_logic.dart';

class VideoChildPage extends BaseStatefulWidget<VideoChildLogic> {
  const VideoChildPage({super.key});

  @override
  bool showAppBar() {
    // TODO: implement showAppBar
    return false;
  }

  @override
  bool showTopBar() {
    // TODO: implement showTopBar
    return true;
  }

  @override
  Widget buildContent(BuildContext context) {
    return PopScope(
        canPop: false,
        onPopInvokedWithResult: (didPop, result) async {
          if (controller.isShare!=null) {
            Get.back(result: 0);
          }else{
            Get.back();
          }
        },
        child: CustomMaterialIndicator(
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
                  onPageChanged: (value) {
                    LogUtils.e('页面切换: 滑动到索引 $value (第${value + 1}集)');

                    // 提交上一个视频的观看记录
                    if (controller.index < controller.mData.length) {
                      controller.submitVideoViewDuration();
                    }
                    // 记录滑动来源和目标
                    final previousIndex = controller.index;
                    LogUtils.d(
                        '页面切换: 从第${previousIndex + 1}集 滑动到第${value + 1}集');

                    // 更新当前索引
                    controller.index = value;
                    // 滑动切换视频时记录观看
                    // 等待微任务队列清空后再执行，确保组件树已更新
                    Future.microtask(() async {
                      MyBetterPlayer.currentPlayerState?.adStatus = 1;
                      MyBetterPlayer.onVideoChanged();
                    });
                    if (MyBetterPlayer
                        .currentPlayerState?.isProductCardVisible ==
                        true) {
                      MyBetterPlayer.currentPlayerState?.hideProductCard();
                    }
                  },
                  itemCount: controller.mData.length,
                  preloadPagesCount: 2,
                  itemBuilder: (context, index) {
                    LogUtils.d('构建视频项: 索引=$index');
                    return MyBetterPlayer(
                      key: ValueKey(
                          'video_${controller.mData[index].id}_$index'),
                      episodeList: controller.videoData,
                      count: controller.episodeCount ?? 0,
                      index: index,
                      type: 1,
                      changeStateData: changeStateData,
                      viewPage: controller.viewPage,
                      onPosition: onPosition,
                      livePoster: controller.livePoster,
                      onVideoFinished: () {
                        controller.jumpToPage(controller.index + 1);
                      },
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
      ),
    ));
  }

  void onPosition(Duration position) {
    // LogUtils.e('打印position${position.toString()}');
    controller.position = position.toString();
    controller.progressSeconds = position.inSeconds;
  }

  void changeStateData(int index,
      {bool? isLike,
      bool? isShare,
      int? likeCount,
      int? commentCount,
      int? shareCount,
      bool? isCollect}) {
    if (isLike != null) {
      if (isLike) {

        controller.videoData[index].like = true;
        controller.videoData[index].likeCount = (controller.videoData[index].likeCount ?? 0) + 1;

        controller.mData[index].videoByAction?.userAction?.isLike = true;
        controller.mData[index].videoByAction?.contentStats?.likeCount =
            (controller.mData[index].videoByAction?.contentStats?.likeCount ??
                    0) +
                1;
      } else {
        controller.videoData[index].like = false;
        controller.mData[index].videoByAction?.userAction?.isLike = false;
        var count = controller.mData[index].videoByAction?.contentStats?.likeCount ?? 0;
        if (count > 0) {
          controller.mData[index].videoByAction?.contentStats?.likeCount = count - 1;
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

        controller.mData[index].videoByAction?.userAction?.isCollect = true;
        controller.mData[index].videoByAction?.contentStats?.collectCount =
            (controller.mData[index].videoByAction?.contentStats?.collectCount ?? 0) + 1;
      } else {
        controller.videoData[index].collect = false;
        controller.mData[index].videoByAction?.userAction?.isCollect = false;
        var count = controller.mData[index].videoByAction?.contentStats?.collectCount ?? 0;
        if (count > 0) {
          controller.mData[index].videoByAction?.contentStats?.collectCount = count - 1;
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
    // TODO: implement appBarColor
    return ColorUtil.black;
  }

  @override
  Color appBarTitleColor() {
    // TODO: implement appBarTitleColor
    return ColorUtil.white;
  }

  @override
  int statusBarStyle() {
    // TODO: implement statusBarStyle
    return 0;
  }
}
