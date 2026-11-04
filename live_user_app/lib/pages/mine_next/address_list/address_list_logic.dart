import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

import '../../../models/user_address.dart';

class AddressListLogic extends BaseController {
  RxList<UserAddressEntity> list = <UserAddressEntity>[].obs;
  RefreshController refreshController = RefreshController(initialRefresh: false);

  @override
  initData() async{
    _initData();
  }

  _initData() async {
    final Map<String, dynamic> result = await NetUtil.getUserAddressList();
    print("收获地址列表：$result");
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      list.value = result['data'].map<UserAddressEntity>((item) {
        return UserAddressEntity.fromJson(item);
      }).toList();

    }

    update();
  }

  void onRefresh() async{
    print("下拉刷新");
    await _initData();
    refreshController.refreshCompleted();
  }

  onSelectDefault(int index) async {
    print("选择默认地址$index----${list[index]}");

    if (list[index].isDefault == "1") return;

    final Map<String, dynamic> result = await NetUtil.setDefaultUserAddress(list[index].id);
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      list[index].isDefault = "1";
      for (var i = 0; i < list.length; i++) {
        if (i != index) {
          list[i].isDefault = "0";
        }
      }
      ToastUtil.showToast("已设为默认");
      update();
    }
  }

  onDeleteAddress(int index) async {
    print("删除地址$index----${list[index]}");

    final Map<String, dynamic> result = await NetUtil.deleteUserAddress(list[index].id);
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      list.removeAt(index);
      ToastUtil.showToast("删除成功");
      update();
    }
  }

  @override
  void onClose() {
    refreshController.dispose();
    super.onClose();
  }

}
