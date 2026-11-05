
import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'init/app_init.dart';
import 'init/application.dart';
import 'init/config.dart';


void main() {
  //捕获异常
  runZonedGuarded(
        () => init(),
    // ignore: avoid_print
        (err, stace) => print(FlutterErrorDetails(exception: err, stack: stace)),
    zoneSpecification: ZoneSpecification(
      print: (Zone self, ZoneDelegate parent, Zone zone, String line) {
        if (kDebugMode) {
          parent.print(zone, line);
        }
      },
    ),
  );
}

void init() async {
  // PaintingBinding.instance.imageCache.maximumSize=10000;
  ///环境修改
  Config.env = Env.DEV;
  // 第三方初始化
  await AppInit.init();
  runApp(const Application());
  // if (Platform.isAndroid) {
  //   // 以下两行 设置android状态栏为透明的沉浸。写在组件渲染之后，是为了在渲染后进行set赋值，覆盖状态sg栏，写在渲染之前MaterialApp组件会覆盖掉这个值。
  //   SystemUiOverlayStyle systemUiOverlayStyle = const SystemUiOverlayStyle(
  //     /// 安卓系统状态栏存在底色，所以需要加这个
  //     statusBarColor: ColorUtil.transparent,
  //     systemNavigationBarIconBrightness: Brightness.light,
  //
  //     ///设置安卓系统导航栏
  //     statusBarIconBrightness: Brightness.dark,
  //
  //     /// 状态栏字体颜色
  //     // statusBarBrightness: Brightness.dark, ///该属性仅用于 iOS 设备顶部状态栏亮度；
  //   );
  //   SystemChrome.setSystemUIOverlayStyle(systemUiOverlayStyle);
  // }
}


