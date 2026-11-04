import 'dart:convert';

import 'package:common_utils/common_utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:live_app/widgets/dialog/confirm_dialog.dart';

import '../../../models/watch_model.dart';
import '../../../network/net_util.dart';
import '../../../services/user_info_ctl.dart';
import '../../../utils/color_util.dart';
import '../../../utils/log_utils.dart';
import '../../../widgets/cache_image.dart';
import '../../../widgets/easy_loading_mixin.dart';
import '../../../widgets/jh_text.dart';
import '../../../widgets/refresh_list.dart';

/// Created by Jcy
/// on 2025/5/16
/// 
class RecordTabPage extends StatefulWidget {
  String tag;

  RecordTabPage({super.key, required this.tag});

  @override
  State<RecordTabPage> createState() => _RecordTabPageState();
}

class _RecordTabPageState extends State<RecordTabPage> with AutomaticKeepAliveClientMixin, EasyLoadingMixin {
  CustomListViewController listViewController = CustomListViewController();
  List<WatchModel> mData = [];
  int current = 1;

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return CustomListView(
      controller: listViewController,
      initialRefresh: true,
      total: mData.length,
      itemBuilder: (BuildContext context, int index) {
        return InkWell(
          child: itemView(index),
          onTap: () {},
        );
      },
      onRefresh: () async {
        current = 1;
        await handleRefresh();
      },
      onLoad: () async {
        current++;
        await handleRefresh();
      },
    );
  }

  Widget itemView(index) {
    return Container(
      margin: EdgeInsets.only(left: 10.w, right: 10.w, top: 10.w),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(8),
        color: ColorUtil.white,
        boxShadow: [
          BoxShadow(
            color: ColorUtil.black12,
            blurRadius: 4,
            offset: Offset(0, 2),
          )
        ],
      ),
      child: Row(
        children: [
          // 封面图
          CacheImage(
            mData[index].cover ?? '',
            width: 134.w,
            height: 90.w,
            radius: 7.r,
            fit: BoxFit.cover,
          ),
          SizedBox(width: 5.w,),
          // 右侧内容
          Expanded(child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Expanded(child: JhText.cText(mData[index].title, null, 14)),
                  InkWell(
                    child: Container(
                      padding: EdgeInsets.symmetric(horizontal: 15.w, vertical: 5.w),
                      child: Image.asset(
                        'assets/images/ic_del.png',
                        width: 10.w,
                        height: 10.w,
                      ),
                    ),
                    onTap: () {
                      ConfirmDialog.show(
                        context: context,
                        content: '确认要删除该记录吗？',
                        onConfirm: () {
                          delData(index);
                        },
                      );
                    },
                  ),
                ],
              ),
              SizedBox(height: 10.w,),
              Row(
                children: [
                  mData[index].contentType=='2'?
                  JhText.cText('观看至${mData[index].episodeNumber}集', ColorUtil.grey[600], 12):const SizedBox.shrink(),
                  const Spacer(),
                  InkWell(
                    child: Container(
                      width: 65.w,
                      alignment: Alignment.center,
                      margin: EdgeInsets.only(right: 5.w),
                      padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 5.w),
                      decoration: BoxDecoration(
                        color: mData[index].isFollow ?? false ? ColorUtil.red : ColorUtil.white,
                        border: Border.all(
                          color: mData[index].isFollow ?? false ? ColorUtil.red : ColorUtil.followGrey,
                        ),
                        borderRadius: BorderRadius.circular(20),
                      ),
                      child: JhText.cText(
                          mData[index].isFollow ?? false ? "已追剧" : "追剧", mData[index].isFollow ?? false ? ColorUtil.white : ColorUtil.black, 14),
                    ),
                    onTap: () {
                      mData[index].isFollow = mData[index].isFollow == null ? false : !mData[index].isFollow!;
                      setState(() {});
                      submitData(index);
                    },
                  ),
                ],
              )
            ],
          )),
        ],
      ),
    );
  }

  Future<void> handleRefresh() async {
    try {
      Map<String, dynamic>jsonMap = {};
      jsonMap.putIfAbsent('contentType', () => widget.tag);
      jsonMap.putIfAbsent('userId', () => UserInfoCtl.to.profile.id);
      jsonMap.putIfAbsent('current', () => current);
      jsonMap.putIfAbsent('size', () => 10);
      var result = await NetUtil.watchHistoryList(data: jsonMap);
      var jsonStr = jsonEncode(result);
      if (result['code'] == 200) {
        if (ObjectUtil.isNotEmpty(result['data'])) {
          final List<dynamic> dataList = result['data']['records'];
          List<WatchModel> newData = dataList
              .map((e) => WatchModel.fromJson(e))
              .toList();
          if (current == 1) {
            mData.clear();
          }
          if (!ObjectUtil.isEmptyList(newData)) {
            mData.addAll(newData);
          }
          setState(() {});
        }
      }
    } catch (e) {
      print('刷新数据失败: $e');
    }
  }

  submitData(index) async {

    Map<String, dynamic>params = {};
    params.putIfAbsent('userId', () => UserInfoCtl.to.profile.id);
    params.putIfAbsent('shopPlayId', () => mData[index].episodeId);
    params.putIfAbsent('follow', () => mData[index].isFollow);
    LogUtils.e('打印参数$params');
    var result = await NetUtil.setFollow(data: params);
    print(result['code']);
  }

  delData(index) async {
    showEasyLoading();
    try {
      var result = await NetUtil.delWatchHistory(data: {'id': mData[index].id});
      if (result['code'] == 200) {
        mData.remove(mData[index]);
        setState(() {});
        ToastUtil.showToast('删除成功');
      } else {
        ToastUtil.showToast(result['msg']);
      }
      dismissEasyLoading();
    } catch (e) {
      print(e);
      dismissEasyLoading();
    }
  }

  @override
  // TODO: implement wantKeepAlive
  bool get wantKeepAlive => true;
}
