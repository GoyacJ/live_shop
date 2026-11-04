class EpisodesModel {
  String? id;
  String? shortPlayId;
  String? title;
  int? episodeNumber;
  int? episodeCount;
  String? cover;
  String? description;
  bool? isFree;
  int? sort;
  VideoByAction? videoByAction;

  EpisodesModel(
      {this.id,
        this.shortPlayId,
        this.title,
        this.episodeNumber,
        this.episodeCount,
        this.cover,
        this.description,
        this.isFree,
        this.sort,
        this.videoByAction});

  EpisodesModel.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    shortPlayId = json['shortPlayId'];
    title = json['title'];
    episodeNumber = json['episodeNumber'];
    episodeCount = json['episodeCount'];
    cover = json['cover'];
    description = json['description'];
    isFree = json['free'];
    sort = json['sort'];
    videoByAction = json['videoByAction'] != null
        ? new VideoByAction.fromJson(json['videoByAction'],episodeNumber)
        : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['shortPlayId'] = this.shortPlayId;
    data['title'] = this.title;
    data['episodeNumber'] = this.episodeNumber;
    data['episodeCount'] = this.episodeCount;
    data['cover'] = this.cover;
    data['description'] = this.description;
    data['free'] = this.isFree;
    data['sort'] = this.sort;
    if (this.videoByAction != null) {
      data['videoByAction'] = this.videoByAction!.toJson();
    }
    return data;
  }
}

class VideoByAction {
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
  UserWatchProgress? userWatchProgress;
  ContentStats? contentStats;
  String? goodsId;
  String? goodsName;
  double? goodsPrice;
  String? hostImage;
  int? startShowGoodsSecond;
  int? showGoodsSecond;
  int? episodeNumber;

  VideoByAction(
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
        this.goodsId,
        this.goodsName,
        this.goodsPrice,
        this.hostImage,
        this.startShowGoodsSecond,
        this.showGoodsSecond,
        this.episodeNumber,

      });

  VideoByAction.fromJson(Map<String, dynamic> json,int?episodeNumber) {
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
    userWatchProgress = json['userWatchProgress'] != null
        ? new UserWatchProgress.fromJson(json['userWatchProgress'])
        : null;
    userAction = json['userAction'] != null
        ? new UserAction.fromJson(json['userAction'])
        : null;
    contentStats = json['contentStats'] != null
        ? new ContentStats.fromJson(json['contentStats'])
        : null;
    goodsId = json['goodsId'];
    goodsName = json['goodsName'];
    goodsPrice = json['goodsPrice'];
    hostImage = json['hostImage'];
    startShowGoodsSecond = json['startShowGoodsSecond'];
    showGoodsSecond = json['showGoodsSecond'];
    episodeNumber = episodeNumber;
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
    data['goodsId'] = this.goodsId;
    data['goodsName'] = this.goodsName;
    data['goodsPrice'] = this.goodsPrice;
    data['hostImage'] = this.hostImage;
    data['startShowGoodsSecond'] = this.startShowGoodsSecond;
    data['showGoodsSecond'] = this.showGoodsSecond;
    data['episodeNumber'] = this.episodeNumber;
    return data;
  }
}

class UserWatchProgress {

  String? viewProgress;
  String? lastWatchTime;

  UserWatchProgress(
      {
        this.viewProgress,
        this.lastWatchTime,
      });

  UserWatchProgress.fromJson(Map<String, dynamic> json) {

    viewProgress = json['viewProgress'];
    lastWatchTime = json['lastWatchTime'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['viewProgress'] = this.viewProgress;
    data['lastWatchTime'] = this.lastWatchTime;
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

  UserAction(
      {this.userId,
        this.isLike,
        this.isCollect,
        this.isFollow,
        this.isComment,
        this.isShare,
        this.isReport,
      });

  UserAction.fromJson(Map<String, dynamic> json) {
    userId = json['userId'];
    isLike = json['like'];
    isCollect = json['collect'];
    isFollow = json['follow'];
    isComment = json['comment'];
    isShare = json['share'];
    isReport = json['report'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['userId'] = this.userId;
    data['like'] = this.isLike;
    data['collect'] = this.isCollect;
    data['follow'] = this.isFollow;
    data['comment'] = this.isComment;
    data['share'] = this.isShare;
    data['report'] = this.isReport;
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
