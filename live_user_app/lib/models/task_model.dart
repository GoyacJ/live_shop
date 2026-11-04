class TaskModel {
  String? taskId;
  String? taskName;
  String? taskDesc;
  String? icon;
  int? sort;
  String? taskType;
  String? taskCategory;
  String? taskBusinessCategory;
  double? rewardIntegral;
  int? frequencyLimit;
  int? refreshTime;
  int? isLimitTime;
  String? validityPeriodStart;
  String? validityPeriodEnd;
  String? jumpRouting;
  String? userId;
  int? isComplete;
  int? completeCount;

  TaskModel(
      {this.taskId,
        this.taskName,
        this.taskDesc,
        this.icon,
        this.sort,
        this.taskType,
        this.taskCategory,
        this.taskBusinessCategory,
        this.rewardIntegral,
        this.frequencyLimit,
        this.refreshTime,
        this.isLimitTime,
        this.validityPeriodStart,
        this.validityPeriodEnd,
        this.jumpRouting,
        this.userId,
        this.isComplete,
        this.completeCount});

  TaskModel.fromJson(Map<String, dynamic> json) {
    taskId = json['taskId'];
    taskName = json['taskName'];
    taskDesc = json['taskDesc'];
    icon = json['icon'];
    sort = json['sort'];
    taskType = json['taskType'];
    taskCategory = json['taskCategory'];
    taskBusinessCategory = json['taskBusinessCategory'];
    rewardIntegral = json['rewardIntegral'];
    frequencyLimit = json['frequencyLimit'];
    refreshTime = json['refreshTime'];
    isLimitTime = json['isLimitTime'];
    validityPeriodStart = json['validityPeriodStart'];
    validityPeriodEnd = json['validityPeriodEnd'];
    jumpRouting = json['jumpRouting'];
    userId = json['userId'];
    isComplete = json['isComplete'];
    completeCount = json['completeCount'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['taskId'] = this.taskId;
    data['taskName'] = this.taskName;
    data['taskDesc'] = this.taskDesc;
    data['icon'] = this.icon;
    data['sort'] = this.sort;
    data['taskType'] = this.taskType;
    data['taskCategory'] = this.taskCategory;
    data['taskBusinessCategory'] = this.taskBusinessCategory;
    data['rewardIntegral'] = this.rewardIntegral;
    data['frequencyLimit'] = this.frequencyLimit;
    data['refreshTime'] = this.refreshTime;
    data['isLimitTime'] = this.isLimitTime;
    data['validityPeriodStart'] = this.validityPeriodStart;
    data['validityPeriodEnd'] = this.validityPeriodEnd;
    data['jumpRouting'] = this.jumpRouting;
    data['userId'] = this.userId;
    data['isComplete'] = this.isComplete;
    data['completeCount'] = this.completeCount;
    return data;
  }
}
