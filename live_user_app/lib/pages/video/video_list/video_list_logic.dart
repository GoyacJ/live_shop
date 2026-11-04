import 'package:common_utils/common_utils.dart';
import 'package:get/get.dart';
import 'package:live_app/base/controller/base_controller.dart';

import '../../../models/home_video_model.dart';
import '../../../network/net_util.dart';
import '../../../services/user_info_ctl.dart';
import '../../../utils/toast_util.dart';
import '../../../widgets/refresh_list.dart';

class VideoListLogic extends BaseController {
  var current = 1.obs;
  List<HomeVideoModel> mData = [];
  CustomListViewController customListViewController = CustomListViewController();

  @override
  initData() async{
    // TODO: implement initData
   await handleRefresh();
  }

  ///短剧首页推荐
  Future<void> handleRefresh({bool isRefresh = true}) async {
    try {
      Map<String,dynamic>jsonMap = {};
      jsonMap.putIfAbsent('current', ()=>current.value);
      jsonMap.putIfAbsent('size', ()=>10);
      jsonMap.putIfAbsent('userId', ()=>UserInfoCtl.to.profile.id);
      var result = await NetUtil.getShortVideoList(data: jsonMap);
      if (result['code'] == 200) {
        if (ObjectUtil.isNotEmpty(result['data'])) {
          final List<dynamic> dataList = result['data']['records'];
          List<HomeVideoModel> newData = dataList.map((e) =>
              HomeVideoModel.fromJson(e)).toList();
          if (isRefresh) {
            // 刷新操作：清空数据
            current.value = 1;
            mData.clear();
            mData.addAll(newData);
          } else {
            // 加载更多操作：追加数据
            mData.addAll(newData);
            if (ObjectUtil.isEmptyList(newData) || newData.length < 10) {
              customListViewController.loadNoData();
            }
          }
          update();
        } else {
          ToastUtil.showToast(result['msg']);
        }
      }
    } catch (e) {
      LogUtil.e('视频列表异常错误$e');
    }
  }
}
