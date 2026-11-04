import 'package:flutter/material.dart';
import 'package:live_app/utils/toast_util.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../utils/color_util.dart';
import 'clickable_gesture_detector.dart';

/// Created by Jcy
/// on 2025/5/19
///

/// 自定义可点击效果构建器
typedef ClickableBuilder = Widget Function(WidgetState? state, Widget child);

class Clickable extends StatefulWidget {
  final Widget child;
  final bool disabled;
  final bool clickable;
  final VoidCallback? onTap;
  final ValueChanged<bool>? onLongPress;

  // 一、颜色变化（次优先级）
  final Color? color;
  final Color? pressedColor;
  final Color? disabledColor;

  // 二、透明度变化（默认效果，优先级最低）
  final double opacity;
  final double pressedOpacity;
  final double disabledOpacity;

  // 三、自定义（优先级最高，如设置则优先使用）
  final ClickableBuilder? builder;

  const Clickable({
    super.key,
    required this.child,
    this.disabled = false,
    this.clickable = true,
    this.onTap,
    this.onLongPress,
    this.color,
    this.pressedColor,
    this.disabledColor,
    this.opacity = 1.0,
    this.pressedOpacity = 0.6,
    this.disabledOpacity = 0.4,
    this.builder,
  });

  @override
  State<StatefulWidget> createState() => _ClickableState();
}

class _ClickableState extends State<Clickable> {
  WidgetState? _state;
  late bool _disabled;
  late bool _clickable;
  late bool _withColor;
  late ClickableBuilder builder;

  @override
  void initState() {
    super.initState();
    _initAttrs();
  }

  @override
  Widget build(BuildContext context) {
    Widget child = builder.call(_state, widget.child);
    if (_disabled || !_clickable) {
      return child;
    }
    return ClickableGestureDetector(
      onTap: widget.onTap,
      onLongPress: widget.onLongPress,
      onHandle: _onHandle,
      child: child,
    );
  }

  @override
  void didUpdateWidget(covariant Clickable oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.clickable != widget.clickable ||
        oldWidget.disabled != widget.disabled ||
        oldWidget.color != widget.color ||
        oldWidget.disabledColor != widget.disabledColor ||
        oldWidget.disabledOpacity != widget.disabledOpacity ||
        oldWidget.builder != widget.builder) {
      _initAttrs();
    }
  }

  void _onHandle(bool active) {
    setState(() => _state = active ? WidgetState.pressed : null);
  }

  void _initAttrs() {
    _disabled = widget.disabled;
    _clickable = widget.clickable;
    _withColor = widget.color != null;
    _state = widget.disabled ? WidgetState.disabled : null;
    builder = widget.builder == null ? (_withColor ? _withColorBuilder : _withOpacityBuilder) : widget.builder!;
  }

  /// 效果一：颜色变化构建器
  /// 通过改变组件的背景颜色达到效果反馈的目的

  Widget _withColorBuilder(WidgetState? state, Widget child) {
    Color color = widget.color!;
    if (state == WidgetState.pressed) {
      if (widget.pressedColor != null) {
        color = widget.pressedColor!;
      } else {
        color = color.withValues(alpha: widget.pressedOpacity);
      }
    } else if (state == WidgetState.disabled) {
      if (widget.disabledColor != null) {
        color = widget.disabledColor!;
      } else {
        color = color.withValues(alpha: widget.disabledOpacity);
      }
    }
    return ColoredBox(color: color, child: child);
  }

  /// 效果二：透明度变化构建器
  /// 通过改变组件的透明度达到效果反馈的目的
  Widget _withOpacityBuilder(WidgetState? state, Widget child) {
    final opacity = state == WidgetState.pressed ? widget.pressedOpacity : (state == WidgetState.disabled ? widget.disabledOpacity : widget.opacity);
    return Opacity(opacity: opacity, child: child);
  }
}
class TestPage extends StatefulWidget {
  const TestPage({super.key});

  @override
  State<TestPage> createState() => _TestPageState();
}

class _TestPageState extends State<TestPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: JhText.cText("万能点击组件", ColorUtil.white, 18.0)),
      body: Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            // 普通文本（点击透明度发申变化）
            Clickable(
              child: JhText.cText("点我", ColorUtil.black, 16.0),
              onTap: () => ToastUtil.showToast("点了"),
              onLongPress: (active) => ToastUtil.showToast(active ? "长按" : "取消长按"),
            ),
            const SizedBox(height: 20),

            // 色块背景（点击背景颜色透明度发生变化）
            Clickable(
              color: ColorUtil.redAccent,
              onTap: () => ToastUtil.showToast("点了"),
              onLongPress: (active) => ToastUtil.showToast(active ? "长按" : "取消长按"),
              child: JhText.cText("点我", ColorUtil.white, 16.0),
            ),
            const SizedBox(height: 20),

            // 色块背景（点击背景颜色在指定颜色之间切换）
            Clickable(
              color: ColorUtil.redAccent,
              pressedColor: ColorUtil.greenAccent,
              onTap: () => ToastUtil.showToast("点了"),
              onLongPress: (active) => ToastUtil.showToast(active ? "长按" : "取消长按"),
              child: JhText.cText("点我", ColorUtil.white, 16.0),
            ),
            const SizedBox(height: 20),

            // 自定义按钮效果（点击背景颜色和阴影颜色均会发生变化）
            Clickable(
              builder: (state, child) {
                final pressed = state == WidgetState.pressed;
                final color = pressed ? ColorUtil.greenAccent : ColorUtil.blueAccent;
                return Container(
                  padding: const EdgeInsets.all(10),
                  decoration: BoxDecoration(
                    color: color,
                    borderRadius: BorderRadius.circular(60),
                    boxShadow: [
                      BoxShadow(
                        color: color,
                        blurRadius: 10,
                        offset: const Offset(0, 5),
                      )
                    ],
                  ),
                  child: child,
                );
              },
              pressedColor: ColorUtil.greenAccent,
              onTap: () => ToastUtil.showToast("点了"),
              onLongPress: (active) => ToastUtil.showToast(active ? "长按" : "取消长按"),
              child: JhText.cText("点我", ColorUtil.white, 16.0),
            ),
            const SizedBox(height: 20),

            // 自定义图片背景（点击切换背景图片）
            Clickable(
              builder: (state, child) {
                return Container(
                  padding: const EdgeInsets.symmetric(
                    vertical: 10,
                    horizontal: 50,
                  ),
                  decoration: BoxDecoration(
                    image: DecorationImage(
                      image: state == WidgetState.pressed
                          ? const AssetImage("assets/btn-a.png")
                          : const AssetImage("assets/btn-b.png"),
                    ),
                  ),
                  child: child,
                );
              },
              onTap: () => ToastUtil.showToast("点了"),
              onLongPress: (active) => ToastUtil.showToast(active ? "长按" : "取消长按"),
              child: JhText.cText("点我", ColorUtil.white, 16.0),
            ),
            const SizedBox(height: 20),

            // 设置项效果（点击整体透明度发生变化）
            Clickable(
              color: ColorUtil.white,
              onTap: () => ToastUtil.showToast("点了"),
              onLongPress: (active) => ToastUtil.showToast(active ? "长按" : "取消长按"),
              child: Padding(
                padding: const EdgeInsets.all(15),
                child: Row(
                  children: [Expanded(child: JhText.cText("点我", ColorUtil.black, 16.0))],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
