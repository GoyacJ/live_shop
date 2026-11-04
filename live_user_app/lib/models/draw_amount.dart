import 'package:common_utils/common_utils.dart';

class DrawAmountEntity {
  String id = "";
  double amount = 0.00;
  String condition = "";

  DrawAmountEntity({
    required this.id,
    required this.amount,
    required this.condition,
  });

  // 构造方法
  DrawAmountEntity.empty() {
    id = '';
    amount = 0.00;
    condition = '';
  }

  DrawAmountEntity.fromJson(Map<dynamic, dynamic> json) {
    id = json['id'].toString();
    amount = (NumUtil.getNumByValueStr(json['amount'].toString(),
        fractionDigits: 2) ??
        0.00) as double;
    condition = json['condition'].toString();
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = id;
    data['amount'] = amount;
    data['condition'] = condition;
    return data;
  }
}
