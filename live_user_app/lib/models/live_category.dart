import 'live_item.dart';

class LiveCategory {
  String id = "";
  String title = "";
  List<LiveItem> list = [];

  LiveCategory({
    required this.id,
    required this.title,
    required this.list
  });

  LiveCategory.fromJson(Map<dynamic, dynamic> json) {
    id = json['id'];
    title = json['title'];
    list = json['list'].map<LiveItem>((v) => LiveItem.fromJson(v)).toList();
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['title'] = title;
    data['list'] = list;
    return data;
  }
}