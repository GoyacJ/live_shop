import 'package:common_utils/common_utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:get/get_core/src/get_main.dart';
import 'package:like_button/like_button.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:live_app/widgets/video/video_comment.dart';
import 'package:modal_bottom_sheet/modal_bottom_sheet.dart';

import '../../services/user_info_ctl.dart';
import '../../utils/color_util.dart';
import '../dialog/popup_share_dia.dart';
import '../jh_text.dart';

class RightBar extends StatefulWidget {
  final String? goodId;
  final int type;
  final bool isLike; // 是否已点赞
  final bool isColl; // 是否已收藏
  final int likeCount; // 点赞数量
  final int commentCount; // 评论数量
  final int shareCount; // 分享数量
  final int collectCount; // 收藏数量
  final double screenHeight; // 屏幕高度
  final Function changeRightBarData; // 用于改变右侧栏数据的回调函数
  final bool isPlaying; // 视频是否正在播放
  final String avatar; // 用户头像
  final String ?livePoster; // 分享海报
  const RightBar(
      {super.key,
      this.goodId,
      required this.screenHeight,
      required this.type,
      required this.isLike,
      required this.likeCount,
      required this.commentCount,
      required this.shareCount,
      required this.changeRightBarData,
      required this.isColl,
      required this.collectCount,
      required this.isPlaying,
      required this.avatar,
      this.livePoster,
      });

  @override
  State<RightBar> createState() => _RightBar();
}

class _RightBar extends State<RightBar> {
  @override
  void initState() {
    super.initState();
  }

  void changeCommentHeight(double height, {bool? hideComment}) {
    widget.changeRightBarData(
        commentShow: true,
        screenRate: (height / widget.screenHeight),
        hideComment: hideComment);
  }

