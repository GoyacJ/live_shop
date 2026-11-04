class ImgNav {
  String? id;
  String? title;
  String? image;

  ImgNav({
    required this.id,
    required this.title,
    required this.image
  });

  ImgNav.fromJson(Map<dynamic, dynamic> json) {
    id = json['id'];
    title = json['title'];
    image = json['image'];
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['title'] = title;
    data['image'] = image;
    return data;
  }
}
