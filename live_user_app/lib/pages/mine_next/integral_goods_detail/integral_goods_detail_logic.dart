import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/services/user_info_ctl.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:get/get.dart';

import '../../../models/integral_goods.dart';
import '../../../models/user_address.dart';

class IntegralGoodsDetailLogic extends BaseController {
  late String _id = "";
  Rx<IntegralGoodsEntity> goodsDetail = IntegralGoodsEntity.empty().obs;
  Rx<UserAddressEntity> address = UserAddressEntity.empty().obs;

  @override
  initData() async{
    // TODO: implement initData
   await _initData();
  }


  _initData() async {
    _id = Get.arguments;
    final result = await NetUtil.getIntegralGoodsDetail(_id);
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      goodsDetail.value = IntegralGoodsEntity.fromJson(result['data']['goods_detail']);
      address.value = UserAddressEntity.fromJson(result['data']['default_address']);
    }
    update();
  }

  onSelectAddress(UserAddressEntity selectAddress) {
    address.value = selectAddress;
    update();
  }

  onSubmit() async {
    if (address.value.id == '0') {
      ToastUtil.showToast('请选择收货地址');
      return;
    }

    if (goodsDetail.value.integral > UserInfoCtl.to.profile.integral) {
      ToastUtil.showToast('积分不足');
      return;
    }

    final result = await NetUtil.exchangeIntegralGoods({
      "id": goodsDetail.value.id,
      "addressId": address.value.id,
    });
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      ToastUtil.showToast("兑换成功");
      UserInfoCtl to = Get.find();
      to.getProfile();
    }
  }

}
