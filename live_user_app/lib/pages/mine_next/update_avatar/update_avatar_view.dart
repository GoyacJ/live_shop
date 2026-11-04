import 'package:common_utils/common_utils.dart';
import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../../widgets/cache_image.dart';
import 'update_avatar_logic.dart';

class UpdateAvatarPage extends BaseStatefulWidget<UpdateAvatarLogic> {
  const UpdateAvatarPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '更换头像';
  }

  @override
  Widget buildContent(BuildContext context) {
    return _buildView();
  }

  // 主视图
  Widget _buildView() {
    return Column(
      children: [
        Obx(()=>CacheImage(
          controller.imageUrl.value,
          width: 375.w,
          height: 375.w,
        )),

        InkWell(
          onTap: () => controller.onSelectImage(),
          child: Container(
            margin: EdgeInsets.only(top: 50.w),
            width: 300.w,
            height: 50.w,
            decoration: BoxDecoration(
              color: ColorUtil.primaryColor,
              borderRadius: BorderRadius.circular(6.w),
            ),
            child: Center(
              child: JhText.cText('更换头像', ColorUtil.white, 18.0),
            ),
          ),
        ),

        InkWell(
          onTap: () async {
            if (ObjectUtil.isEmpty(controller.imageUrl)) {
              ToastUtil.showToast("您还未设置头像");
              return ;
            }
            controller.saveData();
          },
          child: Container(
            margin: EdgeInsets.only(top: 20.w),
            width: 300.w,
            height: 50.w,
            decoration: BoxDecoration(
              color: ColorUtil.primaryColor,
              borderRadius: BorderRadius.circular(6.w),
            ),
            child: Center(
              child: JhText.cText('保存头像', ColorUtil.white, 18.0),
            ),
          ),
        ),
      ],
    );
  }
}
