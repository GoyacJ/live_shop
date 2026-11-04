import 'package:common_utils/common_utils.dart';

import 'order_goods.dart';

class OrderListEntity {
  String id = "";
  String orderNumber = "";
  double orderFee = 0.00;
  List<OrderGoodsEntity> goods = [];
  int status = 0;
  String createTime = "";

  OrderListEntity({
    required this.id,
    required this.orderNumber,
    required this.orderFee,
    required this.goods,
    required this.status,
    required this.createTime,
  });

  //  构造方法
  OrderListEntity.empty() {
    id = '';
    orderNumber = '';
    orderFee = 0.00;
    goods = [];
    status = 0;
    createTime = '';
  }

  OrderListEntity.fromJson(Map<dynamic, dynamic> json) {
    id = json['id'] ?? '';
    orderNumber = json['orderNumber'] ?? '';
    orderFee = (NumUtil.getNumByValueStr(json['orderFee'].toString(),
        fractionDigits: 2) ??
        0.00) as double;
    goods = (json['goods'] as List)
        .map((i) => OrderGoodsEntity.fromJson(i))
        .toList();
    status = NumUtil.getIntByValueStr(json['status'].toString()) ?? 0;
    createTime = json['createTime'] ?? '';
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['orderNumber'] = orderNumber;
    data['orderFee'] = orderFee;
    data['goods'] = goods.map((v) => v.toJson()).toList();
    data['status'] = status;
    data['createTime'] = createTime;
    return data;
  }
}
