import 'video_model.dart';
import 'episodes_model.dart';

/// 视频播放器数据模型
class VideoPlayerModel {
   String? id;                    // 视频ID
   String? title;                 // 视频标题
   String? cover;                 // 封面图片
   String? videoUrl;              // 视频地址
   String? viewProgress;          // 观看进度
   int? episodeNumber;            // 剧集序号
   bool? like;                    // 是否点赞
   bool? collect;                 // 是否收藏
   int? likeCount;                // 点赞数
   int? commentCount;             // 评论数
   int? shareCount;               // 分享数
   int? collectCount;            // 收藏数
   String? goodsId;               // 商品ID
   String? goodsName;             // 商品名称
   double? goodsPrice;            // 商品价格
   String? hostImage;             // 商品图片
   int? startShowGoodsSecond;     // 开始显示商品的时间（秒）
   int? showGoodsSecond;          // 商品显示时长（秒）

  VideoPlayerModel({
    this.id,
    this.title,
    this.cover,
    this.videoUrl,
    this.viewProgress,
    this.episodeNumber,
    this.like,
    this.collect,
    this.likeCount,
    this.commentCount,
    this.shareCount,
    this.collectCount,
    this.goodsId,
    this.goodsName,
    this.goodsPrice,
    this.hostImage,
    this.startShowGoodsSecond,
    this.showGoodsSecond,
  });

  /// 从 VideoModel 创建 VideoPlayerModel
  factory VideoPlayerModel.fromVideoModel(VideoModel model) {
    return VideoPlayerModel(
      id: model.id,
      title: model.title,
      cover: model.cover,
      videoUrl: model.videoUrl,
      viewProgress: model.userAction?.viewProgress,
      episodeNumber: 1,
      like: model.userAction?.isLike,
      collect: model.userAction?.isCollect,
      likeCount: model.contentStats?.likeCount,
      commentCount: model.contentStats?.commentCount,
      shareCount: model.contentStats?.shareCount,
      collectCount: model.contentStats?.collectCount,
      goodsId: model.goodsSampleInfo?.goodsId,
      goodsName: model.goodsSampleInfo?.goodsName,
      goodsPrice: model.goodsSampleInfo?.goodsPrice,
      hostImage: model.goodsSampleInfo?.hostImage,
      startShowGoodsSecond: model.goodsSampleInfo?.startShowGoodsSecond,
      showGoodsSecond: model.goodsSampleInfo?.showGoodsSecond,
    );
  }

  /// 从 VideoByAction 创建 VideoPlayerModel
  factory VideoPlayerModel.fromVideoByAction(VideoByAction model,int? episodeNumber) {
    return VideoPlayerModel(
      id: model.id,
      title: model.title,
      cover: model.cover,
      videoUrl: model.videoUrl,
      viewProgress: model.userWatchProgress?.viewProgress,
      episodeNumber: episodeNumber??1,
      like: model.userAction?.isLike,
      collect: model.userAction?.isCollect,
      likeCount: model.contentStats?.likeCount,
      commentCount: model.contentStats?.commentCount,
      shareCount: model.contentStats?.shareCount,
      collectCount: model.contentStats?.collectCount,
      goodsId: model.goodsId,
      goodsName: model.goodsName,
      goodsPrice: model.goodsPrice,
      hostImage: model.hostImage,
      startShowGoodsSecond: model.startShowGoodsSecond,
      showGoodsSecond: model.showGoodsSecond,
    );
  }

  /// 从 EpisodesModel 创建 VideoPlayerModel
  factory VideoPlayerModel.fromEpisodesModel(EpisodesModel model) {
    if (model.videoByAction == null) {
      return VideoPlayerModel(
        id: model.id,
        title: model.title,
        cover: model.cover,
        episodeNumber: model.episodeNumber,
      );
    }
    return VideoPlayerModel.fromVideoByAction(model.videoByAction!,model.episodeNumber);
  }

  /// 从 JSON 创建 VideoPlayerModel
  factory VideoPlayerModel.fromJson(Map<String, dynamic> json) {
    return VideoPlayerModel(
      id: json['id'],
      title: json['title'],
      cover: json['cover'],
      videoUrl: json['videoUrl'],
      viewProgress: json['viewProgress'],
      episodeNumber: json['episodeNumber'],
      like: json['like'],
      collect: json['collect'],
      likeCount: json['likeCount'],
      commentCount: json['commentCount'],
      shareCount: json['shareCount'],
      collectCount: json['collectCount'],
      goodsId: json['goodsId'],
      goodsName: json['goodsName'],
      goodsPrice: json['goodsPrice']?.toDouble(),
      hostImage: json['hostImage'],
      startShowGoodsSecond: json['startShowGoodsSecond'],
      showGoodsSecond: json['showGoodsSecond'],
    );
  }

  /// 转换为 JSON
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'cover': cover,
      'videoUrl': videoUrl,
      'viewProgress': viewProgress,
      'episodeNumber': episodeNumber,
      'like': like,
      'collect': collect,
      'likeCount': likeCount,
      'commentCount': commentCount,
      'shareCount': shareCount,
      'collectCount': collectCount,
      'goodsId': goodsId,
      'goodsName': goodsName,
      'goodsPrice': goodsPrice,
      'hostImage': hostImage,
      'startShowGoodsSecond': startShowGoodsSecond,
      'showGoodsSecond': showGoodsSecond,
    };
  }

  /// 从 VideoModel 列表创建 VideoPlayerModel 列表
  static List<VideoPlayerModel> fromVideoModelList(List<VideoModel> models) {
    return models.map((model) => VideoPlayerModel.fromVideoModel(model)).toList();
  }

  /// 从 VideoByAction 列表创建 VideoPlayerModel 列表
  static List<VideoPlayerModel> fromVideoByActionList(List<VideoByAction> models) {
    return models.map((model) => VideoPlayerModel.fromVideoByAction(model,model.episodeNumber)).toList();
  }

  /// 从 EpisodesModel 列表创建 VideoPlayerModel 列表
  static List<VideoPlayerModel> fromEpisodesModelList(List<EpisodesModel> models) {
    return models.map((model) => VideoPlayerModel.fromEpisodesModel(model)).toList();
  }

  /// 从 JSON 列表创建 VideoPlayerModel 列表
  static List<VideoPlayerModel> fromJsonList(List<dynamic> jsonList) {
    return jsonList.map((json) => VideoPlayerModel.fromJson(json)).toList();
  }
} 