import 'package:common_utils/common_utils.dart';
import 'package:dio/dio.dart';
import 'package:live_app/services/user_info_ctl.dart';

import '../utils/download_util.dart';
import '../utils/http_util.dart';
import '../utils/platform_util.dart';
import 'http_api.dart';

/// 网络请求工具类
/// 封装具体的业务请求
class NetUtil {

  /// 获取验证码
  static Future getCaptcha(String phone) async {
    var response = await HttpUtil.instance.post(HttpApi.getCaptcha, data: {
      'mobile': phone,
    });
    return response.data;
  }

  /// h5获取验证码
  static Future getH5Captcha(String phone) async {
    var response = await HttpUtil.instance.get(HttpApi.getH5Captcha, queryParameters: {
      'phone': phone,
    });
    return response.data;
  }

  /// 登录/注册
  static Future login(String phone, String captcha, {String rcode=''}) async {
    String clientId = await PlatformUtil.generateClientId();
    Map<String, dynamic> data = {};
    data.putIfAbsent('mobile', ()=>phone);
    data.putIfAbsent('validCode', ()=>captcha);
    data.putIfAbsent('rcode', ()=>rcode);
    if(!ObjectUtil.isEmptyString(clientId)){
      data.putIfAbsent('clientId', ()=>clientId);
    }
    var response = await HttpUtil.instance.post(HttpApi.login, data: data);
    return response.data;
  }

  /// h5登录/注册
  static Future h5Login(String phone, String captcha, {String rcode=''}) async {
    Map<String, dynamic> data = {};
    data.putIfAbsent('phone', ()=>phone);
    data.putIfAbsent('code', ()=>captcha);
    if(!ObjectUtil.isEmptyString(rcode)){
      data.putIfAbsent('rcode', ()=>rcode);
    }
    var response = await HttpUtil.instance.post(HttpApi.h5Login, data: data);
    return response.data;
  }

  ///获取版本号
  static Future getAppVersion() async {
    final response = await HttpUtil.instance.get(HttpApi.getAppVersion);
    return response.data;
  }

  ///获取首页数据
  static Future getHomeData() async {
    final response = await HttpUtil.instance.get(HttpApi.getHomeData);
    return response.data;
  }

  /// 获取用户信息
  static Future getUserInfo() async {
    final response = await HttpUtil.instance.get(HttpApi.getUserInfo);
    return response.data;
  }

  /// 退出登录
  static Future logout() async {
    final response = await HttpUtil.instance.get(HttpApi.logout);
    return response.data;
  }

  /// 修改用户信息
  static Future updateUserInfo(Map<String, String> postData) async {
    var response = await HttpUtil.instance.post(HttpApi.updateUserinfo, data: postData);
    return response.data;
  }

  /// 注册
  static Future registerUser() async {
    var response = await HttpUtil.instance.post(HttpApi.registerUser);
    return response.data;
  }

  /// 获取token
  static Future getUserToken(int platformId) async {
    var response = await HttpUtil.instance.post(HttpApi.getUserToken, data: {
      "platformId": platformId
    });
    return response.data;
  }

  /// 获取直播间详情
  static Future getLiveDetail(String id) async {
    var response = await HttpUtil.instance.get(HttpApi.liveDetail, queryParameters: {
      'liveId': id,
    });
    return response.data;
  }

  /// 获取APP推广海报
  static Future getUserPoster(int category,String businessId) async {
    Map<String, dynamic> data = {};
    data.putIfAbsent('category', ()=>category);
    data.putIfAbsent('businessId', ()=>businessId);
    data.putIfAbsent('userId', ()=>UserInfoCtl.to.profile.id);
    var response = await HttpUtil.instance.get(HttpApi.getUserPoster,queryParameters: data);
    return response.data;
  }

  /// 积分广告
  static Future integralAd(taskId) async {
    Map<String, dynamic> data = {};
    data.putIfAbsent('userId', ()=>UserInfoCtl.to.profile.id);
    data.putIfAbsent('taskId', ()=>taskId);
    var response = await HttpUtil.instance.post(HttpApi.integralAd,data: data);
    return response.data;
  }

  /// 客服微信
  static Future customerWechat() async {
    Map<String, dynamic> data = {};
    data.putIfAbsent('id', ()=>UserInfoCtl.to.profile.id);
    var response = await HttpUtil.instance.post(HttpApi.customerWechat,data: data);
    return response.data;
  }

  /// 获取余额记录
  static Future getCashLog(int page) async {
    var response = await HttpUtil.instance.get(HttpApi.getUserCashLog, queryParameters: {
      "balanceType": 1,
      "current": page
    });
    return response.data;
  }

  /// 获取积分记录
  static Future getIntegralChange(int page) async {
    var response = await HttpUtil.instance.get(HttpApi.getIntegralChange, queryParameters: {
      "size":10,
      "current": page,
      "userId":UserInfoCtl.to.profile.id
    });
    return response.data;
  }

