import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_staggered_grid_view/flutter_staggered_grid_view.dart';
import 'package:live_app/widgets/public_view.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';
import '../utils/color_util.dart';
import 'jh_text.dart';

class CustomListViewController {
  late VoidCallback resetNoData;
  late VoidCallback loadNoData;
  late VoidCallback requestRefresh;
  CustomListViewController();
}

class CustomListView extends StatefulWidget {
  final CustomListViewController? controller;
  final int total;
  final bool isList;
  final bool initialRefresh; // 默认开启首次刷新
  final Future<void> Function()? onRefresh;
  final Future<void> Function()? onLoad;
  final Widget? noDataWidget;
  final bool? showFooter;
  final Widget Function(BuildContext context, int index) itemBuilder;
  final bool enableLoadMore;

  const CustomListView({
    super.key,
    required this.total,
    this.isList = true,
    this.controller,
    this.onRefresh,
    this.onLoad,
    this.noDataWidget,
    this.showFooter,
    this.initialRefresh = true,
    required this.itemBuilder,
    this.enableLoadMore = true,
  });

  @override
  _CustomListViewState createState() => _CustomListViewState();
}

class _CustomListViewState extends State<CustomListView> {
  final RefreshController _refreshController = RefreshController();
  bool _isRefreshing = false;

  @override
  void initState() {
    super.initState();
    _bindController();
    _scheduleInitialRefresh();
  }

  void _bindController() {
    if (widget.controller != null) {
      widget.controller!
        ..resetNoData = _refreshController.resetNoData
        ..loadNoData = _refreshController.loadNoData
        ..requestRefresh = _triggerRefresh;
    }
  }

  // 刷新触发方法独立于initialRefresh配置
  void _triggerRefresh() {
    if (widget.onRefresh != null) {
      _refreshController.requestRefresh();
    }
  }

  void _scheduleInitialRefresh() {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      // 仅控制自动刷新，不影响手动调用
      if (widget.initialRefresh && widget.onRefresh != null) {
        _triggerRefresh();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return SmartRefresher(
      enablePullDown: widget.onRefresh != null,
      enablePullUp: _shouldEnablePullUp,
      header: PublicView.buildRefreshHeader(),
      footer: PublicView.buildLoadMoreFooter(),
      controller: _refreshController,
      onRefresh: _handleRefresh,
      onLoading: _handleLoadMore,
      child: _buildContent(),
    );
  }

  bool get _shouldEnablePullUp {
    return widget.enableLoadMore &&
        !_isRefreshing &&
        widget.total > 0 &&
        widget.onLoad != null;
  }

  Widget _buildRefreshHeader() {
    return WaterDropHeader(
      // refresh: Column(
      //   mainAxisAlignment: MainAxisAlignment.center,
      //   // children: [
      //     // MaterialImage.asset(
      //     //   "assets/images/loading.gif",
      //     //   width: 30,
      //     //   height: 30,
      //     // ),
      //   // ],
      // ),
      refresh: SizedBox(
        width: 20.0,
        height: 20.0,
        child: CircularProgressIndicator(
            color: ColorUtil.primaryColor, strokeWidth: 2.0),
      ),
      completeDuration: Duration.zero,
      complete: Center(child: JhText.cText("刷新成功", ColorUtil.black, 14.0)),
    );
  }

  _buildLoadMoreFooter() {
    return CustomFooter(
      height: widget.showFooter == false ? 0 : 60,
      builder: (context, mode) {
        Widget body;
        if (mode == LoadStatus.idle) {
          body = JhText.cText("上拉加载", ColorUtil.black, 14.0);
        } else if (mode == LoadStatus.loading) {
          body = const CupertinoActivityIndicator();
        } else if (mode == LoadStatus.failed) {
          body = JhText.cText("加载失败！点击重试！", ColorUtil.black, 14.0);
        } else if (mode == LoadStatus.canLoading) {
          body = JhText.cText("松手,加载更多!", ColorUtil.black, 14.0);
        } else {
          body = JhText.cText("没有更多数据了!", ColorUtil.black, 14.0);
        }
        return SizedBox(
          height: 55.0,
          child: Center(child: body),
        );
      },
    );
  }

  // Widget _buildLoadMoreFooter() {
  //   return CustomFooter(
  //     height: widget.showFooter == false ? 0 : 60,
  //     builder: (context, mode) {
  //       if (widget.showFooter == false) return Container();
  //
  //       Widget? content;
  //       switch (mode) {
  //         case LoadStatus.loading:
  //           content = _buildFooterText("loading...");
  //           break;
  //         case LoadStatus.noMore:
  //           content = _buildFooterText("no_more_data");
  //           break;
  //         case LoadStatus.idle:
  //           content = null;
  //           break;
  //         default:
  //           content = null;
  //       }
  //       return Container(child: content);
  //     },
  //   );
  // }

  Widget _buildFooterText(String text) {
    return Container(
      alignment: Alignment.center,
      padding: const EdgeInsets.only(top: 10, bottom: 20),
      child: JhText.cText(
        text,
        ColorUtil.black, 14.0,
        fontWeight: FontWeight.w400,
      ),
    );
  }

  Future<void> _handleRefresh() async {
    if (widget.onRefresh == null) return;

    setState(() => _isRefreshing = true);
    try {
      await widget.onRefresh!();
      _refreshController.refreshCompleted();
    } finally {
      setState(() => _isRefreshing = false);
    }
    _refreshController.resetNoData();
  }

  Future<void> _handleLoadMore() async {
    if (widget.onLoad == null || _isRefreshing) return;

    try {
      await widget.onLoad!();
      _refreshController.loadComplete();
    } catch (e) {
      _refreshController.loadNoData();
    }
  }

  Widget _buildContent() {
    if (widget.total > 0) {
      return widget.isList
          ? ListView.builder(
              shrinkWrap: true,
              cacheExtent: 800,
              itemCount: widget.total,
              itemBuilder: widget.itemBuilder,
            )
          : _buildGrid();
    }
    return Container(
      child: widget.noDataWidget ?? Center(child: _buildNull()),
    );
  }

  _buildGrid() {
    return MasonryGridView.count(
      crossAxisCount: 2,
      mainAxisSpacing: 10.w,
      crossAxisSpacing: 7.w,
      shrinkWrap: true,
      cacheExtent: 800,
      padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 20.w),
      itemCount: widget.total,
      itemBuilder: widget.itemBuilder,
    );
  }

  // 暂无记录
  Widget _buildNull() {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Image.asset("assets/images/empty.png",
            height: 100.0, fit: BoxFit.contain),
        JhText.cText("暂无数据",
            ColorUtil.black38, 16.0)
      ],
    );
  }
}
