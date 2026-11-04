class FeedbackEntity {
  String feedbackMessage = "";
  String replyMessage = "";
  String createTime = "";
  String updateTime = "";

  FeedbackEntity({
    required this.feedbackMessage,
    required this.replyMessage,
    required this.createTime,
    required this.updateTime,
  });

  // 构造方法
  // FeedbackEntity.empty() {
  //   id = '';
  //   nickname = '';
  //   mobile = '';
  //   provinceName = '';
  //   provinceCode = '';
  //   cityName = '';
  //   cityCode = '';
  //   countiesName = '';
  //   countiesCode = '';
  //   address = '';
  //   isDefault = '';
  //   createTime = '';
  // }

  FeedbackEntity.fromJson(Map<dynamic, dynamic> json) {
    feedbackMessage = json['feedbackMessage'] ?? '';
    replyMessage = json['replyMessage'] ?? '';
    createTime = json['createTime'] ?? '';
    updateTime = json['updateTime'] ?? '';
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data ={};
    data['feedbackMessage'] = feedbackMessage;
    data['replyMessage'] = replyMessage;
    data['createTime'] = createTime;
    data['updateTime'] = updateTime;
    return data;
  }
}
