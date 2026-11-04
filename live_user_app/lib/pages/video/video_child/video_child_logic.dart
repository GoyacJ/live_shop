
import 'dart:convert';

import 'package:common_utils/common_utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:get/get.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/pages/video/my_better_player.dart';
import 'package:live_app/services/user_info_ctl.dart';
import 'package:live_app/utils/log_utils.dart';
import 'package:preload_page_view/preload_page_view.dart';

import '../../../models/episodes_model.dart';
import '../../../models/video_player_model.dart';
import '../../../utils/toast_util.dart';

class VideoChildLogic extends BaseController {
  // 数据相关
  final List<EpisodesModel> mData = [];
  List<VideoPlayerModel> videoData = [];
  final RxBool isLoading = false.obs;
  final RxBool hasMore = true.obs;
  final RxInt current = 1.obs;
  
  // UI相关
  final UniqueKey uniqueKey = UniqueKey();
  
  // 播放器状态
  String? position;
  int progressSeconds = 0;
  int index = 0;
  int? type;
  int? episodeCount;
  String? shortPlayId;
  String livePoster = '';
  int? viewDuration;
  bool? isShare;

  // 控制器相关
  bool _isDisposed = false;


  PreloadPageController? _pageController;
  
  // 懒加载页面控制器
  PreloadPageController get pageController {
    if (_pageController == null) {
      LogUtils.d('初始化页面控制器');
      _pageController = PreloadPageController();
      if (!_isDisposed) {
        _pageController!.addListener(_pageContainerListener);
      }
    }
    return _pageController!;
  }

  @override
  Future<void> initData() async {
    // 清理旧资源并初始化时间
    MyBetterPlayer.clearStaticResources();
    // 从参数中获取类型
    type = Get.arguments['type'];
    isShare = Get.arguments['isShare'];

    try {
      // 根据类型加载数据
      shortPlayId = Get.arguments['id'];
      await _fetchEpisodeData(isInitialLoad: true);
      
      // 获取推广海报（仅登录用户）
      if (UserInfoCtl.to.isLogin) {
        await _fetchPoster();
      }
      
      // 初始化页面控制器
      _initPageController();
      
    } catch (e) {
      LogUtils.e('初始化数据出错: $e');
      ToastUtil.showToast('加载异常，请重试');
    }
  }

  // 初始化页面控制器
  void _initPageController() {
    // 如果已经存在控制器，先安全销毁
    if (_pageController != null) {
      _disposePageController();
    }
    
    // 创建新控制器
    _pageController = PreloadPageController();
    if (!_isDisposed) {
      _pageController!.addListener(_pageContainerListener);
    }
    LogUtils.d('页面控制器初始化完成');
  }

  // 安全销毁页面控制器
  void _disposePageController() {
    try {
      if (_pageController != null && _pageController!.hasClients) {
        _pageController!.removeListener(_pageContainerListener);
        _pageController!.dispose();
      }
      _pageController = null;
    } catch (e) {
      LogUtils.e('销毁页面控制器出错: $e');
      _pageController = null;
    }
  }

  // 页面滚动监听器
  void _pageContainerListener() {
    // 空值和状态检查
    if (_pageController == null || _isDisposed) return;
    
    try {
      // 确保控制器已连接
      if (!_pageController!.hasClients) return;
      
      // 获取当前页面位置
      final double currentPage = _pageController!.page ?? 0;
      final int currentPageRound = currentPage.round();
      
      // 只在页面接近整数值时执行索引检查，避免滚动过程中产生太多处理
      if ((currentPage - currentPageRound).abs() < 0.01) {
        // 检查索引是否一致
        if (currentPageRound != index) {
          LogUtils.d('页面滚动监听: 当前页面=$currentPageRound, 记录索引=$index');
          
          // 如果滚动停止在某个页面，但索引没有更新，这可能是onPageChanged没有触发
          // 这通常发生在页面跳转时
          index = currentPageRound;
          
          if (!_isDisposed) {
            update();
            
            // 滚动切换视频时记录观看
            Future.microtask(() {
              MyBetterPlayer.onVideoChanged();
            });
          }
        }
      }
      
      // 加载更多数据相关逻辑
      if (type != 0 && !isLoading.value && hasMore.value) {
        // 计算阈值
        final int thresholdIndex = mData.length - 2;
        
        // 接近数据末尾时加载更多
        if (currentPageRound >= thresholdIndex) {
          _loadNextPage();
        }
      }
    } catch (e) {
      LogUtils.e('页面滚动监听器错误: $e');
    }
  }

