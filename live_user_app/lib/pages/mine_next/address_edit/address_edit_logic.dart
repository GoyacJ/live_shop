import 'package:city_pickers/city_pickers.dart';
import 'package:flutter/cupertino.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:get/get.dart';

import '../../../utils/color_util.dart';
import '../../../widgets/jh_text.dart';
import '../address_list/address_list_logic.dart';

class AddressEditLogic extends BaseController {
  late String _id = "";
  RxString province = ''.obs;
  RxString provinceCode = ''.obs;
  RxString city = ''.obs;
  RxString cityCode = ''.obs;
  RxString counties = ''.obs;
  RxString countiesCode = ''.obs;
  final TextEditingController nicknameController = TextEditingController();
  final TextEditingController mobileController = TextEditingController();
  final TextEditingController addressController = TextEditingController();

  @override
  initData() async {
    _initData();
  }

  _initData() async {
    var addressListGetX = Get.find<AddressListLogic>();
    var address = addressListGetX.list[Get.arguments];
    _id = address.id ?? '';
    print("当前选择的地址为：${address.toJson()}");
    nicknameController.text = address.nickname ?? '';
    mobileController.text = address.mobile ?? '';
    addressController.text = address.address ?? '';
    province.value = address.provinceName ?? '';
    provinceCode.value = address.provinceCode ?? '';
    city.value = address.cityName ?? '';
    cityCode.value = address.cityCode ?? '';
    counties.value = address.countiesName ?? '';
    countiesCode.value = address.countiesCode ?? '';
  }

  onSelectAddress() async {
    print("当前选择的地址为：${countiesCode.value}");
    Result? result = await CityPickers.showCityPicker(
      context: Get.context!,
      locationCode: countiesCode.value,
      cancelWidget: JhText.cText('取消', ColorUtil.grey, 16.0),
      confirmWidget: JhText.cText("确定", ColorUtil.blue, 16.0),
    );
    if (result != null) {
      province.value = result.provinceName ?? '';
      provinceCode.value = result.provinceId ?? '';
      city.value = result.cityName ?? '';
      cityCode.value = result.cityId ?? '';
      counties.value = result.areaName ?? '';
      countiesCode.value = result.areaId ?? '';

      update();
    }
  }

  onSubmit() async {
    if (nicknameController.text.isEmpty) {
      ToastUtil.showToast('请输入收货人姓名');
      return;
    }
    if (mobileController.text.isEmpty) {
      ToastUtil.showToast('请输入收货人手机号');
      return;
    }
    if (addressController.text.isEmpty) {
      ToastUtil.showToast('请输入收货人地址');
      return;
    }
    if (province.value.isEmpty) {
      ToastUtil.showToast('请选择省份');
      return;
    }
    if (city.value.isEmpty) {
      ToastUtil.showToast('请选择城市');
      return;
    }
    if (counties.value.isEmpty) {
      ToastUtil.showToast('请选择区县');
      return;
    }
    var result = await NetUtil.updateUserAddress({
      'id': _id,
      'nickname': nicknameController.text,
      'mobile': mobileController.text,
      'address': addressController.text,
      'provinceName': province.value,
      'provinceCode': provinceCode.value,
      'cityName': city.value,
      'cityCode': cityCode.value,
      'countiesName': counties.value,
      'countiesCode': countiesCode.value,
    });
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      ToastUtil.showToast('修改成功');
      Get.back(result: "refresh");
    }
  }

  @override
  void onClose() {
    nicknameController.dispose();
    mobileController.dispose();
    addressController.dispose();
    super.onClose();
  }
}
