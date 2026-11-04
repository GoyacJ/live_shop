import 'dart:io';

import 'package:live_app/base/controller/base_controller.dart';
import 'package:get/get.dart';
import '../../../models/app_version_model.dart';
import '../../../network/net_util.dart';

class MySettingLogic extends BaseController {
  var version = ''.obs;

  @override
  Future<void> initData() async {
    await getVersion();
  }

  Future<void> getVersion() async {
    try {
      Map<String, dynamic> response = await NetUtil.getAppVersion();
      if (response['code'] == 200) {
        AppVersionModel model = AppVersionModel.fromJson(response['data']);

        if (Platform.isAndroid) {
          version.value = model.android.appVersion;
        } else if (Platform.isIOS) {
          version.value = model.ios.appVersion;
        }
        print("服务器版本：${version.value}");
      }
    } catch (e) {
      print('获取服务器版本信息失败: $e');
    }
  }
}
