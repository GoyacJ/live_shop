
class ChatContactEntity {
  String qrcode = "";
  String mobile = "";

  ChatContactEntity({
    required this.qrcode,
    required this.mobile,
  });

  // 构造方法
  ChatContactEntity.empty() {
    qrcode = '';
    mobile = '';
  }

  ChatContactEntity.fromJson(Map<dynamic, dynamic> json) {
    qrcode = json['qrcode'] ?? '';
    mobile = json['mobile'] ?? '';
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['qrcode'] = this.qrcode;
    data['mobile'] = this.mobile;
    return data;
  }
}
