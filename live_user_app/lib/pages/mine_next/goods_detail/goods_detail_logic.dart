import 'package:carousel_slider/carousel_options.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:live_app/network/net_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:get/get.dart';

import '../../../models/goods_detail.dart';
import '../../../models/live_goods.dart';
import '../../../widgets/dialog/popup_share_dia.dart';

class GoodsDetailLogic extends BaseController {
  late String _id = "";
  late LiveGoodsEntity liveGoods = LiveGoodsEntity.empty();
  Rx<GoodsDetailEntity> goodsDetail = GoodsDetailEntity.empty().obs;
  RxInt currentIndex = 0.obs;
  int? type;
  String livePoster = '';
  bool? isShare;

  @override
  initData() async {
    // TODO: implement initData
    isShare = Get.arguments['isShare'];
    await _initData();
  }

  _initData() async {
    if (Get.arguments['data'] is LiveGoodsEntity) {
      liveGoods = Get.arguments['data'];
      _id = liveGoods.id;
      type = 0;
    } else {
      _id = Get.arguments['id'];
      type = 1;
    }
    print(liveGoods.toJson());
    print(_id);

    if (_id.isNotEmpty) {
      final result = await NetUtil.getGoodsDetail(_id);
      if (result['code'] == 200) {
        goodsDetail.value = GoodsDetailEntity.fromJson(result['data']);
        if (liveGoods.price > 0) {
          goodsDetail.value.price = liveGoods.price;
        }
        update();
      } else {
        ToastUtil.showToast(result['msg']);
        Get.back();
      }
    }

    // var addressListGetX = Get.find<AddressListController>();
    // var address = addressListGetX.list[Get.arguments];
    // _id = address.id ?? '';
    // print("当前选择的地址为：${address.toJson()}");
  }

  /// 获取APP推广海报
  Future<void> getUserPoster() async {
    try {
      showEasyLoading();
      var result = await NetUtil.getUserPoster(303, _id);
      if (result['code'] == 200) {
        livePoster = result['data'] ?? '';
        Get.bottomSheet(PopupShareDia(
          poster: livePoster,
        ));
      }
      dismissEasyLoading();
    } catch (e) {
      dismissEasyLoading();
      print(e);
    }
  }

  onSliderChanged(int index, CarouselPageChangedReason reason) {
    currentIndex.value = index;
    // update(["goods_detail"]);
  }
}
