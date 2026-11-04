class ApiResponse<T> {
  final int code;
  final String msg;
  final T? data;
  
  ApiResponse({
    required this.code,
    required this.msg,
    this.data,
  });
  
  bool get isSuccess => code == 200;
  
  factory ApiResponse.fromJson(Map<String, dynamic> json) {
    return ApiResponse(
      code: json['code'] as int,
      msg: json['msg'] as String,
      data: json['data'] as T?,
    );
  }
  
  Map<String, dynamic> toJson() {
    return {
      'code': code,
      'msg': msg,
      'data': data,
    };
  }
}
