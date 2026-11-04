class UserAddressEntity {
  String id = "";
  String nickname = "";
  String mobile = "";
  String provinceName = "";
  String provinceCode = "";
  String cityName = "";
  String cityCode = "";
  String countiesName = "";
  String countiesCode = "";
  String address = "";
  String isDefault = "";
  String createTime = "";

  UserAddressEntity({
    required this.id,
    required this.nickname,
    required this.mobile,
    required this.provinceName,
    required this.provinceCode,
    required this.cityName,
    required this.cityCode,
    required this.countiesName,
    required this.countiesCode,
    required this.address,
    required this.isDefault,
    required this.createTime,
  });

  // 构造方法
  UserAddressEntity.empty() {
    id = '';
    nickname = '';
    mobile = '';
    provinceName = '';
    provinceCode = '';
    cityName = '';
    cityCode = '';
    countiesName = '';
    countiesCode = '';
    address = '';
    isDefault = '';
    createTime = '';
  }

  UserAddressEntity.fromJson(Map<dynamic, dynamic> json) {
    id = json['id'] ?? '';
    nickname = json['nickname'] ?? '';
    mobile = json['mobile'] ?? '';
    provinceName = json['provinceName'] ?? '';
    provinceCode = json['provinceCode'] ?? '';
    cityName = json['cityName'] ?? '';
    cityCode = json['cityCode'] ?? '';
    countiesName = json['countiesName'] ?? '';
    countiesCode = json['countiesCode'] ?? '';
    address = json['address'] ?? '';
    isDefault = json['isDefault'] ?? '';
    createTime = json['createTime'] ?? '';
  }

  Map<dynamic, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['id'] = id;
    data['nickname'] = nickname;
    data['mobile'] = mobile;
    data['provinceName'] = provinceName;
    data['provinceCode'] = provinceCode;
    data['cityName'] = cityName;
    data['cityCode'] = cityCode;
    data['countiesName'] = countiesName;
    data['countiesCode'] = countiesCode;
    data['address'] = address;
    data['isDefault'] = isDefault;
    data['createTime'] = createTime;
    return data;
  }
}
