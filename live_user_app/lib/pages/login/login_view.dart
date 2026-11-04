import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/network/http_api.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import '../../base/controller/base_controller.dart';
import '../../widgets/jh_text.dart';

import '../../utils/config_util.dart';
import 'login_logic.dart';

class LoginPage extends BaseStatefulWidget<LoginLogic> {
  const LoginPage({super.key});

  @override
  bool showAppBar() {
    return false;
  }

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(
      child: SingleChildScrollView(
        child: _buildView(),
      ),
    );
  }

  // 主视图
  Widget _buildView() {
    return Container(
      alignment: Alignment.center,
      margin: const EdgeInsets.only(top: 50.0),
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 30.0),
            child: Column(
              children: [
                Image.asset('assets/images/applogo.png',
                    height: 80.0, width: 80.0, fit: BoxFit.cover),
                const SizedBox(
                  height: 4.0,
                ),
                // const Text(EnvConfig.appName, style: TextStyle(color: FStyle.primaryColor, fontSize: 24.0, fontWeight: FontWeight.bold)),
                JhText.cText(ConfigUtil.slogan,
                    ColorUtil.primaryColor, 18.0),
              ],
            ),
          ),
          // 手机号
          Container(
            height: 60.0,
            margin: const EdgeInsets.symmetric(vertical: 5.0, horizontal: 30.0),
            padding: const EdgeInsets.symmetric(horizontal: 16.0),
            decoration: BoxDecoration(
              color: ColorUtil.backgroundColor,
              border: Border.all(
                  width: 1.0,
                  color: controller.showPhoneClear.value
                      ? ColorUtil.primaryColor
                      : ColorUtil.transparent),
              borderRadius: BorderRadius.circular(8.0),
            ),
            child: Row(
              children: [
                const SizedBox(
                    width: 30.0,
                    child: Center(
                        child: Icon(Icons.account_circle_outlined,
                            size: 20.0, color: ColorUtil.black))),
                Expanded(
                  child: TextField(
                    keyboardType: TextInputType.phone,
                    controller: controller.phoneController,
                    style:
                        const TextStyle(color: ColorUtil.black, fontSize: 18.0),
                    maxLength: 11,
                    cursorColor: ColorUtil.black26,
                    focusNode: controller.phoneFocusNode,
                    decoration: InputDecoration(
                      hintText: '请输入手机号',
                      hintStyle: const TextStyle(color: ColorUtil.black26),
                      counterText: "",
                      suffixIcon: Visibility(
                          visible: controller.showPhoneClear.value,
                          child: InkWell(
                            onTap: controller.phoneClear,
                            child: const Icon(
                              Icons.clear,
                              color: ColorUtil.black26,
                              size: 24.0,
                            ),
                          )),
                      contentPadding: const EdgeInsets.symmetric(
                          vertical: 0, horizontal: 12.0),
                      border:
                          const OutlineInputBorder(borderSide: BorderSide.none),
                    ),
                  ),
                )
              ],
            ),
          ),
          Container(
            margin: const EdgeInsets.symmetric(vertical: 20),
            padding: const EdgeInsets.symmetric(horizontal: 30.0),
            alignment: Alignment.centerLeft,
            child: JhText.cText("未注册的手机号将自动创建颐心相伴账号", ColorUtil.black26, 14.0)
          ),
          // 验证码
          Container(
            height: 60.0,
            margin: const EdgeInsets.symmetric(vertical: 5.0, horizontal: 30.0),
            padding: const EdgeInsets.symmetric(horizontal: 16.0),
            decoration: BoxDecoration(
              color: ColorUtil.backgroundColor,
              border: Border.all(
                  width: 1.0,
                  color: controller.showCaptchaClear.value
                      ? ColorUtil.primaryColor
                      : ColorUtil.transparent),
              borderRadius: BorderRadius.circular(8.0),
            ),
            child: Row(
              children: [
                const SizedBox(
                    width: 30.0,
                    child: Center(
                        child: Icon(Icons.security,
                            size: 20.0, color: ColorUtil.black))),
                Expanded(
                  child: TextField(
                    keyboardType: TextInputType.number,
                    controller: controller.captchaController,
                    style:
                        const TextStyle(color: ColorUtil.black, fontSize: 18.0),
                    maxLength: 4,
                    cursorColor: ColorUtil.black26,
                    focusNode: controller.captchaFocusNode,
                    decoration: const InputDecoration(
                      hintText: '短信验证码',
                      hintStyle: TextStyle(color: ColorUtil.black26),
                      counterText: "",
                      contentPadding:
                          EdgeInsets.symmetric(vertical: 0, horizontal: 12.0),
                      border: OutlineInputBorder(borderSide: BorderSide.none),
                    ),
                  ),
                ),
                Obx(() => InkWell(
                      onTap: controller.smsClickable.value
                          ? controller.getCaptcha
                          : null,
                      child: JhText.cText(controller.captchaText.value,
                          ColorUtil.primaryColor, 14.0),
                    )),
              ],
            ),
          ),
          Container(
            margin: const EdgeInsets.only(top: 10.0, bottom: 30.0, left: 30.0),
            child: Row(
              // mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Obx(() => IconButton(
                      onPressed: controller.onSelectProtocol,
                      icon: controller.isSelectedProtocol.value
                          ? const Icon(Icons.check_circle_outline,
                              color: ColorUtil.primaryColor, size: 24.0)
                          : const Icon(Icons.circle_outlined,
                              color: ColorUtil.black26, size: 24.0),
                    )),
                JhText.cText("我已阅读并同意",
                    ColorUtil.black38, 14.0),
                InkWell(
                  onTap: () {
                    Get.toNamed(AppPages.myWebViewPage, arguments: {
                      'title': '用户协议',
                      'urlStr': HttpApi.userAgreement,
                      'isTitleShow': false
                    });
                  },
                  child: JhText.cText("《用户协议》",
                      ColorUtil.primaryColor, 14.0),
                ),
                JhText.cText("和",
                    ColorUtil.black38, 14.0),
                InkWell(
                  onTap: () {
                    Get.toNamed(AppPages.myWebViewPage, arguments: {
                      'title': '隐私政策',
                      'urlStr': HttpApi.privacyPolicy,
                      'isTitleShow': false
                    });
                  },
                  child: JhText.cText("《隐私政策》",
                      ColorUtil.primaryColor, 14.0),
                ),
              ],
            ),
          ),

          InkWell(
            onTap: controller.onSubmit,
            // onTap: controller.onSubmit,
            child: Container(
              width: 300.w,
              height: 50.w,
              decoration: BoxDecoration(
                  color: ColorUtil.primaryColor,
                  borderRadius: BorderRadius.circular(30.0)),
              child: Center(
                child: JhText.cText("登录",
                    ColorUtil.white, 18.0),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
