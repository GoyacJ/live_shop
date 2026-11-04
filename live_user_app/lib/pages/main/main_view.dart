import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:get/get.dart';
import '../../routes/app_pages.dart';
import '../../utils/toast_util.dart';
import '../../widgets/keepAlive_wrapper.dart';
import 'main_logic.dart';

/// MainPage
/// 应用的主页面
/// 包含底部导航栏和对应的子页面
class MainPage extends BaseStatefulWidget<MainLogic> {

  @override
  bool showAppBar() {
    // TODO: implement showAppBar
    return false;
  }

  @override
  bool showTopBar() {
    // TODO: implement showTopBar
    return true;
  }

  @override
  bool showBackButton() {
    // TODO: implement showBackButton
    return false;
  }
  const MainPage({super.key});

  @override
  Widget buildContent(BuildContext context) {
    // TODO: implement buildContent
    return Obx(() => PopScope(
      canPop: false,
      onPopInvokedWithResult: (didPop,result) async {
        if (didPop) return; // 系统已经处理了 pop，无需自定义
        if (controller.lastPopTime == null ||
            DateTime.now().difference(controller.lastPopTime!) > const Duration(seconds: 2)) {
          controller.lastPopTime = DateTime.now();
          ToastUtil.showToast("再按一次退出app");
        } else {
          await SystemNavigator.pop(); // 退出 App
        }
      },
      child: Scaffold(
        // 使用 KeepAliveWrapper 保持页面状态
        body: KeepAliveWrapper(
          child: PageView(
            controller: controller.pageController,
            // 禁用页面滑动
            physics: const NeverScrollableScrollPhysics(),
            children: controller.pages,
          ),
        ),
        // 底部导航栏
        bottomNavigationBar: Theme(
          data: Theme.of(context).copyWith(
            splashFactory: NoSplash.splashFactory, // 禁用水波纹效果
            highlightColor: ColorUtil.transparent, // 禁用高亮效果
          ),
          child: BottomNavigationBar(
            currentIndex: controller.currentIndex.value,
            type: BottomNavigationBarType.fixed,
            selectedItemColor: ColorUtil.primaryColor,
            unselectedItemColor: ColorUtil.black54,
            backgroundColor: ColorUtil.white,
            selectedFontSize: 14.sp, // 使用适配的字体大小
            unselectedFontSize: 14.sp, // 使用适配的字体大小
            items:  [
              BottomNavigationBarItem(
                  icon: Image.asset(
                    'assets/images/ic_mall_nor.png',
                    width: 25.w,
                    height: 25.w,
                  ),
                  activeIcon: Image.asset(
                    'assets/images/ic_mall_sl.png',
                    width: 25.w,
                    height: 25.w,
                  ),
                  label: '商城'
              ),
              BottomNavigationBarItem(
                  icon: Image.asset(
                    'assets/images/ic_integral_nor.png',
                    width: 25.w,
                    height: 25.w,
                  ),
                  activeIcon: Image.asset(
                    'assets/images/ic_integral_sl.png',
                    width: 25.w,
                    height: 25.w,
                  ),
                  label: '赚积分'
              ),
              BottomNavigationBarItem(
                  icon: Image.asset(
                    'assets/images/ic_mine_nor.png',
                    width: 25.w,
                    height: 25.w,
                  ),
                  activeIcon: Image.asset(
                    'assets/images/ic_mine_sl.png',
                    width: 25.w,
                    height: 25.w,
                  ),
                  label: '我的'
              )
            ],
            onTap: controller.switchTab,
          )
        ),
      ),
    ));
  }
}
