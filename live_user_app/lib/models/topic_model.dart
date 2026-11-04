
/// Created by Jcy
/// on 2025/5/13
/// 
class TopicModel {
  int? carousel;
  String? img;
  String? specialTopicId;
  String? specialTopicName;

  TopicModel({this.img, this.specialTopicId, this.specialTopicName});

  TopicModel.fromJson(Map<String, dynamic> json) {
    carousel = json['carousel'];
    img = json['img'];
    specialTopicId = json['specialTopicId'];
    specialTopicName = json['specialTopicName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['carousel'] = carousel;
    data['img'] = img;
    data['specialTopicId'] = specialTopicId;
    data['specialTopicName'] = specialTopicName;
    return data;
  }
}
