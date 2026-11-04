import 'package:common_utils/common_utils.dart';

class OrderGoodsEntity {
  String goodsId = "";
  String goodsTitle = "";
  String goodsCover = "";
  double goodsPrice = 0.00;
  int goodsCount = 0;

  OrderGoodsEntity({
    required this.goodsId,
    required this.goodsTitle,
    required this.goodsCover,
    required this.goodsPrice,
    required this.goodsCount,
  });

  //  构造方法
  OrderGoodsEntity.empty() {
    goodsId = '';
    goodsTitle = '';
    goodsCover = '';
    goodsPrice = 0.00;
    goodsCount = 0;
  }

  OrderGoodsEntity.fromJson(Map<dynamic, dynamic> json) {
    goodsId = json['goodsId'].toString();
    goodsTitle = json['goodsTitle'].toString();
    goodsCover = json['goodsCover'].toString();
    goodsPrice = (NumUtil.getNumByValueStr(json['goodsPrice'].toString(),
        fractionDigits: 2) ??
        0.00) as double;
    goodsCount = NumUtil.getIntByValueStr(json['goodsCount'].toString()) ?? 0;
  }


  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['goodsId'] = goodsId;
    data['goodsTitle'] = goodsTitle;
    data['goodsCover'] = goodsCover;
    data['goodsPrice'] = goodsPrice;
    data['goodsCount'] = goodsCount;
    return data;
  }
}
