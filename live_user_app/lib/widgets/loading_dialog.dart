import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../utils/color_util.dart';

class LoadingDialog extends StatelessWidget {
  const LoadingDialog({super.key});

  @override
  Widget build(BuildContext context) {
    return Center(
      child: GestureDetector(
        onTap: () {},
        child: Container(
          width: 120.w,
          height: 120.w,
          decoration:const ShapeDecoration(
            color: ColorUtil.black54,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.all(
                Radius.circular(10.0),
              ),
            ),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const CircularProgressIndicator(color: ColorUtil.white),
              SizedBox(height: 10.w),
              JhText.cText(
                "正在处理中",
                ColorUtil.white, 15.0,
              )
            ],
          ),
        ),
      ),
    );
  }
}