class IntegralRecordModel {
  String? userId;
  String? integralId;
  String? integralRecordId;
  String? integralSource;
  String? integralSourceId;
  String? changeType;
  String? integralCategory;
  String? operationTime;
  double? integral;
  String? operationSource;
  String? operationUserId;
  double? integralBalance;
  double? frozenIntegralBalance;
  double? expireIntegralBalance;
  int? clearExpireStatus;
  String? validityPeriod;
  String? remark;

  IntegralRecordModel(
      {this.userId,
        this.integralId,
        this.integralRecordId,
        this.integralSource,
        this.integralSourceId,
        this.changeType,
        this.integralCategory,
        this.operationTime,
        this.integral,
        this.operationSource,
        this.operationUserId,
        this.integralBalance,
        this.frozenIntegralBalance,
        this.expireIntegralBalance,
        this.clearExpireStatus,
        this.validityPeriod,
        this.remark});

  IntegralRecordModel.fromJson(Map<String, dynamic> json) {
    userId = json['userId'];
    integralId = json['integralId'];
    integralRecordId = json['integralRecordId'];
    integralSource = json['integralSource'];
    integralSourceId = json['integralSourceId'];
    changeType = json['changeType'];
    integralCategory = json['integralCategory'];
    operationTime = json['operationTime'];
    integral = json['integral'];
    operationSource = json['operationSource'];
    operationUserId = json['operationUserId'];
    integralBalance = json['integralBalance'];
    frozenIntegralBalance = json['frozenIntegralBalance'];
    expireIntegralBalance = json['expireIntegralBalance'];
    clearExpireStatus = json['clearExpireStatus'];
    validityPeriod = json['validityPeriod'];
    remark = json['remark'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['userId'] = this.userId;
    data['integralId'] = this.integralId;
    data['integralRecordId'] = this.integralRecordId;
    data['integralSource'] = this.integralSource;
    data['integralSourceId'] = this.integralSourceId;
    data['changeType'] = this.changeType;
    data['integralCategory'] = this.integralCategory;
    data['operationTime'] = this.operationTime;
    data['integral'] = this.integral;
    data['operationSource'] = this.operationSource;
    data['operationUserId'] = this.operationUserId;
    data['integralBalance'] = this.integralBalance;
    data['frozenIntegralBalance'] = this.frozenIntegralBalance;
    data['expireIntegralBalance'] = this.expireIntegralBalance;
    data['clearExpireStatus'] = this.clearExpireStatus;
    data['validityPeriod'] = this.validityPeriod;
    data['remark'] = this.remark;
    return data;
  }
}
