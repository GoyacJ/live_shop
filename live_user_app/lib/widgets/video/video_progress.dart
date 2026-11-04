import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

import '../../utils/color_util.dart';

class VideoProgress extends StatefulWidget {
  final Duration position;
  final Duration duration;
  final Function setPosition;
  final Function setTimeChange;

  const VideoProgress({super.key, required this.position, required this.duration, required this.setPosition, required this.setTimeChange});

  @override
  State<VideoProgress> createState() => _VideoProgress();
}

class _VideoProgress extends State<VideoProgress> {
  double duration = 100;
  bool seekFlag = false;
  double playTime = 0;

  @override
  void initState() {
    super.initState();
    try {
      var temp = widget.duration.toString().split(":");
      if (temp.length >= 3) {
        var totalTime = double.parse(temp[0]) * 60 * 60 + double.parse(temp[1]) * 60 + double.parse(temp[2]);
        if (totalTime > 0 && !totalTime.isNaN && !totalTime.isInfinite) {
          setState(() {
            duration = totalTime;
          });
        }
      }
    } catch (e) {
      // 解析失败时保持默认值 100
      duration = 100;
    }
  }

  double formatTime() {
    try {
      var temp = widget.position.toString().split(":");
      if (temp.length < 3) return 0.0;
      
      var totalTime = double.parse(temp[0]) * 60 * 60 + double.parse(temp[1]) * 60 + double.parse(temp[2]);
      
      if (duration <= 0) {
        return 0.0;
      }
      
      var time = totalTime / duration * 100;
      
      // 确保值在有效范围内，避免 NaN
      if (time.isNaN || time.isInfinite) {
        time = 0.0;
      } else if (time > 100) {
        time = 100.0;
      } else if (time < 0) {
        time = 0.0;
      }
      
      // 只有在不拖动时才更新 playTime，避免冲突
      if (!seekFlag) {
        setState(() {
          playTime = time;
        });
      }
      
      return time;
    } catch (e) {
      // 解析错误时返回安全值
      return 0.0;
    }
  }

  /// 获取安全的滑块值
  double _getSafeSliderValue() {
    double currentValue;
    
    // 拖动中使用拖动值，否则使用播放器位置
    if (seekFlag) {
      currentValue = playTime;
    } else {
      final timeValue = formatTime();
      currentValue = timeValue;
    }
    
    // 确保值在有效范围内
    if (currentValue.isNaN || currentValue.isInfinite) {
      return 0.0;
    } else if (currentValue > 100.0) {
      return 100.0;
    } else if (currentValue < 0.0) {
      return 0.0;
    }
    
    return currentValue;
  }

  @override
  Widget build(BuildContext context) {
    return Positioned(
      width: MediaQuery.of(context).size.width,
      bottom: MediaQuery.of(context).padding.bottom + 10,
      child: Padding(
        padding: EdgeInsets.zero,
        child: SliderTheme(
            data: SliderTheme.of(context).copyWith(
              activeTrackColor: seekFlag ? const Color.fromRGBO(255, 255, 255, .8) : const Color.fromRGBO(255, 255, 255, .5),
              inactiveTrackColor: const Color.fromRGBO(255, 255, 255, .3),
              thumbShape: RoundSliderThumbShape(enabledThumbRadius: seekFlag ? 6 : 3), // 滑块大小
              trackShape: FullWidthTrackShape(),
              valueIndicatorTextStyle: const TextStyle(
                color: ColorUtil.white,
              ),
              trackHeight: seekFlag ? 6 : 3,
              // overlayShape: SliderComponentShape.noOverlay,
              overlayShape: RoundSliderOverlayShape(overlayRadius: 10.0), // 拖动响应范围
              thumbColor: seekFlag ? const Color.fromRGBO(255, 255, 255, .9) : const Color.fromRGBO(255, 255, 255, .6),
              overlayColor: ColorUtil.transparent,
            ),
            child: Slider(
              value: _getSafeSliderValue(),
              min: 0.0,
              max: 100.0,
              onChanged: (val) {
                setState(() {
                  playTime = val;
                });
                int secondsTemp = ((val / 100 * duration) % 60).floor();
                String seconds = secondsTemp < 9 ? '0${secondsTemp.toString()}' : secondsTemp.toString();
                int minutesTemp = ((val / 100 * duration) / 60).floor();
                String minutes = minutesTemp < 10 ? '0${minutesTemp.toString()}' : minutesTemp.toString();
                widget.setTimeChange('$minutes:$seconds');
              },
              onChangeStart: (val) {
                setState(() {
                  seekFlag = true;
                });
              },
              onChangeEnd: (val) {
                setState(() {
                  seekFlag = false;
                  playTime = val;
                });
                widget.setPosition(val / 100 * duration);
              },
            )),
      ),
    );
  }
}

class FullWidthTrackShape extends RoundedRectSliderTrackShape {
  FullWidthTrackShape({this.addHeight = 0});

  double addHeight;

  @override
  void paint(
    PaintingContext context,
    Offset offset, {
    required RenderBox parentBox,
    required SliderThemeData sliderTheme,
    required Animation<double> enableAnimation,
    required TextDirection textDirection,
    required Offset thumbCenter,
    Offset? secondaryOffset,
    bool isDiscrete = false,
    bool isEnabled = false,
    double additionalActiveTrackHeight = 2,
  }) {
    super.paint(context, offset,
        parentBox: parentBox,
        sliderTheme: sliderTheme,
        enableAnimation: enableAnimation,
        textDirection: textDirection,
        thumbCenter: thumbCenter,
        additionalActiveTrackHeight: addHeight);
  }
}
