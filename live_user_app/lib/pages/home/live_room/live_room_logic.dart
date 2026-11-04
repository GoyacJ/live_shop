import 'dart:convert';

import 'package:aliplayer_widget/aliplayer_widget_lib.dart';
import 'package:common_utils/common_utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter_openim_sdk/flutter_openim_sdk.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:path_provider/path_provider.dart';

import '../../../base/controller/base_controller.dart';
import '../../../init/config.dart';
import '../../../models/chat_message.dart';
import '../../../models/live.dart';
import '../../../models/live_goods.dart';
import '../../../network/net_util.dart';
import '../../../routes/app_pages.dart';
import '../../../services/user_info_ctl.dart';
import '../../../utils/color_util.dart';
import '../../../utils/config_util.dart';
import '../../../utils/log_utils.dart';
import '../../../utils/toast_util.dart';
import '../../../widgets/cache_image.dart';
import '../../../widgets/jh_text.dart';

class LiveRoomLogic extends BaseController {
  late AliPlayerWidgetController aliPlayerController;
  String liveId = "";
  RxBool playStatus = true.obs;
  Rx<LiveEntity> liveDetail = LiveEntity.empty().obs;
  RxList<LiveGoodsEntity> liveGoods = <LiveGoodsEntity>[].obs;
  LiveGoodsEntity?goodsEntity;
  RxList<ChatMessageEntity> chatMessages = <ChatMessageEntity>[].obs;
  final RxBool _isOpenImInit = false.obs;
  RxString livePoster = "".obs;
  RxBool isLoading = true.obs;
  RxDouble keyboardHeight = 0.0.obs;
  int second = -1;
  bool? isShare;

  OverlayEntry? overlayEntry; // 提前声明
  late DateTime startTime;

  // 聊天区域的滚动控制器
  final ScrollController chatScrollController = ScrollController();

  // 输入框控制器
  final TextEditingController messageTextController = TextEditingController();

  @override
  initData() async {
    startTime = DateTime.now();
    liveId = Get.arguments['id'];
    isShare = Get.arguments['isShare'];
    print("进入直播间");
    // 直播
    aliPlayerController = AliPlayerWidgetController(Get.context!);
    aliPlayerController.playErrorNotifier.addListener(() {
      aliPlayerController.playStateNotifier.value = PlayState.stopped;
      // showDialog();
    });
    _initData();
    _livePoster();
  }

  _initData() async {
    print("开始获取直播间数据");
    final Map<String, dynamic> result = await NetUtil.getLiveDetail(liveId);
    print(result);
    if (result['code'] == 200) {
      liveDetail.value = LiveEntity.fromJson(result['data']);
      liveGoods.value = result['data']['goods'].map<LiveGoodsEntity>((item) {
        return LiveGoodsEntity.fromJson(item);
      }).toList();

      final data = AliPlayerWidgetData(
        sceneType: SceneType.live,
        videoUrl: liveDetail.value.url,
        // coverUrl: liveDetail.value.cover
      );
      print("播放地址：${liveDetail.value.url}");
      aliPlayerController.configure(data);
      isLoading.value = false;
      await openImInit();
    } else {
      isLoading.value = false;
    }
    update();
  }

  _livePoster() async {
    final result = await NetUtil.getUserPoster(402,liveId);
    if (result['code'] == 200) {
      livePoster.value = result['data'] ?? '';
      update();
    }
  }

