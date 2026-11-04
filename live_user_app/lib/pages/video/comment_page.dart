import 'package:chat_bottom_container/chat_bottom_container.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

import '../../utils/color_util.dart';
import '../../widgets/jh_text.dart';
import '../../widgets/video/input_widget.dart';
/// Created by Jcy
/// on 2025/5/7
///


enum PanelType {
  none,
  keyboard,
  emoji,
  tool,
}

class CommentPage extends StatefulWidget {
  const CommentPage({
    super.key,
    this.safeAreaBottom,
    this.showAppBar = true,
    this.changeKeyboardPanelHeight,
    this.onControllerCreated,
  });

  final double? safeAreaBottom;

  final bool showAppBar;

  final ChatKeyboardChangeKeyboardPanelHeight? changeKeyboardPanelHeight;

  final Function(ChatBottomPanelContainerController)? onControllerCreated;

  @override
  State<CommentPage> createState() => _CommentPageState();
}

class _CommentPageState extends State<CommentPage> with RouteAware {
  Color panelBgColor = ColorUtil.grey100;

  final FocusNode inputFocusNode = FocusNode();

  final controller = ChatBottomPanelContainerController<PanelType>();

  PanelType currentPanelType = PanelType.none;

  bool readOnly = false;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
  }

  @override
  void initState() {
    super.initState();

    widget.onControllerCreated?.call(controller);
  }

  @override
  void dispose() {
    inputFocusNode.dispose();
    super.dispose();
  }

  @override
  void didPushNext() {
    super.didPushNext();
    hidePanel();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      appBar: widget.showAppBar
          ? AppBar(
        title: JhText.cText('Chat Page', ColorUtil.black, 18.0),
      )
          : null,
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Expanded(child: _buildList()),
          _buildInputView(),
          _buildPanelContainer(),
        ],
      ),
    );
  }

  Widget _buildList() {
    Widget resultWidget = ListView.builder(
      padding: EdgeInsets.zero,
      reverse: true,
      itemBuilder: (context, index) {
        return ListTile(
          title: JhText.cText('Item $index', ColorUtil.black, 16.0),
        );
      },
    );
    resultWidget = Listener(
      child: resultWidget,
      onPointerDown: (event) {
        // Hide panel when touch ListView.
        hidePanel();
      },
    );
    return resultWidget;
  }

  Widget _buildPanelContainer() {
    return ChatBottomPanelContainer<PanelType>(
      controller: controller,
      inputFocusNode: inputFocusNode,
      otherPanelWidget: (type) {
        if (type == null) return const SizedBox.shrink();
        switch (type) {
          case PanelType.emoji:
            return _buildEmojiPickerPanel();
          case PanelType.tool:
            return _buildToolPanel();
          default:
            return const SizedBox.shrink();
        }
      },
      onPanelTypeChange: (panelType, data) {
        debugPrint('panelType: $panelType');
        switch (panelType) {
          case ChatBottomPanelType.none:
            currentPanelType = PanelType.none;
            break;
          case ChatBottomPanelType.keyboard:
            currentPanelType = PanelType.keyboard;
            break;
          case ChatBottomPanelType.other:
            if (data == null) return;
            switch (data) {
              case PanelType.emoji:
                currentPanelType = PanelType.emoji;
                break;
              case PanelType.tool:
                currentPanelType = PanelType.tool;
                break;
              default:
                currentPanelType = PanelType.none;
                break;
            }
            break;
        }
      },
      changeKeyboardPanelHeight: widget.changeKeyboardPanelHeight,
      panelBgColor: panelBgColor,
      safeAreaBottom: widget.safeAreaBottom,
    );
  }

  Widget _buildToolPanel() {
    return Container(
      height: 450,
      color: ColorUtil.red[50],
      child: Center(
        child: JhText.cText('Tool Panel', ColorUtil.black, 16.0),
      ),
    );
  }

  Widget _buildEmojiPickerPanel() {
    // If the keyboard height has been recorded, priority is given to setting
    // the height to the keyboard height.
    double height = 300;
    final keyboardHeight = controller.keyboardHeight;
    if (keyboardHeight != 0) {
      if (widget.changeKeyboardPanelHeight != null) {
        height = widget.changeKeyboardPanelHeight!.call(keyboardHeight);
      } else {
        height = keyboardHeight;
      }
    }

    return Container(
      padding: EdgeInsets.zero,
      height: height,
      color: ColorUtil.blue[50],
      child: Center(
        child: JhText.cText('Emoji Panel', ColorUtil.black, 16.0),
      ),
    );
  }

  Widget _buildInputView() {
    return Container(
      height: 50,
      color: ColorUtil.white,
      child: Row(
        children: [
          const SizedBox(width: 15),
          Expanded(
            child: Listener(
              onPointerUp: (event) {
                // Currently it may be emojiPanel.
                if (readOnly) {
                  updatePanelType(PanelType.keyboard);
                }
              },
              child: TextField(
                focusNode: inputFocusNode,
                readOnly: readOnly,
                showCursor: true,
              ),
            ),
          ),
          GestureDetector(
            child: const Icon(Icons.emoji_emotions_outlined, size: 30),
            onTap: () {
              updatePanelType(
                PanelType.emoji == currentPanelType
                    ? PanelType.keyboard
                    : PanelType.emoji,
              );
            },
          ),
          GestureDetector(
            child: const Icon(Icons.add, size: 30),
            onTap: () {
              updatePanelType(
                PanelType.tool == currentPanelType
                    ? PanelType.keyboard
                    : PanelType.tool,
              );
            },
          ),
          const SizedBox(width: 15),
        ],
      ),
    );
  }

  updatePanelType(PanelType type) async {
    final isSwitchToKeyboard = PanelType.keyboard == type;
    final isSwitchToEmojiPanel = PanelType.emoji == type;
    bool isUpdated = false;
    switch (type) {
      case PanelType.keyboard:
        updateInputView(isReadOnly: false);
        break;
      case PanelType.emoji:
        isUpdated = updateInputView(isReadOnly: true);
        break;
      default:
        break;
    }

    updatePanelTypeFunc() {
      controller.updatePanelType(
        isSwitchToKeyboard
            ? ChatBottomPanelType.keyboard
            : ChatBottomPanelType.other,
        data: type,
        forceHandleFocus: isSwitchToEmojiPanel
            ? ChatBottomHandleFocus.requestFocus
            : ChatBottomHandleFocus.none,
      );
    }

    if (isUpdated) {
      // Waiting for the input view to update.
      WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
        updatePanelTypeFunc();
      });
    } else {
      updatePanelTypeFunc();
    }
  }

  hidePanel() {
    if (inputFocusNode.hasFocus) {
      inputFocusNode.unfocus();
    }
    updateInputView(isReadOnly: false);
    if (ChatBottomPanelType.none == controller.currentPanelType) return;
    controller.updatePanelType(ChatBottomPanelType.none);
  }

  bool updateInputView({
    required bool isReadOnly,
  }) {
    if (readOnly != isReadOnly) {
      readOnly = isReadOnly;
      // You can just refresh the input view.
      setState(() {});
      return true;
    }
    return false;
  }
}