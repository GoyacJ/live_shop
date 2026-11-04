
class AppVersionModel {
  final AppVersionInfo android;
  final AppVersionInfo ios;

  AppVersionModel({
    required this.android,
    required this.ios,
  });

  factory AppVersionModel.fromJson(Map<String, dynamic> json) {
    return AppVersionModel(
      android: AppVersionInfo.fromJson(json['android'] ?? {}),
      ios: AppVersionInfo.fromJson(json['ios'] ?? {}),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'android': android.toJson(),
      'ios': ios.toJson(),
    };
  }
}

class AppVersionInfo {
  final String appType;
  final String appUrl;
  final String appVersion;
  final String versionCode;
  final String appDesc;

  AppVersionInfo({
    required this.appType,
    required this.appUrl,
    required this.appVersion,
    required this.versionCode,
    required this.appDesc,
  });

  factory AppVersionInfo.fromJson(Map<String, dynamic> json) {
    return AppVersionInfo(
      appType: json['appType'] ?? '',
      appUrl: json['appUrl'] ?? '',
      appVersion: json['appVersion'] ?? '',
      versionCode: json['versionCode'] ?? '',
      appDesc: json['appDesc'] ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'appType': appType,
      'appUrl': appUrl,
      'appVersion': appVersion,
      'versionCode': versionCode,
      'appDesc': appDesc,
    };
  }
}


