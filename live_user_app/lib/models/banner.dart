class MBanner {
  String title = "";
  String image = "";
  String url = "";
  String whetherToClick = "";
  String skipMode = "";


  MBanner({
    required this.title,
    required this.image,
    required this.url,
    required this.whetherToClick,
    required this.skipMode,
  });

  MBanner.fromJson(Map<dynamic, dynamic> json) {
    title = json['title'];
    image = json['image'];
    url = json['url'];
    whetherToClick = json['whetherToClick'];
    skipMode = json['skipMode'];
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['title'] = title;
    data['image'] = image;
    data['url'] = whetherToClick;
    data['image'] = image;
    data['skipMode'] = skipMode;
    return data;
  }
}
