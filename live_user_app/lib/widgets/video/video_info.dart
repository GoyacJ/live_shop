import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:live_app/widgets/jh_text.dart';
import 'package:live_app/widgets/video/text_expanded.dart';

import '../../utils/color_util.dart';

/// type  0  短剧 1 短剧集数
class VideoInfo extends StatefulWidget {
  final String title;
  final String author;
  final int type;
  final int count;
  final int episodeNumber;
  final VoidCallback onTap;

  const VideoInfo({
    super.key,
    required this.title,
    required this.author,
    required this.type,
    required this.count,
    required this.episodeNumber,
    required this.onTap,
  });

  @override
  State<VideoInfo> createState() => _VideoInfo();
}

class _VideoInfo extends State<VideoInfo> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Positioned(
        bottom: 10,
        child: Container(
          width: MediaQuery.of(context).size.width,
          padding: const EdgeInsets.fromLTRB(10, 0, 0, 36),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              SizedBox(
                width: MediaQuery.of(context).size.width - 200,
                child: Text(
                  widget.author,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(
                      color: ColorUtil.white,
                      height: 3.0,
                      fontSize: 16.sp,
                      fontWeight: FontWeight.bold),
                ),
              ),
              SizedBox(
                width: MediaQuery.of(context).size.width - 100,
                child: BrnExpandableText(
                  textStyle: TextStyle(
                      height: 1.2, fontSize: 16.sp, color: ColorUtil.white),
                  color: ColorUtil.black,
                  text: widget.title,
                  maxLines: 3,
                ),
              ),
              Visibility(
                  visible: widget.type == 1,
                  child: InkWell(
                    onTap: widget.onTap,
                    child: Container(
                      margin: EdgeInsets.only(top: 10.w),
                      padding: EdgeInsets.symmetric(
                          horizontal: 20.w, vertical: 10.w),
                      decoration: BoxDecoration(
                          color: ColorUtil.black.withValues(alpha: 0.5),
                          borderRadius: BorderRadius.circular(8.r)),
                      child: JhText.cText(
                          widget.type == 0
                              ? '立即观看短剧·全${widget.count}集'
                              : '共${widget.count}集',
                          ColorUtil.white,
                          15),
                    ),
                  ))
              // ScrollText(
              //   content: '@${widget.author}创建的原声',
              //   marquee: true,
              //   contentTextColor: ColorUtil.white,
              //   backgroundColor: ColorUtil.transparent,
              // ),
            ],
          ),
        ));
  }
}
