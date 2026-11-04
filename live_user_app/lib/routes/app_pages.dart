import 'package:live_app/pages/mine_next/language/language_binding.dart';
import 'package:live_app/pages/mine_next/language/language_view.dart';
import 'package:live_app/pages/mine_next/setting/setting_binding.dart';
import 'package:live_app/pages/mine_next/setting/setting_view.dart';
import 'package:get/get.dart';
import 'package:live_app/pages/my_web_view/my_web_view_view.dart';
import 'package:live_app/pages/welcome/welcome_binding.dart';
import 'package:live_app/pages/welcome/welcome_view.dart';
import '../pages/home/live_room/live_room_binding.dart';
import '../pages/home/live_room/live_room_view.dart';
import '../pages/login/login_binding.dart';
import '../pages/login/login_view.dart';
import '../pages/main/main_binding.dart';
import '../pages/main/main_view.dart';
import '../pages/mall/topics/topics_binding.dart';
import '../pages/mall/topics/topics_view.dart';
import '../pages/message/message_binding.dart';
import '../pages/message/message_list/message_list_binding.dart';
import '../pages/message/message_list/message_list_view.dart';
import '../pages/message/message_view.dart';
import '../pages/mine_next/address_edit/address_edit_binding.dart';
import '../pages/mine_next/address_edit/address_edit_view.dart';
import '../pages/mine_next/address_form/address_form_binding.dart';
import '../pages/mine_next/address_form/address_form_view.dart';
import '../pages/mine_next/address_list/address_list_binding.dart';
import '../pages/mine_next/address_list/address_list_view.dart';
import '../pages/mine_next/balance/balance_binding.dart';
import '../pages/mine_next/balance/balance_view.dart';
import '../pages/mine_next/cash_log/cash_log_binding.dart';
import '../pages/mine_next/cash_log/cash_log_view.dart';
import '../pages/mine_next/contact/contact_binding.dart';
import '../pages/mine_next/contact/contact_view.dart';
import '../pages/mine_next/coupon/coupon_binding.dart';
import '../pages/mine_next/coupon/coupon_view.dart';
import '../pages/mine_next/create_order/create_order_binding.dart';
import '../pages/mine_next/create_order/create_order_view.dart';
import '../pages/mine_next/feedback/feedback_binding.dart';
import '../pages/mine_next/feedback/feedback_view.dart';
import '../pages/mine_next/feedback_list/feedback_list_binding.dart';
import '../pages/mine_next/feedback_list/feedback_list_view.dart';
import '../pages/mine_next/goods_detail/goods_detail_binding.dart';
import '../pages/mine_next/goods_detail/goods_detail_view.dart';
import '../pages/mine_next/integral/integral_binding.dart';
import '../pages/mine_next/integral/integral_view.dart';
import '../pages/mine_next/integral_goods/integral_goods_binding.dart';
import '../pages/mine_next/integral_goods/integral_goods_view.dart';
import '../pages/mine_next/integral_goods_detail/integral_goods_detail_binding.dart';
import '../pages/mine_next/integral_goods_detail/integral_goods_detail_view.dart';
import '../pages/mine_next/integral_order_list/integral_order_list_binding.dart';
import '../pages/mine_next/integral_order_list/integral_order_list_view.dart';
import '../pages/mine_next/logistics_info/logistics_info_binding.dart';
import '../pages/mine_next/logistics_info/logistics_info_view.dart';
import '../pages/mine_next/my_setting/my_setting_binding.dart';
import '../pages/mine_next/my_setting/my_setting_view.dart';
import '../pages/mine_next/order_detail/order_detail_binding.dart';
import '../pages/mine_next/order_detail/order_detail_view.dart';
import '../pages/mine_next/order_list/order_list_binding.dart';
import '../pages/mine_next/order_list/order_list_view.dart';
import '../pages/mine_next/poster/poster_binding.dart';
import '../pages/mine_next/poster/poster_view.dart';
import '../pages/mine_next/update_avatar/update_avatar_binding.dart';
import '../pages/mine_next/update_avatar/update_avatar_view.dart';
import '../pages/mine_next/user_promotion/user_promotion_binding.dart';
import '../pages/mine_next/user_promotion/user_promotion_view.dart';
import '../pages/mine_next/withdrawal/withdrawal_binding.dart';
import '../pages/mine_next/withdrawal/withdrawal_view.dart';
import '../pages/my_web_view/my_web_view_binding.dart';
import '../pages/search/search_binding.dart';
import '../pages/search/search_view.dart';
import '../pages/video/video_binding.dart';
import '../pages/video/video_child/video_child_binding.dart';
import '../pages/video/video_child/video_child_view.dart';
import '../pages/video/video_list/video_list_binding.dart';
import '../pages/video/video_list/video_list_view.dart';
import '../pages/video/video_view.dart';
import '../pages/video/watch_history/watch_history_binding.dart';
import '../pages/video/watch_history/watch_history_view.dart';
import 'route_middleware.dart';

