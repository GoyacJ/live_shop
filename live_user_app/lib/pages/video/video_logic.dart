import 'dart:convert';

import 'package:common_utils/common_utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:get/get.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/pages/video/my_better_player.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:preload_page_view/preload_page_view.dart';

import '../../models/video_model.dart';
import '../../models/video_player_model.dart';
import '../../services/user_info_ctl.dart';
import '../../utils/log_utils.dart';

/// 视频页面逻辑控制器
/// 负责视频列表的加载、分页、状态管理等
class VideoLogic extends BaseController {
  var current = 1.obs; // 当前页码
  var size = 5; // 每页数量
  var isLoading = false.obs; // 是否正在加载
  var hasMore = true.obs; // 是否还有更多数据
  List<VideoModel> mData = []; // 原始视频数据
  List<VideoPlayerModel> videoData = []; // 播放器格式的视频数据
  late PreloadPageController pageController; // 页面控制器
  UniqueKey uniqueKey = UniqueKey(); // 唯一标识
  int index = 0; // 当前播放索引
  String? position; // 播放位置
  int progressSeconds = 0; // 播放秒数
  String livePoster = ''; // 分享海报

  @override
  initData() async {
    await getData();
    if (UserInfoCtl.to.isLogin) {
      await getUserPoster();
    }
    pageControllerInit();
  }

  /// 获取视频列表数据
  /// 支持分页加载，自动处理加载状态和错误
  Future<void> getData() async {
    try {
      var result = await NetUtil.getVideoList(data: {
        'current': current.value,
        'userId': UserInfoCtl.to.profile.id,
        'size': size,
      });
      if (result['code'] == 200) {
        String jsonStr = jsonEncode(result);
        if (ObjectUtil.isNotEmpty(result['data'])) {
          final List<dynamic> dataList = result['data']['records'];
          List<VideoModel> newData =
          dataList.map((e) => VideoModel.fromJson(e)).toList();
          if (current.value == 1) {
            index = 0;
            mData.clear();
            videoData.clear();
          }
          if (!ObjectUtil.isEmptyList(newData)) {
            mData.addAll(newData);
            videoData.addAll(VideoPlayerModel.fromVideoModelList(newData));
            hasMore.value = true;
          } else {
            hasMore.value = false;
          }
          update();
        } else {
          ToastUtil.showToast(result['msg']);
          hasMore.value = false;
        }
      }
    } catch (e) {
      print(e);
      hasMore.value = false;
    } finally {
      isLoading.value = false;
    }
  }

  /// 获取APP推广海报
  /// 用于视频分享功能
  Future<void> getUserPoster() async {
    var result = await NetUtil.getUserPoster(502, mData[index].id ?? '');
    if (result['code'] == 200) {
      livePoster = result['data'] ?? '';
    }
  }

  /// 销毁页面控制器
  /// 清理页面控制器资源，防止内存泄漏
  void pageControllerDispose() {
    pageController.dispose();
    pageController.removeListener(_pageContainerListener);
  }

  /// 初始化页面控制器
  /// 创建页面控制器并添加滚动监听
  void pageControllerInit() {
    pageController = PreloadPageController();
    pageController.addListener(_pageContainerListener);
  }

  /// 页面滚动监听器
  /// 监听页面滚动，实现自动加载更多数据
  void _pageContainerListener() {
    if (mData.length < 5) return;
    if (index == mData.length - 2) {
      if (!isLoading.value && hasMore.value) {
        isLoading.value = true;
        current.value++;
        getData().then((_) {
          if (!hasMore.value) {
            ToastUtil.showToast('没有更多数据了');
          }
        }).catchError((error) {
          ToastUtil.showToast('加载失败，请检查网络');
        });
      }
    }
  }

  /// 提交用户操作数据
  /// type: 0-点赞, 1-收藏, 2-分享, 3-评论, -1-都不传
  /// 对应后端：LIKE("1"), COLLECT("2"), SHARE("3"), COMMENT("4")
  submitData({int? type}) async {
    if (!UserInfoCtl.to.isLogin) {
      return;
    }
    Map<String, dynamic> params = {};
    params.putIfAbsent('userId', () => UserInfoCtl.to.profile.id);
    params.putIfAbsent('videoId', () => mData[index].id);
    if (type == 0) {
      params.putIfAbsent('contentActionType', () => 1);
      params.putIfAbsent(
          'isActive', () => mData[index].userAction?.isLike == true ? 0 : 1);
    } else if (type == 1) {
      params.putIfAbsent('contentActionType', () => 2);
      params.putIfAbsent(
          'isActive', () => mData[index].userAction?.isCollect == true ? 0 : 1);
    } else if (type == 2) {
      params.putIfAbsent('contentActionType', () => 3);
    } else if (type == 3) {
      params.putIfAbsent('contentActionType', () => 4);
    }
    LogUtils.e('打印参数$params');
    var result = await NetUtil.addUserAction(data: params);
    print(result['code']);
  }

  /// 提交单个视频的观看时长
  /// 记录用户观看进度，用于断点续播和数据分析
  Future<void> submitVideoViewDuration() async {
    if (!UserInfoCtl.to.isLogin || index >= mData.length) return;
    try {
      if (!UserInfoCtl.to.isLogin) return;
      final Map<String, dynamic> params = {};
      params.putIfAbsent('userId', () => UserInfoCtl.to.profile.id);
      params.putIfAbsent('contentId', () => mData[index].id);
      params.putIfAbsent('contentType', () => '1');
      params.putIfAbsent(
          'progressSeconds', () => progressSeconds < 0 ? 0 : progressSeconds);
      if (position != null) {
        params.putIfAbsent('viewProgress', () => position);
      }
      bool isEmpty = hasEmptyValue(params);
      if (isEmpty) {
        LogUtils.e('提交视频参数为空');
        return;
      }
      if(progressSeconds < 1){
        LogUtils.e('提交视频观看时间小于1');
        return;
      }
      final result = await NetUtil.watchDuration(data: params);
      LogUtils.d('提交结果: ${result['code']}');
    } catch (e) {
      LogUtils.e('提交视频观看时长出错: $e');
    }
  }

  bool hasEmptyValue(Map<String, dynamic> params) {
    return params.values.any((value) => value == null || value.toString().trim().isEmpty);
  }

  @override
  void onPause() {
    // 页面暂停时提交观看数据
    if (MyBetterPlayer.currentPlayerState?.isVideoPlaying() == true) {
      submitVideoViewDuration();
    }
  }

  @override
  void onClose() {
    // 提交观看数据（如果有数据）
    if (mData.isNotEmpty) {
      submitVideoViewDuration();
    }

    // 清理播放器静态资源
    MyBetterPlayer.clearStaticResources();

    // 销毁页面控制器
    pageControllerDispose();

    super.onClose();
  }
}
