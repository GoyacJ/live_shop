import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_app_update/azhon_app_update.dart';
import 'package:flutter_app_update/result_model.dart';
import 'package:flutter_app_update/update_model.dart';
import 'package:live_app/utils/color_util.dart';
import 'package:live_app/widgets/jh_text.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

import '../../utils/toast_util.dart';

/**
 *Created by Jcy
 *on 2023/6/2
 */
class AppUpdateDiaLog extends StatefulWidget {
  final String content;
  final String apkUrl;

  const AppUpdateDiaLog({
    Key? key,
    required this.content,
    required this.apkUrl,
  }) : super(key: key);

  @override
  State<AppUpdateDiaLog> createState() => _AppUpdateDiaLogState();
}

class _AppUpdateDiaLogState extends State<AppUpdateDiaLog> {
  double progressValue = 0.0;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    AzhonAppUpdate.listener((ResultModel model) {
      if (model.type == ResultType.downloading) {
        int progress = model.progress ?? 0;
        int max = model.max ?? 0;
        progressValue = progress / max;
      }
      setState(() {});
      debugPrint(model.type?.value);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      type: MaterialType.transparency,
      child: bodyView(),
    );
  }

  bodyView() {
    return Center(
      child: Container(
        decoration: BoxDecoration(
          color: ColorUtil.white,
          borderRadius: BorderRadius.circular(8)
        ),
        margin: EdgeInsets.symmetric(horizontal: 30.w),
        padding: EdgeInsets.only(top: 20.w, right: 15.w, bottom: 20.w, left: 15.w),
        child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Center(
                child: JhText.cText("有新版本可用", null, 16,
                    fontWeight: FontWeight.bold),
              ),
              Container(
                  constraints: BoxConstraints(maxHeight: 200.w),
                  child: SingleChildScrollView(
                      child: Column(children: [
                        Container(
                          height: 10,
                        ),
                        Container(
                          padding:
                          EdgeInsets.symmetric(horizontal: 5.w,vertical: 15.w),
                          width: double.infinity,
                          child: JhText.cText(widget.content, null, 13,
                              fontWeight: FontWeight.w500),
                        ),
                        Visibility(
                            visible: progressValue != 0.0,
                            child: Row(
                              children: [
                                Expanded(
                                    child: ClipRRect(
                                      borderRadius: BorderRadius.circular(50),
                                      child: LinearProgressIndicator(
                                        minHeight: 8,
                                        value: progressValue,
                                        backgroundColor: ColorUtil.inputBg,
                                        valueColor: const AlwaysStoppedAnimation<Color>(
                                            ColorUtil.primaryColor),
                                      ),
                                    )),
                                const SizedBox(
                                  width: 10,
                                ),
                                JhText.cText(
                                    '${(progressValue * 100).toStringAsFixed(0)}%',
                                    null,
                                    13)
                              ],
                            ))
                      ]))),
              Container(
                width: double.infinity,
                padding: const EdgeInsets.only(top: 20),
                child: Row(
                  children: [
                    Expanded(
                        child: ElevatedButton(
                          style: ElevatedButton.styleFrom(
                            backgroundColor: ColorUtil.grey,
                            padding:
                            EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                            textStyle: TextStyle(fontSize: 16), // 可选，加字体大小
                          ),
                          onPressed: () async {
                            Navigator.pop(context);
                          },
                          child: JhText.cText("稍后再说", ColorUtil.black, 16.0),
                        )),
                    const SizedBox(
                      width: 20,
                    ),
                    Expanded(
                        child: ElevatedButton(
                          style: ElevatedButton.styleFrom(
                            backgroundColor: ColorUtil.primaryColor,
                            padding:
                            EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                            textStyle: TextStyle(fontSize: 16), // 可选，加字体大小
                          ),
                          onPressed: () async {
                            appUpdate();
                          },
                          child: JhText.cText("立即更新", ColorUtil.white, 16.0),
                        )),
                  ],
                ),
              )
            ]),
      ),
    );
  }

  appUpdate() {
    UpdateModel model = UpdateModel(
      ///apk下载地址
      widget.apkUrl,
      //'http://18.192.203.78/solar-web/app/app-release-1.0.3.apk',
      "yxxb.apk",

      ///apk名字
      "ic_launcher",

      ///ios地址
      'https://apps.apple.com/app/6744408893',

      showBgdToast: false,
      showNotification: false,
    );
    AzhonAppUpdate.update(model).then((value) => debugPrint('$value'));
    if(Platform.isAndroid){
      ToastUtil.showToast('正在后台下载新版本...');
    }
  }
}