  /// 获取积分商品列表-分页
  static Future getIntegralGoodsList(int page) async {
    var response = await HttpUtil.instance.get(HttpApi.getIntegralGoodsList, queryParameters: {
      'current': page,
      'size': 10,
    });
    return response.data;
  }

  /// 获取积分商品详情
  static Future getIntegralGoodsDetail(String id) async {
    var response = await HttpUtil.instance.get(HttpApi.getIntegralGoodsDetail, queryParameters: {
      'id': id,
    });
    return response.data;
  }

  /// 积分商品兑换
  static Future exchangeIntegralGoods(data) async {
    var response = await HttpUtil.instance.post(HttpApi.exchangeIntegralGoods, data: data);
    return response.data;
  }

  /// 获取积分商品订单列表-分页
  static Future getIntegralOrderList(int page) async {
    var response = await HttpUtil.instance.get(HttpApi.getIntegralOrderList, queryParameters: {
      'current': page,
      'size': 10,
    });
    return response.data;
  }

  /// 获取个人中心订单
  static Future getOrderList(int status) async {
    var response = await HttpUtil.instance.get(HttpApi.getOrderList, queryParameters: {
      'status': status
    });
    return response.data;
  }

  /// 获取订单详情
  static Future getOrderDetail(String orderId) async {
    var response = await HttpUtil.instance.get(HttpApi.getOrderDetail, queryParameters: {
      "orderId": orderId,
    });
    return response.data;
  }

  /// 取消订单
  static Future cancelOrder(String orderId) async {
    var response = await HttpUtil.instance.post(HttpApi.cancelOrder, data: {
      "orderId": orderId,
    });
    return response.data;
  }

  /// 确认收货
  static Future completeOrder(String orderId) async {
    var response = await HttpUtil.instance.post(HttpApi.completeOrder, data: {
      "orderId": orderId,
    });
    return response.data;
  }

  /// 获取支付参数
  static Future getPayUrl(String orderId, String payType) async {
    var response = await HttpUtil.instance.post(HttpApi.getPayUrl, data: {
      "orderId": orderId,
      "payType": payType,
    });
    return response.data;
  }

  /// 查询支付状态
  static Future getPaymentStatus(String orderId) async {
    var response = await HttpUtil.instance.get(HttpApi.getPaymentStatus, queryParameters: {
      "orderNum": orderId,
    });
    return response.data;
  }

  /// 获取用户推广记录
  static Future getUserPromotion(data) async {
    var response = await HttpUtil.instance.get(HttpApi.getUserPromotion, queryParameters: data);
    return response.data;
  }

  /// 获取用户收货地址
  static Future getUserAddressList() async {
    var response = await HttpUtil.instance.get(HttpApi.getUserAddressList);
    return response.data;
  }

  /// 新增用户收货地址
  static Future addUserAddressList(Map<String, String> postData) async {
    var response = await HttpUtil.instance.post(HttpApi.addUserAddressList, data: postData);
    return response.data;
  }

  /// 修改用户收货地址
  static Future updateUserAddress(Map<String, String> postData) async {
    var response = await HttpUtil.instance.post(HttpApi.updateUserAddress, data: postData);
    return response.data;
  }

  /// 删除用户收货地址
  static Future deleteUserAddress(String id) async {
    var response = await HttpUtil.instance.post(HttpApi.deleteUserAddress, data: {
      'id': id
    });
    return response.data;
  }

  /// 设置默认收货地址
  static Future setDefaultUserAddress(String id) async {
    var response = await HttpUtil.instance.post(HttpApi.setDefaultUserAddress, data: {
      'id': id
    });
    return response.data;
  }

  ///获取客服
  static Future getChatContact() async {
    var response = await HttpUtil.instance.get(HttpApi.getChatContact);
    return response.data;
  }

  /// 新增意见反馈
  static Future addFeedback(Map<String, String> postData) async {
    var response = await HttpUtil.instance.post(HttpApi.addFeedback, data: postData);
    return response.data;
  }

  /// 获取意见反馈列表
  static Future getFeedbackList() async {
    var response = await HttpUtil.instance.get(HttpApi.getFeedbackList);
    return response.data;
  }

  /// 获取可提现金额
  static Future getWithdrawalAmount() async {
    var response = await HttpUtil.instance.get(HttpApi.getWithdrawalAmount);
    return response.data;
  }

  /// 获取下单基础信息
  static Future getOrderBaseInfo() async {
    var response = await HttpUtil.instance.get(HttpApi.getOrderBaseInfo);
    return response.data;
  }

  /// 下单
  static Future createOrder( data) async {
    var response = await HttpUtil.instance.post(HttpApi.createOrder, data: data);
    return response.data;
  }

