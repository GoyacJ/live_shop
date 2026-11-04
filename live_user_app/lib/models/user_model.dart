import 'package:common_utils/common_utils.dart';

class UserEntity {
  String id = "";
  String mobile = "";
  String nickname = "";
  String rcode = "";
  String avatar = "";
  int levelId = 0;
  double cash = 0.00;
  double frozenCash = 0.00;
  double integral = 0.00;
  double frozenIntegral = 0.00;

  // String vipCode = "";
  // String vipIcon = "";
  // String vipTitle = "";
  // String token = "";

  UserEntity({
    required this.id,
    required this.mobile,
    required this.nickname,
    required this.rcode,
    required this.avatar,
    required this.levelId,
    required this.cash,
    required this.frozenCash,
    required this.integral,
    required this.frozenIntegral,
    // required this.token
  });

  // 构造方法
  UserEntity.empty() {
    id = '';
    mobile = '';
    nickname = '';
    rcode = '';
    avatar = '';
    cash = 0.00;
    frozenCash = 0.00;
    integral = 0.00;
    frozenIntegral = 0.00;
    // token = '';
  }

  UserEntity.fromJson(Map<dynamic, dynamic> json) {
    id = json['id'] ?? '';
    mobile = json['mobile'] ?? '';
    nickname = json['nickname'];
    rcode = json['rcode'].toString();
    avatar = json['avatar'].toString();
    levelId = NumUtil.getIntByValueStr(json['levelId'].toString()) ?? 0;

    cash = (NumUtil.getNumByValueStr(json['cash'].toString(),
        fractionDigits: 2) ??
        0.00) as double;
    frozenCash = (NumUtil.getNumByValueStr(json['frozenCash'].toString(),
        fractionDigits: 2) ??
        0.00) as double;
    integral = (NumUtil.getNumByValueStr(json['integral'].toString(),
        fractionDigits: 2) ??
        0.00) as double;
    frozenIntegral = (NumUtil.getNumByValueStr(json['frozenIntegral'].toString(),
        fractionDigits: 2) ??
        0.00) as double;
    // token = json['token'] ?? '';
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['phone'] = mobile;
    data['nickname'] = nickname;
    data['rcode'] = rcode;
    data['avatar'] = avatar;
    data['levelId'] = levelId;
    data['cash'] = cash;
    data['frozenCash'] = frozenCash;
    data['integral'] = integral;
    data['frozenIntegral'] = frozenIntegral;
    // data['token'] = this.token;
    return data;
  }
}
