// 复制为 config.dart 后填入本地/测试环境真实值。
// config.dart 已被 .gitignore 排除，禁止把真实密钥提交到仓库。

// ignore: dangling_library_doc_comments
/// Created by Jcy
/// on 2025/2/26
///

enum Env {
  // 线上生产
  // ignore: constant_identifier_names
  PROD,
  // 线上
  // ignore: constant_identifier_names
  DEV,
}
class Config {
  static Env env = Env.DEV;

  static ConfigEntity get apiHost {
    ConfigEntity entity = ConfigEntity();

    switch (env) {
      case Env.DEV:
        entity.baseUrl = "http://172.16.201.189:8081";
        entity.apiSecret = "YOUR_API_SECRET";
        entity.openIMApi = "https://im.live.weilaizhihuigu.cn";
        entity.openIMWs = "wss://ims.live.weilaizhihuigu.cn";
        entity.wxAppId = "wxf2d73edc18790025";
        entity.wxSecret = "YOUR_WX_SECRET";
        entity.adId = "test0001";
        entity.splashAdId = "sjmad_test002";
        entity.videoAdId = "sjmad_test006";
        entity.rewardId = "sjmad_test001";
        break;
      case Env.PROD:
        entity.baseUrl = "https://api.live.weilaizhihuigu.cn";
        entity.apiSecret = "YOUR_API_SECRET";
        entity.openIMApi = "https://im.live.weilaizhihuigu.cn";
        entity.openIMWs = "wss://ims.live.weilaizhihuigu.cn";
        entity.wxAppId = "wxf2d73edc18790025";
        entity.wxSecret = "YOUR_WX_SECRET";
        entity.adId = "1224p10002037";
        entity.splashAdId = "1224d10006532";
        entity.videoAdId = "1224d10006533";
        entity.rewardId = "1224d10006534";
        break;
    }
    return entity;
  }
}

class ConfigEntity {
  late String baseUrl;
  late String apiSecret;
  late String openIMApi;
  late String openIMWs;
  late String wxAppId;
  late String wxSecret;
  late String adId;
  late String splashAdId;
  late String videoAdId;
  late String rewardId;

  ConfigEntity();
}