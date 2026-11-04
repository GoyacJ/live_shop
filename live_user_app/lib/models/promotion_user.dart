class PromotionUserEntity {
  String? orderId;
  String? orderNumber;
  String? promotionName;
  double? integral;
  String? settleTime;
  String? settlementStatus;

  PromotionUserEntity(
      {this.orderId,
        this.orderNumber,
        this.promotionName,
        this.integral,
        this.settleTime,
        this.settlementStatus});

  PromotionUserEntity.fromJson(Map<String, dynamic> json) {
    orderId = json['orderId'];
    orderNumber = json['orderNumber'];
    promotionName = json['promotionName'];
    integral = json['integral'];
    settleTime = json['settleTime'];
    settlementStatus = json['settlementStatus'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['orderId'] = this.orderId;
    data['orderNumber'] = this.orderNumber;
    data['promotionName'] = this.promotionName;
    data['integral'] = this.integral;
    data['settleTime'] = this.settleTime;
    data['settlementStatus'] = this.settlementStatus;
    return data;
  }
}