  /// 获取商品详情
  static Future getGoodsDetail(String id) async {
    var response = await HttpUtil.instance.get(HttpApi.getGoodsDetail, queryParameters: {
      'id': id,
    });
    return response.data;
  }

  /// 用户进群组
  static Future addUserToGroup(dynamic data) async {
    var response = await HttpUtil.instance.post(HttpApi.addUserToGroup, data: data);
    return response.data;
  }

  /// 获取已购买用户信息
  static Future getPurchaseUser() async {
    var response = await HttpUtil.instance.get(HttpApi.getPurchaseUser);
    return response.data;
  }

  /// 获取物流信息
  static Future getLogisticsInfo(orderNumber) async {
    var response = await HttpUtil.instance.get(HttpApi.getLogisticsInfo,queryParameters: {'orderNumber':orderNumber});
    return response.data;
  }

  /// 设备访问信息
  static Future submitDevice(data) async {
    var response = await HttpUtil.instance.post(HttpApi.submitDevice,data: data);
    return response.data;
  }

  /// 获取观看直播
  static Future getDuration(data) async {
    var response = await HttpUtil.instance.post(HttpApi.getDuration,data: data);
    return response.data;
  }

  /// 专题列表
  static Future topicList() async {
    var response = await HttpUtil.instance.get(HttpApi.topicList);
    return response.data;
  }

  /// 专题商品列表
  static Future topicGoodsList(data) async {
    var response = await HttpUtil.instance.get(HttpApi.topicGoodsList,queryParameters: data);
    return response.data;
  }

  /// 搜索
  static Future searchList({data}) async {
    var response = await HttpUtil.instance.get(HttpApi.searchList,queryParameters: data);
    return response.data;
  }

  /// 首页短剧
  static Future getSkitList({data}) async {
    var response = await HttpUtil.instance.get(HttpApi.getSkitList,queryParameters: data);
    return response.data;
  }

  /// 短剧分页
  static Future getShortVideoList({data}) async {
    var response = await HttpUtil.instance.get(HttpApi.getShortVideoList,queryParameters: data);
    return response.data;
  }

  /// 视频分页
  static Future getVideoList({data}) async {
    var response = await HttpUtil.instance.get(HttpApi.getVideoList,queryParameters: data);
    return response.data;
  }

  /// 点赞收藏分享
  static Future addUserAction({data}) async {
    var response = await HttpUtil.instance.post(HttpApi.addUserAction,data: data);
    return response.data;
  }

  /// 删除观看记录
  static Future delWatchHistory({data}) async {
    var response = await HttpUtil.instance.post(HttpApi.deleteWatchHistory,data: data);
    return response.data;
  }

  /// 剧集分页
  static Future getEpisodeList({data}) async {
    var response = await HttpUtil.instance.get(HttpApi.getEpisodeList,queryParameters: data);
    return response.data;
  }

  /// 观看记录
  static Future watchHistoryList({data}) async {
    var response = await HttpUtil.instance.get(HttpApi.watchHistoryList,queryParameters: data);
    return response.data;
  }

  /// 观看记录
  static Future watchDuration({data}) async {
    var response = await HttpUtil.instance.post(HttpApi.watchDuration,data: data);
    return response.data;
  }

  /// 追剧
  static Future setFollow({data}) async {
    var response = await HttpUtil.instance.post(HttpApi.setFollow,data: data);
    return response.data;
  }

  /// 观看时长
  static Future liveDuration({data}) async {
    var response = await HttpUtil.instance.post(HttpApi.liveDuration,data: data);
    return response.data;
  }

  /// 短剧首页推荐
  static Future getHomeSkitList({data}) async {
    var response = await HttpUtil.instance.get(HttpApi.getHomeSkitList,queryParameters: data);
    return response.data;
  }

  /// 获取任务列表
  static Future getTaskList() async {
    var response = await HttpUtil.instance.get(HttpApi.getTaskList,queryParameters: {'userId':UserInfoCtl.to.profile.id});
    return response.data;
  }


  /// 签到
  static Future signIn() async {
    var response = await HttpUtil.instance.post(HttpApi.signIn,data: {'userId':UserInfoCtl.to.profile.id});
    return response.data;
  }


  /// 获取用户信息
  // static Future<ApiResponse<UserModel>> getUserInfo() async {
  //   final response = await HttpUtil.instance.get(
  //     '/user/info',
  //   );
  //
  //   if (response.data != null) {
  //     final apiResponse = ApiResponse<UserModel>.fromJson(response.data);
  //     if (apiResponse.isSuccess && apiResponse.data != null) {
  //       final userJson = apiResponse.data as Map<String, dynamic>;
  //       final user = UserModel.fromJson(userJson);
  //       return ApiResponse(
  //         code: apiResponse.code,
  //         msg: apiResponse.msg,
  //         data: user,
  //       );
  //     }
  //     return apiResponse;
  //   }
  //
  //   return ApiResponse(
  //     code: -1,
  //     msg: '请求失败',
  //     data: null,
  //   );
  // }

