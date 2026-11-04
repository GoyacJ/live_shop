import 'dart:convert';
import 'dart:typed_data';

import 'package:cached_network_image/cached_network_image.dart';
import 'package:crypto/crypto.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:fluwx/fluwx.dart';
import 'package:get/get.dart';
import 'package:image_gallery_saver_plus/image_gallery_saver_plus.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../init/config.dart';
import '../../utils/color_util.dart';
import '../cache_image.dart';

/// 底部更多框
class PopupShareDia extends StatefulWidget {
  const PopupShareDia({
    super.key,
    required this.poster,
    this.onTap,
  });

  final String poster;
  final Function? onTap;

  @override
  State<PopupShareDia> createState() => _PopupShareDiaState();
}

class _PopupShareDiaState extends State<PopupShareDia> {
  List circleList = [
    {'icon': const Icon(Icons.share), 'label': '分享'},
    // {'icon': const Icon(Icons.chat_bubble), 'label': '聊天设置'},
    {'icon': const Icon(Icons.link), 'label': '转发'},
    // {'icon': const Icon(Icons.card_giftcard), 'label': '送礼特效'},
    {'icon': const Icon(Icons.clear_all), 'label': '清屏'},
    // {'icon': const Icon(Icons.desk), 'label': '添加到桌面'},
    // {'icon': const Icon(Icons.local_mall), 'label': '电商组件'},
  ];
  List moreList = [
    {'icon': "icon-share-wx", 'label': '微信好友'},
    {'icon': "icon-share-pyq", 'label': '朋友圈'},
    {'icon': "icon-download", 'label': '保存图片'},
  ];
  final String poster = "";
  final bool isLoading = true;
  final Fluwx _fluwx = Fluwx();

  @override
  void initState() {
    super.initState();
    _fluwx.registerApi(appId: Config.apiHost.wxAppId,universalLink: "https://your.univerallink.com/link/");
    _fluwx.addSubscriber((res) {
      if (res is WeChatShareResponse) {
        if (res.isSuccessful) {
          Fluttertoast.showToast(msg: "分享成功", backgroundColor: ColorUtil.green, gravity: ToastGravity.CENTER);
          widget.onTap!(true);
        }
      }
    });
  }

  @override
  void dispose() {
    super.dispose();
  }

  _onShare(WeChatScene scene) async {
    var response = await Dio().get(widget.poster, options: Options(responseType: ResponseType.bytes));

    String fileName = md5.convert(utf8.encode(DateTime.now().millisecondsSinceEpoch.toString())).toString();
    final result = await ImageGallerySaverPlus.saveImage(Uint8List.fromList(response.data), quality: 80, name: fileName);
    if (result != null && result['isSuccess']) {
      String filePath = result['filePath'];
      _fluwx.share(WeChatShareImageModel(
          WeChatImageToShare(
              localImagePath: filePath
          ),
          scene: scene
      ));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Expanded(
          child: CacheImage(
            radius: 10.r,
            widget.poster,
            width: 300.w,
            height: 480.w,
            fit: BoxFit.cover,
          ),
        ),
        Material(
          color: ColorUtil.white,
          borderRadius: const BorderRadius.only(topLeft: Radius.circular(10.0), topRight: Radius.circular(10.0),),
          child: Column(
            children: [
              Row(
                children: moreList.map((item) {
                  return Expanded(
                      child: Container(
                        padding: const EdgeInsets.symmetric(horizontal: 10.0, vertical: 20.0),
                        child: InkWell(
                          onTap: () async {
                            if (item['label'] == '朋友圈') {
                              await _onShare(WeChatScene.timeline);
                              return ;
                            }
                            if (item['label'] == '微信好友') {
                              await _onShare(WeChatScene.session);
                              return ;
                            }

                            if (item['label'] == '保存图片') {
                              if (widget.poster.isEmpty) {
                                Fluttertoast.showToast(msg: "海报获取失败，无法下载", backgroundColor: ColorUtil.red, gravity: ToastGravity.CENTER);
                                return ;
                              }
                              var response = await Dio().get(widget.poster, options: Options(responseType: ResponseType.bytes));
                              String fileName = md5.convert(utf8.encode(DateTime.now().millisecondsSinceEpoch.toString())).toString();
                              final result = await ImageGallerySaverPlus.saveImage(Uint8List.fromList(response.data), quality: 80, name: fileName);
                              if (result['isSuccess']) {
                                ToastUtil.showToast("保存成功");
                              } else {
                                ToastUtil.showToast("保存失败");
                              }
                            } else {
                              ToastUtil.showToast("暂不支持");
                            }
                          },
                          child: Column(
                            children: [
                              Image.asset("assets/images/${item['icon']}.png", width: 45.0, height: 45.0,),
                              const SizedBox(height: 8.0),
                              JhText.cText('${item['label']}', ColorUtil.black54, 12.0)
                            ],
                          ),
                        ),
                      )
                  );
                }).toList(),
              ),
              const Divider(height: 1.0, color: ColorUtil.black12),
              InkWell(
                onTap: () {
                  Get.back();
                },
                child: Container(
                  width: double.infinity,
                  padding: const EdgeInsets.symmetric(vertical: 12.0),
                  // color: ColorUtil.red,
                  child: Center(
                    child: JhText.cText("取消", ColorUtil.black, 16.0),
                  ),
                ),
              )
            ],
          ),
        ),
      ],
    );
  }
}
