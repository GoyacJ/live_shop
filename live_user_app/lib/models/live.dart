
class LiveEntity {
  String id = "";
  String avatar = "";
  String anchor = "";
  String cover = "";
  String url = "";
  String viewership = "";


  LiveEntity({
    required this.id,
    required this.avatar,
    required this.anchor,
    required this.cover,
    required this.url,
    required this.viewership,
  });

  // 构造方法
  LiveEntity.empty() {
    id = '';
    avatar = '';
    anchor = '';
    cover = '';
    url = '';
    viewership = '';
  }

  LiveEntity.fromJson(Map<dynamic, dynamic> json) {
    id = json['id'] ?? '';
    avatar = json['avatar'] ?? '';
    anchor = json['anchor'] ?? '';
    cover = json['cover'] ?? '';
    url = json['url'] ?? '';
    viewership = json['viewership'] ?? '';
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['avatar'] = avatar;
    data['anchor'] = anchor;
    data['cover'] = cover;
    data['url'] = url;
    data['viewership'] = viewership;
    return data;
  }
}
