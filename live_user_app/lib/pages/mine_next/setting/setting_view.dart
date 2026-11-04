import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/pages/mine_next/setting/setting_logic.dart';
import 'package:get/get.dart';
import '../../../routes/app_pages.dart';
import '../../../utils/color_util.dart';
import '../../../widgets/jh_text.dart';
import 'widgets/setting_skeleton.dart';

class SettingPage extends BaseStatefulWidget<SettingLogic> {
  const SettingPage({super.key});

  @override
  String? title() => 'setting_title'.tr;

  @override
  Widget buildContent(BuildContext context) {
    return Obx(() =>
      controller.loading.value
        ? const SettingSkeleton()
        : ListView(
            children: [
              // 用户信息卡片
              _buildUserCard(),
              const SizedBox(height: 16),
              // 设置列表
              _buildSettingsList(),
            ],
          ));
  }

  // 构建用户信息卡片
  Widget _buildUserCard() {
    return Container(
      padding: const EdgeInsets.all(16),
      color: ColorUtil.white,
      child: Row(
        children: [
          // 头像
          CircleAvatar(
            radius: 30,
            backgroundColor: ColorUtil.blue.shade100,
            child: const Icon(
              Icons.person,
              size: 36,
              color: ColorUtil.blue,
            ),
          ),
          const SizedBox(width: 16),
          // 用户信息
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                JhText.cText(
                  '用户名',
                  ColorUtil.black, 18.0, fontWeight: FontWeight.bold,
                ),
                SizedBox(height: 4),
                JhText.cText(
                  '查看或编辑个人资料',
                  ColorUtil.grey, 14.0,
                ),
              ],
            ),
          ),
          // 箭头
          const Icon(Icons.arrow_forward_ios, color: ColorUtil.grey),
        ],
      ),
    );
  }

  // 构建设置列表
  Widget _buildSettingsList() {
    return Container(
      color: ColorUtil.white,
      child: Column(
        children: [
          // 语言设置
          ListTile(
            leading: const Icon(Icons.language),
            title: JhText.cText('setting_language'.tr, ColorUtil.black, 16.0),
            trailing: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                // 显示当前语言
                JhText.cText(
                  controller.getLanguageName(),
                  ColorUtil.grey, 14.0,
                ),
                const SizedBox(width: 8),
                const Icon(Icons.arrow_forward_ios, color: ColorUtil.grey),
              ],
            ),
            onTap: () => Get.toNamed(AppPages.language),
          ),
          const Divider(height: 1),
          // 主题设置
          ListTile(
            leading: const Icon(Icons.palette),
            title: JhText.cText('setting_theme'.tr, ColorUtil.black, 16.0),
            trailing: const Icon(Icons.arrow_forward_ios, color: ColorUtil.grey),
            onTap: () {},
          ),
          const Divider(height: 1),
          // 通知设置
          ListTile(
            leading: const Icon(Icons.notifications),
            title: JhText.cText('setting_notification'.tr, ColorUtil.black, 16.0),
            trailing: const Icon(Icons.arrow_forward_ios, color: ColorUtil.grey),
            onTap: () {},
          ),
          const Divider(height: 1),
          // 隐私设置
          ListTile(
            leading: const Icon(Icons.security),
            title: JhText.cText('setting_privacy'.tr, ColorUtil.black, 16.0),
            trailing: const Icon(Icons.arrow_forward_ios, color: ColorUtil.grey),
            onTap: () {},
          ),
          const Divider(height: 1),
          // 关于
          ListTile(
            leading: const Icon(Icons.info),
            title: JhText.cText('setting_about'.tr, ColorUtil.black, 16.0),
            trailing: const Icon(Icons.arrow_forward_ios, color: ColorUtil.grey),
            onTap: () {},
          ),
        ],
      ),
    );
  }
}
