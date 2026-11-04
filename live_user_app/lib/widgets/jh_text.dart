import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../utils/color_util.dart';

/// Created by Jcy
/// on 2025/2/26
/// 
class JhText {
  static Widget cText(
      String? title, Color? color, double fontSize,
      {FontWeight? fontWeight,int? maxLines, TextOverflow? overflow,height,textAlign=TextAlign.start}) {
    return Text(
      title??'',
      maxLines: maxLines,
      overflow: overflow,
      textAlign: textAlign,
      style: TextStyle(
          color: color ?? ColorUtil.textPrimary,
          fontWeight: fontWeight ?? FontWeight.w400,
          height: height,
          fontSize: fontSize.sp),
    );
  }

}