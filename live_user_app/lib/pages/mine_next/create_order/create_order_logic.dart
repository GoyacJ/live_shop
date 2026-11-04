import 'package:common_utils/common_utils.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/init/config.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/utils/config_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:live_app/utils/log_utils.dart';
import 'package:fluwx/fluwx.dart';
import 'package:get/get.dart';
import '../../../models/payment.dart';
import '../../../models/user_address.dart';
import '../../../utils/web_pay_util.dart';

class CreateOrderLogic extends BaseController {
  Rx<UserAddressEntity> address = UserAddressEntity.empty().obs;
  RxList<PaymentEntity> paymentList = <PaymentEntity>[].obs;
  RxInt quantity = 1.obs;
  RxString totalPrice = '0.0'.obs;
  RxString payType = 'wx'.obs;

  String orderId = "";

  // late LiveGoodsEntity goodsDetail = LiveGoodsEntity.empty();
  TextEditingController remarkController = TextEditingController();
  final Fluwx _fluwx = Fluwx();
  late Function(WeChatResponse) responseListener;
  Map<String, dynamic> params = {};

  @override
  initData() async {
    if(!kIsWeb){
      _fluwx.registerApi(appId: Config.apiHost.wxAppId, universalLink: "https://your.univerallink.com/link/");
      responseListener = (response) {
        if (response is WeChatPaymentResponse) {
          if (response.isSuccessful) {
            ToastUtil.showToast('支付成功');
            Get.offNamed(AppPages.orderDetailPage, arguments: orderId);
          } else {
            ToastUtil.showToast('取消支付');
          }
        }
      };
      _fluwx.addSubscriber(responseListener);
    }
    _initData();
  }

  _initData() async {
    // 商品信息
    params = Get.arguments;
    // goodsDetail = Get.arguments;
    // print("商品信息${goodsDetail.toJson()}");

    totalPrice.value = (params['price'] * quantity.value).toStringAsFixed(2);

    // 获取默认地址
    final result = await NetUtil.getOrderBaseInfo();
    if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
      paymentList.value = result['data']['payment_list'].map<PaymentEntity>((item) {
        return PaymentEntity.fromJson(item);
      }).toList();
      address.value = UserAddressEntity.fromJson(result['data']['default_address']);
      print(address.value.toJson());
    }
    update();
  }

  onSelectAddress(UserAddressEntity selectAddress) {
    address.value = selectAddress;
    update();
  }

  onSelectPayment(String type) {
    payType.value = type;
    update();
  }

  onGoodsQuantityChange(int num) async {
    if (quantity.value + num < 1) {
      ToastUtil.showToast('商品数量不能小于1');
      return;
    }

    quantity.value += num;
    totalPrice.value = (params['price'] * quantity.value).toStringAsFixed(2);
    update();
  }

  onSubmit() async {
    if (address.value.id == '') {
      ToastUtil.showToast('请选择收货地址');
      return;
    }
    if (payType.value == '') {
      ToastUtil.showToast('请选择支付方式');
      return;
    }

    try {
      showEasyLoading();
      if (orderId.isNotEmpty) {
        print("重新发起支付");
        await onPayTest(orderId);
      } else {
        Map<String, dynamic> data = {};
        data.putIfAbsent('goodsId', () => params['id']);
        data.putIfAbsent('goodsCount', () => quantity.value);
        data.putIfAbsent('addressId', () => address.value.id);
        data.putIfAbsent('orderType', () => params['liveId'] != null ? "2" : "1");
        data.putIfAbsent('payType', () => payType.value);
        if (params['liveId'] != null) {
          data.putIfAbsent('sourceId', () => params['liveId']);
        }
        data.putIfAbsent('remark', () => remarkController.text);
        final result = await NetUtil.createOrder(data);
        if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
          orderId = result['data'];
          await onPayTest(result['data']);
        }
      }
      dismissEasyLoading();
    } catch (e) {
      print(e);
      dismissEasyLoading();
    }
  }

  onPayTest(String orderId) async {
    // final result = await NetUtil.getPayUrl(orderId, payType.value);
    String payChannel = _getPayChannelFromType(payType.value);
    final result = await NetUtil.getPayUrl(orderId, payChannel);
    print("支付参数$result");
    LogUtils.d('支付接口返回: $result');
    
    if(kIsWeb){
      // 检查返回的支付URL
      if (result['data'] != null && result['data']['h5Url'] != null) {
        String payUrl = result['data']['h5Url'];
        LogUtils.d('获取到支付URL: $payUrl');
        await h5Pay(orderId, payUrl,result['data']);
      } else {
        LogUtils.e('支付URL为空或格式错误');
        LogUtils.e('返回数据结构: ${result['data']}');
        ToastUtil.showToast('获取支付链接失败，请检查服务器配置');
      }
    }else{
      if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
        bool payStatus = await _fluwx.pay(
            which: Payment(
              appId: result['data']['appid'].toString(),
              partnerId: result['data']['partnerid'].toString(),
              prepayId: result['data']['prepayid'].toString(),
              packageValue: result['data']['package'].toString(),
              nonceStr: result['data']['noncestr'].toString(),
              timestamp: NumUtil.getIntByValueStr(result['data']['timestamp'].toString()) ?? 0,
              sign: result['data']['sign'].toString(),
            ));
        if (!payStatus) {
          ToastUtil.showToast('掉起支付失败');
        }
      }
    }
  }

  h5Pay(String orderId, String payUrl,Map result) async {
    try {
      // WeChatPay.initSDK(result['appid'], result['timestamp'], result['noncestr'], result['sign']);
      //
      // // 发起支付
      // await WeChatPay.pay(
      //   timestamp: result['timestamp'],
      //   nonceStr: result['nonceStr'],
      //   prepayId: result['prepayId'],
      //   signType: result['signType'],
      //   paySign: result['sign'],
      //   onSuccess: () {
      //     print('支付成功');
      //     // 跳转到成功页面
      //   },
      //   onFailure: (error) {
      //     print('支付失败: $error');
      //     // 显示错误提示
      //   },
      // );
      // 根据用户选择的支付方式确定支付渠道
      String payChannel = _getPayChannelFromType(payType.value);
      await WebPayUtil.launchH5Pay(
        orderId: orderId,
        payUrl: payUrl,
        payChannel: payChannel,
        onSuccess: (orderId) {
          ToastUtil.showToast('支付成功');
          Get.offNamed(AppPages.orderDetailPage, arguments: orderId);
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
      LogUtils.e('H5支付发起失败: $e');
      ToastUtil.showToast('支付发起失败：${e.toString()}');
    }
  }

  /// 根据支付方式类型获取对应的支付渠道
  String _getPayChannelFromType(String payType) {
    switch (payType.toLowerCase()) {
      case 'wx':
      case 'wechat':
      case 'weixin':
        return WebPayUtil.PAY_CHANNEL_WECHAT;
      case 'alipay':
      case 'ali':
        return WebPayUtil.PAY_CHANNEL_ALIPAY;
      default:
        // 默认使用微信支付
        return WebPayUtil.PAY_CHANNEL_WECHAT;
    }
  }

  @override
  void onClose() {
    remarkController.dispose();
    if(!kIsWeb){
      _fluwx.removeSubscriber(responseListener);
    } else {
      // Web环境下清理支付相关资源
      WebPayUtil.dispose();
    }
    super.onClose();
  }
}
