import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/pages/video/watch_history/record_tab.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:live_app/widgets/cache_image.dart';
import 'package:live_app/widgets/jh_text.dart';

import 'watch_history_logic.dart';

class WatchHistoryPage extends BaseStatefulWidget<WatchHistoryLogic> {
  const WatchHistoryPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '观看记录';
  }

  @override
  List<Widget>? appBarActionWidget() {
    // TODO: implement appBarActionWidget
    return [TextButton(onPressed: () {}, child: JhText.cText('编辑', ColorUtil.textSecondary, 14))];
  }

  @override
  Widget buildContent(BuildContext context) {
    return SafeArea(
        child: Column(
      children: [
        _buildCustomTabBar(),
        Expanded(child: _buildTabContent()),
      ],
    ));
  }

  Widget _buildCustomTabBar() {
    return Container(
      color: ColorUtil.white,
      alignment: Alignment.center,
      child: TabBar(
        controller: controller.tabController,
        indicator: UnderlineTabIndicator(
          borderSide: BorderSide(
            color: ColorUtil.primaryColor,
            width: 2,
          ),
          insets: EdgeInsets.symmetric(horizontal: 40),
        ),
        indicatorPadding: EdgeInsets.symmetric(horizontal: 45.w),
        labelColor: ColorUtil.black,
        unselectedLabelColor: ColorUtil.grey,
        labelStyle: TextStyle(
          fontSize: 16,
          fontWeight: FontWeight.bold,
        ),
        unselectedLabelStyle: TextStyle(
          fontSize: 16,
          fontWeight: FontWeight.normal,
        ),
        tabs: [
          Tab(text: "记录"),
          Tab(text: "追剧"),
        ],
      ),
    );
  }

  Widget _buildTabContent() {
    return TabBarView(
      controller: controller.tabController,
      children: [
        // 记录页内容
        RecordTabPage(tag: '1',),
        // 追剧页内容（根据需求自定义）
        RecordTabPage(tag: '2'),
        // Center(child: Text("追剧页面内容")),
      ],
    );
  }


  Widget _buildRecordList() {
    return ListView.separated(
      padding: EdgeInsets.all(16),
      itemCount: controller.records.length,
      separatorBuilder: (_, __) => SizedBox(height: 16),
      itemBuilder: (context, index) {
        final item = controller.records[index];
        return _buildRecordItem(item);
      },
    );
  }

  Widget _buildRecordItem(Map<String, dynamic> item) {
    return Container(
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(8),
        color: ColorUtil.white,
        boxShadow: [
          BoxShadow(
            color: ColorUtil.black12,
            blurRadius: 4,
            offset: Offset(0, 2),
          )
        ],
      ),
      child: Row(
        children: [
          // 封面图
          CacheImage(
            item["cover"],
            width: 134.w,
            height: 90.w,
            radius: 7.r,
            fit: BoxFit.cover,
          ),
          SizedBox(width: 5.w,),
          // 右侧内容
          Expanded(child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              JhText.cText(item["title"],null,14),
              SizedBox(height: 8),
              JhText.cText(item["progress"],ColorUtil.grey[600],12),
            ],
          )),
          InkWell(
            child: Container(
              padding: EdgeInsets.symmetric(horizontal: 10.w,vertical: 5.w),
              decoration: BoxDecoration(
                color: item["followed"] ? ColorUtil.red : ColorUtil.white,
                border: Border.all(
                  color: item["followed"] ? ColorUtil.red : ColorUtil.followGrey,
                ),
                borderRadius: BorderRadius.circular(20),
              ),
              child: JhText.cText(item["followed"] ? "已追剧" : "追剧", item["followed"] ? ColorUtil.white : ColorUtil.black, 14),
            ),
            onTap: (){
              item["followed"] = !item["followed"];
              controller.update();
            },
          ),
          InkWell(
            child: Container(
              padding: EdgeInsets.symmetric(horizontal: 15.w,vertical: 5.w),
              child: Image.asset(
                'assets/images/ic_del.png',
                width: 10.w,
                height: 10.w,
              ),
            ),
            onTap: (){
              item["followed"] = !item["followed"];
              controller.update();
            },
          ),
        ],
      ),
    );
  }
}
