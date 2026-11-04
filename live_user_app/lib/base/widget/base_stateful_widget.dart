import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import '../../utils/color_util.dart';
import '../../widgets/easy_loading_mixin.dart';
import '../../widgets/empty_appbar.dart';
import '../../widgets/jh_text.dart';
import '../controller/base_controller.dart';

/// BaseStatefulWidget
/// 基础有状态组件
/// 提供通用的状态管理和生命周期功能
///
/// 泛型参数:
/// - [T] 继承自 [BaseController] 的控制器类型
abstract class BaseStatefulWidget<T extends BaseController>
    extends StatefulWidget with EasyLoadingMixin {
  final String? tag = null;

  const BaseStatefulWidget({super.key});

  /// 获取控制器实例
  T get controller {
    // LogD('tag>>>>>>>>>>>>>>${tag}');
    // LogUtils.d('Controller>>>>>>>>>>>>>>${T.toString()}');
    return GetInstance().find<T>(tag: tag);
  }

  /// AppBar相关配置
  /// 返回null则不显示AppBar
  bool showAppBar() => true;

  ///是否隐藏状态栏
  bool showTopBar() => false;

  bool showBackButton() => true;

  String? title() => null;

  Color setTopBarColor() => ColorUtil.transparent;

  Color appBarColor() => ColorUtil.white;

  Color appBarTitleColor() => ColorUtil.black87;

  double appBarElevation() => 0;

  List<Widget>? appBarActionWidget() => null;

  PreferredSizeWidget? appBarBottom() => null;

  Widget? appBarLeading() => null;

  double? appBarLeadingWidth() => null;

  Widget? flexibleSpaceWidget() => null;

  bool appBarCenterTitle() => true;

  TextStyle appBarTitleStyle() => TextStyle(
        color: appBarTitleColor(),
        fontSize: 18.sp,
        fontWeight: FontWeight.bold,
      );

  /// 页面相关配置
  Color backgroundColor() => ColorUtil.white;

  /// 状态栏样式：0=白色字体，1=黑色字体
  int statusBarStyle() => 1;

  bool wantKeepAlive() => true;

  bool enablePullRefresh() => false;

  /// 加载状态配置
  Color loadingBackgroundColor() => ColorUtil.black26;

  // Widget loadingWidget() => const CircularProgressIndicator();

  Widget loadingWidget() => Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SizedBox(
              height: 60.w,
              width: 60.w,
              // CircularProgressIndicator() 圆环加载框
              child: CupertinoActivityIndicator(
                radius: 13,
                color: backgroundColor()==ColorUtil.white?ColorUtil.textPlaceholder:ColorUtil.white,
              ),
            ),
            SizedBox(height: 15.w),
            JhText.cText('加载中...', backgroundColor()==ColorUtil.white?ColorUtil.black:ColorUtil.white, 14)
          ],
        ),
      );

  /// 错误状态配置
  /// [message] 错误信息
  /// [onRetry] 重试回调
  Widget errorWidget(String message, VoidCallback onRetry) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          // Text(message, style: const TextStyle(color: ColorUtil.grey)),
          Text('网络异常，请稍后重试！！！', style: TextStyle(color: backgroundColor()==ColorUtil.white?ColorUtil.black:ColorUtil.white)),
          const SizedBox(height: 16),
          TextButton(
            onPressed: onRetry,
            child: const Text('重试',style: TextStyle(
              color: ColorUtil.primaryColor
            ),),
          ),
        ],
      ),
    );
  }

  /// 空状态配置
  Widget emptyWidget() {
    return const Center(
      child: Text('暂无数据', style: TextStyle(color: ColorUtil.grey)),
    );
  }

  /// 构建页面内容
  /// [context] 构建上下文
  Widget buildContent(BuildContext context);

  @override
  State<BaseStatefulWidget> createState() => _BaseStatefulWidgetState<T>();
}

/// BaseStatefulWidget的状态类
/// 管理组件的生命周期和状态变化
class _BaseStatefulWidgetState<T extends BaseController>
    extends State<BaseStatefulWidget>
    with AutomaticKeepAliveClientMixin, WidgetsBindingObserver {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    if (widget.tag != null) {
      Get.delete<T>(tag: widget.tag);
    }
    super.dispose();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);
    switch (state) {
        /// 应用回到前台（可交互）
      case AppLifecycleState.resumed:
        widget.controller.onResume();
        break;
        /// 应用进入后台
      case AppLifecycleState.paused:
        widget.controller.onPause();
        break;
        /// 短暂不可用（如电话）
      case AppLifecycleState.inactive:
        widget.controller.onInactive();
        break;
        /// 程序已被分离，可能被系统清理（并不一定触发）
      case AppLifecycleState.detached:
        widget.controller.onDetached();
        break;
      default:
        break;
    }
  }

  @override
  void didChangeMetrics() {
    // TODO: implement didChangeMetrics
    super.didChangeMetrics();
    double height = MediaQuery.of(Get.context!).viewInsets.bottom;
    widget.controller.didChangeMetrics(height);
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return AnnotatedRegion<SystemUiOverlayStyle>(
      value: widget.statusBarStyle() == 0
          ? SystemUiOverlayStyle.light
          : SystemUiOverlayStyle.dark,
      child: Scaffold(
        backgroundColor: widget.backgroundColor(),
        resizeToAvoidBottomInset: false,
        appBar: createAppBar(),
        body: _buildBody(),
      ),
    );
  }

  createAppBar() {
    if (widget.showAppBar()) {
      return _buildAppBar();
    } else if (widget.showTopBar()) {
      return null;
    } else {
      return EmptyAppBar(
        topBarColor:widget.setTopBarColor(),
      );
    }
  }

  Widget _buildBody() {
    Widget content = GetBuilder<T>(
      tag: widget.tag,
      builder: (controller) => _buildMainContent(),
    );

    if (widget.enablePullRefresh()) {
      content = RefreshIndicator(
        color: ColorUtil.primaryColor,
        onRefresh: () => widget.controller.refreshData(),
        child: content,
      );
    }

    return content;
  }

  ///构建Scaffold-body主体内容
  Widget _buildMainContent() {
    return widget.controller.obx((state) {
      // LogUtils.e('打印状态值$state');
      return widget.buildContent(context);
    },
        onLoading: widget.loadingWidget(),
        onError: (error) => widget.errorWidget(
              error ?? '',
              () => widget.controller.refreshData(),
            ),
        onEmpty: widget.emptyWidget());
  }

  PreferredSizeWidget? _buildAppBar() {
    return AppBar(
      backgroundColor: widget.appBarColor(),
      elevation: widget.appBarElevation(),
      centerTitle: widget.appBarCenterTitle(),
      systemOverlayStyle: widget.statusBarStyle() == 0
          ? SystemUiOverlayStyle.light
          : SystemUiOverlayStyle.dark,
      leading: widget.appBarLeading() ??
          (widget.showBackButton() ? _buildBackButton() : null),
      leadingWidth: widget.appBarLeadingWidth(),
      flexibleSpace: widget.flexibleSpaceWidget(),
      title: widget.title() != null
          ? Text(
              widget.title()!,
              style: widget.appBarTitleStyle(),
            )
          : null,
      actions: widget.appBarActionWidget(),
      bottom: widget.appBarBottom(),
    );
  }

  Widget _buildBackButton() {
    return IconButton(
      icon: Icon(
        Icons.arrow_back_ios,
        color: widget.appBarTitleColor(),
        size: 24,
      ),
      onPressed: () => Navigator.maybePop(context),
    );
  }

  @override
  bool get wantKeepAlive => widget.wantKeepAlive();
}
