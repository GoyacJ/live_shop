import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get_navigation/src/root/get_material_app.dart';
import 'package:get/get_navigation/src/routes/transitions_type.dart';
import 'package:flutter/foundation.dart';

import '../lang/app_translations.dart';
import '../routes/app_pages.dart';
import '../services/theme_service.dart';
import '../utils/config_util.dart';
import '../utils/key_board_util.dart';
import '../utils/language_util.dart';

/// Created by Jcy
/// on 2025/2/26
/// 

class Application extends StatelessWidget {
  const Application({super.key});

  @override
  Widget build(BuildContext context) {
    final easyLoading = EasyLoading.init();
    return ScreenUtilInit(
      // 设计稿尺寸，适配Web和移动端
      designSize: const Size(375, 812),
      // 确保在Web平台上也能正常工作
      minTextAdapt: true,
      splitScreenMode: true,
      builder: (context, child) => GestureDetector(
        onTap: KeyBoardUtil.hideSoftKeyBoard,
        child: GetMaterialApp(
          title: ConfigUtil.appName,
          // 主题配置
          theme: ThemeService.to.lightTheme,
          darkTheme: ThemeService.to.darkTheme,
          themeMode: ThemeService.to.isDarkMode ? ThemeMode.dark : ThemeMode.light,
          // 路由配置
          initialRoute: ConfigUtil.getInitialRoute(),
          getPages: AppPages.routes,
          defaultTransition: Transition.cupertino,
          // 国际化配置
          translations: AppTranslations(),
          locale: const Locale('zh', 'CN'),
          fallbackLocale: const Locale('zh', 'CN'),
          supportedLocales: LanguageUtil.supportedLocales,

          // 本地化代理
          localizationsDelegates: const [
            GlobalMaterialLocalizations.delegate,
            GlobalWidgetsLocalizations.delegate,
            GlobalCupertinoLocalizations.delegate,
          ],
          builder: (context, widget) {
            return MediaQuery(
              ///设置文字大小不随系统设置改变
              data: MediaQuery.of(context).copyWith(textScaler: TextScaler.linear(1.0)),
              child: easyLoading(context, widget),
            );
          },
          // 调试标签
          debugShowCheckedModeBanner: false,
        ),
      ),
    );
  }
}