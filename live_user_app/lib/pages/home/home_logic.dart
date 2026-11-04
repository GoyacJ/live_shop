import 'dart:io';

import 'package:carousel_slider/carousel_controller.dart';
import 'package:carousel_slider/carousel_options.dart';
import 'package:common_utils/common_utils.dart';
import 'package:device_info_plus/device_info_plus.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';
import '../../base/controller/base_controller.dart';
import '../../models/app_version_model.dart';
import '../../models/banner.dart';
import '../../models/home_video_model.dart';
import '../../models/img_nav.dart';
import '../../models/live_category.dart';
import '../../models/live_item.dart';
import '../../models/topic_model.dart';
import '../../network/net_util.dart';
import '../../services/user_info_ctl.dart';
import '../../utils/config_util.dart';
import '../../utils/platform_util.dart';
import '../../utils/sp_util.dart';
import '../../utils/toast_util.dart';
import '../../widgets/dialog/appupdate_dialog.dart';
import '../../widgets/dialog/privacy_pollicy_dialog.dart';

class HomeLogic extends BaseController {
  final CarouselSliderController carouselController =
      CarouselSliderController();
  final CarouselSliderController liveController = CarouselSliderController();
  RxList<MBanner> bannerList = <MBanner>[].obs;
  RxList<ImgNav> livePreviewList = <ImgNav>[].obs;
  RxList<LiveCategory> liveTypeList = <LiveCategory>[].obs;
  RxList<LiveItem> liveList = <LiveItem>[].obs;
  RxInt currSliderIndex = 0.obs;
  final currLiveIndex = 0.obs;
  RxInt currLiveTabIndex = 0.obs;
  List<TopicModel> topicList = [];
  final RefreshController refreshController =
      RefreshController(initialRefresh: false);
  var current = 1.obs;
  List<HomeVideoModel> mData = [];

  bool isAppUpgrade = true;

  @override
  Future<void> initData() async {
    try {
      await getData();
    } catch (e) {
      handleError(e);
    }
  }

  getData() async {
    current.value = 1;
    
    // 并行执行主要的网络请求
    // getTopicList();
    await _initData();
    await getHomeSkitList();

    // 异步执行非关键操作，不阻塞页面显示
    Future.microtask(() async {
      bool isAgreement =
          SpUtil.getData(ConfigUtil.isAgreement, defaultValue: false) ?? false;
      if (!isAgreement) {
        // 在显示弹框前，确保数据加载完成且页面稳定
        await Future.delayed(const Duration(milliseconds: 200));
        showPrivacyDiaLog();
      } else {
        // 异步执行应用更新检查和设备信息提交
        _checkAppUpdate();
        submitDevice();
      }
    });
  }

  Future<void> _checkAppUpdate() async {
    final String localVersion = await PlatformUtil.getAppVersion();
    try {
      Map<String, dynamic> response = await NetUtil.getAppVersion();
      if (response['code'] == 200) {
        String serverVersion = '';
        AppVersionModel model = AppVersionModel.fromJson(response['data']);

        if (Platform.isAndroid) {
          serverVersion = model.android.appVersion;
        } else if (Platform.isIOS) {
          serverVersion = model.ios.appVersion;
        }
        LogUtil.d("本地版本：$localVersion");
        LogUtil.d("服务器版本：$serverVersion");

        if (compareVersions(serverVersion, localVersion) > 0) {
          if (Platform.isAndroid) {
            showAppUpDateDiaLog(model.android.appUrl, model.android.appDesc);
          } else if (Platform.isIOS) {
            showAppUpDateDiaLog(model.ios.appUrl, model.ios.appDesc);
          }
        }
      }
    } catch (e) {
      LogUtil.e('获取服务器版本信息失败: $e');
    }
  }

  Future<void> _initData() async {
    try {
      final result = await NetUtil.getHomeData();
      bannerList.value = result['data']['bannerList'].map<MBanner>((item) {
        return MBanner.fromJson(item);
      }).toList();

      livePreviewList.value =
          result['data']['livePreviewList'].map<ImgNav>((item) {
        return ImgNav.fromJson(item);
      }).toList();

      update();
    } catch (e) {
      LogUtil.e('获取首页数据失败: $e');
    }
  }

