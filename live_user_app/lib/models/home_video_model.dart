class HomeVideoModel {
  String? id;
  String? title;
  String? category;
  String? tags;
  int? episodeCount;
  String? cover;
  String? poster;
  String? description;
  int? sort;
  int? recommendLevel;
  String? publishTime;
  bool? allowComment;
  bool? allowShare;
  int? viewCount;
  int? likeCount;
  int? commentCount;
  int? shareCount;
  int? followCount;
  int? favoriteCount;
  bool? isFree;
  String? userId;
  bool? follow;

  HomeVideoModel(
      {this.id,
      this.title,
      this.category,
      this.tags,
      this.episodeCount,
      this.cover,
      this.poster,
      this.description,
      this.sort,
      this.recommendLevel,
      this.publishTime,
      this.allowComment,
      this.allowShare,
      this.viewCount,
      this.likeCount,
      this.commentCount,
      this.shareCount,
      this.followCount,
      this.favoriteCount,
      this.isFree,
      this.userId,
      this.follow});

  HomeVideoModel.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    title = json['title'];
    category = json['category'];
    tags = json['tags'];
    episodeCount = json['episodeCount'];
    cover = json['cover'];
    poster = json['poster'];
    description = json['description'];
    sort = json['sort'];
    recommendLevel = json['recommendLevel'];
    publishTime = json['publishTime'];
    allowComment = json['allowComment'];
    allowShare = json['allowShare'];
    viewCount = json['viewCount'];
    likeCount = json['likeCount'];
    commentCount = json['commentCount'];
    shareCount = json['shareCount'];
    followCount = json['followCount'];
    favoriteCount = json['favoriteCount'];
    isFree = json['free'];
    userId = json['userId'];
    follow = json['follow'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['id'] = this.id;
    data['title'] = this.title;
    data['category'] = this.category;
    data['tags'] = this.tags;
    data['episodeCount'] = this.episodeCount;
    data['cover'] = this.cover;
    data['poster'] = this.poster;
    data['description'] = this.description;
    data['sort'] = this.sort;
    data['recommendLevel'] = this.recommendLevel;
    data['publishTime'] = this.publishTime;
    data['allowComment'] = this.allowComment;
    data['allowShare'] = this.allowShare;
    data['viewCount'] = this.viewCount;
    data['likeCount'] = this.likeCount;
    data['commentCount'] = this.commentCount;
    data['shareCount'] = this.shareCount;
    data['followCount'] = this.followCount;
    data['favoriteCount'] = this.favoriteCount;
    data['free'] = this.isFree;
    data['userId'] = this.userId;
    data['follow'] = this.follow;
    return data;
  }
}