  openImInit() async {
    final path = (await getApplicationDocumentsDirectory()).path;
    await OpenIM.iMManager.initSDK(
        platformID: IMPlatform.android,
        apiAddr: Config.apiHost.openIMApi,
        wsAddr: Config.apiHost.openIMWs,
        logLevel: 6,
        logFilePath: '$path/',
        dataDir: '$path/',
        listener: OnConnectListener(
          onConnectSuccess: () {
            // 已经成功连接到服务器
            print("已经成功连接到服务器");
          },
          onConnecting: () {
            // 正在连接到服务器，适合在 UI 上展示“正在连接”状态。
            print("正在连接服务器");
          },
          onConnectFailed: (code, errorMsg) {
            // 连接服务器失败，可以提示用户当前网络连接不可用
            print("连接服务器失败");
          },
          onUserTokenExpired: () {
            // 登录票据已经过期，请使用新签发的 UserSig 进行登录。
            print("登录票据已经过期");
          },
          onKickedOffline: () {
            // 当前用户被踢下线，此时可以 UI 提示用户“您已经在其他端登录了当前账号，是否重新登录？”
          },
        ));

    print("openim初始化第一步");
    OpenIM.iMManager
      ..userManager.setUserListener(OnUserListener())
      // 添加消息监听器（不用时移除）
      ..messageManager.setAdvancedMsgListener(OnAdvancedMsgListener(
        onRecvNewMessage: (message) async {
          print("收到新消息1：${message.toJson()}");

          if (message.contentType == MessageType.text) {
            // 特定用户/特定标识 业务处理
            // 观看人数 - 系统用户
            // 福袋 - 系统用户
            chatMessages.add(ChatMessageEntity.fromJson({
              "message": message.textElem!.content,
              "nickname": message.senderNickname,
            }));
            update();
            // 滚动到底部
            _scrollToBottom();
          }

          // OpenIM.iMManager.messageManager.getAdvancedHistoryMessageList(
          //   conversationID: ""
          // );

          // Message sendMsg = await OpenIM.iMManager.messageManager.createTextMessage(
          //   text: "我收到消息了，并发送了一条新消息",
          // );
          // Message msg = await OpenIM.iMManager.messageManager.sendMessage(
          //     message: sendMsg,
          //     groupID: "3892076524",
          //     offlinePushInfo: OfflinePushInfo()
          // );
          // print("发消息：${msg.toJson()}");
        },
      ))
      // 设置消息发送进度监听器
      ..messageManager.setMsgSendProgressListener(OnMsgSendProgressListener())
      // 设置会话监听器
      ..conversationManager.setConversationListener(OnConversationListener(
          // onTotalUnreadMessageCountChanged: (count) {
          //   print("收到新消息5");
          //   // print(count);
          // },
          ))
      // 设置群组监听器
      ..groupManager.setGroupListener(OnGroupListener(onGroupInfoChanged:(info){
        if(ObjectUtil.isNotEmpty(info.ex)){
          Map<String,dynamic>jsonMap = jsonDecode(info.ex!);
          if(jsonMap['state']=='2'){
            goodsEntity = LiveGoodsEntity(
              liveId: jsonMap['tips']['liveId'],
              id: jsonMap['tips']['goodsId'],
              title: jsonMap['tips']['goodsName'],
              cover: jsonMap['tips']['hostImage'],
              price: jsonMap['tips']['price'],
              quantity: 0,
              sales: 0,
              stock: 0,
            );
            //显示弹窗
            showBottomRightPopup(jsonMap['tips']['yellowBikeEndTime']);
          }else if(jsonMap['state']=='1'){
            //刷新列表
            goodsEntity = LiveGoodsEntity(
              liveId: jsonMap['top']['liveId'],
              id: jsonMap['top']['goodsId'],
              title: jsonMap['top']['goodsName'],
              cover: jsonMap['top']['hostImage'],
              price: jsonMap['top']['price'],
              quantity: 0,
              sales: 0,
              stock: 0,
            );
            liveGoods.insert(0,goodsEntity!);
            update();
          }
        }
        LogUtils.e('收到后台消息${info.ex}');
      }));
    if (UserInfoCtl.to.isLogin) {
      await openImLogin();
    }
  }

  openImLogin() async {
    print("用户ID：${UserInfoCtl.to.profile.id}，token=${UserInfoCtl.to.imToken}");
    UserInfo userInfo = await OpenIM.iMManager.login(
        userID: UserInfoCtl.to.profile.id, token: UserInfoCtl.to.imToken);
    print("登录成功，用户信息：${userInfo.toJson()}");
    second = DateTime.now().second;
    bool isJoined = await OpenIM.iMManager.groupManager.isJoinedGroup(
      groupID: liveDetail.value.id,
    );

    print("加群状态-${liveDetail.value.id}：$isJoined");
    if (!isJoined) {
      await OpenIM.iMManager.groupManager.joinGroup(
        groupID: liveDetail.value.id,
        reason: '',
        joinSource: 2,
      );
    }
    bool isAgainJoined = await OpenIM.iMManager.groupManager.isJoinedGroup(
      groupID: liveDetail.value.id,
    );

    if (!isAgainJoined) {
      addUserToGroup();
    }

    _isOpenImInit.value = true;
  }

