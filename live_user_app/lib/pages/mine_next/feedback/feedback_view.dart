import 'package:flutter/material.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../../base/widget/base_stateful_widget.dart';
import 'feedback_logic.dart';

class FeedbackPage extends BaseStatefulWidget<FeedbackLogic> {
  const FeedbackPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '意见反馈';
  }

  @override
  List<Widget>? appBarActionWidget() {
    // TODO: implement appBarActionWidget
    return [
      Container(
        alignment: Alignment.center,
        padding: const EdgeInsets.only(right: 16.0),
        child: InkWell(
          onTap: () {
            Get.toNamed(AppPages.feedbackListPage);
          },
          splashColor: ColorUtil.transparent,
          highlightColor: ColorUtil.transparent,
          hoverColor: ColorUtil.transparent,
          focusColor: ColorUtil.transparent,
          child: JhText.cText("反馈记录",
              ColorUtil.primaryColor, 18.0, fontWeight: FontWeight.bold),
        ),
      )
    ];
  }

  @override
  Widget buildContent(BuildContext context) {
    return SingleChildScrollView(
      child: _buildView(),
    );
  }

  // 主视图
  Widget _buildView() {
    return Column(
      children: [
        Container(
          margin: const EdgeInsets.only(top: 10.0, left: 10.0, right: 10.0),
          padding: const EdgeInsets.all(15.0),
          decoration: BoxDecoration(
            color: ColorUtil.white,
            borderRadius: BorderRadius.circular(6.0),
          ),
          child: TextField(
            minLines: 8,
            maxLines: 8,
            maxLength: 300,
            keyboardType: TextInputType.text,
            controller: controller.contentController,
            style: TextStyle(color: ColorUtil.black, fontSize: 16.sp),
            cursorColor: ColorUtil.black54,
            decoration: InputDecoration(
              contentPadding:
                  const EdgeInsets.symmetric(vertical: 16.0, horizontal: 10.0),
              hintText: '亲，您遇到什么问题或者是对我们产品有什么建议，欢迎随时提给我们，谢谢！',
              hintStyle: const TextStyle(color: ColorUtil.black38),
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(6.0),
                borderSide: BorderSide.none,
              ),
              fillColor: ColorUtil.backgroundColor,
              filled: true,
            ),
            onChanged: (value) {},
          ),
        ),
        const SizedBox(height: 80.0),
        InkWell(
          onTap: controller.onSubmit,
          child: Container(
            width: 300.w,
            height: 50.w,
            decoration: BoxDecoration(
                color: ColorUtil.primaryColor,
                borderRadius: BorderRadius.circular(25.w)),
            child: Center(
              child: JhText.cText("立即提交",
                  ColorUtil.white, 18.0),
            ),
          ),
        ),
      ],
    );
  }
}
