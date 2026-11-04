/// HTTP API 请求类
class HttpApi {
  static const String userAgreement = 'https://h5.live.weilaizhihuigu.cn/#/pages/agreement/user';
  static const String privacyPolicy = 'https://h5.live.weilaizhihuigu.cn/#/pages/agreement/privacy';

  /// 获取验证码
  static const String getCaptcha = '/mapi/getPhoneValidCode';

  /// H5获取验证码
  static const String getH5Captcha = '/h5/getCode';

  /// 登录/注册
  static const String login = '/mapi/doLoginBySms';

  /// H5登录/注册
  static const String h5Login = '/h5/login';

  ///获取版本号
  static const String getAppVersion = '/getAppVersion';

  ///获取首页数据
  static const String getHomeData = '/mapi/getHomeData';

  /// 获取用户信息
  static const String getUserInfo = '/mapi/auth/getUserDetail';

  /// 退出登录
  static const String logout = '/mapi/auth/logout';

  /// 修改用户信息
  static const String updateUserinfo = '/mapi/auth/updateUserinfo';

  /// 获取token
  static const String getUserToken = '/mapi/auth/im/getUserToken';

  /// 注册
  static const String registerUser = '/mapi/auth/im/registerUser';

  /// 获取直播间详情
  static const String liveDetail = '/mapi/live/detail';

  /// 获取APP推广海报
  static const String getUserPoster = '/mapi/integral/sharing-links';

  /// 积分广告
  static const String integralAd = '/mapi/advertisement/visit';

  /// 客服微信
  static const String customerWechat = '/mapi/add-customer-wechat';

  /// 获取余额记录
  static const String getUserCashLog = '/mapi/auth/getUserCashLog';

  /// 获取积分商品列表-分页
  static const String getIntegralGoodsList = '/mapi/auth/integral/goods/list';

  /// 获取积分商品详情
  static const String getIntegralGoodsDetail = '/mapi/auth/integral/goods/detail';

  /// 积分商品兑换
  static const String exchangeIntegralGoods = '/mapi/auth/integral/goods/buy';

  /// 获取积分商品订单列表-分页
  static const String getIntegralOrderList = '/mapi/auth/integral/order/list';

  /// 获取个人中心订单
  static const String getOrderList = '/mapi/auth/order/list';

  /// 获取订单详情
  static const String getOrderDetail = '/mapi/auth/order/detail';

  /// 取消订单
  static const String cancelOrder = '/mapi/auth/order/cancel';

  /// 确认收货
  static const String completeOrder = '/mapi/auth/order/complete';

  /// 获取支付参数
  static const String getPayUrl = '/mapi/auth/order/payUrl';

  /// 查询支付状态
  static const String getPaymentStatus = '/mapi/auth/order/pay-result';

  /// 获取用户推广记录
  static const String getUserPromotion = '/mapi/auth/user/promotion';

  /// 获取用户收货地址
  static const String getUserAddressList = '/mapi/auth/getUserAddressList';

  /// 新增用户收货地址
  static const String addUserAddressList = '/mapi/auth/userAddressAdd';

  /// 修改用户收货地址
  static const String updateUserAddress = '/mapi/auth/userAddressEdit';

  /// 删除用户收货地址
  static const String deleteUserAddress = '/mapi/auth/deleteUserAddress';

  /// 设置默认收货地址
  static const String setDefaultUserAddress = '/mapi/auth/setDefaultUserAddress';

  ///获取客服
  static const String getChatContact = '/mapi/chat/contact';

  /// 新增意见反馈
  static const String addFeedback = '/mapi/auth/feedbackAdd';

  /// 获取意见反馈列表
  static const String getFeedbackList = '/mapi/auth/feedbackList';

  /// 获取可提现金额
  static const String getWithdrawalAmount = '/mapi/auth/getAccountAndAvailable';

  /// 获取下单基础信息
  static const String getOrderBaseInfo = '/mapi/auth/order/baseInfo';

  /// 下单
  static const String createOrder = '/mapi/auth/order/create';

  /// 获取商品详情
  static const String getGoodsDetail = '/mapi/goods/detail';

  /// 用户进群组
  static const String addUserToGroup = '/live/add/addUserToGroup';

  /// 获取已购买用户信息
  static const String getPurchaseUser = '/mapi/auth/integral/order/buy/user/list';

  /// 获取物流信息
  static const String getLogisticsInfo = '/mapi/auth/integral/goods/logistics/info';

  /// 获取观看直播
  static const String getDuration = '/mapi/auth/live/user-visit-time';

  /// 设备访问信息
  static const String submitDevice = '/device/access';

  ///专题列表
  static const String topicList = '/mapi/shop/special-topic';

  ///专题商品列表
  static const String topicGoodsList = '/mapi/shop/special-topic-goods';

  ///搜索
  static const String searchList = '/mapi/shop/search';

  ///首页短剧
  static const String getSkitList = '/mapi/content/short-play/index';

  ///短剧分页
  static const String getShortVideoList = '/mapi/content/short-play/page';

  ///短剧分页
  static const String getVideoList = '/mapi/content/video/page';

  ///点赞相关
  static const String addUserAction = '/mapi/content/user-action/action';

  ///删除观看记录
  static const String deleteWatchHistory = '/mapi/content/history/delete-record';

  ///剧集分页
  static const String getEpisodeList = '/mapi/content/short-play/episode/page';

  ///观看记录分页
  static const String watchHistoryList = '/mapi/content/history/record';

  ///观看时长
  static const String watchDuration = '/mapi/content/user-action/visit';

  static const String setFollow = '/mapi/content/short-play/follow';

  ///观看直播时长
  static const String liveDuration = '/view-duration';

  ///短剧首页推荐
  static const String getHomeSkitList = '/mapi/short-play/index';

  ///获取任务列表
  static const String getTaskList = '/mapi/task/list';

  ///签到
  static const String signIn = '/mapi/task/sign-in';

  ///积分变更记录
  static const String getIntegralChange = '/mapi/integral/user-integral-change-page';
}