/// 应用路由配置
 class AppPages {

  /// 路由名称
  static const String welcome = '/';
  static const String main = '/main';
  static const String login = '/login';
  static const String home = '/home';
  static const String liveRoom = '/live_room';

  static const String message = '/message';
  static const String messageListPage = '/MessageListPage';

  static const String mine = '/mine';
  static const String language = '/language';
  static const String setting = '/setting';

  static const String updateAvatarPage = '/UpdateAvatarPage';
  static const String balancePage = '/BalancePage';
  static const String integralPage = '/IntegralPage';
  static const String integralGoodsPage = '/IntegralGoodsPage';
  static const String integralOrderListPage = '/IntegralOrderListPage';
  static const String integralGoodsDetailPage = '/IntegralGoodsDetailPage';
  static const String couponPage = '/CouponPage';
  static const String orderListPage = '/OrderListPage';
  static const String orderDetailPage = '/OrderDetailPage';
  static const String logisticsInfoPage = '/LogisticsInfoPage';


  static const String posterPage = '/PosterPage';
  static const String userPromotionPage = '/UserPromotionPage';
  static const String addressListPage = '/AddressListPage';
  static const String addressFormPage = '/AddressFormPage';
  static const String addressEditPage = '/AddressEditPage';
  static const String contactPage = '/ContactPage';
  static const String feedbackPage = '/FeedbackPage';
  static const String feedbackListPage = '/FeedbackListPage';
  static const String mySettingPage = '/MySettingPage';
  static const String withdrawalPage = '/WithdrawalPage';
  static const String cashLogPage = '/CashLogPage';
  static const String createOrderPage = '/CreateOrderPage';
  static const String goodsDetailPage = '/GoodsDetailPage';
  static const String myWebViewPage = '/MyWebViewPage';
  static const String videoListPage = '/VideoListPage';
  static const String videoPage = '/VideoPage';
  static const String videoChildPage = '/VideoChildPage';
  static const String searchPage = '/SearchPage';
  static const String watchHistoryPage = '/WatchHistoryPage';
  static const String topicsPage = '/TopicsPage';

  /// 路由列表
  static final routes = [
    GetPage(
      name: welcome,
      page: () => const WelcomePage(),
      binding: WelcomeBinding(),
    ),
    GetPage(
      name: main,
      page: () => const MainPage(),
      binding: MainBinding(),
    ),
    // 个人中心
    GetPage(
      name: setting,
      page: () => const SettingPage(),
      binding: SettingBinding(),
    ),
    // 语言设置
    GetPage(
      name: language,
      page: () => const LanguagePage(),
      binding: LanguageBinding(),
    ),
    GetPage(
      name: login,
      page: () => const LoginPage(),
      binding: LoginBinding(),
    ),
    GetPage(
      name: liveRoom,
      page: () => const LiveRoomPage(),
      binding: LiveRoomBinding(),
    ),

    GetPage(
      name: message,
      page: () => const MessagePage(),
      binding: MessageBinding(),
    ),

    GetPage(
      name: messageListPage,
      page: () => const MessageListPage(),
      binding: MessageListBinding(),
      middlewares: [RouteMiddleware()],
    ),
    GetPage(
      name: updateAvatarPage,
      page: () => const UpdateAvatarPage(),
      binding: UpdateAvatarBinding(),
      middlewares: [RouteMiddleware()],
    ),
    GetPage(
      name: balancePage,
      page: () => const BalancePage(),
      binding: BalanceBinding(),
      middlewares: [RouteMiddleware()],
    ),
    GetPage(
      name: integralPage,
      page: () => const IntegralPage(),
      binding: IntegralBinding(),
      middlewares: [RouteMiddleware()],
    ),
    GetPage(
      name: integralGoodsPage,
      page: () => const IntegralGoodsPage(),
      binding: IntegralGoodsBinding(),
      middlewares: [RouteMiddleware()],
    ),
    GetPage(
      name: integralOrderListPage,
      page: () => const IntegralOrderListPage(),
      binding: IntegralOrderListBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: integralGoodsDetailPage,
      page: () => const IntegralGoodsDetailPage(),
      binding: IntegralGoodsDetailBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: couponPage,
      page: () => const CouponPage(),
      binding: CouponBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: orderListPage,
      page: () => const OrderListPage(),
      binding: OrderListBinding(),
      middlewares: [RouteMiddleware()],
    ),

  GetPage(
      name: orderDetailPage,
      page: () => const OrderDetailPage(),
      binding: OrderDetailBinding(),
    middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: logisticsInfoPage,
      page: () => const LogisticsInfoPage(),
      binding: LogisticsInfoBinding(),
    middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: posterPage,
      page: () => const PosterPage(),
      binding: PosterBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: userPromotionPage,
      page: () => const UserPromotionPage(),
      binding: UserPromotionBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: addressListPage,
      page: () => const AddressListPage(),
      binding: AddressListBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: addressFormPage,
      page: () => const AddressFormPage(),
      binding: AddressFormBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: addressEditPage,
      page: () => const AddressEditPage(),
      binding: AddressEditBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: contactPage,
      page: () => const ContactPage(),
      binding: ContactBinding(),
    ),

    GetPage(
      name: feedbackPage,
      page: () => const FeedbackPage(),
      binding: FeedbackBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: feedbackListPage,
      page: () => const FeedbackListPage(),
      binding: FeedbackListBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: mySettingPage,
      page: () => const MySettingPage(),
      binding: MySettingBinding(),
    ),

    GetPage(
      name: withdrawalPage,
      page: () => const WithdrawalPage(),
      binding: WithdrawalBinding(),
    ),

    GetPage(
      name: cashLogPage,
      page: () => const CashLogPage(),
      binding: CashLogBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: createOrderPage,
      page: () => const CreateOrderPage(),
      binding: CreateOrderBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: goodsDetailPage,
      page: () => const GoodsDetailPage(),
      binding: GoodsDetailBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: myWebViewPage,
      page: () => const MyWebViewPage(),
      binding: MyWebViewBinding(),
    ),

    GetPage(
      name: videoPage,
      page: () => const VideoPage(),
      binding: VideoBinding(),
    ),

    GetPage(
      name: videoChildPage,
      page: () => const VideoChildPage(),
      binding: VideoChildBinding(),
    ),

    GetPage(
      name: videoListPage,
      page: () => const VideoListPage(),
      binding: VideoListBinding(),
    ),

    GetPage(
      name: searchPage,
      page: () => const SearchPage(),
      binding: SearchBinding(),
    ),

    GetPage(
      name: watchHistoryPage,
      page: () => const WatchHistoryPage(),
      binding: WatchHistoryBinding(),
      middlewares: [RouteMiddleware()],
    ),

    GetPage(
      name: topicsPage,
      page: () => const TopicsPage(),
      binding: TopicsBinding(),
      middlewares: [RouteMiddleware()],
    ),
  ];

}
