import 'dart:async';
import 'dart:collection';
import 'package:better_player_plus/better_player_plus.dart';
import 'package:common_utils/common_utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/init/config.dart';
import 'package:live_app/models/video_player_model.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/services/user_info_ctl.dart';
import 'package:live_app/utils/log_utils.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:live_app/utils/custom_date_util.dart';
import 'package:live_app/widgets/cache_image.dart';
import 'package:live_app/widgets/jh_text.dart';
import 'package:live_app/widgets/video/custom_linear_progress.dart';
import 'package:live_app/widgets/video/episode_dialog.dart';
import 'package:live_app/widgets/video/right_bar.dart';
import 'package:live_app/widgets/video/timelabel.dart';
import 'package:live_app/widgets/video/video_info.dart';
import 'package:live_app/widgets/video/video_progress.dart';
import 'package:visibility_detector/visibility_detector.dart';

/// 视频播放组件
/// 支持短视频和剧集播放，包含播放控制和广告展示功能
/// 
/// 主要功能：
/// - 支持短视频和剧集两种播放模式
/// - 自动播放控制（可见性检测）
/// - 广告展示（每3个视频展示一次）
/// - 商品卡片展示
/// - 播放进度记录和断点续播
/// - 多播放器实例管理（防止串台）
class MyBetterPlayer extends StatefulWidget {
  final List<VideoPlayerModel> episodeList;     // 剧集列表
  final int index;                          // 当前索引
  final int type;                           // 视频类型(0:短视频,1:剧集)
  final int count;                          // 剧集总数
  final String? livePoster;                 // 分享海报
  final bool showProductCardPerVideo;       // 商品卡片显示策略

  // 回调函数
  final Function changeStateData;           // 状态更新回调
  final Function? viewPage;                 // 切换剧集回调
  final Function? onPosition;               // 播放进度回调
  final Function? onVideoFinished;          // 播放完成回调

  // 静态状态管理 - 用于全局播放器控制
  static MyBetterPlayerState? _currentPlayerState;
  static MyBetterPlayerState? get currentPlayerState => _currentPlayerState;
  static set currentPlayerState(MyBetterPlayerState? state) {
    _currentPlayerState = state;
  }

  // 视频观看记录 - 用于广告展示控制
  static int viewCount = 0;                 // 观看视频计数
  static final Set<String> viewedVideoIds = HashSet<String>();  // 已观看视频ID集合

  // 常量配置
  static const int adDisplayInterval = 3;                  // 每3个视频展示一次广告
  static const double defaultAspectRatio = 9 / 16;         // 默认竖屏比例
  static const int maxCacheSize = 100 * 1024 * 1024;       // 缓存大小(100MB)
  static const int maxCacheFileSize = 10 * 1024 * 1024;    // 单文件缓存上限(10MB)
  static const int maxViewedVideoIds = 50;                 // 记录最大观看视频数
  static const Duration animationDuration = Duration(milliseconds: 300);
  static const Duration bufferCheckInterval = Duration(milliseconds: 200);
  static const int progressUpdateThreshold = 100;          // 进度更新阈值(毫秒)
  static const double playIconSizeRatio = 0.25;            // 播放图标大小比例
  static const double playIconContainerRatio = 0.2;        // 播放图标容器比例

  // 全局播放器列表 - 用于多播放器实例管理
  static final List<MyBetterPlayerState> allPlayers = [];

  const MyBetterPlayer({
    super.key,
    required this.episodeList,
    required this.index,
    required this.type,
    required this.count,
    required this.changeStateData,
    this.viewPage,
    this.onPosition,
    this.livePoster,
    this.showProductCardPerVideo = true,
    this.onVideoFinished,
  });

  /// 处理视频切换和广告展示
  /// 在视频切换时调用，用于：
  /// 1. 记录观看历史
  /// 2. 控制广告展示频率
  /// 3. 重置播放状态
  static void onVideoChanged() {
    final state = currentPlayerState;
    if (state?.episode == null || state?.isDisposed == true || !state!.mounted) return;

    final videoId = state.episode!.id!;
    // 防止重复计数
    if (viewedVideoIds.contains(videoId)) return;

    // 记录观看并限制列表大小
    viewedVideoIds.add(videoId);
    if (viewedVideoIds.length > maxViewedVideoIds) {
      viewedVideoIds.remove(viewedVideoIds.first);
    }

    viewCount++;

    // 检查是否需要展示广告（每3个视频展示一次）
    if (viewCount % adDisplayInterval == 0) {
      Future.delayed(Duration(seconds: 3),(){
      });
    }

    // 重置暂停状态，便于自动播放
    if (!state.isDisposed && state.mounted) {
      state.setState(() {
        state.isManualPaused = false;
        state.hidePlayIcon = true;
      });
    }
  }

