import 'package:flutter/cupertino.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/services/user_info_ctl.dart';
import 'package:get/get.dart';
import '../utils/config_util.dart';
import '../utils/sp_util.dart';
import '../utils/log_utils.dart';

/// 路由中间件
/// 处理路由拦截，权限验证等
class RouteMiddleware extends GetMiddleware {
  @override
  RouteSettings? redirect(String? route) {
    return UserInfoCtl.to.isLogin ? null : const RouteSettings(name: AppPages.login);
  }
}
