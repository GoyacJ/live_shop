import 'package:common_utils/common_utils.dart';

class GoodsDetailEntity {
  String id = "";
  String title = "";
  String detail = "";
  String hostImage = "";
  double price = 0.00;
  double costPrice = 0.00;
  int sales = 0;
  List<String> slider = [];

  GoodsDetailEntity({
    required this.id,
    required this.title,
    required this.detail,
    required this.hostImage,
    required this.price,
    required this.costPrice,
    required this.sales,
    required this.slider,
  });

//  构造方法
  GoodsDetailEntity.empty() {
    id = '';
    title = '';
    detail = '';
    hostImage = '';
    price = 0.00;
    costPrice = 0.00;
    sales = 0;
    slider = [];
  }

  GoodsDetailEntity.fromJson(Map<dynamic, dynamic> json) {
    id = json['id'] ?? '';
    title = json['title'] ?? '';
    hostImage = json['hostImage'] ?? '';
    detail = json['detail'] ?? '';
    price = (NumUtil.getNumByValueStr(json['price'].toString(),
        fractionDigits: 2) ??
        0.00) as double;
    costPrice = (NumUtil.getNumByValueStr(json['costPrice'].toString(),
        fractionDigits: 2) ??
        0.00) as double;
    sales = NumUtil.getIntByValueStr(json['sales'].toString()) ?? 0;
    // slider = json['slider'].map<String>((v) => v).toList() ?? [];
    if (json['slider'] != null) {
      final v = json['slider'];
      final arr0 = <String>[];
      v.forEach((v) {
        arr0.add(v.toString());
      });
      slider = arr0;
    }
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['title'] = title;
    data['hostImage'] = hostImage;
    data['detail'] = detail;
    data['price'] = price;
    data['costPrice'] = costPrice;
    data['sales'] = sales;
    data['slider'] = slider;
    // if (slider != null) {
    //   final v = slider;
    //   final arr0 = [];
    //   v.forEach((v) {
    //     arr0.add(v);
    //   });
    //   data['slider'] = arr0;
    // }
    return data;
  }
}
