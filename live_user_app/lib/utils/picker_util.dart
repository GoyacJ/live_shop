import 'package:common_utils/common_utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter_picker/Picker.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:live_app/widgets/clickable.dart';

import '../widgets/jh_text.dart';


/**
 *Created by Jcy
 *on 2023/8/18
 */
class PickerUtil {
  /// 圆角背景选择日期
  static Future<String> showPickerYM(BuildContext context,
      {DateTime? minValue,
        DateTime? maxValue,
        int? type,
        String title = '请选择',
        sValue}) async {
    String date = '';
    var picker = Picker(
      height: 300.w,
      itemExtent: 60.w,
      adapter: DateTimePickerAdapter(
        type: type ?? PickerDateTimeType.kYM,
        isNumberMonth: true,
        yearSuffix: "年",
        monthSuffix: "月",
        value: ObjectUtil.isEmpty(sValue)
            ? DateTime.now()
            : DateTime.parse(sValue),
        minValue: minValue ?? DateTime(2025),
        maxValue: maxValue ?? DateTime(DateTime.now().year),
      ),
      hideHeader: false,
      backgroundColor: Colors.transparent,
      selectionOverlay: Container(
        decoration: BoxDecoration(
            border: Border(
                bottom: BorderSide(color: const Color(0xffebedf0), width: 1.w),
                top: BorderSide(color: const Color(0xffebedf0), width: 1.w))),
      ),
      headerDecoration: BoxDecoration(
        color: ColorUtil.white,
        borderRadius: BorderRadius.only(
            topLeft: Radius.circular(8.r), topRight: Radius.circular(8.r)),
      ),
      title: Stack(
        alignment: Alignment.center,
        children: [
          JhText.cText(title, ColorUtil.black, 18, fontWeight: FontWeight.bold),
          Align(
            alignment: Alignment.centerRight,
            child: Clickable(
              child: Container(
                padding: EdgeInsets.only(
                    right: 20.w, top: 10.w, bottom: 10.w, left: 20.w),
                // margin: EdgeInsets.only(right: 50.w),
                child: Image.asset(
                  'assets/images/iv_close_gray.png',
                  width: 28.w,
                  height: 28.w,
                ),
              ),
              onTap: () {
                Get.back();
              },
            ),
          ),
        ],
      ),
      cancelText: "",
      confirmText: "",
      containerColor: ColorUtil.white,
    );

    await picker.showModal(context, backgroundColor: Colors.transparent,
        builder: (context, view) {
          return Material(
              color: ColorUtil.white,
              borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(8.r), topRight: Radius.circular(8.r)),
              child: Container(
                padding: EdgeInsets.only(top: 8.w),
                child: Column(
                  children: [
                    view,
                    Clickable(
                        child: Container(
                          height: 50.w,
                          margin: EdgeInsets.only(top: 10.w, bottom: 15.w,left: 30.w,right: 30.w),
                          alignment: Alignment.center,
                          decoration: BoxDecoration(
                              color: ColorUtil.primaryColor,
                              borderRadius: BorderRadius.circular(30.r)),
                          child: JhText.cText('确定', ColorUtil.white, 16,
                              fontWeight: FontWeight.w500),
                        ),
                        onTap: () {
                          date = DateUtil.formatDate(
                              (picker.adapter as DateTimePickerAdapter).value,
                              format: DateFormats.zh_y_mo);
                          debugPrint(date);
                          Get.back();
                        })
                  ],
                ),
              ));
        });
    return date;
  }

}
