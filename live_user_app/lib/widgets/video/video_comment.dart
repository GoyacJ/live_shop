import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../utils/color_util.dart';
import 'input_widget.dart';

class VideoComment extends StatefulWidget {
  final Function changeCommentHeight;
  final double screenHeight;

  const VideoComment(
      {super.key,
      required this.changeCommentHeight,
      required this.screenHeight});

  @override
  State<VideoComment> createState() => _VideoComment();
}

class _VideoComment extends State<VideoComment> {
  final GlobalKey _globalKey = GlobalKey();
  bool openFullScreen = false;
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Listener(
      child: SizedBox(
        key: _globalKey,
        height:
            MediaQuery.of(context).size.height * (openFullScreen ? 0.95 : 0.7),
        width: MediaQuery.of(context).size.width,
        child: Column(
          children: [
            Container(
                width: MediaQuery.of(context).size.width,
                alignment: Alignment.center,
                height: 66,
                decoration:  BoxDecoration(
                    border: Border(
                        bottom: BorderSide(color: Color(0xffeeeeee),width: 1))),
                child: Stack(
                  alignment: Alignment.centerRight,
                  children: [
                    Align(
                      child: JhText.cText(
                        '6.6万条评论',
                        ColorUtil.black, 14.0,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    openFullScreen
                        ? const SizedBox.shrink()
                        : Container(
                      padding: const EdgeInsets.only(right: 20),
                      child: InkWell(
                        onTap: () {
                          Navigator.of(context).pop();
                        },
                        child: const Icon(
                          Icons.close,
                          color: ColorUtil.black,
                          size: 20,
                        ),
                      ),
                    )
                  ],
                )),
            Expanded(
                child: Container(
              width: MediaQuery.of(context).size.width,
              color: ColorUtil.white,
              child: ListView.builder(
                itemCount: 6,
                shrinkWrap: true,
                itemBuilder: (BuildContext context, int index) {
                  return Container(
                    padding: const EdgeInsets.only(left: 10, right: 10),
                    margin: const EdgeInsets.only(bottom: 15),
                    child: Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          CircleAvatar(
                            radius: 0.065.sw,
                            backgroundColor: ColorUtil.white,
                            child: CircleAvatar(
                              radius: 0.06.sw,
                              backgroundColor: ColorUtil.white,
                              backgroundImage: const NetworkImage(
                                  'https://i1.hdslb.com/bfs/face/cff5c0a548b4b98f7e8630b86c3240304fa49801.jpg@60w_60h_1c_1s_!web-avatar-comment.webp'),
                            ),
                          ),
                          Container(
                              margin: const EdgeInsets.only(left: 6),
                              width: MediaQuery.of(context).size.width - 0.3.sw,
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.start,
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  JhText.cText(
                                    '视觉',
                                    ColorUtil.black45, 14.0,
                                  ),
                                  JhText.cText(
                                    '愿各位看官，皆遇良人，前程似锦，未来可期.愿各位看官，皆遇良人，前程似锦，未来可期.',
                                    ColorUtil.black, 14.0,
                                    height: 1.2,
                                  ),
                                  Row(
                                    mainAxisAlignment:
                                        MainAxisAlignment.spaceBetween,
                                    children: [
                                      Row(
                                        children: [
                                          JhText.cText(
                                            '05-10 · 广东',
                                            ColorUtil.black38, 12.0,
                                          ),
                                          Container(
                                            margin:
                                                const EdgeInsets.only(left: 10),
                                            child: JhText.cText(
                                              '回复',
                                              ColorUtil.black54, 12.0,
                                            ),
                                          )
                                        ],
                                      ),
                                      Row(
                                        children: [
                                          Row(
                                            children: [
                                              const Icon(
                                                Icons.favorite_border,
                                                color: ColorUtil.black45,
                                                size: 18,
                                              ),
                                              Container(
                                                padding: const EdgeInsets.only(
                                                    left: 2, right: 16),
                                                child: JhText.cText(
                                                  '233',
                                                  ColorUtil.black38, 12.0,
                                                ),
                                              )
                                            ],
                                          ),
                                          const Icon(
                                            Icons.heart_broken_outlined,
                                            color: ColorUtil.black45,
                                            size: 18,
                                          )
                                        ],
                                      )
                                    ],
                                  ),
                                  Row(
                                    children: [
                                      JhText.cText(
                                        '----展开66条回复',
                                        ColorUtil.black54, 12.0,
                                      ),
                                      Icon(
                                        Icons.expand_more,
                                        color: ColorUtil.black54,
                                      )
                                    ],
                                  )
                                ],
                              ))
                        ]),
                  );
                },
              ),
            )),
            InputWidget()
          ],
        ),
      ),

      /// 移动操作
      onPointerMove: (PointerMoveEvent pointerMoveEvent) {
        final offset =
            (_globalKey.currentContext?.findRenderObject() as RenderBox)
                .localToGlobal(Offset.zero);
        widget.changeCommentHeight(offset.dy);
      },
      onPointerUp: (event) {
        final offset =
            (_globalKey.currentContext?.findRenderObject() as RenderBox)
                .localToGlobal(Offset.zero);
        if (offset.dy / widget.screenHeight < 0.7) {
          widget.changeCommentHeight(offset.dy, hideComment: false);
        }
      },
    );
  }
}
