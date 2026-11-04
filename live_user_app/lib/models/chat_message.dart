class ChatMessageEntity {
  String nickname = "";
  String message = "";

  ChatMessageEntity({
    required this.nickname,
    required this.message,
  });

  ChatMessageEntity.fromJson(Map<dynamic, dynamic> json) {
    nickname = json['nickname'];
    message = json['message'];
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['nickname'] = nickname;
    data['message'] = message;
    return data;
  }
}
