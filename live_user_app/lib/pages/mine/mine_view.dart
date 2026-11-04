import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:live_app/widgets/clickable.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../services/user_info_ctl.dart';
import '../../widgets/dialog/confirm_dialog_demo.dart';
import 'mine_logic.dart';

class MinePage extends BaseStatefulWidget<MineLogic> {
  const MinePage({super.key});

  @override
  bool showBackButton() {
    // TODO: implement showBackButton
    return false;
  }

  @override
  bool showAppBar() {
    // TODO: implement showAppBar
    return false;
  }

  @override
  bool enablePullRefresh() {
    // TODO: implement enablePullRefresh
    return true;
  }

  @override
  Widget buildContent(BuildContext context) {
    // TODO: implement buildContent
    // double statusBarHeight = MediaQuery.of(context).padding.top;
    return _buildView(kToolbarHeight, context);
  }

  Future<void> _showInputDialog(BuildContext context) async {
    String? inputValue;

    await showDialog(
      context: context,
      builder: (BuildContext context) {
        TextEditingController nameController = TextEditingController();
        return AlertDialog(
          backgroundColor: ColorUtil.white,
          title: JhText.cText('修改昵称', ColorUtil.black, 18.0),
          content: TextField(
            controller: nameController,
            maxLength: 20,
            decoration: const InputDecoration(
              hintText: '请输入新的昵称',
            ),
          ),
          actions: <Widget>[
            InkWell(
              onTap: () {
                Get.back();
              },
              child: Container(
                width: 80.w,
                height: 40.w,
                decoration: BoxDecoration(
                  color: ColorUtil.backgroundColor,
                  borderRadius: BorderRadius.circular(4.w),
                ),
                child: Center(
                  child: JhText.cText('取消', ColorUtil.black45, 15.0),
                ),
              ),
            ),
            InkWell(
              onTap: () {
                inputValue = nameController.text;
                if (inputValue!.isNotEmpty) {
                  controller.onEditNickname(inputValue!);
                } else {
                  ToastUtil.showToast("请输入新的昵称");
                }
              },
              child: Container(
                width: 80.w,
                height: 40.w,
                margin: EdgeInsets.only(left: 10.w),
                decoration: BoxDecoration(
                  color: ColorUtil.primaryColor,
                  borderRadius: BorderRadius.circular(4.w),
                ),
                child: Center(
                  child: JhText.cText('确定', ColorUtil.white, 15.0),
                ),
              ),
            ),
          ],
        );
      },
    );

    if (inputValue != null) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: JhText.cText('输入的值为: $inputValue', ColorUtil.black, 14.0)),
      );
    }
  }

  // 主视图
  Widget _buildView(double statusBarHeight, BuildContext context) {
    return ListView(
      padding: EdgeInsets.zero,
      children: [
        Container(
            // height: statusBarHeight + 180.w,
            width: double.infinity,
            decoration: const BoxDecoration(color: ColorUtil.white),
            child: Stack(
              children: [
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    SizedBox(height: 15.w),
                    _buildUserInfo(context),
                    _buildBalance(),
                  ],
                ),
                Container(
                  alignment: Alignment.topRight,
                  padding: EdgeInsets.only(right: 20.w, top: 15.w),
                  child: Clickable(
                    child: JhText.cText('消息', ColorUtil.black, 18),
                    onTap: () {
                      Get.toNamed(AppPages.message);
                    },
                  ),
                )
              ],
            )),
        Container(
          height: 10.0,
          decoration: const BoxDecoration(color: ColorUtil.cardBg),
        ),
        _buildOrder(),
        _buildService(),
        // TextButton(onPressed: (){
        //   Get.to(ConfirmDialogDemo());
        // }, child: JhText.cText('弹窗', null, 16))
      ],
    );
  }

  // 用户基础信息
  Widget _buildUserInfo(BuildContext context) {
    return Row(
      children: <Widget>[
        SizedBox(width: 15.w),
        InkWell(
          onTap: () {
            Get.toNamed(AppPages.updateAvatarPage);
          },
          child: Obx(() => CircleAvatar(
                radius: 40.w,
                backgroundColor: ColorUtil.black26,
                backgroundImage: UserInfoCtl.to.profile.avatar.isNotEmpty
                    ? NetworkImage(UserInfoCtl.to.profile.avatar)
                    : const AssetImage('assets/images/default-avatar.jpg'),
              )),
        ),
        SizedBox(width: 10.w),
        Obx(() => UserInfoCtl.to.isLogin
            ? Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  SizedBox(
                    width: 255.w,
                    child: Row(
                      children: [
                        Container(
                          constraints: BoxConstraints(
                            maxWidth: 200.w,
                          ),
                          child: Obx(() => JhText.cText(
                                UserInfoCtl.to.profile.nickname,
                                ColorUtil.black, 18.0,
                                fontWeight: FontWeight.bold,
                                overflow: TextOverflow.ellipsis,
                              )),
                        ),
                        SizedBox(width: 5.w),
                        IconButton(
                            onPressed: () {
                              _showInputDialog(context);
                            },
                            icon: Icon(Icons.edit_outlined, size: 20.w, color: ColorUtil.black54)),
                      ],
                    ),
                  ),
                  const SizedBox(
                    height: 5.0,
                  ),
                  // const Row(
                  //   children: [
                  //     const Text('等级：3', style: TextStyle(fontSize: 12.0, color: ColorUtil.grey),),
                  //     // 二维码
                  //     // const SizedBox(width: 5.0,),
                  //     // InkWell(
                  //     //   onTap: () {qrcodeAlertDialog(context);},
                  //     //   child: const Icon(Icons.qr_code, size: 18.0, color: ColorUtil.grey,),
                  //     // ),
                  //   ],
                  // ),
                ],
              )
            : InkWell(
                onTap: () {
                  Get.toNamed(AppPages.login);
                },
                child: JhText.cText("点击登录", ColorUtil.black, 18.0),
              )),
      ],
    );
  }

  // 账户余额
  Widget _buildBalance() {
    return Container(
        padding: const EdgeInsets.only(top: 30.0, bottom: 20.0),
        child: Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
          Obx(() => Expanded(
                child: InkWell(
                  onTap: () {
                    Get.toNamed(AppPages.balancePage);
                  },
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      JhText.cText(
                        UserInfoCtl.to.profile.cash.toStringAsFixed(2),
                        ColorUtil.black, 20.0,
                        fontWeight: FontWeight.bold,
                      ),
                      JhText.cText(
                        "账户余额",
                        ColorUtil.black54, 16.0,
                      ),
                    ],
                  ),
                ),
              )),
          Obx(() => Expanded(
                child: InkWell(
                  onTap: () {
                    Get.toNamed(AppPages.integralPage);
                  },
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      JhText.cText(
                        '${UserInfoCtl.to.integer.value}',
                        ColorUtil.black, 20.0,
                        fontWeight: FontWeight.bold,
                      ),
                      JhText.cText(
                        "积分",
                        ColorUtil.black54, 16.0,
                      ),
                    ],
                  ),
                ),
              )),
          Expanded(
            child: InkWell(
              onTap: () {
                Get.toNamed(AppPages.couponPage);
              },
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  JhText.cText(
                    "0",
                    ColorUtil.black, 20.0,
                    fontWeight: FontWeight.bold,
                  ),
                  JhText.cText(
                    "优惠券",
                    ColorUtil.black54, 16.0,
                  ),
                ],
              ),
            ),
          ),
        ]));
  }

  // 订单
  Widget _buildOrder() {
    return Container(
        padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 20.w),
        child: Row(children: [
          Expanded(
              child: Row(children: [
            ...controller.orderStatusList.asMap().entries.map((item) {
              return Expanded(
                child: InkWell(
                    onTap: () async {
                      Get.toNamed(AppPages.orderListPage, arguments: item.key+1);
                    },
                    child: Column(
                      children: [
                        Icon(item.value['icon'], size: 30.w, color: ColorUtil.primaryColor),
                        const SizedBox(
                          height: 5.0,
                        ),
                        Text(
                          item.value['title'],
                          style: TextStyle(fontSize: 16.sp, color: ColorUtil.black54),
                        ),
                      ],
                    )),
              );
            }),
          ]))
        ]));
  }

  // 服务
  Widget _buildService() {
    return Container(
        width: double.infinity,
        padding: const EdgeInsets.symmetric(horizontal: 15.0),
        child: Column(
          children: [
            ...controller.serviceList.map((item) {
              return InkWell(
                splashColor: ColorUtil.grey[200],
                child: Container(
                    padding: const EdgeInsets.symmetric(vertical: 12.0),
                    decoration: const BoxDecoration(border: Border(top: BorderSide(color: ColorUtil.black26, width: .1))),
                    child: Row(
                      children: [
                        Icon(item['icon'], color: item['color'], size: 20.w),
                        const SizedBox(
                          width: 8.0,
                        ),
                        JhText.cText(
                          item['title'],
                          ColorUtil.black, 18.0,
                        ),
                        const Spacer(),
                        const Icon(Icons.arrow_forward_ios, size: 12.0, color: ColorUtil.grey),
                      ],
                    )),
                onTap: () {
                  // 跳转页面
                  Get.toNamed(item['nav']);
                },
              );
            })
          ],
        ));
  }
}
