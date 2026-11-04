import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

import '../../utils/color_util.dart';
import '../jh_text.dart';

/// 确认弹窗
/// 用于需要用户确认的操作，如删除、支付、退出等
class ConfirmDialog extends StatelessWidget {
  const ConfirmDialog({
    super.key,
    this.title = '提示',
    required this.content,
    this.confirmText = '确认',
    this.cancelText = '取消',
    this.onConfirm,
    this.onCancel,
    this.showCloseIcon = false,
    this.enableDismiss = true,
    this.contentWidget,
    this.confirmButtonColor,
    this.cancelButtonColor,
    this.titleColor,
    this.contentColor,
  });

  /// 标题
  final String title;
  
  /// 内容文本
  final String content;
  
  /// 确认按钮文本
  final String confirmText;
  
  /// 取消按钮文本
  final String cancelText;
  
  /// 确认回调
  final VoidCallback? onConfirm;
  
  /// 取消回调
  final VoidCallback? onCancel;
  
  /// 是否显示右上角关闭图标
  final bool showCloseIcon;
  
  /// 点击背景是否可关闭
  final bool enableDismiss;
  
  /// 自定义内容Widget
  final Widget? contentWidget;
  
  /// 确认按钮颜色
  final Color? confirmButtonColor;
  
  /// 取消按钮颜色
  final Color? cancelButtonColor;
  
  /// 标题颜色
  final Color? titleColor;
  
  /// 内容颜色
  final Color? contentColor;

  /// 显示确认弹窗的静态方法
  static Future<bool?> show({
    String title = '提示',
    required String content,
    required BuildContext context,
    String confirmText = '确认',
    String cancelText = '取消',
    VoidCallback? onConfirm,
    VoidCallback? onCancel,
    bool showCloseIcon = false,
    bool enableDismiss = true,
    Widget? contentWidget,
    Color? confirmButtonColor,
    Color? cancelButtonColor,
    Color? titleColor,
    Color? contentColor,
  }) {
    return showDialog(context: context, builder: (context){
      return ConfirmDialog(
        title: title,
        content: content,
        confirmText: confirmText,
        cancelText: cancelText,
        onConfirm: onConfirm,
        onCancel: onCancel,
        showCloseIcon: showCloseIcon,
        enableDismiss: enableDismiss,
        contentWidget: contentWidget,
        confirmButtonColor: confirmButtonColor,
        cancelButtonColor: cancelButtonColor,
        titleColor: titleColor,
        contentColor: contentColor,
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    final Color primaryColor = ColorUtil.primaryColor;
    final Color confirmColor = confirmButtonColor ?? primaryColor;
    final Color cancelColor = cancelButtonColor ?? ColorUtil.grey[600]!;
    
    return Center(
      child: Material(
        color: ColorUtil.transparent,
        child: GestureDetector(
          onTap: () {}, // 防止点击事件穿透到背景
          child: Container(
            width: 0.85.sw,
            decoration: BoxDecoration(
              color: ColorUtil.white,
              borderRadius: BorderRadius.circular(8.r),
            ),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                // 标题栏
                Padding(
                  padding: EdgeInsets.fromLTRB(20.w, 20.h, 20.w, 10.h),
                  child: Stack(
                    alignment: Alignment.center,
                    children: [
                      // 标题文本
                      JhText.cText(
                        title,
                        titleColor ?? ColorUtil.black87, 18.0,
                        fontWeight: FontWeight.bold,
                      ),
                      
                      // 右上角关闭图标
                      if (showCloseIcon)
                        Positioned(
                          right: 0,
                          child: InkWell(
                            onTap: () {
                              if (onCancel != null) {
                                onCancel!();
                              }
                              Get.back(result: false);
                            },
                            child: Icon(
                              Icons.close,
                              size: 20.sp,
                              color: ColorUtil.grey,
                            ),
                          ),
                        ),
                    ],
                  ),
                ),
                
                // 内容区域
                Padding(
                  padding: EdgeInsets.fromLTRB(20.w, 10.h, 20.w, 25.h),
                  child: contentWidget ?? JhText.cText(
                    content,
                    contentColor ?? ColorUtil.black54, 15.0,
                    textAlign: TextAlign.center,
                    height: 1.5,
                  ),
                ),
                
                // 分割线
                Divider(height: 1.h, color: ColorUtil.grey[200]),
                
                // 按钮区域
                SizedBox(
                  height: 50.h,
                  child: Row(
                    children: [
                      // 取消按钮
                      Expanded(
                        child: InkWell(
                          onTap: () {
                            if (onCancel != null) {
                              onCancel!();
                            }
                            Get.back(result: false);
                          },
                          child: Center(
                            child: JhText.cText(
                              cancelText,
                              cancelColor, 16.0,
                              fontWeight: FontWeight.w500,
                            ),
                          ),
                        ),
                      ),
                      
                      // 按钮分割线
                      VerticalDivider(width: 1.w, color: ColorUtil.grey[200]),
                      
                      // 确认按钮
                      Expanded(
                        child: InkWell(
                          onTap: () {
                            if (onConfirm != null) {
                              onConfirm!();
                            }
                            Get.back(result: true);
                          },
                          child: Center(
                            child: JhText.cText(
                              confirmText,
                              confirmColor, 16.0,
                              fontWeight: FontWeight.w500,
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
} 