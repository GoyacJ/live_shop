import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:live_app/init/app_init.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:flutter/gestures.dart';
import '../../network/http_api.dart';
import '../../services/user_info_ctl.dart';
import '../../utils/color_util.dart';
import '../../utils/config_util.dart';
import '../../utils/sp_util.dart';
import '../jh_text.dart';

/// 隐私政策和用户协议弹窗
/// 1. 所有颜色使用 ColorUtil 类中定义的颜色
/// 2. 所有文本组件使用 JhText.cText() 方法创建
/// 3. 布局尺寸使用 ScreenUtil 进行适配
class PrivacyPolicyDialog extends StatelessWidget {
  Function onAgree;
  PrivacyPolicyDialog({super.key,required this.onAgree});

  @override
  Widget build(BuildContext context) {
    return Dialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20.r)),
      insetPadding: EdgeInsets.symmetric(horizontal: 32.w),
      child: Container(
        padding: EdgeInsets.symmetric(horizontal: 20.w, vertical: 24.w),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(20.r),
        ),
        child: ListView(
          padding: EdgeInsets.zero,
          shrinkWrap: true,
          children: [
            // 标题
            Center(
              child: JhText.cText(
                '隐私政策与用户协议',
                ColorUtil.black,
                18,
                fontWeight: FontWeight.bold,
              ),
            ),
            SizedBox(height: 18.w),
            // 正文
            RichText(
              textAlign: TextAlign.left,
              text: TextSpan(
                style: TextStyle(
                  color: ColorUtil.textPrimary,
                  fontSize: 15.sp,
                  height: 1.6,
                ),
                children: [
                  const TextSpan(
                    text:
                        '欢迎使用"颐心相伴"！在您使用本App服务前，请务必仔细阅读并充分理解《用户协议》和《隐私政策》的全部条款。我们会严格按照您同意的各项条款收集、使用您的个人信息，以便为您提供更优质的服务和体验。\n\n',
                  ),
                  TextSpan(
                    text: '《隐私政策》',
                    style: TextStyle(
                        color: ColorUtil.primaryColor,
                        decoration: TextDecoration.underline),
                    recognizer: TapGestureRecognizer()
                      ..onTap = () {
                        Get.toNamed(AppPages.myWebViewPage, arguments: {
                          'title': '隐私政策',
                          'urlStr': HttpApi.privacyPolicy,
                          'isTitleShow': false
                        });
                      },
                  ),
                  const TextSpan(text: '与'),
                  TextSpan(
                    text: '《用户协议》',
                    style: TextStyle(
                        color: ColorUtil.primaryColor,
                        decoration: TextDecoration.underline),
                    recognizer: TapGestureRecognizer()
                      ..onTap = () {
                        Get.toNamed(AppPages.myWebViewPage, arguments: {
                          'title': '用户协议',
                          'urlStr': HttpApi.userAgreement,
                          'isTitleShow': false
                        });
                      },
                  ),
                  const TextSpan(
                    text:
                        '详细说明了我们如何收集、使用、存储和保护您的信息。如您同意，请点击"同意"开始使用我们的服务。如您不同意，将无法正常使用本App。',
                  ),
                ],
              ),
            ),
            SizedBox(height: 24.w),
            Divider(height: 1, color: Color(0xFFE5E5E5)),
            SizedBox(height: 4.w),
            Row(
              children: [
                Expanded(
                  child: TextButton(
                    onPressed: () async {
                      await SystemNavigator.pop(); // 退出 App
                    },
                    style: TextButton.styleFrom(
                      foregroundColor: ColorUtil.primaryColor,
                      padding: EdgeInsets.symmetric(vertical: 16.w),
                      shape: const RoundedRectangleBorder(
                          borderRadius: BorderRadius.only(
                              bottomLeft: Radius.circular(20))),
                    ),
                    child: JhText.cText('不同意并退出APP', ColorUtil.grey600, 16),
                  ),
                ),
                Container(width: 1, height: 44.w, color: Color(0xFFE5E5E5)),
                Expanded(
                  child: TextButton(
                    onPressed: () async {
                      SpUtil.setData(ConfigUtil.isAgreement, true);
                      SpUtil.setData(ConfigUtil.isFirst, true);
                      Navigator.pop(context);
                      onAgree();
                    },
                    style: TextButton.styleFrom(
                      foregroundColor: ColorUtil.primaryColor,
                      padding: EdgeInsets.symmetric(vertical: 16.w),
                      shape: const RoundedRectangleBorder(
                          borderRadius: BorderRadius.only(
                              bottomRight: Radius.circular(20))),
                    ),
                    child: JhText.cText('同意', ColorUtil.primaryColor, 16,
                        fontWeight: FontWeight.bold),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
