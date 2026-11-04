import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';
import '../../../widgets/jh_text.dart';
import 'message_list_logic.dart';

class MessageListPage extends BaseStatefulWidget<MessageListLogic> {
  const MessageListPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return controller.title.value;
  }

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(
        child: SmartRefresher(
      enablePullDown: true,
      enablePullUp: true,
      header: WaterDropHeader(
        refresh: SizedBox(
          width: 20.0,
          height: 20.0,
          child:  CircularProgressIndicator(color: ColorUtil.primaryColor,strokeWidth: 2.0),
        ),
        complete: Center(child: JhText.cText("刷新成功", ColorUtil.black, 14.0)),
      ),
      footer: CustomFooter(
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
      ),
      controller: controller.refreshController,
      onRefresh: controller.onRefresh,
      onLoading: controller.onLoading,
      child: _buildView(),
    ));
  }

  // 主视图
  Widget _buildView() {
    return ListView.builder(
        itemCount: controller.list.isEmpty ? 1 : controller.list.length,
        itemBuilder: (context, index) {
          if (controller.list.isEmpty) {
            return _buildNull();
          }
          return Container(
            decoration: const BoxDecoration(
              border: Border(
                  bottom:
                      BorderSide(width: 1, color: ColorUtil.backgroundColor)),
            ),
            child: ListTile(
              title: JhText.cText(controller.list[index].reason,
                  ColorUtil.black, 18.0,
                  fontWeight: FontWeight.bold),
              subtitle: JhText.cText(controller.list[index].createTime,
                  ColorUtil.black45, 15.0),
              trailing: JhText.cText(
                  controller.list[index].amount > 0
                      ? "+ ${controller.list[index].amount}"
                      : "- ${controller.list[index].amount}",
                  ColorUtil.primaryColor, 16.0,
                  fontWeight: FontWeight.bold),
            ),
          );
        });
  }

  // 暂无记录
  Widget _buildNull() {
    return SizedBox(
      width: double.infinity,
      height: 300.w,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.end,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Image.asset("assets/images/empty.png",
              height: 100.0, fit: BoxFit.contain),
          JhText.cText("暂无记录",
              ColorUtil.black38, 16.0)
        ],
      ),
    );
  }
}
