import 'package:aliplayer_widget/aliplayer_widget_lib.dart';
import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/services/user_info_ctl.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

import '../../../models/chat_message.dart';
import '../../../widgets/cache_image.dart';
import '../../../widgets/dialog/popup_goods_dia.dart';
import '../../../widgets/dialog/popup_share_dia.dart';
import '../../../widgets/jh_text.dart';
import 'live_room_logic.dart';

class LiveRoomPage extends BaseStatefulWidget<LiveRoomLogic> with RouteAware{
  const LiveRoomPage({super.key});

  @override
  bool showAppBar() {
    return false;
  }

  @override
  statusBarStyle() => 0;

  @override
  Color backgroundColor() {
    return ColorUtil.black;
  }

  @override
  Widget buildContent(BuildContext context) {
    return PopScope(
      canPop: true,
      onPopInvokedWithResult: (didPop, result) async {
        if (controller.isShare!=null) {
          Get.back(result: 0);
        }else{
          Get.back();
        }
      },
      child: OrientationBuilder(builder: (context, orientation) {
      return GestureDetector(
        onTap: () => FocusScope.of(context).unfocus(), // 点击页面任何地方收起键盘
        child: controller.isLoading.value
            ? SizedBox(
          width: 375.w,
          height: 812.h,
          child: const Center(
            child: CircularProgressIndicator(
              color: ColorUtil.white,
            ),
          ),
        )
            : buildContentBody(context, orientation),
      );
    }),
    );
  }

  buildContentBody(BuildContext context, Orientation orientation) {
    if (orientation == Orientation.portrait) {
      // 竖屏布局：使用 Stack 实现浮层
      return _buildView(context);
    } else {
      // 横屏布局：保持原有布局
      return _buildPlayWidget();
    }
  }

  // 主视图
  Widget _buildView(BuildContext context) {
    return Stack(
      children: [
        // 底层：播放器组件
        _buildPlayWidget(),

        // 顶部区域
        Align(
          alignment: Alignment.topCenter,
          child: _buildTop(context),
        ),

        // 聊天区域
        Positioned(
          // top: MediaQuery.of(context).size.height / 2,
          // 顶部区域高度
          bottom: (controller.keyboardHeight.value > 0 ? controller.keyboardHeight.value : 0) + 80,
          // bottom: 80,
          // 键盘弹起时调整底部偏移
          left: 0,
          right: 0,
          height: 200.w,
          child: _buildChatArea(context),
        ),

        // 消息输入框
        Positioned(
          bottom: controller.keyboardHeight.value > 10 ? controller.keyboardHeight.value : 0,
          // bottom: 0,
          left: 0,
          right: 0,
          child: _buildBottom(),
        ),
      ],
    );
  }

  /// 构建播放器组件
  Widget _buildPlayWidget() {
    return AliPlayerWidget(controller.aliPlayerController);
  }