  /// 更新用户信息
  // static Future<ApiResponse<bool>> updateUserInfo(UserModel user) async {
  //   final response = await HttpUtil.instance.put(
  //     '/user/info',
  //     data: user.toJson(),
  //   );
  //
  //   if (response.data != null) {
  //     final apiResponse = ApiResponse<bool>.fromJson(response.data);
  //     return apiResponse;
  //   }
  //
  //   return ApiResponse(
  //     code: -1,
  //     msg: '请求失败',
  //     data: false,
  //   );
  // }

  /// 获取用户列表
  // static Future<ApiResponse<List<UserModel>>> getUserList({
  //   int page = 1,
  //   int pageSize = 20,
  // }) async {
  //   final response = await HttpUtil.instance.get(
  //     '/users',
  //     queryParameters: {
  //       'page': page,
  //       'page_size': pageSize,
  //     },
  //   );
  //
  //   if (response.data != null) {
  //     final apiResponse = ApiResponse<List<dynamic>>.fromJson(response.data);
  //     if (apiResponse.isSuccess && apiResponse.data != null) {
  //       final userList = apiResponse.data!
  //           .map((json) => UserModel.fromJson(json as Map<String, dynamic>))
  //           .toList();
  //       return ApiResponse(
  //         code: apiResponse.code,
  //         msg: apiResponse.msg,
  //         data: userList,
  //       );
  //     }
  //     return ApiResponse(
  //       code: apiResponse.code,
  //       msg: apiResponse.msg,
  //       data: [],
  //     );
  //   }
  //
  //   return ApiResponse(
  //     code: -1,
  //     msg: '请求失败',
  //     data: [],
  //   );
  // }


  /// 上传文件
  static Future<Map<String, dynamic>> uploadFile({
    required String url,
    required String filePath,
    String? fileName,
    Map<String, dynamic>? data,
    Function(int count, int total)? onProgress,
  }) async {
    try {
      // 创建 FormData
      final formData = FormData.fromMap({
        fileName ?? 'file': await MultipartFile.fromFile(filePath),
        if (data != null) ...data,
      });

      // 发送请求
      final dio = Dio();
      final response = await dio.post(
        url,
        data: formData,
        onSendProgress: onProgress,
      );

      return response.data as Map<String, dynamic>;
    } catch (e) {
      throw Exception('上传失败: $e');
    }
  }

  /// 下载文件
  /// 
  /// [url] 下载地址
  /// [fileName] 保存文件名
  /// [params] URL参数
  /// [onProgress] 下载进度回调
  static Future<DownloadTask?> downloadFile({
    required String url,
    required String fileName,
    Map<String, dynamic>? params,
    Function(int count, int total)? onProgress,
  }) async {
    try {
      // 检查存储权限
      final hasPermission = await DownloadUtils.instance.checkStoragePermission();
      if (!hasPermission) {
        throw Exception('没有存储权限');
      }
      // 添加到下载队列
      DownloadTask? task =  await DownloadUtils.instance.addDownloadTask(url,fileName: fileName);

      return task;
    } catch (e) {
      throw Exception('下载失败: $e');
    }
  }

  /// 批量下载文件
  /// 
  /// [downloads] 下载任务列表，每个元素包含url和fileName
  /// [params] URL参数
  /// [onProgress] 下载进度回调，参数为：当前完成数量，总数量，当前任务进度
  static Future<List<DownloadTask?>> downloadFiles({
    required List<Map<String, String>> downloads,
    Map<String, dynamic>? params,
    Function(int completed, int total, double currentProgress)? onProgress,
  }) async {
    try {
      // 检查存储权限
      final hasPermission = await DownloadUtils.instance.checkStoragePermission();
      if (!hasPermission) {
        throw Exception('没有存储权限');
      }

      final List<DownloadTask?> taskList = [];
      int completedCount = 0;
      final int totalCount = downloads.length;

      // 添加所有下载任务
      for (final download in downloads) {
        final String url = download['url'] ?? '';
        final String fileName = download['fileName'] ?? '';
        
        if (url.isEmpty || fileName.isEmpty) {
          taskList.add(null);
          completedCount++;
          continue;
        }

        final task = await DownloadUtils.instance.addDownloadTask(url, fileName: fileName);
        taskList.add(task);

        if (task != null && onProgress != null) {
          // 监听每个任务的进度
          task.progress.addListener(() {
            if (task.status.value == DownloadStatus.completed) {
              completedCount++;
            }
            onProgress(completedCount, totalCount, task.progress.value);
          });
        }
      }

      return taskList;
    } catch (e) {
      throw Exception('批量下载失败: $e');
    }
  }
}