  @override
  Widget build(BuildContext context) {
    return Positioned(
      bottom: 80,
      right: 10,
      child: Column(
        children: [
          // SizedBox(
          //     height: 80,
          //     child: InkWell(
          //       onTap: () {
          //         debugPrint('点击了头像');
          //       },
          //       child: Stack(
          //         alignment: Alignment.center,
          //         children: [
          //           CircleAvatar(
          //             radius: 0.065.sw,
          //             backgroundColor: ColorUtil.white,
          //             child: CircleAvatar(
          //               radius: 0.06.sw,
          //               backgroundColor: ColorUtil.white,
          //               backgroundImage: NetworkImage(widget.avatar),
          //             ),
          //           ),
          //           Positioned(
          //               bottom: 0.02.sw,
          //               child: InkWell(
          //                 onTap: () {
          //                   debugPrint('点击了关注');
          //                 },
          //                 child: Stack(
          //                   alignment: Alignment.center,
          //                   children: [
          //                     CircleAvatar(
          //                       backgroundColor: ColorUtil.white,
          //                       radius: 0.02.sw,
          //                     ),
          //                     Positioned(
          //                         child: Icon(
          //                       Icons.add_circle,
          //                       color: ColorUtil.redAccent,
          //                       size: 0.06.sw,
          //                     ))
          //                   ],
          //                 ),
          //               ))
          //         ],
          //       ),
          //     )),
          InkWell(
            onTap: () {
              // videoCommentDialog();
            },
            child: Column(
              children: [
                Icon(
                  Icons.mode_comment_rounded,
                  color: ColorUtil.white,
                  size: 0.1.sw,
                ),
                SizedBox(
                  height: 26,
                  child: JhText.cText(
                    widget.commentCount == 0
                        ? '评论'
                        : widget.commentCount.toString(),
                    ColorUtil.white, 10.0,
                  ),
                )
              ],
            ),
          ),
          LikeButton(
            size: 0.1.sw,
            isLiked: widget.isColl,
            circleColor: const CircleColor(
                start: Color.fromRGBO(251, 184, 3, 1),
                end: Color.fromRGBO(251, 184, 3, 1)),
            bubblesColor: const BubblesColor(
              dotPrimaryColor: Color.fromRGBO(251, 184, 3, 1),
              dotSecondaryColor: Color.fromRGBO(251, 184, 3, 1),
            ),
            onTap: (bool isCollect) async {

              if(UserInfoCtl.to.isLogin){
                widget.changeRightBarData(isCollect: !isCollect);
              }else{
                ToastUtil.showToast('请先登录！！！');
              }
              return UserInfoCtl.to.isLogin?!isCollect:false;
            },
            likeBuilder: (bool isCollect) {
              return AnimatedCrossFade(
                duration: const Duration(milliseconds: 200),
                crossFadeState: isCollect
                    ? CrossFadeState.showFirst
                    : CrossFadeState.showSecond,
                firstChild: Icon(
                  Icons.star_rounded,
                  color: const Color.fromRGBO(251, 184, 3, 1),
                  size: 0.1.sw,
                ),
                secondChild: Icon(
                  Icons.star_rounded,
                  color: ColorUtil.white,
                  size: 0.1.sw,
                ),
              );
            },
            likeCountAnimationType: LikeCountAnimationType.all,
            countPostion: CountPostion.bottom,
            countDecoration: (Widget count, int? collectCount) {
              return Row(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: <Widget>[
                  SizedBox(
                    height: 26,
                    child: JhText.cText(
                      widget.likeCount == 0
                          ? '收藏'
                          : widget.collectCount.toString(),
                      ColorUtil.white, 10.0,
                    ),
                  )
                ],
              );
            },
          ),
          LikeButton(
            size: 0.1.sw,
            isLiked: widget.isLike,
            circleColor: const CircleColor(
                start: Color.fromRGBO(248, 48, 88, 1),
                end: Color.fromRGBO(248, 48, 88, 1)),
            bubblesColor: const BubblesColor(
              dotPrimaryColor: Color.fromRGBO(248, 48, 88, 1),
              dotSecondaryColor: Color.fromRGBO(248, 48, 88, 1),
            ),
            onTap: (bool isLiked) async {
              if(UserInfoCtl.to.isLogin){
                widget.changeRightBarData(isLike: !isLiked);
              }else{
                ToastUtil.showToast('请先登录！！！');
              }
              return UserInfoCtl.to.isLogin?!isLiked:false;
            },
            likeBuilder: (bool isLiked) {
              return AnimatedCrossFade(
                duration: const Duration(milliseconds: 200),
                crossFadeState: isLiked
                    ? CrossFadeState.showFirst
                    : CrossFadeState.showSecond,
                firstChild: Icon(
                  Icons.favorite_rounded,
                  color: const Color.fromRGBO(248, 48, 88, 1),
                  size: 0.1.sw,
                ),
                secondChild: Icon(
                  Icons.favorite_rounded,
                  color: ColorUtil.white,
                  size: 0.1.sw,
                ),
              );
            },
            likeCountAnimationType: LikeCountAnimationType.all,
            countPostion: CountPostion.bottom,
            likeCount: 665,
            countDecoration: (Widget count, int? likeCount) {
              return Row(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: <Widget>[
                  SizedBox(
                    height: 26,
                    child: JhText.cText(
                      widget.likeCount == 0
                          ? '喜欢'
                          : widget.likeCount.toString(),
                      ColorUtil.white, 10.0,
                    ),
                  )
                ],
              );
            },
          ),
          InkWell(
            onTap: () {
              sharedDialog();
            },
            child: Column(
              children: [
                Transform(
                  alignment: Alignment.center,
                  transform: Matrix4.rotationY(3.1416), // π 弧度，即 180 度水平翻转
                  child: Icon(
                    Icons.reply_rounded,
                    color: ColorUtil.white,
                    size: 0.1.sw,
                  ),
                ),
                SizedBox(
                  height: 26,
                  child: JhText.cText(
                    widget.shareCount == 0
                        ? '分享'
                        : widget.shareCount.toString(),
                    ColorUtil.white, 10.0,
                  ),
                )
              ],
            ),
          ),
          Visibility(
              visible: widget.goodId != null,
              child: InkWell(
            onTap: () {
              if(UserInfoCtl.to.isLogin){
                widget.changeRightBarData(shop: true);
              }else{
                ToastUtil.showToast('请先登录！！！');
              }
            },
            child: Image.asset('assets/images/ic_shop.png',width: 0.1.sw,height: 0.1.sw,),
          ))
          ,
          // RotateMusicPic(
          //     isPlaying: widget.isPlaying, avatar: widget.avatar)
        ],
      ),
    );
  }

