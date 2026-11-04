import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:live_app/widgets/cache_image.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../../routes/app_pages.dart';
import '../../../widgets/refresh_list.dart';
import 'video_list_logic.dart';

class VideoListPage extends BaseStatefulWidget<VideoListLogic> {
  const VideoListPage({super.key});

  @override
  String? title() {
    return '热门短剧';
  }

  @override
  Widget buildContent(BuildContext context) {
    return CustomListView(
      isList: false,
      initialRefresh: false,
      total: controller.mData.length,
      controller: controller.customListViewController,
      itemBuilder: (BuildContext context, int index) {
        return InkWell(
          child: itemView(index),
          onTap: (){
            Get.toNamed(AppPages.videoChildPage,arguments: {
              'type':1,
              'id':controller.mData[index].id,
            });
          },
        );
      },
      onRefresh: ()async{
        controller.current.value =1;
        controller.handleRefresh();
      },
      onLoad: ()async{
        controller.current.value++;
        controller.handleRefresh(isRefresh: false);
      },
    );
  }

  itemView(int index) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        CacheImage(controller.mData[index].cover ?? '',radius: 8.r,height: 190.w,),
        Container(
          padding: EdgeInsets.only(left: 10.w),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              JhText.cText(controller.mData[index].title ?? '', ColorUtil.black, 15),
              JhText.cText('共${controller.mData[index].episodeCount}集', ColorUtil.black, 15),
            ],
          ),
        )
      ],
    );
  }

}
