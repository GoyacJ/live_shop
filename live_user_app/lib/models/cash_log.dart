import 'package:common_utils/common_utils.dart';

class CashLogEntity {
  String reason = "";
  double amount = 0.00;
  String createTime = "";

  CashLogEntity({
    required this.reason,
    required this.amount,
    required this.createTime
  });

  CashLogEntity.fromJson(Map<dynamic, dynamic> json) {
    reason = json['reason'].toString();
    amount = (NumUtil.getNumByValueStr(json['changeBalance'].toString(),
        fractionDigits: 2) ??
        0.00) as double;
    createTime = json['createTime'].toString();
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['reason'] = reason;
    data['changeBalance'] = amount;
    data['createTime'] = createTime;
    return data;
  }
}