  videoCommentDialog(){
    widget.changeRightBarData(commentShow: true, screenRate: 0.3);
    Future result = showMaterialModalBottomSheet(
      useRootNavigator: true,
      animationCurve: Curves.linear,
      closeProgressThreshold: 0.8,
      duration: const Duration(milliseconds: 200),
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.only(
            topLeft: Radius.circular(10.0),
            topRight: Radius.circular(10.0)),
      ),
      context: context,
      barrierColor: ColorUtil.transparent,
      backgroundColor: ColorUtil.white,
      builder: (context) {
        return VideoComment(
            changeCommentHeight: changeCommentHeight,
            screenHeight: widget.screenHeight);
      },
    );
    result.then((value) {
      widget.changeRightBarData(commentShow: false);
    });
    debugPrint('评论');
  }

  sharedDialog(){

    if(!UserInfoCtl.to.isLogin){
      ToastUtil.showToast('请先登录！！！');
      return;
    }
    if(ObjectUtil.isEmptyString(widget.livePoster)){
      ToastUtil.showToast('无法获取分享链接，请重试！！！');
     }else{
      Get.bottomSheet(PopupShareDia(
          poster: widget.livePoster!,
          onTap: (value){
            widget.changeRightBarData(isShare: value);
          },
      ));
     }
  }

  // sharedDialog(){
  //   showMaterialModalBottomSheet(
  //     useRootNavigator: true,
  //     duration: const Duration(milliseconds: 200),
  //     closeProgressThreshold: 0.95,
  //     shape: const RoundedRectangleBorder(
  //       borderRadius: BorderRadius.only(
  //           topLeft: Radius.circular(10.0),
  //           topRight: Radius.circular(10.0)),
  //     ),
  //     context: context,
  //     barrierColor: ColorUtil.transparent,
  //     backgroundColor: ColorUtil.white,
  //     builder: (context) {
  //       return SizedBox(
  //         // width: MediaQuery.of(context).size.width,
  //           height: 300,
  //           child: Column(
  //             children: [
  //               Container(
  //                 // width: MediaQuery.of(context).size.width,
  //                 padding: const EdgeInsets.only(
  //                     left: 15, right: 15, top: 10, bottom: 10),
  //                 child: Row(
  //                   mainAxisAlignment: MainAxisAlignment.spaceBetween,
  //                   children: [
  //                     Container(
  //                       alignment: Alignment.center,
  //                       child: const Text(
  //                         '分享到',
  //                         style: TextStyle(
  //                             fontWeight: FontWeight.bold,
  //                             color: ColorUtil.black),
  //                       ),
  //                     ),
  //                     InkWell(
  //                       onTap: () => Navigator.of(context).pop(),
  //                       child: const Icon(
  //                         Icons.close,
  //                         size: 18,
  //                         color: ColorUtil.grey,
  //                       ),
  //                     )
  //                   ],
  //                 ),
  //               ),
  //               Container(
  //                 decoration: BoxDecoration(
  //                   border: Border(
  //                     bottom: BorderSide(
  //                       color: ColorUtil.grey.withOpacity(0.2),
  //                       width: 1.0,
  //                     ),
  //                   ),
  //                 ),
  //                 width: MediaQuery.of(context).size.width,
  //                 margin: const EdgeInsets.only(
  //                     left: 15, right: 15, bottom: 15),
  //                 padding: const EdgeInsets.only(bottom: 15),
  //                 child: SingleChildScrollView(
  //                   scrollDirection: Axis.horizontal,
  //                   child: Row(
  //                     children: [
  //                       Container(
  //                         margin: const EdgeInsets.only(right: 20),
  //                         child: Column(
  //                           children: [
  //                             CircleAvatar(
  //                               radius: 0.065.sw,
  //                               backgroundColor: ColorUtil.white,
  //                               child: CircleAvatar(
  //                                 radius: 0.06.sw,
  //                                 backgroundColor: ColorUtil.white,
  //                                 backgroundImage:
  //                                 NetworkImage(widget.avatar),
  //                               ),
  //                             ),
  //                             Container(
  //                               padding:
  //                               const EdgeInsets.only(top: 4),
  //                               width: 48,
  //                               child: const Text(
  //                                 'ISO_Studio',
  //                                 maxLines: 1,
  //                                 overflow: TextOverflow.ellipsis,
  //                                 style:
  //                                 TextStyle(color: ColorUtil.black),
  //                               ),
  //                             )
  //                           ],
  //                         ),
  //                       ),
  //                       Container(
  //                         margin: const EdgeInsets.only(right: 20),
  //                         child: Column(
  //                           children: [
  //                             CircleAvatar(
  //                               radius: 0.065.sw,
  //                               backgroundColor: ColorUtil.white,
  //                               child: CircleAvatar(
  //                                 radius: 0.06.sw,
  //                                 backgroundColor: ColorUtil.white,
  //                                 backgroundImage: const NetworkImage(
  //                                     'https://i1.hdslb.com/bfs/face/40213ffa924f6ea36f96d1f767f6d78c31290519.jpg@96w_96h_1c.webp'),
  //                               ),
  //                             ),
  //                             Container(
  //                               padding:
  //                               const EdgeInsets.only(top: 4),
  //                               width: 48,
  //                               child: const Text(
  //                                 'Kototo',
  //                                 maxLines: 1,
  //                                 overflow: TextOverflow.ellipsis,
  //                                 style:
  //                                 TextStyle(color: ColorUtil.black),
  //                               ),
  //                             )
  //                           ],
  //                         ),
  //                       )
  //                     ],
  //                   ),
  //                 ),
  //               ),
  //               Container(
  //                   width: MediaQuery.of(context).size.width,
  //                   margin: const EdgeInsets.only(
  //                       left: 15, right: 15, bottom: 15),
  //                   child: SingleChildScrollView(
  //                       scrollDirection: Axis.horizontal,
  //                       child: Row(children: [
  //                         Container(
  //                             margin:
  //                             const EdgeInsets.only(right: 20),
  //                             child: Column(
  //                               children: [
  //                                 Container(
  //                                   width: 36,
  //                                   height: 36,
  //                                   decoration: const BoxDecoration(
  //                                     shape: BoxShape.circle,
  //                                     color: Color.fromRGBO(
  //                                         248, 208, 24, 1),
  //                                   ),
  //                                   child: const Center(
  //                                     child: Icon(Icons.flash_on),
  //                                   ),
  //                                 ),
  //                                 Center(
  //                                   child: Container(
  //                                     padding: const EdgeInsets.only(
  //                                         top: 4),
  //                                     child: const Text(
  //                                       '转发',
  //                                       style: TextStyle(
  //                                           color: ColorUtil.black45,
  //                                           fontSize: 12),
  //                                     ),
  //                                   ),
  //                                 )
  //                               ],
  //                             )),
  //                         Container(
  //                             margin:
  //                             const EdgeInsets.only(right: 20),
  //                             child: Column(
  //                               children: [
  //                                 Container(
  //                                   width: 36,
  //                                   height: 36,
  //                                   decoration: const BoxDecoration(
  //                                     shape: BoxShape.circle,
  //                                     color: Color.fromRGBO(
  //                                         156, 101, 244, 1),
  //                                   ),
  //                                   child: const Center(
  //                                     child: Icon(Icons.thumb_up),
  //                                   ),
  //                                 ),
  //                                 Center(
  //                                   child: Container(
  //                                     padding: const EdgeInsets.only(
  //                                         top: 4),
  //                                     child: const Text(
  //                                       '推荐给朋友',
  //                                       style: TextStyle(
  //                                           color: ColorUtil.black45,
  //                                           fontSize: 12),
  //                                     ),
  //                                   ),
  //                                 )
  //                               ],
  //                             )),
  //                         Container(
  //                             margin:
  //                             const EdgeInsets.only(right: 20),
  //                             child: Column(
  //                               children: [
  //                                 Container(
  //                                   width: 36,
  //                                   height: 36,
  //                                   decoration: const BoxDecoration(
  //                                     shape: BoxShape.circle,
  //                                     color: Color.fromRGBO(
  //                                         255, 71, 105, 1),
  //                                   ),
  //                                   child: const Center(
  //                                     child: Icon(Icons.send),
  //                                   ),
  //                                 ),
  //                                 Center(
  //                                   child: Container(
  //                                     padding: const EdgeInsets.only(
  //                                         top: 4),
  //                                     child: const Text(
  //                                       '私信朋友',
  //                                       style: TextStyle(
  //                                           color: ColorUtil.black45,
  //                                           fontSize: 12),
  //                                     ),
  //                                   ),
  //                                 )
  //                               ],
  //                             )),
  //                         Container(
  //                             margin:
  //                             const EdgeInsets.only(right: 20),
  //                             child: Column(
  //                               children: [
  //                                 Container(
  //                                   width: 36,
  //                                   height: 36,
  //                                   decoration: const BoxDecoration(
  //                                     shape: BoxShape.circle,
  //                                     color: Color.fromRGBO(
  //                                         18, 192, 91, 1),
  //                                   ),
  //                                   child: const Center(
  //                                     child: Icon(Icons.group_add),
  //                                   ),
  //                                 ),
  //                                 Center(
  //                                   child: Container(
  //                                     padding: const EdgeInsets.only(
  //                                         top: 4),
  //                                     child: const Text(
  //                                       '新建分享',
  //                                       style: TextStyle(
  //                                           color: ColorUtil.black45,
  //                                           fontSize: 12),
  //                                     ),
  //                                   ),
  //                                 )
  //                               ],
  //                             )),
  //                         Container(
  //                             margin:
  //                             const EdgeInsets.only(right: 20),
  //                             child: Column(
  //                               children: [
  //                                 Container(
  //                                   width: 36,
  //                                   height: 36,
  //                                   decoration: const BoxDecoration(
  //                                     shape: BoxShape.circle,
  //                                     color: Color.fromRGBO(
  //                                         59, 190, 236, 1),
  //                                   ),
  //                                   child: const Center(
  //                                     child: Icon(Icons.insert_link),
  //                                   ),
  //                                 ),
  //                                 Center(
  //                                   child: Container(
  //                                     padding: const EdgeInsets.only(
  //                                         top: 4),
  //                                     child: const Text(
  //                                       '复制链接',
  //                                       style: TextStyle(
  //                                           color: ColorUtil.black45,
  //                                           fontSize: 12),
  //                                     ),
  //                                   ),
  //                                 )
  //                               ],
  //                             )),
  //                         Container(
  //                             margin:
  //                             const EdgeInsets.only(right: 20),
  //                             child: Column(
  //                               children: [
  //                                 Container(
  //                                   width: 36,
  //                                   height: 36,
  //                                   decoration: const BoxDecoration(
  //                                     shape: BoxShape.circle,
  //                                     color: Color.fromRGBO(
  //                                         255, 155, 38, 1),
  //                                   ),
  //                                   child: const Center(
  //                                     child: Icon(Icons.image),
  //                                   ),
  //                                 ),
  //                                 Center(
  //                                   child: Container(
  //                                     padding: const EdgeInsets.only(
  //                                         top: 4),
  //                                     child: const Text(
  //                                       '生成图片',
  //                                       style: TextStyle(
  //                                           color: ColorUtil.black45,
  //                                           fontSize: 12),
  //                                     ),
  //                                   ),
  //                                 )
  //                               ],
  //                             ))
  //                       ]))),
  //               Container(
  //                   width: MediaQuery.of(context).size.width,
  //                   margin: const EdgeInsets.only(
  //                       left: 15, right: 15, bottom: 15),
  //                   child: SingleChildScrollView(
  //                       scrollDirection: Axis.horizontal,
  //                       child: Row(children: [
  //                         Container(
  //                             margin:
  //                             const EdgeInsets.only(right: 20),
  //                             child: Column(
  //                               children: [
  //                                 Container(
  //                                   width: 36,
  //                                   height: 36,
  //                                   decoration: const BoxDecoration(
  //                                     shape: BoxShape.circle,
  //                                     color: Color.fromRGBO(
  //                                         243, 243, 243, 1),
  //                                   ),
  //                                   child: const Center(
  //                                     child: Icon(
  //                                       Icons.weekend,
  //                                       color: ColorUtil.black45,
  //                                     ),
  //                                   ),
  //                                 ),
  //                                 Center(
  //                                   child: Container(
  //                                     padding: const EdgeInsets.only(
  //                                         top: 4),
  //                                     child: const Text(
  //                                       '一起看视频',
  //                                       style: TextStyle(
  //                                           color: ColorUtil.black45,
  //                                           fontSize: 12),
  //                                     ),
  //                                   ),
  //                                 )
  //                               ],
  //                             )),
  //                         Container(
  //                             margin:
  //                             const EdgeInsets.only(right: 20),
  //                             child: Column(
  //                               children: [
  //                                 Container(
  //                                   width: 36,
  //                                   height: 36,
  //                                   decoration: const BoxDecoration(
  //                                     shape: BoxShape.circle,
  //                                     color: Color.fromRGBO(
  //                                         243, 243, 243, 1),
  //                                   ),
  //                                   child: const Center(
  //                                     child: Icon(
  //                                       Icons.report_problem,
  //                                       color: ColorUtil.black45,
  //                                     ),
  //                                   ),
  //                                 ),
  //                                 Center(
  //                                   child: Container(
  //                                     padding: const EdgeInsets.only(
  //                                         top: 4),
  //                                     child: const Text(
  //                                       '举报',
  //                                       style: TextStyle(
  //                                           color: ColorUtil.black45,
  //                                           fontSize: 12),
  //                                     ),
  //                                   ),
  //                                 )
  //                               ],
  //                             )),
  //                         Container(
  //                             margin:
  //                             const EdgeInsets.only(right: 20),
  //                             child: Column(
  //                               children: [
  //                                 Container(
  //                                   width: 36,
  //                                   height: 36,
  //                                   decoration: const BoxDecoration(
  //                                     shape: BoxShape.circle,
  //                                     color: Color.fromRGBO(
  //                                         243, 243, 243, 1),
  //                                   ),
  //                                   child: const Center(
  //                                     child: Icon(
  //                                       Icons.people_alt,
  //                                       color: ColorUtil.black45,
  //                                     ),
  //                                   ),
  //                                 ),
  //                                 Center(
  //                                   child: Container(
  //                                     padding: const EdgeInsets.only(
  //                                         top: 4),
  //                                     child: const Text(
  //                                       '合拍',
  //                                       style: TextStyle(
  //                                           color: ColorUtil.black45,
  //                                           fontSize: 12),
  //                                     ),
  //                                   ),
  //                                 )
  //                               ],
  //                             )),
  //                         Container(
  //                             margin:
  //                             const EdgeInsets.only(right: 20),
  //                             child: Column(
  //                               children: [
  //                                 Container(
  //                                   width: 36,
  //                                   height: 36,
  //                                   decoration: const BoxDecoration(
  //                                     shape: BoxShape.circle,
  //                                     color: Color.fromRGBO(
  //                                         243, 243, 243, 1),
  //                                   ),
  //                                   child: const Center(
  //                                     child: Icon(
  //                                       Icons.motion_photos_auto,
  //                                       color: ColorUtil.black45,
  //                                     ),
  //                                   ),
  //                                 ),
  //                                 Center(
  //                                   child: Container(
  //                                     padding: const EdgeInsets.only(
  //                                         top: 4),
  //                                     child: const Text(
  //                                       '动态壁纸',
  //                                       style: TextStyle(
  //                                           color: ColorUtil.black45,
  //                                           fontSize: 12),
  //                                     ),
  //                                   ),
  //                                 )
  //                               ],
  //                             )),
  //                         Container(
  //                             margin:
  //                             const EdgeInsets.only(right: 20),
  //                             child: Column(
  //                               children: [
  //                                 Container(
  //                                   width: 36,
  //                                   height: 36,
  //                                   decoration: const BoxDecoration(
  //                                     shape: BoxShape.circle,
  //                                     color: Color.fromRGBO(
  //                                         243, 243, 243, 1),
  //                                   ),
  //                                   child: const Center(
  //                                     child: Icon(
  //                                       Icons.heart_broken,
  //                                       color: ColorUtil.black45,
  //                                     ),
  //                                   ),
  //                                 ),
  //                                 Center(
  //                                   child: Container(
  //                                     padding: const EdgeInsets.only(
  //                                         top: 4),
  //                                     child: const Text(
  //                                       '不感兴趣',
  //                                       style: TextStyle(
  //                                           color: ColorUtil.black45,
  //                                           fontSize: 12),
  //                                     ),
  //                                   ),
  //                                 )
  //                               ],
  //                             )),
  //                       ])))
  //             ],
  //           ));
  //     },
  //   );
  // }
}
