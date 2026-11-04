
class PaymentEntity {
  String id = "";
  String type = "";
  String title = "";
  String icon = "";


  PaymentEntity({
    required this.id,
    required this.type,
    required this.title,
    required this.icon,
  });

  // 构造方法
  PaymentEntity.empty() {
    id = '';
    type = '';
    title = '';
    icon = '';
  }

  PaymentEntity.fromJson(Map<dynamic, dynamic> json) {
    id = json['id'] ?? '';
    type = json['type'] ?? '';
    title = json['title'] ?? '';
    icon = json['icon'] ?? '';
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['type'] = type;
    data['title'] = title;
    data['icon'] = icon;
    return data;
  }
}
