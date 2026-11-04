class VideoModel {
  String? id;
  String? title;
  String? category;
  String? tags;
  String? cover;
  String? description;
  String? videoUrl;
  int? duration;
  String? source;
  int? downloadable;
  int? shareable;
  int? allowComment;
  int? sort;
  UserAction? userAction;
  ContentStats? contentStats;
  GoodsSampleInfo? goodsSampleInfo;

  VideoModel(
      {this.id,
        this.title,
        this.category,
        this.tags,
        this.cover,
        this.description,
        this.videoUrl,
        this.duration,
        this.source,
        this.downloadable,
        this.shareable,
        this.allowComment,
        this.sort,
        this.userAction,
        this.contentStats,
        this.goodsSampleInfo
      });

  VideoModel.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    title = json['title'];
    category = json['category'];
    tags = json['tags'];
    cover = json['cover'];
    description = json['description'];
    videoUrl = json['videoUrl'];
    duration = json['duration'];
    source = json['source'];
    downloadable = json['downloadable'];
    shareable = json['shareable'];
    allowComment = json['allowComment'];
    sort = json['sort'];
    userAction = json['userAction'] != null
        ? new UserAction.fromJson(json['userAction'])
        : null;
    contentStats = json['contentStats'] != null
        ? new ContentStats.fromJson(json['contentStats'])
        : null;
    goodsSampleInfo = json['goodsSampleInfo'] != null
        ? new GoodsSampleInfo.fromJson(json['goodsSampleInfo'])
        : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['title'] = this.title;
    data['category'] = this.category;
    data['tags'] = this.tags;
    data['cover'] = this.cover;
    data['description'] = this.description;
    data['videoUrl'] = this.videoUrl;
    data['duration'] = this.duration;
    data['source'] = this.source;
    data['downloadable'] = this.downloadable;
    data['shareable'] = this.shareable;
    data['allowComment'] = this.allowComment;
    data['sort'] = this.sort;
    if (this.userAction != null) {
      data['userAction'] = this.userAction!.toJson();
    }
    if (this.contentStats != null) {
      data['contentStats'] = this.contentStats!.toJson();
    }
    if (this.goodsSampleInfo != null) {
      data['goodsSampleInfo'] = this.goodsSampleInfo!.toJson();
    }
    return data;
  }
}

class UserAction {
  String? userId;
  bool? isLike;
  bool? isCollect;
  bool? isFollow;
  bool? isComment;
  bool? isShare;
  bool? isReport;
  String? viewProgress;
  String? lastWatchTime;
  String? episodeId;

  UserAction(
      {this.userId,
        this.isLike,
        this.isCollect,
        this.isFollow,
        this.isComment,
        this.isShare,
        this.isReport,
        this.viewProgress,
        this.lastWatchTime,
        this.episodeId});

  UserAction.fromJson(Map<String, dynamic> json) {
    userId = json['userId'];
    isLike = json['like'];
    isCollect = json['collect'];
    isFollow = json['follow'];
    isComment = json['comment'];
    isShare = json['share'];
    isReport = json['report'];
    viewProgress = json['viewProgress'];
    lastWatchTime = json['lastWatchTime'];
    episodeId = json['episodeId'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['userId'] = this.userId;
    data['like'] = this.isLike;
    data['collect'] = this.isCollect;
    data['follow'] = this.isFollow;
    data['comment'] = this.isComment;
    data['share'] = this.isShare;
    data['isReport'] = this.isReport;
    data['viewProgress'] = this.viewProgress;
    data['lastWatchTime'] = this.lastWatchTime;
    data['episodeId'] = this.episodeId;
    return data;
  }
}

class ContentStats {
  int? viewCount;
  int? likeCount;
  int? commentCount;
  int? shareCount;
  int? collectCount;

  ContentStats(
      {this.viewCount,
        this.likeCount,
        this.commentCount,
        this.shareCount,
        this.collectCount});

  ContentStats.fromJson(Map<String, dynamic> json) {
    viewCount = json['viewCount'];
    likeCount = json['likeCount'];
    commentCount = json['commentCount'];
    shareCount = json['shareCount'];
    collectCount = json['collectCount'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['viewCount'] = this.viewCount;
    data['likeCount'] = this.likeCount;
    data['commentCount'] = this.commentCount;
    data['shareCount'] = this.shareCount;
    data['collectCount'] = this.collectCount;
    return data;
  }
}

class GoodsSampleInfo {
  String? goodsId;
  String? goodsName;
  double? goodsPrice;
  String? hostImage;
  int? startShowGoodsSecond;
  int? showGoodsSecond;

  GoodsSampleInfo(
      {this.goodsId,
        this.goodsName,
        this.goodsPrice,
        this.hostImage,
        this.startShowGoodsSecond,
        this.showGoodsSecond});

  GoodsSampleInfo.fromJson(Map<String, dynamic> json) {
    goodsId = json['goodsId'];
    goodsName = json['goodsName'];
    goodsPrice = json['goodsPrice'];
    hostImage = json['hostImage'];
    startShowGoodsSecond = json['startShowGoodsSecond'];
    showGoodsSecond = json['showGoodsSecond'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['goodsId'] = this.goodsId;
    data['goodsName'] = this.goodsName;
    data['goodsPrice'] = this.goodsPrice;
    data['hostImage'] = this.hostImage;
    data['startShowGoodsSecond'] = this.startShowGoodsSecond;
    data['showGoodsSecond'] = this.showGoodsSecond;
    return data;
  }
}
