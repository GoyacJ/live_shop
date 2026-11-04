import 'dart:convert';
import 'dart:io';

import 'package:common_utils/common_utils.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:live_app/utils/log_utils.dart';
import 'package:live_app/utils/sp_util.dart';
import 'package:get/get.dart';

import '../models/user_model.dart';


class UserInfoCtl extends GetxController {
  static UserInfoCtl get to => Get.find();

  // 是否登录
  final _isLogin = false.obs;
  // 令牌 token
  String token = '';
  // openim token
  String _imToken = '';
  // 用户 profile
  final Rx<UserEntity> _profile = UserEntity.empty().obs;

  bool get isLogin => _isLogin.value;
  UserEntity get profile => _profile.value;
  String get imToken => _imToken;
  bool get hasToken => token.isNotEmpty;

  var integer = 0.obs;

  @override
  void onInit() async {
    super.onInit();
    initData();
  }

  initData(){
    token = SpUtil.getData(ConfigUtil.token,defaultValue: '')!;
    LogUtils.d("token值：$token");
    if (ObjectUtil.isNotEmpty(token)) {
      getProfile();
      // getOpenIMToken();
    }
  }

  // 保存 token
  Future<void> setToken(String value) async {
    SpUtil.setData(ConfigUtil.token, value);
    token = value;
  }

  Future<void> setAvatar(String avatar) async {
    _profile.value.avatar = avatar;
  }

  // 获取 profile
  Future<void> getProfile() async {
    if (token.isEmpty) return;
    try {
      final result = await NetUtil.getUserInfo();
      if (result['code'] == 200) {
        _profile.value = UserEntity.fromJson(result['data']);
        _isLogin.value = true;
        integer.value = _profile.value.integral.toInt();
        SpUtil.setData(ConfigUtil.token, result['data']['token'] ?? '');
        LogUtils.d('获取用户信息成功');
      } else {
        // 获取用户信息失败，设置登录状态为false
        LogUtils.w('获取用户信息失败: ${result['msg']}');
        _isLogin.value = false;
        _profile.value = UserEntity.empty();
      }
    } catch (e) {
      // 异常情况下设置登录状态为false
      LogUtils.e('获取用户信息异常', error: e);
      _isLogin.value = false;
      _profile.value = UserEntity.empty();
    }
  }

  // 保存 profile
  Future<void> saveProfile(Map profile) async {
    _isLogin.value = true;
    setToken(profile["token"] ?? "");
    _profile.value = UserEntity.fromJson(profile);
    // Storage.write(EnvCacheConfig.userInfo, UserEntity.fromJson(profile));
  }

  // 注销
  Future<void> onLogout() async {
    if (_isLogin.value) await NetUtil.logout();
    SpUtil.remove('token');
    _profile.value = UserEntity.empty();
    _isLogin.value = false;
    integer.value = 0;
    token = '';
  }

  // 获取openIM 用户token
  Future<void> getOpenIMToken() async {
    int platformId = Platform.isAndroid ? 2 : 1;
    final result = await NetUtil.getUserToken(platformId);
    print("获取openIM 用户token:$result");
    if (result['code'] == 200) {
      _imToken = result['data']['token'].toString();
    }
  }

  /// 检查并刷新登录状态
  /// 用于支付完成后检查登录状态
  Future<bool> checkAndRefreshLoginStatus() async {
    if (token.isEmpty) {
      LogUtils.d('Token为空，用户未登录');
      return false;
    }
    
    // 如果已经有用户信息且登录状态为true，直接返回
    if (_isLogin.value && _profile.value.id.isNotEmpty) {
      LogUtils.d('用户已登录，无需刷新');
      return true;
    }
    
    // 尝试重新获取用户信息
    LogUtils.d('尝试刷新用户登录状态');
    await getProfile();
    return _isLogin.value;
  }
}