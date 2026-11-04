
/// Created by Jcy
/// on 2025/5/21
/// 
class WatchHistoryModel {
  String? episodeCover;
  String? episodeId;
  String? episodeTitle;
  String? id;
  int? playCount;
  String? shortPlayCover;
  String? shortPlayId;
  String? shortPlayTitle;
  String? userId;
  int? viewDuration;
  String? viewProgress;
  String? viewTime;

  WatchHistoryModel(
      {this.episodeCover,
        this.episodeId,
        this.episodeTitle,
        this.id,
        this.playCount,
        this.shortPlayCover,
        this.shortPlayId,
        this.shortPlayTitle,
        this.userId,
        this.viewDuration,
        this.viewProgress,
        this.viewTime});

  WatchHistoryModel.fromJson(Map<String, dynamic> json) {
    episodeCover = json['episodeCover'];
    episodeId = json['episodeId'];
    episodeTitle = json['episodeTitle'];
    id = json['id'];
    playCount = json['playCount'];
    shortPlayCover = json['shortPlayCover'];
    shortPlayId = json['shortPlayId'];
    shortPlayTitle = json['shortPlayTitle'];
    userId = json['userId'];
    viewDuration = json['viewDuration'];
    viewProgress = json['viewProgress'];
    viewTime = json['viewTime'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['episodeCover'] = episodeCover;
    data['episodeId'] = episodeId;
    data['episodeTitle'] = episodeTitle;
    data['id'] = id;
    data['playCount'] = playCount;
    data['shortPlayCover'] = shortPlayCover;
    data['shortPlayId'] = shortPlayId;
    data['shortPlayTitle'] = shortPlayTitle;
    data['userId'] = userId;
    data['viewDuration'] = viewDuration;
    data['viewProgress'] = viewProgress;
    data['viewTime'] = viewTime;
    return data;
  }
}
