import 'package:common_utils/common_utils.dart';

class IntegralGoodsEntity {
  String id = "";
  String title = "";
  String cover = "";
  String detail = "";
  int integral = 0;
  int stock = 0;
  double marketPrice = 0.00;


  IntegralGoodsEntity({
    required this.id,
    required this.title,
    required this.cover,
    required this.detail,
    required this.integral,
    required this.stock,
    required this.marketPrice,
  });

  // 构造方法
  IntegralGoodsEntity.empty() {
    id = '';
    title = '';
    cover = '';
    detail = '';
    integral = 0;
    stock = 0;
    marketPrice = 0.00;
  }

  IntegralGoodsEntity.fromJson(Map<dynamic, dynamic> json) {
    id = json['id'] ?? '';
    title = json['title'] ?? '';
    cover = json['cover'] ?? '';
    detail = json['detail'] ?? '';
    integral = NumUtil.getIntByValueStr(json['integral'].toString()) ?? 0;
    stock = NumUtil.getIntByValueStr(json['stock'].toString()) ?? 0;
    marketPrice = (NumUtil.getNumByValueStr(json['marketPrice'].toString(),
        fractionDigits: 2) ??
        0.00) as double;
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['title'] = title;
    data['cover'] = cover;
    data['detail'] = detail;
    data['integral'] = integral;
    data['stock'] = stock;
    data['marketPrice'] = marketPrice;
    return data;
  }
}