  // 发弹幕
  void onSendMessage() async {
    if (!UserInfoCtl.to.isLogin) {
      ToastUtil.showToast("请先登录");
      return;
    }

    if (messageTextController.text.isEmpty) {
      ToastUtil.showToast("请输入内容");
      return;
    }

    // 发送消息
    Message sendMsg = await OpenIM.iMManager.messageManager.createTextMessage(
      text: messageTextController.text.trim(),
    );
    sendMessage(sendMsg);

    // Message msg = await OpenIM.iMManager.messageManager.sendMessage(
    //     message: sendMsg,
    //     groupID: liveDetail.value.id,
    //     offlinePushInfo: OfflinePushInfo()
    // );
    //
    // chatMessages.add(ChatMessageEntity.fromJson({
    //   "message": messageTextController.text.trim(),
    //   "nickname": UserInfoCtl.to.profile.nickname,
    // }));

    // messageTextController.clear();
    // update();
    // // 滚动到底部
    // _scrollToBottom();
  }

  /// 滚动到底部 / Scroll to bottom
  void _scrollToBottom({bool animated = true}) {
    if (!chatScrollController.hasClients) return;

    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (animated) {
        chatScrollController.animateTo(
          chatScrollController.position.maxScrollExtent,
          duration: const Duration(milliseconds: 300),
          curve: Curves.easeOut,
        );
      } else {
        chatScrollController
            .jumpTo(chatScrollController.position.maxScrollExtent);
      }
    });
  }

  Future sendMessage(Message message) {
    LogUtils.d('send : ${json.encode(message)}');
    return OpenIM.iMManager.messageManager
        .sendMessage(
          message: message,
          groupID: liveDetail.value.id,
          offlinePushInfo: OfflinePushInfo(),
        )
        .then((value) => _sendSucceeded(message, value))
        .catchError(
            (error, _) => _senFailed(message, liveDetail.value.id, error, _))
        .whenComplete(() => _completed());
  }

  void _sendSucceeded(Message oldMsg, Message newMsg) {
    LogUtils.d('message send success----${oldMsg.toJson()}');
    LogUtils.d('message send success----${newMsg.toJson()}');
    oldMsg.update(newMsg);
  }

  void _senFailed(Message message, String? groupId,error, stack) async {
    LogUtils.d('message send failed  groupId:$groupId, catch error :$error  $stack');
    message.status = MessageStatus.failed;
  }

  void _completed() {
    chatMessages.add(ChatMessageEntity.fromJson({
      "message": messageTextController.text.trim(),
      "nickname": UserInfoCtl.to.profile.nickname,
    }));
    messageTextController.clear();
    update();
    // 滚动到底部
    _scrollToBottom();
  }


  showDialog() {
    Get.dialog(
        barrierDismissible: false,
        PopScope(
          canPop: false,
          child: AlertDialog(
              backgroundColor: ColorUtil.white,
              content: const SizedBox(
                height: 60.0,
                child: Center(
                  child: Text('直播还未开始',
                      style: TextStyle(fontSize: 16.0, color: ColorUtil.red)),
                ),
              ),
              actions: [
                InkWell(
                  onTap: () {
                    Get.back();
                    Get.until((route) => route.settings.name=='/');
                  },
                  child: Container(
                      width: double.infinity,
                      height: 50,
                      decoration: const BoxDecoration(
                        // 只有顶部有边框
                        border: Border(
                          top: BorderSide(
                            color: Color(0xFFE8E8E8),
                            width: 1.0,
                          ),
                        ),
                      ),
                      child: const Center(
                        child: Text("稍后再来", style: TextStyle(fontSize: 16.0)),
                      )),
                )
              ]),
        ));
  }

  addUserToGroup() async {
    NetUtil.addUserToGroup(
            {'groupId': liveId, 'invitedUserID': UserInfoCtl.to.profile.id})
        .onError((err, stc) {
      LogUtils.e(err.toString());
      throw '请求错误';
    });
  }

  // 提交单个视频的观看时长
  Future<void> submitLiveViewDuration() async {
    if (!UserInfoCtl.to.isLogin) return;
    final DateTime endTime = DateTime.now();
    final seconds = endTime.difference(startTime).inSeconds;
    try {
      if (!UserInfoCtl.to.isLogin) return;
      final Map<String, dynamic> params = {
        'userId': UserInfoCtl.to.profile.id,
        'liveId': liveId,
        'duration': seconds,
        'startTime': ConfigUtil.formatDateTimeWithZeroTime(startTime),
        'endTime': ConfigUtil.formatDateTimeWithZeroTime(endTime),
      };
      final result = await NetUtil.liveDuration(data: params);
      LogUtils.d('提交直播观看时长: ${result['code']}');
    } catch (e) {
      LogUtils.e('提交视频观看时长出错: $e');
    }
  }


  @override
  void onClose() async {
    int closeSeconds = DateTime.now().second;
    Map<String,dynamic>jsonMap={};
    jsonMap.putIfAbsent('userId', () => UserInfoCtl.to.profile.id);
    jsonMap.putIfAbsent('liveId', () => liveDetail.value.id);
    jsonMap.putIfAbsent('visitTime', () => closeSeconds-second);
    NetUtil.getDuration(jsonMap);
    aliPlayerController.destroy();
    if (_isOpenImInit.value) {
      await OpenIM.iMManager.logout();
    }
    // OpenIM.iMManager.unInitSDK();
    if(overlayEntry!=null){
      overlayEntry?.dispose();
    }
    submitLiveViewDuration();
    super.onClose();
  }

  /// 监听键盘高度变化
  @override
  void didChangeMetrics(height) {
    // 获取键盘高度
    keyboardHeight.value = height;
    update();
  }

  ///弹窗
  void showBottomRightPopup(int targetTimestamp) {
    final currentTime = DateTime.now().millisecondsSinceEpoch;
    final overlay = Overlay.of(Get.context!);
    overlayEntry = OverlayEntry(
      builder: (context) => Positioned(
        right: 20,
        bottom: 40,
        child: Material(
          color: ColorUtil.transparent,
          child: AnimatedOpacity(
            opacity: 1.0,
            duration: const Duration(milliseconds: 300),
            child: InkWell(
              child: Container(
                // width: 120.w,
                decoration: BoxDecoration(
                  color: ColorUtil.white,
                  borderRadius: BorderRadius.circular(10.r),
                ),
                padding: EdgeInsets.all(5.w),
                child: Column(
                  children: [
                    Stack(
                      alignment: Alignment.topRight,
                      children: [
                        CacheImage(
                          goodsEntity?.cover??'',
                          width: 105.w,
                          height: 105.w,
                          radius: 10.r,
                        ),
                        InkWell(
                          onTap: () {
                            overlayEntry?.remove(); // ✅ 关闭弹窗
                          },
                          child: const Icon(Icons.close_rounded, color: ColorUtil.white),
                        )
                      ],
                    ),
                    SizedBox(height: 5.w,),
                    SizedBox(
                      width: 105.w,
                      child: JhText.cText(
                        goodsEntity?.title??'',
                        ColorUtil.black,
                        7,
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ),
                    Container(
                      height: 20.w,
                      decoration: BoxDecoration(
                        color: ColorUtil.brightRed,
                        borderRadius: BorderRadius.circular(20.r),
                      ),
                      margin: EdgeInsets.only(top: 10.w),
                      padding: EdgeInsets.symmetric(horizontal: 5.w),
                      child: Row(
                        children: [
                          JhText.cText('￥', ColorUtil.white, 7),
                          JhText.cText('${goodsEntity?.price??0.0}', ColorUtil.white, 10, fontWeight: FontWeight.bold),
                          Container(
                            height: 20.w,
                            alignment: Alignment.center,
                            margin: EdgeInsets.only(left: 10.w),
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(20.r),
                              gradient: const LinearGradient(
                                colors: [
                                  ColorUtil.gradientRed1,
                                  ColorUtil.gradientRed2,
                                ],
                              ),
                            ),
                            child: JhText.cText('立即抢购', ColorUtil.white, 10, fontWeight: FontWeight.bold),
                          )
                        ],
                      ),
                    ),
                  ],
                ),
              ),
              onTap: (){
                Map<String,dynamic>params = {
                  'id':goodsEntity!.id,
                  'price':goodsEntity!.price,
                  'liveId':goodsEntity!.liveId,
                  'cover':goodsEntity!.cover,
                  'title':goodsEntity!.title,
                };
                Get.toNamed(AppPages.createOrderPage, arguments:params);
              },
            ),
          ),
        ),
      ),
    );

    overlay.insert(overlayEntry!);
    final remainingTime = targetTimestamp - currentTime;
    if (remainingTime <= 0) {
      // 目标时间已过，立即执行
      overlayEntry?.remove();
    } else {
      Future.delayed(Duration(milliseconds: remainingTime), () {
        if (overlayEntry!.mounted) {
          overlayEntry!.remove();
        }
      });
    }
  }
}
