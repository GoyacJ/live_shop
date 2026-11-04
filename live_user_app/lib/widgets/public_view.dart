import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';
import '../utils/color_util.dart';
import 'jh_text.dart';

class PublicView {
  static buildRefreshHeader() {
    return WaterDropHeader(
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

  static buildLoadMoreFooter() {
    return CustomFooter(
      height: 60,
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
}
