import 'package:common_utils/common_utils.dart';

class LiveGoodsEntity {
  String liveId = "";
  String id = "";
  String title = "";
  String cover = "";
  double price = 0.00;
  int quantity = 0;
  int sales = 0;
  int stock = 0;

  LiveGoodsEntity({
    required this.liveId,
    required this.id,
    required this.title,
    required this.cover,
    required this.price,
    required this.quantity,
    required this.sales,
    required this.stock,
  });

//  构造方法
  LiveGoodsEntity.empty() {
    liveId = '';
    id = '';
    title = '';
    cover = '';
    price = 0.00;
    quantity = 0;
    sales = 0;
    stock = 0;
  }

  LiveGoodsEntity.fromJson(Map<dynamic, dynamic> json) {
    liveId = json['live_id'] ?? '';
    id = json['goods_id'] ?? '';
    title = json['goods_title'] ?? '';
    cover = json['goods_cover'] ?? '';
    price = (NumUtil.getNumByValueStr(json['price'].toString(),
        fractionDigits: 2) ??
        0.00) as double;
    quantity = NumUtil.getIntByValueStr(json['sales'].toString()) ?? 0;
    sales = NumUtil.getIntByValueStr(json['sales'].toString()) ?? 0;
    stock = NumUtil.getIntByValueStr(json['stock'].toString()) ?? 0;
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data ={};
    data['live_id'] = liveId;
    data['id'] = id;
    data['title'] = title;
    data['cover'] = cover;
    data['price'] = price;
    data['quantity'] = quantity;
    data['sales'] = sales;
    data['stock'] = stock;
    return data;
  }
}