  // 加载下一页数据
  Future<void> _loadNextPage() async {
    if (_isDisposed || isLoading.value || !hasMore.value) return;
    
    try {
      LogUtils.d('触发加载更多数据，当前页: ${current.value}');
      isLoading.value = true;
      
      // 增加页码
      current.value++;
      
      // 加载数据
      await _fetchEpisodeData();
      
      LogUtils.d('加载完成，数据总量: ${mData.length}');
    } catch (e) {
      LogUtils.e('加载下一页数据出错: $e');
    } finally {
      if (!_isDisposed) {
        isLoading.value = false;
      }
    }
  }

  // 获取剧集数据
  Future<void> _fetchEpisodeData({bool isInitialLoad = false, bool updateUI = true}) async {
    try {
      // 准备请求参数
      final Map<String, dynamic> params = {
        'userId': UserInfoCtl.to.profile.id,
        'shortPlayId': shortPlayId,
        'current': current.value,
        'size': 10
      };

      // 发送网络请求
      final result = await NetUtil.getEpisodeList(data: params);

      if (result['code'] == 200) {
        String jsonStr = jsonEncode(result);
        if (ObjectUtil.isNotEmpty(result['data'])) {
          final List<dynamic> dataList = result['data']['records'];
          final List<EpisodesModel> newData = dataList.map((e) =>
              EpisodesModel.fromJson(e)).toList();

          // 清空数据（仅初始加载时）
          if (isInitialLoad) {
            index = 0;
            mData.clear();
            videoData.clear();
          }

          // 添加新数据
          if (newData.isNotEmpty) {
            mData.addAll(newData);
            episodeCount = mData[0].episodeCount;
            videoData.addAll(VideoPlayerModel.fromEpisodesModelList(newData));
            // 检查是否还有更多数据
            if (newData.length < 10) {
              hasMore.value = false;
            }
          } else {
            hasMore.value = false;
          }

          // 更新UI
          if (updateUI && !_isDisposed) {
            update();
          }
        } else {
          ToastUtil.showToast(result['msg']);
          hasMore.value = false;
        }
      }
    } catch (e) {
      LogUtils.e('获取剧集数据出错: $e');
      hasMore.value = false;
      
      // 只在初始加载出错时返回
      if (isInitialLoad) {
        ToastUtil.showToast('播放异常，请稍后再试');
        Get.back();
      } else {
        throw e; // 重新抛出异常，让调用者处理
      }
    } finally {
      isLoading.value = false;
    }
  }

  // 获取推广海报
  Future<void> _fetchPoster() async {
    try {
      final result = await NetUtil.getUserPoster(502,mData[index].videoByAction?.id??'');
      if (result['code'] == 200) {
        livePoster = result['data'] ?? '';
      }
    } catch (e) {
      LogUtils.e('获取推广海报失败: $e');
      // 非关键错误，不影响主流程
    }
  }

  // 切换到指定页面
  Future<void> viewPage(int targetIndex) async {
    if (_isDisposed) return;
    
    LogUtils.d('viewPage: 请求跳转到索引 $targetIndex (第${targetIndex+1}集)');

    // 如果目标索引超出当前数据范围，尝试加载更多数据
    if (targetIndex >= mData.length) {
      if (!isLoading.value && hasMore.value) {
        try {
          isLoading.value = true;
          
          // 计算需要加载到哪一页
          final int targetPage = (targetIndex ~/ 10) + 1;
          
          LogUtils.d('viewPage: 目标索引超出范围，需要加载更多数据到第$targetPage页');
          
          // 加载数据直到目标页或没有更多数据
          while (current.value < targetPage && hasMore.value) {
            current.value++;
            await _fetchEpisodeData(updateUI: current.value >= targetPage);
          }
          
          // 检查是否成功加载到目标数据
          if (_isDisposed) return;
          
          if (targetIndex < mData.length) {
            LogUtils.d('viewPage: 数据加载完成，执行跳转到索引 $targetIndex');
            jumpToPage(targetIndex);
          } else {
            LogUtils.e('viewPage: 加载后仍然无法找到目标索引 $targetIndex');
            ToastUtil.showToast('该集数不存在');
          }
        } catch (e) {
          LogUtils.e('viewPage: 加载更多数据出错: $e');
          ToastUtil.showToast('加载失败，请检查网络');
        } finally {
          if (!_isDisposed) {
            isLoading.value = false;
          }
        }
      } else if (!hasMore.value) {
        LogUtils.d('viewPage: 无法加载更多数据，已到达末尾');
        ToastUtil.showToast('没有更多数据了');
      }
    } else {
      // 目标索引在当前数据范围内，直接跳转
      LogUtils.d('viewPage: 目标索引在已有数据范围内，直接跳转');
      jumpToPage(targetIndex);
    }
  }
  
