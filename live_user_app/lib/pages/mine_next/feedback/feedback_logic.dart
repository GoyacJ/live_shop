import 'package:flutter/cupertino.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:get/get.dart';

class FeedbackLogic extends BaseController {

  final TextEditingController contentController = TextEditingController();

  onSubmit() async {
    if (contentController.text.isEmpty) {
      ToastUtil.showToast('请输入反馈内容');
      return;
    }
    var result = await NetUtil.addFeedback({
      'content': contentController.text,
    });
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      ToastUtil.showToast('添加成功');
      contentController.clear();
    }
  }

  @override
  void onClose() {
    contentController.dispose();
    super.onClose();
  }
}
