import 'package:common_utils/common_utils.dart';
import 'package:flutter/foundation.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/init/config.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:fluwx/fluwx.dart';
import 'package:get/get.dart';
import 'package:live_app/services/user_info_ctl.dart';

import '../../../models/user_address.dart';
import '../../../utils/web_pay_util.dart';

class OrderDetailLogic extends BaseController {
  RxMap orderDetail = {}.obs;
  RxBool isLoading = true.obs;

  String orderId = "";
  final Fluwx _fluwx = Fluwx();
  late Function(WeChatResponse) responseListener;

  @override
  initData() async{

    if(!kIsWeb){
      _fluwx.registerApi(appId: Config.apiHost.wxAppId,universalLink: "https://your.univerallink.com/link/");
      responseListener = (response) {
        if (response is WeChatPaymentResponse) {
          if (response.isSuccessful) {
            ToastUtil.showToast('支付成功');
            Get.back(result: 'refresh');
          } else {
            ToastUtil.showToast('取消支付');
          }
        }
      };
      _fluwx.addSubscriber(responseListener);
    }
    await _initData();
  }

  _initData() async {
    // 商品信息
    orderId = Get.arguments;

    // 获取默认地址
    final result = await NetUtil.getOrderDetail(orderId);
    isLoading.value = false;
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      // orderId = result['data']['id'];

      orderDetail.value = result['data'];
    }
    update();
  }

  onSelectAddress(UserAddressEntity selectAddress) {
    // address.value = selectAddress;
    update();
  }

  onCancel() async {
    final result = await NetUtil.cancelOrder(orderId);
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      ToastUtil.showToast('取消成功');
      Get.back(result: 'refresh');
    }
  }

  onPay() async {
    String payType = kIsWeb?'wx_h5':'wx';
    final result = await NetUtil.getPayUrl(orderId, payType);
    print("支付参数$result");
    if(kIsWeb){
      await h5Pay(orderId,result['data']['h5Url']);
    }else{
      if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
        bool payStatus = await _fluwx.pay(
            which: Payment(
              appId: result['data']['appid'].toString(),
              partnerId: result['data']['partnerid'].toString(),
              prepayId: result['data']['prepayid'].toString(),
              packageValue: result['data']['package'].toString(),
              nonceStr: result['data']['noncestr'].toString(),
              timestamp: NumUtil.getIntByValueStr(
                  result['data']['timestamp'].toString()) ?? 0,
              sign: result['data']['sign'].toString(),
            )
        );
        if (!payStatus) {
          ToastUtil.showToast('掉起支付失败');
        }
      }
    }
  }

  h5Pay(String orderId, String payUrl) async {
    try {

      await WebPayUtil.launchH5Pay(
        orderId: orderId,
        payUrl: payUrl,
        payChannel: 'wx_h5',
        onSuccess: (orderId) {
          ToastUtil.showToast('支付成功');
          Get.back(result: 'refresh');
        },
        onFailed: (orderId, error) {
          ToastUtil.showToast('支付失败：${error.toString()}');
        },
        onCancelled: (orderId) {
          ToastUtil.showToast('取消支付');
        },
        onProgress: (status, message) {
          // 可以根据需要显示支付进度
          print('支付状态: $status, 消息: $message');
        },
      );
    } catch (e) {
      print('H5支付发起失败: $e');
      ToastUtil.showToast('支付发起失败：${e.toString()}');
    }
  }

  onComplete() async {
    final result = await NetUtil.completeOrder(orderId);
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      ToastUtil.showToast('确认收货成功');
      Get.back(result: 'refresh');
    }
  }

  @override
  void onClose() {
    _fluwx.removeSubscriber(responseListener);
    super.onClose();
  }
}
