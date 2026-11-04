
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:fluttertoast/fluttertoast.dart';

import 'color_util.dart';

/// Created by Jcy
/// on 2025/2/26
/// 
class ToastUtil {
  static showToast(String? msg, {context}) {
    Fluttertoast.showToast(
        msg: msg!,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        backgroundColor: ColorUtil.black,
        textColor: ColorUtil.white,
        fontSize: 16.0.sp,
        webBgColor: "linear-gradient(to right, #333333, #444444)", // ✅ Web专属样式
        webPosition: "center", // ✅ Web专属位置控制
    );
  }
}