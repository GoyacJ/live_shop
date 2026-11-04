class LiveItem {
  String id = "";
  String title = "";
  String image = "";
  int popularity = 0;
  String realName = "";
  String pictureUrl = "";

  LiveItem({
    required this.id,
    required this.title,
    required this.image,
    required this.popularity,
    required this.realName,
    required this.pictureUrl,
  });

  LiveItem.fromJson(Map<dynamic, dynamic> json) {
    id = json['id'];
    title = json['title'];
    image = json['image'];
    popularity = json['popularity'] ?? 0;
    realName = json['realName'];
    pictureUrl = json['pictureUrl'];
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['title'] = title;
    data['image'] = image;
    data['popularity'] = popularity;
    data['realName'] = realName;
    data['pictureUrl'] = pictureUrl;
    return data;
  }
}
