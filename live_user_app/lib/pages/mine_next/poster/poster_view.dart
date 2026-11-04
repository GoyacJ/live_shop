import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:live_app/widgets/loading_dialog.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

import '../../../widgets/cache_image.dart';
import '../../../widgets/jh_text.dart';
import 'poster_logic.dart';

class PosterPage extends BaseStatefulWidget<PosterLogic> {
  const PosterPage({super.key});

  @override
  String? title() {
    // TODO: implement title
    return '邀请好友海报';
  }

  @override
  List<Widget>? appBarActionWidget() {
    // TODO: implement appBarActionWidget
    return [
      Container(
          alignment: Alignment.center,
          padding: const EdgeInsets.only(right: 16.0),
          child: InkWell(
            onTap: controller.onSaveImage,
            child: JhText.cText('保存图片',
                ColorUtil.primaryColor, 18.0,
                fontWeight: FontWeight.bold),
          ))
    ];
  }

  @override
  Widget buildContent(BuildContext context) {
    return _buildView(context);
  }

  // 主视图
  Widget _buildView(BuildContext context) {
    double height = MediaQuery.of(context).size.height;
    return controller.isLoading.value
        ? const LoadingDialog()
        : CacheImage(
            controller.poster.value,
            height: height,
          );
  }
}
