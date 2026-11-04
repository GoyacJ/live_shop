import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

import '../utils/color_util.dart';

///Created by Jcy
///on 2023/8/8
/// 加载框
mixin EasyLoadingMixin {
  showEasyLoading({msg='加载中...'}) {
    EasyLoading.show(
        status: msg,
        dismissOnTap: true,
        indicator: Container(
          margin: EdgeInsets.only(bottom: 0.w, top: 0.w),
          child: const CircularProgressIndicator(
            color: ColorUtil.white,
            // radius: 12,
          ),
        ));
  }
  dismissEasyLoading() {
    EasyLoading.dismiss();
  }
}
