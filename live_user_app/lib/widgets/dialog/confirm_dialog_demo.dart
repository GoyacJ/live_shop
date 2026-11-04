import 'package:flutter/material.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:live_app/widgets/dialog/confirm_dialog.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../utils/color_util.dart';

/// 确认弹窗演示页面
class ConfirmDialogDemo extends StatelessWidget {
  const ConfirmDialogDemo({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: JhText.cText('确认弹窗示例', ColorUtil.white, 18.0),
        backgroundColor: ColorUtil.primaryColor,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            // 基础确认弹窗
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: ColorUtil.primaryColor,
                padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
              ),
              onPressed: () {
                ConfirmDialog.show(
                  content: '确认要删除这件商品吗？',
                  onConfirm: () {
                    ToastUtil.showToast('已确认删除');
                  }, context: context,
                );
              },
              child: JhText.cText('基础确认弹窗', ColorUtil.white, 16.0),
            ),
            
            const SizedBox(height: 20),
            
            // 自定义按钮文字的确认弹窗
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: ColorUtil.primaryColor,
                padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
              ),
              onPressed: () {
                ConfirmDialog.show(
                  title: '支付确认',
                  content: '确认支付￥99.99？',
                  confirmText: '立即支付',
                  cancelText: '再想想',
                  onConfirm: () {
                    ToastUtil.showToast('支付成功');
                  },
                  onCancel: () {
                    ToastUtil.showToast('取消支付');
                  }, context: context,
                );
              },
              child: JhText.cText('支付确认弹窗', ColorUtil.white, 16.0),
            ),
            
            const SizedBox(height: 20),
            
            // 带关闭图标的确认弹窗
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: ColorUtil.primaryColor,
                padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
              ),
              onPressed: () {
                ConfirmDialog.show(
                  title: '退出登录',
                  content: '确定要退出当前账号吗？',
                  showCloseIcon: true,
                  onConfirm: () {
                    ToastUtil.showToast('已退出登录');
                  }, context: context,
                );
              },
              child: JhText.cText('带关闭图标的弹窗', ColorUtil.white, 16.0),
            ),
            
            const SizedBox(height: 20),
            
            // 自定义内容的确认弹窗
            ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: ColorUtil.primaryColor,
                padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
              ),
              onPressed: () {
                ConfirmDialog.show(
                  title: '优惠券领取成功',
                  content: '',
                  confirmText: '立即使用',
                  cancelText: '稍后使用',
                  contentWidget: Column(
                    children: [
                      Image.asset(
                        'assets/images/icon-share-wx.png',
                        width: 80,
                        height: 80,
                      ),
                      const SizedBox(height: 10),
                      JhText.cText(
                        '恭喜获得满100减50优惠券一张',
                        ColorUtil.primaryColor, 16.0, fontWeight: FontWeight.bold,
                      ),
                      const SizedBox(height: 5),
                      JhText.cText(
                        '有效期：7天',
                        ColorUtil.grey, 14.0,
                      ),
                    ],
                  ),
                  onConfirm: () {
                    ToastUtil.showToast('跳转到商城');
                  }, context: context,
                );
              },
              child: JhText.cText('自定义内容弹窗', ColorUtil.white, 16.0),
            ),
          ],
        ),
      ),
    );
  }
} 