  // 主播信息
  Widget _buildTop(BuildContext context) {
    return SafeArea(
      child: Padding(
        padding: EdgeInsets.only(top: 20.0, left: 12.0),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            CacheImage(
              controller.liveDetail.value.avatar,
              width: 35.w,
              height: 35.w,
              radius: 30.r,
              isFull: true,
              errorImage: 'assets/images/default-avatar.jpg',
            ),
            const SizedBox(
              width: 6.0,
            ),
            // 直播间标题和观众人数
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  JhText.cText(
                    controller.liveDetail.value.anchor,
                    ColorUtil.white, 14.0,
                    fontWeight: FontWeight.bold,
                    overflow: TextOverflow.ellipsis,
                  ),
                  JhText.cText(
                    "人气：${controller.liveDetail.value.viewership}",
                    ColorUtil.white, 12.0,
                  ),
                ],
              ),
            ),
            IconButton(
              icon: const Icon(
                Icons.close,
                color: ColorUtil.white,
                size: 30.0,
              ),
              onPressed: () {
                Get.back();
              },
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildChatItem(ChatMessageEntity item) {
    return Row(
      children: [
        Flexible(
          child: Container(
            margin: const EdgeInsets.symmetric(vertical: 2.0),
            padding: const EdgeInsets.fromLTRB(2.0, 2.0, 7.0, 2.0),
            decoration: BoxDecoration(
              color: ColorUtil.black26,
              borderRadius: BorderRadius.circular(10.0),
            ),
            child: Text.rich(
              TextSpan(
                children: [
                  WidgetSpan(
                    child: Container(
                      margin: const EdgeInsets.only(
                        left: 5.0,
                      ),
                      child: JhText.cText(
                        '${item.nickname}：',
                        ColorUtil.liveBlue, 13.0,
                      ),
                    ),
                  ),
                  TextSpan(
                    text: item.message,
                    style: const TextStyle(color: ColorUtil.white, fontSize: 13.0),
                  ),
                ],
              ),
            ),
          ),
        ),
      ],
    );
  }

  // 构建滚动的聊天区域
  Widget _buildChatArea(BuildContext context) {
    return ListView.builder(
      controller: controller.chatScrollController,
      padding: const EdgeInsets.all(8.0),
      itemCount: controller.chatMessages.length,
      itemBuilder: (context, index) {
        ChatMessageEntity item = controller.chatMessages[index];
        return _buildChatItem(item);
      },
    );
  }

  Widget _buildBottom() {
    return Container(
      padding: const EdgeInsets.all(8.0),
      color: controller.keyboardHeight.value > 10 ? ColorUtil.white : ColorUtil.transparent,
      child: Row(
        children: [
          /// 输入框
          Expanded(
            child: SizedBox(
              height: 60,
              child: TextField(
                controller: controller.messageTextController,
                textInputAction: TextInputAction.send,
                style: const TextStyle(color: ColorUtil.black),
                decoration: InputDecoration(
                  hintText: "请输入内容...",
                  hintStyle: TextStyle(color: controller.keyboardHeight.value > 10 ? ColorUtil.grey : ColorUtil.white),
                  filled: true,
                  fillColor: controller.keyboardHeight.value > 10 ? ColorUtil.backgroundColor : ColorUtil.transparent,
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(40),
                    borderSide: BorderSide.none,
                  ),
                  contentPadding: const EdgeInsets.symmetric(horizontal: 12.0, vertical: 12.0),
                ),
                onSubmitted: (value) => controller.onSendMessage(),
              ),
            ),
          ),

          /// 发送按钮
          if (controller.keyboardHeight.value > 10)
            SizedBox(
              width: 70,
              child: IconButton(
                icon: JhText.cText("发送", Colors.pink, 18.0),
                onPressed: controller.onSendMessage,
              ),
            ),

          /// 购物车
          if (controller.keyboardHeight.value < 10)
            IconButton(
              icon: const Icon(Icons.shopping_cart, color: ColorUtil.orange, size: 24.0),
              onPressed: () {

                Get.bottomSheet(
                  PopupGoodsDia(
                    goodsList: controller.liveGoods,
                    shopCover: controller.liveDetail.value.avatar,
                    shopTitle: controller.liveDetail.value.anchor,
                  ),
                  isScrollControlled: true,
                  isDismissible: true, // 点击空白处是否关闭
                  enableDrag: true,    // 是否允许拖动关闭
                  backgroundColor: ColorUtil.transparent, // 推荐加上，让背景透明
                );
              },
            ),

            /// 分享
            if (controller.keyboardHeight.value < 10)
              IconButton(
                icon: const Icon(Icons.share, color: ColorUtil.white, size: 24.0),
                onPressed: () {
                  if (UserInfoCtl.to.isLogin) {
                    Get.bottomSheet(PopupShareDia(
                      poster: controller.livePoster.value,
                    ));
                  } else {
                    ToastUtil.showToast("请先登录");
                  }
                },
              ),
        ],
      ),
    );
  }

  @override
  void didPushNext() {
    super.didPushNext();
  }
}
