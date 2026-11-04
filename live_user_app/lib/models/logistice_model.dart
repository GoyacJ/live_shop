class LogisticsModel {
  String? deliveryMan;
  String? deliveryManTel;
  String? location;
  String? logisticCode;
  String? orderCode;
  String? shipperCode;
  String? state;
  String? stateEx;
  bool? success;
  List<Traces>? traces;

  LogisticsModel(
      {this.deliveryMan,
        this.deliveryManTel,
        this.location,
        this.logisticCode,
        this.orderCode,
        this.shipperCode,
        this.state,
        this.stateEx,
        this.success,
        this.traces});

  LogisticsModel.fromJson(Map<String, dynamic> json) {
    deliveryMan = json['deliveryMan'];
    deliveryManTel = json['deliveryManTel'];
    location = json['location'];
    logisticCode = json['logisticCode'];
    orderCode = json['orderCode'];
    shipperCode = json['shipperCode'];
    state = json['state'];
    stateEx = json['stateEx'];
    success = json['success'];
    if (json['traces'] != null) {
      traces = <Traces>[];
      json['traces'].forEach((v) {
        traces!.add(Traces.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['deliveryMan'] = deliveryMan;
    data['deliveryManTel'] = deliveryManTel;
    data['location'] = location;
    data['logisticCode'] = logisticCode;
    data['orderCode'] = orderCode;
    data['shipperCode'] = shipperCode;
    data['state'] = state;
    data['stateEx'] = stateEx;
    data['success'] = success;
    if (traces != null) {
      data['traces'] = traces!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class Traces {
  String? acceptStation;
  String? acceptTime;
  String? location;
  String? action;

  Traces({this.acceptStation, this.acceptTime, this.location, this.action});

  Traces.fromJson(Map<String, dynamic> json) {
    acceptStation = json['acceptStation'];
    acceptTime = json['acceptTime'];
    location = json['location'];
    action = json['action'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = {};
    data['acceptStation'] = acceptStation;
    data['acceptTime'] = acceptTime;
    data['location'] = location;
    data['action'] = action;
    return data;
  }
}
