import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:live_app/widgets/cache_image.dart';
import 'package:live_app/utils/log_utils.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../models/video_player_model.dart';
import '../../utils/color_util.dart';

/// Created by Jcy
/// on 2025/5/14
///
class EpisodeDialog extends StatefulWidget {
  final int episodeNumber;
  final int episodeCount;
  final List<VideoPlayerModel> episodeList;
  final Function onTap;

  const EpisodeDialog({
    super.key,
    required this.episodeNumber,
    required this.episodeCount,
    required this.episodeList,
    required this.onTap,
  });

  @override
  State<EpisodeDialog> createState() => _EpisodeDialogState();
}

class _EpisodeDialogState extends State<EpisodeDialog> {

  int episodeNumber = 0;
  // bool isSelected = false;
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    episodeNumber = widget.episodeNumber;
    // isSelected = widget.episodeList[0].follow??false;
  }

  @override
  Widget build(BuildContext context) {

    return Container(
      height: MediaQuery.of(context).size.height * 0.6,
      padding: const EdgeInsets.symmetric(vertical: 12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // 顶部小拖动条
          Center(
            child: Container(
              width: 40,
              height: 5,
              margin: const EdgeInsets.only(bottom: 12),
              decoration: BoxDecoration(
                color: ColorUtil.grey[300],
                borderRadius: BorderRadius.circular(2.5),
              ),
            ),
          ),

          // 顶部剧集信息区域
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: Row(
              children: [
                ClipRRect(
                  borderRadius: BorderRadius.circular(6),
                  child: CacheImage(
                    widget.episodeList[0].cover??'', // 换成真实封面图链接
                    width: 66.w,
                    height: 66.w,
                    fit: BoxFit.cover,
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children:  [
                      JhText.cText(
                        widget.episodeList[0].title??'',
                        ColorUtil.black, 14.0, fontWeight: FontWeight.bold, overflow: TextOverflow.ellipsis, maxLines: 1,
                      ),
                      SizedBox(height: 4),
                      JhText.cText(
                        '已完结 共${widget.episodeCount}集',
                        ColorUtil.grey, 12.0,
                      ),
                    ],
                  ),
                ),
                // ElevatedButton(
                //   onPressed: () {
                //     // TODO: 追剧逻辑
                //     isSelected = !isSelected;
                //     setState(() {});
                //   },
                //   style: ElevatedButton.styleFrom(
                //     backgroundColor: isSelected?Colors.pinkAccent:ColorUtil.grey.shade400,
                //     shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
                //     padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
                //     textStyle: const TextStyle(fontSize: 12),
                //   ),
                //   child: JhText.cText(isSelected?'已追剧':'追剧',ColorUtil.white,14),
                // )
              ],
            ),
          ),

          const SizedBox(height: 16),

          // 集数网格选择
          Expanded(
            child: GridView.builder(
              padding: const EdgeInsets.symmetric(horizontal: 16),
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 6,
                mainAxisSpacing: 12,
                crossAxisSpacing: 12,
                childAspectRatio: 1,
              ),
              itemCount: widget.episodeCount,
              itemBuilder: (_, index) {
                bool isSelected = false;
                if(episodeNumber==0){
                  isSelected =  index == episodeNumber;
                 }else{
                  isSelected =  index == (episodeNumber-1);
                 }
                return GestureDetector(
                  onTap: (){
                    LogUtils.d('选集弹窗: 用户点击第${index+1}集 (索引: $index)');
                    
                    episodeNumber = index+1;
                    
                    LogUtils.d('选集弹窗: 调用 onTap($index)');
                    widget.onTap(index);
                    
                    setState(() {});
                    Navigator.pop(context); // 关闭弹窗
                  },
                  child: Container(
                    alignment: Alignment.center,
                    decoration: BoxDecoration(
                      color: isSelected ? ColorUtil.orange.shade100 : ColorUtil.grey.shade100,
                      borderRadius: BorderRadius.circular(6),
                      border: isSelected ? Border.all(color: ColorUtil.orange, width: 1.5) : Border.all(color: ColorUtil.transparent),
                    ),
                    child: JhText.cText(
                      '${index+1}',
                      ColorUtil.black, 14.0, fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
                    ),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