  // 跳转到指定页面
  void jumpToPage(int targetIndex) {
    if (_isDisposed) return;
    
    LogUtils.d('准备跳转: 目标索引=$targetIndex, 当前索引=$index, 数据长度=${mData.length}');
    
    if (_pageController != null && _pageController!.hasClients && targetIndex < mData.length) {
      try {
        // 记录跳转前的状态
        final currentPage = _pageController!.page;
        LogUtils.d('执行跳转: 从页面$currentPage跳转到页面$targetIndex');
        
        // 使用animateToPage代替jumpToPage，可以触发onPageChanged
        if ((currentPage?.round() ?? -1) != targetIndex) {
          _pageController!.jumpToPage(targetIndex);
          // 在这里手动设置索引，以防onPageChanged没有触发
          index = targetIndex;
          LogUtils.d('跳转完成: 当前索引已更新为$index');
        } else {
          LogUtils.d('跳转跳过: 当前页面已经是目标页面');
        }
        
        if (!_isDisposed) {
          update();
        }
      } catch (e) {
        LogUtils.e('页面跳转出错: $e');
      }
    } else {
      final String reason = _pageController == null ? 
                            "控制器为空" : 
                            (!_pageController!.hasClients ? 
                            "控制器未连接" : 
                            "目标索引超出范围");
      LogUtils.e('无法执行页面跳转: $reason');
    }
  }

  // 点赞相关
  Future<void> submitData({int? type}) async {
    // 用户未登录或组件已销毁，跳过提交
    if (!UserInfoCtl.to.isLogin || _isDisposed) return;

    // 检查索引范围
    if (index >= mData.length) {
      LogUtils.e('索引越界，跳过提交');
      return;
    }

    try {
      Map<String, dynamic> params = {};
      params.putIfAbsent('userId', () => UserInfoCtl.to.profile.id);
      params.putIfAbsent('videoId', () => mData[index].videoByAction?.id);
      if (type == 0) {
        params.putIfAbsent('contentActionType', () => 1);
        params.putIfAbsent('isActive', () => mData[index].videoByAction?.userAction?.isLike == true ? 0 : 1);
      } else if (type == 1) {
        params.putIfAbsent('contentActionType', () => 2);
        params.putIfAbsent('isActive', () => mData[index].videoByAction?.userAction?.isCollect == true ? 0 : 1);
      } else if (type == 2) {
        params.putIfAbsent('contentActionType', () => 3);
      } else if (type == 3) {
        params.putIfAbsent('contentActionType', () => 4);
      }
      LogUtils.e('打印参数$params');
      var result = await NetUtil.addUserAction(data: params);
      LogUtils.d('观看记录提交结果: ${result['code']}');
    } catch (e) {
      LogUtils.e('提交观看数据出错: $e');
      // 非关键错误，不影响用户体验，可以静默失败
    }
  }

  // 提交单个视频的观看时长
  Future<void> submitVideoViewDuration() async {
    if (!UserInfoCtl.to.isLogin || index >= mData.length) return;
    try {
      if (!UserInfoCtl.to.isLogin) return;
      final Map<String, dynamic> params = {};
      params.putIfAbsent('userId', () => UserInfoCtl.to.profile.id);
      params.putIfAbsent('contentId', () => mData[index].shortPlayId);
      params.putIfAbsent('contentType', () => '2');
      params.putIfAbsent('episodeId', () => mData[index].id);
      params.putIfAbsent(
          'progressSeconds', () => progressSeconds < 0 ? 0 : progressSeconds);
      if (position != null) {
        params.putIfAbsent('viewProgress', () => position);
      }
      bool isEmpty = hasEmptyValue(params);
      if (isEmpty) {
        LogUtils.e('提交短剧参数为空');
        return;
      }
      if(progressSeconds < 1){
        LogUtils.e('提交短剧观看时间小于1');
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
    if(MyBetterPlayer.currentPlayerState?.isVideoPlaying()==true){
      submitVideoViewDuration();
    }
  }

  @override
  void onClose() {
    // 标记为已销毁
    _isDisposed = true;
    // 提交观看数据（如果有数据）
    if (mData.isNotEmpty) {
      submitVideoViewDuration();
    }
    // 销毁页面控制器
    _disposePageController();
    
    super.onClose();
  }
}
