class WatchModel {
  String? id;
  String? contentType;
  String? contentId;
  String? userId;
  String? episodeId;
  String? title;
  String? cover;
  int? duration;
  int? episodeNumber;
  String? videoUrl;
  String? viewProgress;
  String? lastWatchTime;
  bool? isFollow;

  WatchModel(
      {this.id,
      this.contentType,
      this.contentId,
      this.userId,
      this.episodeId,
      this.title,
      this.cover,
      this.duration,
      this.episodeNumber,
      this.videoUrl,
      this.viewProgress,
      this.lastWatchTime,
      this.isFollow});

  WatchModel.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    contentType = json['contentType'];
    contentId = json['contentId'];
    userId = json['userId'];
    episodeId = json['episodeId'];
    title = json['title'];
    cover = json['cover'];
    duration = json['duration'];
    episodeNumber = json['episodeNumber'];
    videoUrl = json['videoUrl'];
    viewProgress = json['viewProgress'];
    lastWatchTime = json['lastWatchTime'];
    isFollow = json['follow'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['contentType'] = this.contentType;
    data['contentId'] = this.contentId;
    data['userId'] = this.userId;
    data['episodeId'] = this.episodeId;
    data['title'] = this.title;
    data['cover'] = this.cover;
    data['duration'] = this.duration;
    data['episodeNumber'] = this.episodeNumber;
    data['videoUrl'] = this.videoUrl;
    data['viewProgress'] = this.viewProgress;
    data['lastWatchTime'] = this.lastWatchTime;
    data['follow'] = this.isFollow;
    return data;
  }
}
