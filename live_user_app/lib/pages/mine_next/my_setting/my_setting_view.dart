import 'package:app_settings/app_settings.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/services/user_info_ctl.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:get/get.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../../base/widget/base_stateful_widget.dart';
import '../../../utils/config_util.dart';
import '../../../utils/event/event_bus_util.dart';
import '../../main/main_logic.dart';
import 'my_setting_logic.dart';

class MySettingPage extends BaseStatefulWidget<MySettingLogic> {
  const MySettingPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '设置';
  }

  @override
  Color backgroundColor() {
    // TODO: implement backgroundColor
    return ColorUtil.white;
  }

  @override
  Widget buildContent(BuildContext context) {
    return Column(
      children: [
        Expanded(child: ListView(
          padding: EdgeInsets.zero,
          children: [
            Container(
              margin: EdgeInsets.symmetric(horizontal: 15),
              child: Row(
                children: [
                  CircleAvatar(
                    radius: 40.w,
                    backgroundColor: ColorUtil.black26,
                    backgroundImage: UserInfoCtl.to.profile.avatar.isNotEmpty
                        ? NetworkImage(UserInfoCtl.to.profile.avatar)
                        : const AssetImage('assets/images/default-avatar.jpg'),
                  ),
                  const Spacer(),
                  JhText.cText(
                    UserInfoCtl.to.profile.nickname,
                    ColorUtil.black, 16.0,
                  ),
                ],
              ),
            ),
            // buildItem(context, '个人资料', 'none', ''),
            // const Divider(height: .1, color: ColorUtil.backgroundColor, indent: 15.0,),
            // buildItem(context, '账号安全', 'none', ''),
            //
            // const SizedBox(height: 10.0,),
            // buildItem(context, '关怀模式', 'none', ''),
            // const SizedBox(height: 10.0,),

            buildItem(context, '系统权限', 'outerChain', ''),
            const Divider(
              height: .1,
              color: ColorUtil.backgroundColor,
              indent: 15.0,
              endIndent: 15.0,
            ),
            buildItem(context, '版本', '', controller.version.value),
            const Divider(
              height: .1,
              color: ColorUtil.backgroundColor,
              indent: 15.0,
              endIndent: 15.0,
            ),
            buildItem(context, '关于我们', 'aboutDialog', ''),
          ],
        )),

        UserInfoCtl.to.isLogin?InkWell(
            onTap: () {
              showLogoutDialog(context);
            },
            splashColor: ColorUtil.grey[200],
            child: Container(
                margin: EdgeInsets.symmetric(horizontal: 15.w,vertical: 40.w),
                decoration: BoxDecoration(
                    color: ColorUtil.primaryColor,
                    borderRadius: BorderRadius.circular(8.r)
                ),
                padding: const EdgeInsets.symmetric(vertical: 13.0),
                child: Center(
                    child: JhText.cText(
                      '退出登录',
                      ColorUtil.white, 16.0, fontWeight: FontWeight.w500,
                    )))):Container(),
      ],
    );
  }

  Widget buildItem(
      BuildContext context, String title, String navType, String nav) {
    return InkWell(
      // splashColor: ColorUtil.grey[200],
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 15.0, horizontal: 15.0),
        child: Row(
          children: [
            JhText.cText(
              title,
              ColorUtil.black, 16.0,
            ),
            const Spacer(),
            JhText.cText(
              nav,
              ColorUtil.black, 16.0,
            ),
            SizedBox(width: 10,),
            const Icon(Icons.arrow_forward_ios,
                size: 12.0, color: ColorUtil.grey),
          ],
        ),
      ),
      onTap: () {
        switch (navType) {
          case 'page':
            Get.toNamed(nav);
            break;
          case 'aboutDialog':
            aboutAlertDialog(context);
            break;
          case 'outerChain':
            goToAppPermissionSettings(context);
            break;
          case 'none':
            ToastUtil.showToast("暂未开放");
            break;
          default:
            break;
        }
      },
    );
  }

  /// 关于我们
  void aboutAlertDialog(BuildContext context) {
    showDialog(
        context: context,
        builder: (context) {
          return UnconstrainedBox(
            constrainedAxis: Axis.vertical,
            child: SizedBox(
              width: 360.0,
              child: AlertDialog(
                contentPadding: const EdgeInsets.all(25.0),
                backgroundColor: ColorUtil.white,
                surfaceTintColor: ColorUtil.white,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10.0)),
                content: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 10.0),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Image.asset(
                        'assets/images/applogo.png',
                        width: 90.0,
                        height: 90.0,
                        fit: BoxFit.cover,
                      ),
                      const SizedBox(height: 10.0),
                      Obx(() => Text(
                            '颐心相伴 v${controller.version.value}',
                            style: TextStyle(
                                color: ColorUtil.systemBlue,
                                fontSize: 22.0,
                                fontFamily: 'Maven Pro'),
                          )),
                      const SizedBox(height: 5.0),
                      JhText.cText(
                        '低成本享受高品质生活',
                        ColorUtil.black45, 14.0,
                      ),
                      const SizedBox(height: 30.0),
                      Text(
                        '©',
                        style: TextStyle(
                            color: ColorUtil.grey[400],
                            fontSize: 12.0,
                            fontFamily: 'Maven Pro'),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          );
        });
  }

  /// 暂时不用
  void qrcodeAlertDialog(BuildContext context) {
    showDialog(
        context: context,
        builder: (context) {
          return UnconstrainedBox(
            constrainedAxis: Axis.vertical,
            child: SizedBox(
              width: 360.0,
              child: AlertDialog(
                contentPadding: const EdgeInsets.symmetric(
                    horizontal: 10.0, vertical: 20.0),
                backgroundColor: ColorUtil.white,
                surfaceTintColor: ColorUtil.white,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10.0)),
                content: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 10.0),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Image.asset(
                        'assets/images/qrimg.png',
                        width: 250.0,
                        fit: BoxFit.cover,
                      ),
                      const SizedBox(height: 15.0),
                      JhText.cText(
                        '扫一扫，加我公众号',
                        ColorUtil.black, 16.0,
                      ),
                    ],
                  ),
                ),
              ),
            ),
          );
        });
  }

  // 推出弹框
  void showLogoutDialog(BuildContext context) {
    showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            content: const Text(
              '确认退出当前账号吗？',
              style: TextStyle(fontSize: 16.0),
            ),
            backgroundColor: ColorUtil.white,
            surfaceTintColor: ColorUtil.white,
            shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(10.0)),
            elevation: 2.0,
            actionsPadding: const EdgeInsets.all(15.0),
            actions: [
              // 文字按钮，不要点击水波纹效果
              TextButton(
                  onPressed: () {
                    Get.back();
                  },
                  style: ButtonStyle(
                      overlayColor:
                          WidgetStateProperty.all(ColorUtil.transparent)),
                  child: const Text(
                    '取消',
                    style: TextStyle(color: ColorUtil.black54),
                  )),
              TextButton(
                  onPressed: handleLogout,
                  style: ButtonStyle(
                      overlayColor:
                          WidgetStateProperty.all(ColorUtil.transparent)),
                  child: const Text(
                    '确认退出',
                    style: TextStyle(color: ColorUtil.red),
                  )),
            ],
          );
        });
  }

  // 退出登录
  void handleLogout() async {
    // 清除存储信息
    // MyUtils.logout();
    await UserInfoCtl.to.onLogout();
    // Get.offAndToNamed('/login');
    // Get.offAllNamed('/login', predicate: (route) => false);
    // Get.offNamedUntil('/login', (route) => false);
    Get.back();
    Get.offNamed(AppPages.login);
    // 刷新当前页面数据
    emitUserLogout();
  }

  /// 发布用户登出事件
   void emitUserLogout() {
     final event = SimpleEvent(AppEvents.userLogout);
    EventBusUtil().emit(event);
  }

  ///跳转到系统的应用权限设置
  void goToAppPermissionSettings(BuildContext context) async {
    AppSettings.openAppSettings();
    // Get.toNamed(AppPages.myWebViewPage,arguments: {
    //   'title':'标题',
    //   'urlStr':'https://www.iconfont.cn/manage/index?spm=a313x.manage_type_mylikes.i1.db775f1f3.1bf23a81ZKW6N8&manage_type=myprojects&projectId=4907299'
    // });
  }
}
