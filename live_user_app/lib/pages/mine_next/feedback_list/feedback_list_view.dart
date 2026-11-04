import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../base/widget/base_stateful_widget.dart';
import '../../../models/feedback.dart';
import 'feedback_list_logic.dart';

class FeedbackListPage extends BaseStatefulWidget<FeedbackListLogic> {
  const FeedbackListPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '我的反馈记录';
  }

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(
        child: SmartRefresher(
          enablePullDown: true,
          enablePullUp: false,
          header: const WaterDropHeader(
            refresh: SizedBox(
              width: 20.0,
              height: 20.0,
              child:  CircularProgressIndicator(color: ColorUtil.primaryColor,strokeWidth: 2.0),
            ),
            complete: Center(child: Text("刷新成功")),
          ),
          footer: CustomFooter(
            builder: (context, mode){
              Widget body ;
              if(mode==LoadStatus.idle){
                body = const Text("上拉加载");
              }
              else if(mode==LoadStatus.loading){
                body = const CupertinoActivityIndicator();
              }
              else if(mode == LoadStatus.failed){
                body = const Text("加载失败！点击重试！");
              }
              else if(mode == LoadStatus.canLoading){
                body = const Text("松手,加载更多!");
              }
              else{
                body =const Text("没有更多数据了!");
              }
              return SizedBox(
                height: 55.0,
                child: Center(child:body),
              );
            },
          ),
          controller: controller.refreshController,
          onRefresh: controller.onRefresh,
          child: _buildView(),
        )
    );
  }

  // 主视图
  Widget _buildView() {
    print(controller.list.length);
    return ListView.builder(
        padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 15.w),
        itemCount: controller.list.isEmpty ? 1 : controller.list.length,
        itemBuilder: (context, index) {
          if (controller.list.isEmpty) {
            return _buildNull();
          }

          FeedbackEntity item = controller.list[index];
          return Container(
            margin: EdgeInsets.only(bottom: 10.w),
            padding: EdgeInsets.symmetric(vertical: 10.w, horizontal: 12.0),
            decoration: const BoxDecoration(
                color: ColorUtil.white,
                borderRadius: BorderRadius.all(Radius.circular(6.0))
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: item.replyMessage.isEmpty ? [
                Row(
                  children: [
                    Expanded(
                      child: Text("反馈内容", style: TextStyle(fontSize: 16.sp, color: ColorUtil.black)),
                    ),
                    Text(item.createTime, style: TextStyle(fontSize: 14.sp, color: ColorUtil.black45))
                  ],
                ),
                const SizedBox(height: 10.0),
                Text(item.feedbackMessage+item.feedbackMessage+item.feedbackMessage+item.feedbackMessage, style: TextStyle(fontSize: 15.sp, color: ColorUtil.black45)),
                const SizedBox(height: 10.0),
              ] : [
                Row(
                  children: [
                    Expanded(
                      child: Text("反馈内容", style: TextStyle(fontSize: 16.sp, color: ColorUtil.black)),
                    ),
                    Text(item.createTime, style: TextStyle(fontSize: 14.sp, color: ColorUtil.black45))
                  ],
                ),
                const SizedBox(height: 10.0),
                Text(item.feedbackMessage+item.feedbackMessage+item.feedbackMessage+item.feedbackMessage, style: TextStyle(fontSize: 15.sp, color: ColorUtil.black45)),
                const SizedBox(height: 10.0),
                // 横线
                const Divider(height: 1.0, color: ColorUtil.backgroundColor),
                const SizedBox(height: 10.0),
                Row(
                  children: [
                    Expanded(
                      child: Text("回复内容", style: TextStyle(fontSize: 16.sp, color: ColorUtil.black)),
                    ),
                    Text(item.updateTime, style: TextStyle(fontSize: 14.sp, color: ColorUtil.black45))
                  ],
                ),
                const SizedBox(height: 10.0),
                Text(item.replyMessage, style: TextStyle(fontSize: 15.sp, color: ColorUtil.black45)),
                const SizedBox(height: 10.0),
              ],
            ),
          );
        }
    );
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
          Image.asset("assets/images/empty.png", height: 100.0, fit: BoxFit.contain),
          const Text("暂无记录", style: TextStyle(fontSize: 16.0, color: ColorUtil.black38))
        ],
      ),
    );
  }

}