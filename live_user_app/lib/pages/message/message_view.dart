import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:live_app/routes/app_pages.dart';
import 'package:get/get.dart';

import '../../utils/color_util.dart';
import '../../widgets/jh_text.dart';
import 'message_logic.dart';

class MessagePage extends BaseStatefulWidget<MessageLogic> {
  const MessagePage({super.key});

  @override
  bool showBackButton() {
    // TODO: implement showBackButton
    return true;
  }

  @override
  String? title() {
    // TODO: implement title
    return '消息';
  }

  @override
  Color appBarTitleColor() => ColorUtil.black;

  @override
  bool enablePullRefresh() => true;

  // @override
  // flexibleSpaceWidget(){
  //   return Container(
  //     decoration: const BoxDecoration(
  //         gradient: LinearGradient(
  //           begin: Alignment.topLeft,
  //           end: Alignment.bottomRight,
  //           colors: [
  //             ColorUtil.gradientPink, Color(0xFF9543FF)
  //           ],
  //         )
  //     ),
  //   );
  // }

  @override
  int statusBarStyle() => 1;

  @override
  Widget buildContent(BuildContext context) {
    // TODO: implement buildContent
    return ListView(
      children: [
        _buildItem(
            const Icon(Icons.people, color: ColorUtil.white, size: 28.0),
            '新朋友', false, 'friend'),
        _buildItem(
            const Icon(Icons.local_activity, color: ColorUtil.white),
            '互动消息', false, 'chat'),
        _buildItem(
            const Icon(Icons.message_outlined, color: ColorUtil.white, size: 28.0,),
            '评论回复', false, 'comment'),
        Container(
          height: 10.0,
          color: ColorUtil.backgroundColor,
        ),
        _buildItem(
            const Icon(Icons.notifications_sharp, color: ColorUtil.white, size: 28.0,),
            '平台通知', true, 'notice'),
        _buildItem(
            const Icon(Icons.card_membership_outlined, color: ColorUtil.white, size: 28.0,),
            '会员服务', true, 'vip'),
        _buildItem(
            const Icon(Icons.redeem_outlined, color: ColorUtil.white, size: 28.0,),
            '福利助手', true, 'welfare'),
      ],
    );
  }

  Widget _buildItem(Widget icon, String title, bool isSystem, String type) {
    return Ink(
      color: ColorUtil.white, //置顶颜色
      child: InkWell(
        splashColor: ColorUtil.grey[200],
        onTap: () {
          Get.toNamed(AppPages.messageListPage, arguments: {
            'type': type,
            'title': title
          });
        },
        child: Container(
          padding: const EdgeInsets.symmetric(horizontal: 15.0, vertical: 12.0),
          child: Row(
            children: <Widget>[
              // 图像
              ClipOval(
                  child: Container(
                    alignment: Alignment.center,
                    height: 50.0,
                    width: 50.0,
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(50.0),
                        gradient: isSystem ? const LinearGradient(
                          begin: Alignment.bottomRight,
                          end: Alignment.topLeft,
                          colors: [
                            Color.fromARGB(255, 255, 191, 131), Color.fromARGB(255, 253, 37, 66)
                          ],
                        ) : const LinearGradient(
                          begin: Alignment.topLeft,
                          end: Alignment.bottomRight,
                          colors: [
                            Color.fromARGB(255, 255, 191, 131), Color.fromARGB(255, 253, 37, 66)
                          ],
                        )
                    ),
                    child: icon,
                  )
              ),
              const SizedBox(width: 12.0),
              Expanded(
                child: JhText.cText(title, ColorUtil.black, 18.0),
              ),
              // 右侧
              const Icon(Icons.arrow_forward_ios, color: ColorUtil.grey, size: 14.0)
            ],
          ),
        ),
      ),
    );
  }

}
