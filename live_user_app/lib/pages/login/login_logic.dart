import 'dart:async';

import 'package:common_utils/common_utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/services/user_info_ctl.dart';
import 'package:live_app/utils/log_utils.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:get/get.dart';

import '../../utils/config_util.dart';
import '../../utils/event/event_bus_util.dart';

class LoginLogic extends BaseController {
  final TextEditingController phoneController = TextEditingController();
  final TextEditingController captchaController = TextEditingController();
  final FocusNode phoneFocusNode = FocusNode();
  final FocusNode captchaFocusNode = FocusNode();
  RxString captchaText = "获取验证码".obs;
  RxBool showPhoneClear = false.obs;
  RxBool showCaptchaClear = false.obs;
  RxBool isSelectedProtocol = false.obs;

  RxBool smsClickable = true.obs;
  Timer? _timer;
  int count = 60; //初始倒计时时间
  // RxBool loginClickable = true.obs;
  String? isShare;

  @override
  initData() async {
    isShare = Get.arguments;
    phoneFocusNode.addListener(() {
      showPhoneClear.value = phoneFocusNode.hasFocus;
      update();
    });
    captchaFocusNode.addListener(() {
      showCaptchaClear.value = captchaFocusNode.hasFocus;
      update();
    });
  }

  // 清空文本框
  void phoneClear() {
    phoneController.clear();
  }

  // 协议确认
  onSelectProtocol() {
    isSelectedProtocol.value = !isSelectedProtocol.value;
  }

  // 获取验证码
  getCaptcha() async {
    if (smsClickable.value) {
      if (phoneController.text.length == 11) {
        // 发送验证码
        showEasyLoading();
        var result;
        if (kIsWeb) {
          result = await NetUtil.getH5Captcha(phoneController.text.trim());
        } else {
          result = await NetUtil.getCaptcha(phoneController.text.trim());
        }
        if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
          // 倒计时
          buttonClickListen();
          dismissEasyLoading();
        } else {
          dismissEasyLoading();
        }
      } else {
        smsClickable.value = true;
        ToastUtil.showToast('请输入正确的手机号');
      }
    }
  }

  buttonClickListen() {
    if (smsClickable.value) {
      //当按钮可点击时
      smsClickable.value = false; //按钮状态标记
      initTimer();
    }
  }

  void initTimer() {
    _timer = Timer.periodic(const Duration(seconds: 1), (Timer timer) {
      count--;
      if (count == 0) {
        timer.cancel(); //倒计时结束取消定时器
        smsClickable.value = true; //按钮可点击
        count = 60; //重置时间
        captchaText.value = '发送验证码'; //重置按钮文本
      } else {
        captchaText.value = '已发送($count)'; //更新文本内容
      }
    });
  }

  onSubmit() async {
    // if (!loginClickable.value) return;
    if (!isSelectedProtocol.value) {
      ToastUtil.showToast("请阅读并同意协议");
      return;
    }
    if (phoneController.text.length != 11) {
      return ToastUtil.showToast('请输入正确的手机号${phoneController.text}');
    }
    if (captchaController.text.length != 4) {
      return ToastUtil.showToast('请输入正确的验证码');
    }

    // loginClickable.value = false;
    // update();

    // 登录
    try {
      showEasyLoading();

      var result;
      if (kIsWeb) {
        result =
            await NetUtil.h5Login(phoneController.text, captchaController.text);
      } else {
        result =
            await NetUtil.login(phoneController.text, captchaController.text);
      }
      print("登录结果：$result");
      dismissEasyLoading();
      UserInfoCtl.to.saveProfile(result['data']);
      UserInfoCtl.to.getOpenIMToken();
      ToastUtil.showToast('登录成功');
      // 刷新当前页面数据
      if (isShare != null) {
        Get.back(result: 0);
      } else {
        Get.back();
        UserInfoCtl.to.initData();
        emitUserLogin();
      }
    } catch (e) {
      LogUtils.e('登录出错');
      dismissEasyLoading();
    }
  }

  /// 发布用户登录事件
  void emitUserLogin() {
    final event = SimpleEvent(AppEvents.userLogin);
    EventBusUtil().emit(event);
  }

  @override
  void onClose() {
    phoneController.dispose();
    captchaController.dispose();
    if (ObjectUtil.isNotEmpty(_timer)) {
      _timer?.cancel();
      _timer = null;
    }
    super.onClose();
  }
}
