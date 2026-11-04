import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:get/get.dart';

import '../../../models/chat_contact.dart';

class ContactLogic extends BaseController {

  Rx<ChatContactEntity> contact = ChatContactEntity.empty().obs;
  RxBool isLoading = true.obs;

  bool? isShare;

  @override
  initData() async {
    if(Get.arguments != null){
      isShare = Get.arguments['isShare'];
    }
    _initData();
  }

  _initData() async {
    final result = await NetUtil.getChatContact();
    isLoading.value = false;
    if (result['code'] == 200) {
      contact.value = ChatContactEntity.fromJson(result['data']);
    } else {
      ToastUtil.showToast(result['msg']);
    }

    update();
  }
}
