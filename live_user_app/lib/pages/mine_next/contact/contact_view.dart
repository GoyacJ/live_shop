import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

import '../../../base/widget/base_stateful_widget.dart';
import '../../../utils/color_util.dart';
import '../../../utils/toast_util.dart';
import '../../../widgets/clickable.dart';
import '../../../widgets/jh_text.dart';
import 'contact_logic.dart';

class ContactPage extends BaseStatefulWidget<ContactLogic> {
  const ContactPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '联系客服';
  }

  @override
  Widget buildContent(BuildContext context) {
    return PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop, result) async {
        if (controller.isShare != null) {
          Get.back(result: 0);
        } else {
          Get.back();
        }
      },
      child: controller.isLoading.value
          ? const Center(child: CircularProgressIndicator())
          : _buildView(),
    );
  }

  // 主视图
  Widget _buildView() {
    // Uint8List bytes = base64Decode(controller.contact.value.qrcode);
    return CustomScrollView(
      slivers: [
        SliverToBoxAdapter(
          child: Padding(
            padding: EdgeInsets.symmetric(horizontal: 30.w, vertical: 40.w),
            child: Column(
              children: [
                const SizedBox(height: 30.0),
                JhText.cText("客服微信",
                    ColorUtil.black, 18.0),
                const SizedBox(height: 20.0),
                ClipRRect(
                  borderRadius: BorderRadius.circular(8.0),
                  child: Image.memory(
                    const Base64Decoder()
                        .convert(controller.contact.value.qrcode.split(',')[1]),
                    fit: BoxFit.cover,
                  ),
                ),
                // CachedNetworkImage(
                //   imageUrl: controller.contact.value.qrcode,
                //   width: 300.w,
                //   height: 300.w,
                //   fit: BoxFit.cover,
                // ),
                const SizedBox(height: 20.0),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    JhText.cText("联系电话：${controller.contact.value.mobile}",
                        ColorUtil.black, 18.0),
                    Clickable(
                      child: Padding(
                        padding: EdgeInsets.all(5.w),
                        child: Image.asset(
                          'assets/images/ic_copy.png',
                          width: 18.w,
                          height: 18.w,
                        ),
                      ),
                      onTap: (){
                        Clipboard.setData(ClipboardData(text: controller.contact.value.mobile));
                        ToastUtil.showToast("复制成功");
                      },
                    )
                  ],
                )
              ],
            ),
          ),
        )
      ],
    );
  }
}