  submitDevice() async {
    // 检查是否已经提交过设备信息，避免重复提交
    bool hasSubmitted =
        SpUtil.getData('device_submitted', defaultValue: false) ?? false;
    if (hasSubmitted) {
      return;
    }

    Map<String, dynamic> jsonMap = {};
    if (Platform.isAndroid) {
      AndroidDeviceInfo androidInfo = await PlatformUtil.getDeviceInfo();
      // 品牌
      jsonMap.putIfAbsent('brand', () => androidInfo.brand);
      // 型号
      jsonMap.putIfAbsent('model', () => androidInfo.model);
      // 系统版本
      jsonMap.putIfAbsent('version', () => androidInfo.version.release);
      // 是否模拟器 0: 否 1: 是
      jsonMap.putIfAbsent(
          'simulator', () => androidInfo.isPhysicalDevice ? 1 : 0);
    } else if (Platform.isIOS) {
      IosDeviceInfo iosInfo = await PlatformUtil.getDeviceInfo();
      // 品牌
      jsonMap.putIfAbsent('brand', () => 'iphone');
      // 型号
      jsonMap.putIfAbsent('model', () => iosInfo.model);
      // 系统版本
      jsonMap.putIfAbsent('version', () => iosInfo.systemVersion);
      // 是否模拟器 0: 否 1: 是
      jsonMap.putIfAbsent('simulator', () => iosInfo.isPhysicalDevice ? 1 : 0);
    }
    String clientId = await PlatformUtil.generateClientId();
    // 设备Id
    jsonMap.putIfAbsent('clientId', () => clientId);

    try {
      var result = await NetUtil.submitDevice(jsonMap);
      if (result['code'] == 200) {
        // 标记设备信息已提交
        SpUtil.setData('device_submitted', true);
      }
    } catch (e) {
      LogUtil.e('提交设备信息失败: $e');
    }
  }

  ///隐私协议弹窗
  showPrivacyDiaLog() async {
    await Get.dialog(
      PopScope(
        canPop: false,
        onPopInvokedWithResult: (didPop, result) async {
          if (!didPop) {
            // 用户按返回键，我们直接退出应用
            SystemNavigator.pop(); // Android 直接退出 App
          }
        },
        child: PrivacyPolicyDialog(
          onAgree: () {
            _checkAppUpdate();
          },
        ),
      ),
      barrierDismissible: false,
    );
  }

  ///更新
  showAppUpDateDiaLog(String apkUrl, String content) async {
    await Get.dialog(
      PopScope(
        canPop: false,
        child: AppUpdateDiaLog(
          content: content,
          apkUrl: apkUrl,
        ),
      ),
      barrierDismissible: false,
    );
  }

  /// 轮播图切换
  onSliderChanged(int index, CarouselPageChangedReason reason) {
    currSliderIndex.value = index;
  }

  /// 直播预告轮播图切换
  onLiveChanged(int index, CarouselPageChangedReason reason) {
    currLiveIndex.value = index;
  }

  ///专题列表
  getTopicList() async {
    var result = await NetUtil.topicList();
    if (result['code'] == 200) {
      final List<dynamic> dataList = result['data'];
      topicList = dataList.map((e) => TopicModel.fromJson(e)).toList();
    }
  }

  ///短剧首页推荐
  getHomeSkitList({bool isRefresh = true}) async {
    try {
      Map<String, dynamic> jsonMap = {};
      if (Get.isRegistered<UserInfoCtl>()) {
        jsonMap.putIfAbsent('userId', () => UserInfoCtl.to.profile.id);
      }
      jsonMap.putIfAbsent('current', () => current.value);
      jsonMap.putIfAbsent('size', () => 10);
      var result = await NetUtil.getSkitList(data: jsonMap);
      if (result['code'] == 200) {
        if (ObjectUtil.isNotEmpty(result['data'])) {
          final List<dynamic> dataList = result['data']['records'];
          List<HomeVideoModel> newData =
              dataList.map((e) => HomeVideoModel.fromJson(e)).toList();
          if (isRefresh) {
            // 刷新操作：清空数据
            current.value = 1;
            mData.clear();
            mData.addAll(newData);
            refreshController.refreshCompleted();
          } else {
            // 加载更多操作：追加数据
            mData.addAll(newData);
            if (ObjectUtil.isEmptyList(newData) || newData.length < 10) {
              refreshController.loadNoData();
            } else {
              refreshController.loadComplete();
            }
          }
          update();
        }
      } else {
        ToastUtil.showToast(result['msg']);
      }
    } catch (e) {
      LogUtil.e('HomeLogic$e');
      refreshController.loadFailed();
      refreshController.refreshFailed();
    }
  }

  onLiveTabClick(index) {
    LogUtil.d("点击了：$index");
    currLiveTabIndex.value = index;
    liveList.value = liveTypeList[index].list;

    LogUtil.d(liveTypeList[index].title);

    update();
  }

  int compareVersions(String v1, String v2) {
    final List<int> version1Parts = v1.split('.').map(int.parse).toList();
    final List<int> version2Parts = v2.split('.').map(int.parse).toList();
    for (int i = 0; i < version1Parts.length || i < version2Parts.length; i++) {
      final int part1 = i < version1Parts.length ? version1Parts[i] : 0;
      final int part2 = i < version2Parts.length ? version2Parts[i] : 0;
      if (part1 < part2) return -1;
      if (part1 > part2) return 1;
    }
    return 0;
  }
}
