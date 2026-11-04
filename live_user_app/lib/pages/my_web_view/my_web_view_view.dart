import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:webview_flutter/webview_flutter.dart';

import 'my_web_view_logic.dart';

class MyWebViewPage extends BaseStatefulWidget<MyWebViewLogic> {
  const MyWebViewPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return controller.isTitleShow?controller.title:'';
  }

  @override
  Widget buildContent(BuildContext context) {
    return Container(
      color: ColorUtil.white,
      child: Column(
        children: [
          progressBar(),
          Expanded(
              child: WebViewWidget(controller: controller.webViewController))
        ],
      ),
    );
  }

  progressBar() {
    return controller.lineProgress == 1.0
        ? Container():LinearProgressIndicator(
      minHeight: 2.w,
      backgroundColor: ColorUtil.grey.shade200,
      value: controller.lineProgress == 1.0 ? 0 : controller.lineProgress,
      valueColor: const AlwaysStoppedAnimation<Color>(ColorUtil.primaryColor),
    );
  }
}
