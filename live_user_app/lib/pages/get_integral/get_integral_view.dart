import 'package:common_utils/common_utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/widgets/cache_image.dart';

import '../../services/user_info_ctl.dart';
import '../../utils/color_util.dart';
import '../../widgets/jh_text.dart';
import 'get_integral_logic.dart';

class GetIntegralPage extends BaseStatefulWidget<GetIntegralLogic> {
  const GetIntegralPage({super.key});

  @override
  bool showAppBar() {
    return false;
  }

  @override
  bool enablePullRefresh() {
    return true;
  }

  @override
  bool showTopBar() {
    return true;
  }

  @override
  int statusBarStyle() => 1;

  @override
  Widget buildContent(BuildContext context) {
    return bodyView();
  }
   bodyView(){
    return CustomScrollView(
      slivers: [
        SliverAppBar(
          expandedHeight: 210.w,
          backgroundColor: Colors.white,
          surfaceTintColor: Colors.transparent,
          floating: false,
          snap: false,
          pinned: true,
          stretch: true,
          flexibleSpace: LayoutBuilder(
            builder: (BuildContext context, BoxConstraints constraints) {
              // 计算滚动比例 t：从 0（展开）到 1（完全收起）
              final double t = ((constraints.maxHeight - kToolbarHeight) / (210.w - kToolbarHeight))
                  .clamp(0.0, 1.0);
              LogUtil.e('打印t:$t');
              return FlexibleSpaceBar(
                centerTitle: true,
                collapseMode: CollapseMode.parallax,
                title: Opacity(
                  opacity: t<0.2?1.0:0.0,
                  // opacity: 1.0 - t, // 滚动越多，透明度越高
                  child: JhText.cText(
                    '积分中心',
                    ColorUtil.black,
                    18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                background: Stack(
                  alignment: Alignment.center,
                  children: [
                    Image.asset(
                      'assets/images/ic_integral_bg.png',
                      height: 260.w,
                      fit: BoxFit.cover,
                    ),
                    Column(
                      children: [
                        SizedBox(height: kToolbarHeight - 20),
                        CircleAvatar(
                          radius: 30.w,
                          backgroundColor: ColorUtil.black26,
                          backgroundImage: UserInfoCtl.to.profile.avatar.isNotEmpty
                              ? NetworkImage(UserInfoCtl.to.profile.avatar)
                              : const AssetImage('assets/images/default-avatar.jpg')
                          as ImageProvider,
                        ),
                        const SizedBox(height: 8),
                        JhText.cText('剩余积分(积分当钱花)', ColorUtil.white, 16),
                        const SizedBox(height: 8),
                        Obx(() => JhText.cText(
                          '${UserInfoCtl.to.integer.value}',
                          ColorUtil.white,
                          36,
                          fontWeight: FontWeight.bold,
                        )),
                      ],
                    ),
                  ],
                ),
              );
            },
          ),
        ),
        SliverToBoxAdapter(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 12),
            child: Align(
              alignment: Alignment.centerLeft,
              child: Text(
                '日常任务',
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
            ),
          ),
        ),
        SliverList(
          delegate: SliverChildBuilderDelegate(
                (context, index) {
              return _buildTaskItem(
                controller.mData[index].icon ?? 'assets/images/ic_sign_in.png',
                controller.mData[index].taskName ?? '',
                controller.mData[index].taskDesc ?? '',
                '${controller.mData[index].rewardIntegral ?? 0}',
                controller.mData[index].isComplete == 0 ? "已完成" : "去完成",
                controller.mData[index].frequencyLimit ?? 0,
                controller.mData[index].completeCount ?? 0,
                active: controller.mData[index].isComplete == 0 ? false : true,
                onTap: () {
                  controller.goPageByType(index);
                },
              );
            },
            childCount: controller.mData.length,
          ),
        ),
      ],
    );
   }
  // 任务项构建方法
  Widget _buildTaskItem(String path, String title, String subtitle,
      String point, String actionText,int sum,int count,
      {required bool active, required VoidCallback onTap}) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 6),
      child: Row(
        children: [
          CacheImage(
            path,
            width: 35.w,
            height: 35.w,
            radius: 3.r,
          ),
          const SizedBox(width: 8),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    Expanded(child: JhText.cText(title, null, 16,maxLines: 1,overflow: TextOverflow.ellipsis)),
                    if (point.isNotEmpty)
                      Row(
                        children: [
                          JhText.cText(point, ColorUtil.orange, 14),
                          const Icon(Icons.card_giftcard,
                              color: ColorUtil.orange, size: 18),
                        ],
                      ),
                  ],
                ),
                Row(
                  children: [
                    Expanded(child: JhText.cText(subtitle, ColorUtil.grey, 12)),
                    Container(
                      padding: EdgeInsets.symmetric(horizontal: 4.w, vertical: 2.w),
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(3.r),
                        color: ColorUtil.orange,
                      ),
                      child: JhText.cText(
                        sum == 0 ? '1/1' : '${count > sum ? sum : count}/$sum',
                        ColorUtil.white,
                        12,
                      ),
                    )
                  ],
                )
              ],
            ),
          ),
          SizedBox(width: 10.w),
          ElevatedButton(
            style: ElevatedButton.styleFrom(
              elevation: 0.0,
              backgroundColor: active ? ColorUtil.orange : ColorUtil.grey[300],
              foregroundColor: active ? ColorUtil.white : ColorUtil.black54,
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(20)),
            ),
            onPressed: active ? onTap : null,
            child: Text(actionText),
          ),
        ],
      ),
    );
  }
}