  /// 清理静态资源
  /// 在页面销毁时调用，清理所有静态状态
  static void clearStaticResources() {
    currentPlayerState = null;
    viewCount = 0;
    viewedVideoIds.clear();
    allPlayers.clear();  // 清理全局播放器列表
  }

  @override
  State<MyBetterPlayer> createState() => MyBetterPlayerState();
}

class MyBetterPlayerState extends State<MyBetterPlayer>
    with WidgetsBindingObserver {
  
  /// UI 控制相关
  bool hidePlayIcon = false; // 是否隐藏播放图标
  double aspectRatio = MyBetterPlayer.defaultAspectRatio; // 视频宽高比
  double videoScreenRate = 1.0; // 视频屏幕缩放比例（用于评论模式缩放）

  /// 视频控制器状态
  bool isControllerReady = false; // 控制器是否准备就绪
  bool isManualPaused = false; // 是否手动暂停（用户主动暂停）
  bool isBuffering = false; // 是否正在缓冲
  bool isShowingAd = false; // 是否正在显示广告
  int adStatus = 1; // 广告状态（1:未显示, 0:已显示）
  static bool checkedEpisodes = false; // 是否已检查过剧集（用于自动播放控制）

  /// 生命周期控制相关
  bool _wasPlayingBeforeBackground = false; // 进入后台前是否在播放
  bool isDisposed = false; // 标记组件是否已被销毁

  /// 商品卡片相关
  Timer? _watchTimer; // 观看计时器（用于控制商品卡片显示时机）
  int _watchedSeconds = 0; // 已观看秒数
  bool _hasShownProductCard = false; // 是否已显示商品卡片
  bool isProductCardVisible = false; // 商品卡片是否正在显示
  OverlayEntry? _productOverlayEntry; // 商品卡片 OverlayEntry
  Timer? _productAutoCloseTimer; // 商品卡片自动关闭计时器

  /// 视频播放相关
  late BetterPlayerController betterPlayerController; // 视频播放控制器
  bool _isPlayerInitialized = false; // 是否已初始化控制器
  Duration duration = Duration.zero; // 视频总时长
  Duration position = Duration.zero; // 当前播放位置
  Duration _lastPosition = Duration.zero; // 上次播放位置（用于缓冲检测）
  Duration? _initialPosition; // 初始播放位置（断点续播）
  String changeTime = "00:00"; // 拖动进度条时显示的时间
  bool timeLabelShow = false; // 是否显示时间标签

  /// 缓冲检测相关
  int _stuckFrameCount = 0; // 卡顿帧计数
  Timer? _bufferCheckTimer; // 缓冲检测定时器

  /// 当前播放的剧集信息
  VideoPlayerModel? episode;

  /// 可见性标记 - 用于自动播放控制
  bool isVisible = false;

  /// 安全的setState，避免在组件已被销毁时调用
  /// 防止在dispose后调用setState导致的错误
  @override
  void setState(VoidCallback fn) {
    if (isDisposed || !mounted) return;
    super.setState(fn);
  }

  @override
  void initState() {
    super.initState();
    // 将当前播放器实例添加到全局列表
    MyBetterPlayer.allPlayers.add(this);
    _initialize();
  }

  /// 初始化状态
  /// 完成播放器的完整初始化流程
  void _initialize() {
    episode = widget.episodeList[widget.index];
    _setInitialPosition();
    _initializeController().then((_) {
      // 初始化完成后，如果满足条件则自动播放
      if (mounted && !isDisposed && isControllerReady && !isManualPaused && checkedEpisodes) {
        Future.delayed(const Duration(milliseconds: 300), () {
          if (mounted && !isDisposed && isControllerReady && !isManualPaused) {
            _pauseAllPlayers();
            Future.delayed(const Duration(milliseconds: 100), () {
              if (mounted && !isDisposed && isControllerReady && !isManualPaused) {
                _playIfVisible();
              }
            });
          }
        });
      }
    });
    _startBufferCheck();
    _initWatchTimer();
    WidgetsBinding.instance.addObserver(this);
  }

  /// 初始化观看计时器
  /// 用于控制商品卡片的显示时机
  void _initWatchTimer() {
    _watchTimer?.cancel();
    _watchTimer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (!mounted || !isControllerReady || !_isPlayerInitialized) return;
      if (ObjectUtil.isEmptyString(episode?.goodsId)) return;
      if (!isVideoPlaying() || isShowingAd || isProductCardVisible) return;

      _watchedSeconds++;
      // 达到指定观看时长后显示商品卡片
      if (_watchedSeconds >= (episode?.startShowGoodsSecond ?? 0) && !_hasShownProductCard) {
        if (episode?.goodsId != null) {
          _showProductCard();
        }
      }
    });
  }

  /// 设置初始播放位置（断点续播）
  /// 从服务器获取用户上次观看的进度
  void _setInitialPosition() {
    String? progress = episode?.viewProgress;
    if (progress?.isNotEmpty == true) {
      _initialPosition = CustomDateUtil.parseDuration(progress!);
    }
  }

  /// 初始化视频控制器 - 优化版
  /// 配置播放器参数，设置缓存和缓冲策略
  Future<void> _initializeController() async {
    try {
      final videoUrl = _getVideoUrl();
      if (videoUrl?.isEmpty == true) return;

      // 如果已初始化，先清理旧控制器
      if (_isPlayerInitialized) {
        await _disposeController();
      }

      // 创建新的播放器控制器
      betterPlayerController = BetterPlayerController(
          BetterPlayerConfiguration(
            aspectRatio: MyBetterPlayer.defaultAspectRatio,
            fit: BoxFit.contain,
            autoPlay: widget.index == 0 && widget.type == 1, // 只有首页剧集自动播放
            looping: false,
            handleLifecycle: true,
            autoDispose: true,
            controlsConfiguration: BetterPlayerControlsConfiguration(
              showControls: false,
              enableFullscreen: false,
              enablePlayPause: false,
              enableProgressBar: false,
              enableProgressText: false,
            ),
          )
      );

      // 设置数据源，包含缓存和缓冲配置
      await betterPlayerController.setupDataSource(
          BetterPlayerDataSource(
            BetterPlayerDataSourceType.network,
            videoUrl!,
            cacheConfiguration: BetterPlayerCacheConfiguration(
              useCache: true,
              maxCacheSize: MyBetterPlayer.maxCacheSize,
              maxCacheFileSize: MyBetterPlayer.maxCacheFileSize,
            ),
            bufferingConfiguration: BetterPlayerBufferingConfiguration(
              minBufferMs: 15000,
              maxBufferMs: 30000,
              bufferForPlaybackMs: 2500,
              bufferForPlaybackAfterRebufferMs: 5000,
            ),
          )
      );

      if (!mounted || isDisposed) return;

      // 更新状态
      setState(() {
        _isPlayerInitialized = true;
        isControllerReady = true;
        duration = betterPlayerController.videoPlayerController?.value.duration ?? Duration.zero;
        aspectRatio = betterPlayerController.videoPlayerController?.value.aspectRatio ?? MyBetterPlayer.defaultAspectRatio;
      });

      // 添加事件监听器
      betterPlayerController.addEventsListener(_onPlayerEvent);
      betterPlayerController.videoPlayerController?.addListener(_videoListener);

      // 设置初始播放位置（断点续播）
      if (_initialPosition != null) {
        await betterPlayerController.seekTo(_initialPosition!);
      }
    } catch (e) {
      LogUtils.e('初始化控制器错误: $e');
    }
  }

  /// 获取视频URL
  String? _getVideoUrl() {
    return episode?.videoUrl;
  }

  /// 播放器事件处理
  /// 处理播放器的各种事件（初始化完成、播放结束等）
  void _onPlayerEvent(BetterPlayerEvent event) {
    if (!mounted) return;

    switch (event.betterPlayerEventType) {
      case BetterPlayerEventType.initialized:
        _handleVideoInitialized();
        break;
      case BetterPlayerEventType.finished:
        _handleVideoFinished();
        break;
      default:
        break;
    }
  }

  /// 处理视频初始化完成
  /// 更新视频尺寸信息，并尝试自动播放
  void _handleVideoInitialized() {
    if (!mounted || isDisposed) return;

    final videoAspectRatio = betterPlayerController.videoPlayerController?.value.aspectRatio ?? MyBetterPlayer.defaultAspectRatio;
    final videoDuration = betterPlayerController.videoPlayerController?.value.duration ?? Duration.zero;

    setState(() {
      aspectRatio = videoAspectRatio;
      duration = videoDuration;
    });

    // 如果非手动暂停，则尝试播放
    if (!isManualPaused && mounted && !isDisposed) {
      Future.delayed(const Duration(milliseconds: 300), () {
        if (mounted && !isDisposed && !isVideoPlaying() && !isManualPaused) {
          safePlay();
        }
      });
    }
  }

  /// 处理视频播放完成
  /// 重置播放状态，调用完成回调
  void _handleVideoFinished() {
    if (!mounted || !isControllerReady || !_isPlayerInitialized) return;

    setState(() {
      hidePlayIcon = false;
      isManualPaused = true;
    });

    final position = betterPlayerController.videoPlayerController?.value.position ?? Duration.zero;
    betterPlayerController.seekTo(position).then((_) {
      if (isVideoPlaying()) {
        betterPlayerController.pause();
      }
      widget.onVideoFinished?.call();
    });

    _resetWatchTimer();
  }

  /// 启动缓冲检测
  /// 定期检查播放器是否卡顿，用于显示缓冲指示器
  void _startBufferCheck() {
    _bufferCheckTimer?.cancel();
    _bufferCheckTimer = Timer.periodic(MyBetterPlayer.bufferCheckInterval, (_) {
      if (!mounted || !isControllerReady || !_isPlayerInitialized) return;

      final isPlaying = isVideoPlaying();
      if (!isPlaying) {
        if (isBuffering) setState(() => isBuffering = false);
        return;
      }

      final currentPosition = betterPlayerController.videoPlayerController?.value.position ?? Duration.zero;
      // 检测播放位置是否停滞
      if (isPlaying && currentPosition == _lastPosition) {
        _stuckFrameCount++;
        if (_stuckFrameCount >= 3 && !isBuffering) {
          setState(() => isBuffering = true);
        }
      } else {
        _stuckFrameCount = 0;
        if (isBuffering) {
          setState(() => isBuffering = false);
        }
      }

      _lastPosition = currentPosition;
    });
  }

  /// 检查播放器是否正在播放
  /// 安全地检查播放状态，避免异常
  bool isVideoPlaying() {
    if (!_isPlayerInitialized) return false;
    try {
      return betterPlayerController.isPlaying() ?? false;
    } catch (e) {
      return false;
    }
  }

  /// 视频播放状态监听器
  /// 监听播放进度变化，更新UI状态
  void _videoListener() {
    if (!mounted || !isControllerReady) return;

    try {
      final videoController = betterPlayerController.videoPlayerController;
      if (videoController == null) return;

      final newPosition = videoController.value.position;

      // 更新播放进度（避免频繁更新）
      if ((newPosition - position).inMilliseconds.abs() >= MyBetterPlayer.progressUpdateThreshold) {
        setState(() => position = newPosition);
        widget.onPosition?.call(position);
      }
    } catch (e) {
      LogUtils.e('视频监听器错误: $e');
    }
  }


  /// 安全播放方法
  /// 包含完整的状态检查和错误处理，确保播放安全
  Future<void> safePlay() async {
    // 状态检查
    if (!mounted || isDisposed || !isControllerReady || !_isPlayerInitialized) return;
    if (isShowingAd || isProductCardVisible) return;

    try {
      // 先暂停所有其他播放器（防止串台）
      _pauseAllPlayers();

      final videoController = betterPlayerController.videoPlayerController;

      // 检查是否在视频结尾，如果是则重置到开头
      if (videoController != null) {
        final currentPosition = videoController.value.position;
        final duration = videoController.value.duration;

        if (duration != null && currentPosition >= duration) {
          await betterPlayerController.seekTo(Duration.zero);
        }
      }

      // 播放视频
      if (!isManualPaused) {
        setState(() {
          hidePlayIcon = true;
          isManualPaused = false;
        });
        
        // 确保当前播放器是活跃的
        MyBetterPlayer.currentPlayerState = this;
        
        await Future.delayed(Duration(microseconds: 300));
        if (mounted && !isDisposed) {
          betterPlayerController.play();
        }
      }
    } catch (e) {
      LogUtils.e('播放错误: $e');
    }
  }

  /// 安全暂停方法
  /// 安全地暂停播放器，包含错误处理
  Future<void> safePause() async {
    if (!mounted || !isControllerReady || !_isPlayerInitialized) return;

    try {
      setState(() => hidePlayIcon = false);

      if (isVideoPlaying()) {
        await betterPlayerController.pause();
      }
    } catch (e) {
      LogUtils.e('暂停错误: $e');
    }
  }

  /// 处理单击事件
  /// 用户点击视频区域时的播放/暂停切换
  void onSingleTap() {
    if (!isControllerReady || !_isPlayerInitialized || isShowingAd) return;

    try {
      if (isVideoPlaying()) {
        // 暂停视频
        setState(() {
          hidePlayIcon = false;
          isManualPaused = true;
        });
        betterPlayerController.pause();
      } else {
        // 检查是否在视频结尾，如果是则重置到开头
        final videoController = betterPlayerController.videoPlayerController;
        if (videoController != null) {
          final currentPosition = videoController.value.position;
          final duration = videoController.value.duration;

          if (duration != null && currentPosition >= duration) {
            betterPlayerController.seekTo(Duration.zero);
          }
        }

        // 播放视频
        setState(() {
          hidePlayIcon = true;
          isManualPaused = false;
        });
        Future.delayed(Duration(microseconds: 300), () {
          betterPlayerController.play();
        });
      }
    } catch (e) {
      LogUtils.e('点击播放/暂停错误: $e');
    }
  }

  /// 设置时间变化
  /// 拖动进度条时显示的时间预览
  void setTimeChange(time) {
    setState(() {
      changeTime = time;
      timeLabelShow = true;
    });
  }

  /// 设置播放位置
  /// 拖动进度条结束后跳转到指定位置
  void setPosition(seekTime) {
    if (!isControllerReady || isShowingAd || isProductCardVisible) return;

    setState(() {
      betterPlayerController.seekTo(Duration(seconds: seekTime.floor()));
      timeLabelShow = false;
    });
  }

  /// 更新UI状态
  /// 处理右侧栏的各种状态更新（点赞、收藏、评论、分享等）
  void changeRightBarData({
    bool? isLike,
    bool? isCollect,
    bool? commentShow,
    bool? isShare,
    bool? hideComment,
    double? screenRate,
    bool? shop,
  }) {
    if (isLike != null) {
      widget.changeStateData(widget.index, isLike: isLike);
      return;
    }

    if (isCollect != null) {
      widget.changeStateData(widget.index, isCollect: isCollect);
      return;
    }

    if (commentShow != null) {
      if (commentShow) {
        setState(() {
          if (hideComment != null && hideComment == false) {
            videoScreenRate = 0.36;
            return;
          }

          if (screenRate != null) {
            videoScreenRate = screenRate > 1 ? 1.0 :
            (aspectRatio > 1 ? 0.76 : screenRate + 0.06);
          } else {
            videoScreenRate = 0.36;
          }
        });
      } else {
        setState(() {
          videoScreenRate = 1.0;
        });
      }
    }

    if (isShare != null) {
      widget.changeStateData(widget.index, isShare: isShare);
    }

    if (shop != null) {
      if (episode?.goodsId != null) {
        _showProductCard(display: false);
      }
    }
  }

  /// 显示选集弹窗
  /// 剧集模式下显示选集对话框
  void showEpisodeSelector(BuildContext context) {
    changeRightBarData(commentShow: true, screenRate: 0.3);
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: ColorUtil.white,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (_) {
        return EpisodeDialog(
          episodeNumber: episode?.episodeNumber ?? 0,
          episodeCount: widget.count,
          episodeList: widget.episodeList,
          onTap: (index) {
            if (widget.viewPage != null) {
              // 先暂停当前视频
              if (isVideoPlaying()) {
                betterPlayerController.pause();
              }
              // 重置观看计时器
              _resetWatchTimer();

              // 设置为非手动暂停状态
              setState(() {
                isManualPaused = false;
                hidePlayIcon = true;
                checkedEpisodes = true;
              });

              // 切换到新视频
              widget.viewPage!(index);
            }
          },
        );
      },
    ).then((_) {
      if (mounted && !isDisposed) {
        changeRightBarData(commentShow: false);
      }
    });
  }

  /// 应用生命周期变化处理
  /// 处理应用进入后台和回到前台时的播放状态
  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);

    if (!mounted || !isControllerReady || !_isPlayerInitialized) return;

    switch (state) {
      case AppLifecycleState.paused:
        // 进入后台时记录播放状态并暂停
        _wasPlayingBeforeBackground = isVideoPlaying();
        if (_wasPlayingBeforeBackground) {
          safePause();
        }
        break;
      case AppLifecycleState.resumed:
        // 回到前台时恢复播放
        if (_wasPlayingBeforeBackground && !isManualPaused) {
          Future.delayed(const Duration(milliseconds: 200), () {
            if (mounted && !isManualPaused) {
              safePlay();
            }
          });
        }
        break;
      default:
        break;
    }
  }

  /// 显示商品卡片
  /// 在视频播放指定时长后显示商品推广卡片
  void _showProductCard({bool display = true}) {
    if (isProductCardVisible || !mounted || _productOverlayEntry != null) return;

    // 设置商品卡片状态
    setState(() {
      _hasShownProductCard = true;
      isProductCardVisible = true;
    });

    // 计算尺寸
    final cardWidth = 105.w;
    final cardRadius = 10.r;

    // 创建并显示卡片
    final overlay = Overlay.of(context);
    _productOverlayEntry = OverlayEntry(
      builder: (context) => Positioned(
        right: 0.15.sw,
        bottom: widget.type == 0 ? 100 : 40, // 根据视频类型调整位置
        child: Material(
          color: ColorUtil.transparent,
          child: AnimatedOpacity(
            opacity: 1.0,
            duration: MyBetterPlayer.animationDuration,
            child: InkWell(
              child: Container(
                decoration: BoxDecoration(
                  color: ColorUtil.white,
                  borderRadius: BorderRadius.circular(cardRadius),
                ),
                padding: EdgeInsets.all(3.w),
                child: Column(
                  children: [
                    Stack(
                      alignment: Alignment.topRight,
                      children: [
                        CacheImage(
                          episode?.hostImage ?? '',
                          width: cardWidth,
                          height: cardWidth,
                          radius: cardRadius,
                        ),
                        InkWell(
                          onTap: hideProductCard,
                          child: const Icon(Icons.close_rounded, color: ColorUtil.white),
                        )
                      ],
                    ),
                    SizedBox(height: 5.w),
                    SizedBox(
                      width: cardWidth,
                      child: JhText.cText(
                        episode?.goodsName ?? '精选商品',
                        ColorUtil.black,
                        7,
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ),
                    Container(
                      height: 20.w,
                      decoration: BoxDecoration(
                        color: ColorUtil.brightRed,
                        borderRadius: BorderRadius.circular(20.r),
                      ),
                      margin: EdgeInsets.only(top: 10.w),
                      padding: EdgeInsets.symmetric(horizontal: 5.w),
                      child: Row(
                        children: [
                          JhText.cText('￥', ColorUtil.white, 7),
                          JhText.cText('${episode?.goodsPrice ?? 0.0}', ColorUtil.white, 10,
                              fontWeight: FontWeight.bold),
                          Container(
                            height: 20.w,
                            alignment: Alignment.center,
                            margin: EdgeInsets.only(left: 10.w),
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(20.r),
                              gradient: const LinearGradient(
                                colors: [
                                  ColorUtil.gradientRed1,
                                  ColorUtil.gradientRed2,
                                ],
                              ),
                            ),
                            child: JhText.cText('立即抢购', ColorUtil.white, 10,
                                fontWeight: FontWeight.bold),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
              onTap: () {
                hideProductCard();
                // 跳转到订单页
                Map<String, dynamic> params = {
                  'id': episode!.goodsId,
                  'price': episode!.goodsPrice,
                  'liveId': null,
                  'cover': episode!.hostImage,
                  'title': episode!.goodsName,
                };
                Get.toNamed(AppPages.createOrderPage, arguments: params);
              },
            ),
          ),
        ),
      ),
    );
    overlay.insert(_productOverlayEntry!);

    if(display){
      // 启动自动关闭定时器
      _productAutoCloseTimer = Timer(Duration(seconds: episode?.showGoodsSecond ?? 20), () {
        if (mounted && isProductCardVisible) {
          hideProductCard();
        }
      });
    }
  }

  /// 隐藏商品卡片
  /// 移除商品卡片并清理相关资源
  void hideProductCard() {
    if (!isProductCardVisible || _productOverlayEntry == null) return;

    // 取消自动关闭计时器
    _productAutoCloseTimer?.cancel();
    _productAutoCloseTimer = null;

    setState(() {
      isProductCardVisible = false;
    });

    // 移除卡片
    try {
      _productOverlayEntry?.remove();
    } finally {
      _productOverlayEntry = null;
    }
  }

  /// 重置观看计时器
  /// 在视频切换时重置观看状态和商品卡片
  void _resetWatchTimer() {
    _watchedSeconds = 0;

    // 根据显示模式重置状态
    if (widget.showProductCardPerVideo) {
      _hasShownProductCard = false;
    }

    // 取消计时器
    _productAutoCloseTimer?.cancel();
    _productAutoCloseTimer = null;

    // 清理商品卡片
    if (isProductCardVisible && _productOverlayEntry != null) {
      try {
        _productOverlayEntry?.remove();
      } finally {
        _productOverlayEntry = null;
        isProductCardVisible = false;
      }
    }
  }

  /// 清理控制器
  /// 安全地销毁播放器控制器
  Future<void> _disposeController() async {
    try {
      if (_isPlayerInitialized && isControllerReady) {
        betterPlayerController.removeEventsListener(_onPlayerEvent);
        betterPlayerController.videoPlayerController?.removeListener(_videoListener);
        betterPlayerController.dispose();
      }
    } catch (e) {
      LogUtils.e('清理控制器错误: $e');
    } finally {
      _isPlayerInitialized = false;
      isControllerReady = false;
    }
  }

  /// 暂停所有其他播放器
  /// 防止多个播放器同时播放（串台）
  void _pauseAllPlayers() {
    // 清理无效的播放器实例
    MyBetterPlayer.allPlayers.removeWhere((player) => 
      !player.mounted || player.isDisposed);
    
    for (var player in MyBetterPlayer.allPlayers) {
      if (player.mounted && !player.isDisposed && player != this) {
        player.safePause();
      }
    }
  }

  /// 播放可见的播放器
  /// 在多个播放器中找到可见的播放器并播放
  void _playIfVisible() {
    // 清理无效的播放器实例
    MyBetterPlayer.allPlayers.removeWhere((player) => 
      !player.mounted || player.isDisposed);
    
    MyBetterPlayerState? visiblePlayer;
    for (var player in MyBetterPlayer.allPlayers) {
      if (player.mounted && !player.isDisposed && player.isVisible) {
        visiblePlayer = player;
        break;
      }
    }
    if (visiblePlayer == this && isControllerReady && _isPlayerInitialized) {
      safePlay();
      checkedEpisodes = false;
    }
  }

  /// 处理可见性变化
  /// 当视频进入或离开屏幕时自动控制播放状态
  void _handleVisibilityChange(VisibilityInfo info) {
    if (!mounted || isDisposed) return;
    
    final wasVisible = isVisible;
    isVisible = info.visibleFraction == 1.0; // 完全可见时才认为可见
    
    // 处理可见性变化
    if (isVisible && !wasVisible) {
      // 变为可见时，确保其他播放器暂停
      _pauseAllPlayers();
      
      // 只有在非手动暂停且控制器就绪时才播放
      if (!isManualPaused && isControllerReady && _isPlayerInitialized) {
        Future.delayed(const Duration(milliseconds: 300), () {
          if (mounted && !isDisposed && !isManualPaused && isControllerReady && _isPlayerInitialized) {
            if (widget.index == 0 && widget.type == 0) {
              // 短视频首页特殊处理
              return;
            }
            // 再次检查是否仍然可见
            if (isVisible) {
              safePlay();
            }
          }
        });
      }
    } else if (!isVisible && wasVisible) {
      // 变为不可见时
      if (isProductCardVisible) {
        hideProductCard();
      }
      if (isControllerReady && _isPlayerInitialized && isVideoPlaying()) {
        safePause();
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final mediaQuery = MediaQuery.of(context);
    final screenHeight = mediaQuery.size.height;
    final screenWidth = mediaQuery.size.width;
    final topPadding = mediaQuery.padding.top;
    final isScaled = (videoScreenRate - 1.0).abs() > 0.01; // 判断是否处于缩放状态

    final videoAspectRatio = _isPlayerInitialized && betterPlayerController.videoPlayerController != null
        ? betterPlayerController.videoPlayerController!.value.aspectRatio
        : MyBetterPlayer.defaultAspectRatio;

    final videoDimensions = _calculateVideoDimensions(screenWidth, screenHeight, videoAspectRatio);
    final videoWidth = videoDimensions.width;
    final videoHeight = videoDimensions.height;

    // 更新当前播放器状态
    if (MyBetterPlayer.currentPlayerState != this) {
      MyBetterPlayer.currentPlayerState = this;
    }

    return VisibilityDetector(
      key: Key(episode!.id!),
      onVisibilityChanged: _handleVisibilityChange,
      child: Stack(
        children: [
          _buildVideoPlayer(screenWidth, screenHeight, topPadding, videoWidth, videoHeight, isScaled),
          if (!(hidePlayIcon || isVideoPlaying())) _buildPlayButton(),
          _buildVideoInfo(),
          if (isControllerReady) _buildVideoProgress(),
          _buildRightBar(screenHeight),
          if (isBuffering && isControllerReady) _buildBufferingIndicator(screenWidth, mediaQuery),
          if (widget.type == 1) _buildEpisodeHeader(),
        ],
      ),
    );
  }

  /// 计算视频尺寸
  /// 根据屏幕尺寸和视频宽高比计算最佳显示尺寸
  ({double width, double height}) _calculateVideoDimensions(double screenWidth, double screenHeight, double videoAspectRatio) {
    if (videoAspectRatio > 1) {
      // 横屏视频：以屏幕宽度为准
      return (width: screenWidth, height: screenWidth / videoAspectRatio);
    } else {
      // 竖屏视频：以屏幕高度为准
      return (width: screenHeight * videoAspectRatio, height: screenHeight);
    }
  }

  Widget _buildVideoPlayer(double screenWidth, double screenHeight, double topPadding, double videoWidth, double videoHeight, bool isScaled) {
    final videoAspectRatio = _isPlayerInitialized && betterPlayerController.videoPlayerController != null
        ? betterPlayerController.videoPlayerController!.value.aspectRatio
        : MyBetterPlayer.defaultAspectRatio;

    return AnimatedPositioned(
      duration: Duration(milliseconds: isScaled ? 200 : 0),
      curve: Curves.linear,
      top: !isScaled
          ? (videoAspectRatio > 1 ? (screenHeight - videoHeight) / 2 : 0.0)
          : topPadding,
      left: (screenWidth - videoWidth * videoScreenRate) / 2,
      width: videoWidth * videoScreenRate,
      height: videoHeight * videoScreenRate,
      child: GestureDetector(
        onTap: () => onSingleTap(),
        child: isControllerReady && _isPlayerInitialized
            ? BetterPlayer(controller: betterPlayerController)
            : CacheImage(
          width: videoWidth,
          height: videoHeight,
          episode?.cover ?? '',
          fit: BoxFit.cover,
          errorImage: 'assets/images/video_bg.png',
        ),
      ),
    );
  }

  Widget _buildPlayButton() {
    return Positioned.fill(
      child: Center(
        child: SizedBox(
          width: MyBetterPlayer.playIconContainerRatio.sw,
          height: MyBetterPlayer.playIconContainerRatio.sw,
          child: InkWell(
            onTap: () => onSingleTap(),
            child: Icon(
              Icons.play_arrow_rounded,
              color: const Color.fromRGBO(255, 255, 255, .7),
              size: MyBetterPlayer.playIconSizeRatio.sw,
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildVideoInfo() {
    return timeLabelShow
        ? TimeLabel(duration: duration, changeTime: changeTime)
        : VideoInfo(
      author: '',
      title: episode?.title ?? '',
      type: widget.type,
      count: widget.count,
      episodeNumber: episode?.episodeNumber ?? 1,
      onTap: () {
        showEpisodeSelector(context);
      },
    );
  }

  Widget _buildVideoProgress() {
    return VideoProgress(
        duration: duration,
        position: position,
        setTimeChange: setTimeChange,
        setPosition: setPosition
    );
  }

  Widget _buildRightBar(double screenHeight) {
    return RightBar(
      goodId:episode?.goodsId,
      type: widget.type,
      avatar: episode?.cover ?? '',
      isPlaying: (hidePlayIcon || isVideoPlaying()),
      changeRightBarData: changeRightBarData,
      screenHeight: screenHeight,
      isLike: episode?.like ?? false,
      isColl: episode?.collect ?? false,
      likeCount: episode?.likeCount ?? 0,
      commentCount: episode?.commentCount ?? 0,
      shareCount: episode?.shareCount ?? 0,
      collectCount: episode?.collectCount ?? 0,
      livePoster: widget.livePoster,
    );
  }

  Widget _buildBufferingIndicator(double screenWidth, MediaQueryData mediaQuery) {
    return Positioned(
      bottom: mediaQuery.padding.bottom + 20,
      child: CustomLinearProgressIndicator(
        width: screenWidth,
        height: 1,
        value: 1,
        color: const Color.fromRGBO(255, 255, 255, .5),
      ),
    );
  }

  Widget _buildEpisodeHeader() {
    return Container(
      alignment: Alignment.center,
      height: kToolbarHeight,
      margin: EdgeInsets.only(top: kToolbarHeight - 25),
      child: Stack(
        alignment: Alignment.centerLeft,
        children: [
          Align(
            alignment: Alignment.centerLeft,
            child: IconButton(
              padding: EdgeInsets.only(left: 20.w),
              icon: Icon(
                Icons.arrow_back_ios,
                color: ColorUtil.white,
                size: 24,
              ),
              onPressed: () => Get.back(),
            ),
          ),
          Padding(
            padding: EdgeInsets.only(left: 50.w),
            child: JhText.cText(
                '第${episode?.episodeNumber ?? 1}集',
                ColorUtil.white,
                18,
                fontWeight: FontWeight.w500
            ),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    // 从全局播放器列表中移除当前实例
    MyBetterPlayer.allPlayers.remove(this);
    isDisposed = true;

    // 如果当前实例是活跃播放器，清空引用
    if (identical(MyBetterPlayer.currentPlayerState, this)) {
      MyBetterPlayer.currentPlayerState = null;
    }

    // 取消所有定时器
    _watchTimer?.cancel();
    _productAutoCloseTimer?.cancel();
    _bufferCheckTimer?.cancel();

    // 清理商品卡片
    if (_productOverlayEntry?.mounted == true) {
      try {
        _productOverlayEntry?.remove();
      } catch (e) {
        LogUtils.e('移除OverlayEntry错误: $e');
      }
    }
    _productOverlayEntry = null;

    // 移除生命周期观察者
    WidgetsBinding.instance.removeObserver(this);

    // 同步清理播放器控制器
    if (_isPlayerInitialized || isControllerReady) {
      try {
        betterPlayerController.removeEventsListener(_onPlayerEvent);
        betterPlayerController.videoPlayerController?.removeListener(_videoListener);
        betterPlayerController.dispose();
      } catch (e) {
        LogUtils.e('清理视频控制器错误: $e');
      } finally {
        _isPlayerInitialized = false;
        isControllerReady = false;
      }
    }

    super.dispose();
  }
}
