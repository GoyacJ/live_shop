import 'dart:convert';
import 'dart:typed_data';

import 'package:crypto/crypto.dart';
import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:get/get.dart';
import 'package:image_gallery_saver_plus/image_gallery_saver_plus.dart';

import '../../../services/user_info_ctl.dart';
import '../../../utils/permission_util.dart';

class PosterLogic extends BaseController {

  RxString poster = ''.obs;
  RxBool isLoading = true.obs;
  RxBool isClick = false.obs;
  final GlobalKey globalKey = GlobalKey();

  @override
  initData()async{
    _initData();
    PermissionUtil.requestAll();
  }

  _initData() async {
    final result = await NetUtil.getUserPoster(101, UserInfoCtl.to.profile.id);
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      poster.value = result['data'] ?? '';
      isLoading.value = false;
      update();
    }
  }

  onSaveImage() async {
    if(kIsWeb){
      ToastUtil.showToast("请在app中下载海报");
      return;
    }
    if (isClick.value) return;

    if (poster.value.isEmpty) {
      ToastUtil.showToast("海报获取失败，无法下载");
      return ;
    }
    isClick.value = true;

    var response = await Dio().get(poster.value, options: Options(responseType: ResponseType.bytes));
    String fileName = md5.convert(utf8.encode(DateTime.now().millisecondsSinceEpoch.toString())).toString();
    final result = await ImageGallerySaverPlus.saveImage(Uint8List.fromList(response.data), quality: 80, name: fileName);
    if (result['isSuccess']) {
      ToastUtil.showToast("保存成功");
    } else {
      ToastUtil.showToast("保存失败");
    }
    isClick.value = false;
  }
}
