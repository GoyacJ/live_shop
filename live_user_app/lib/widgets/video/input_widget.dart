import 'package:extended_text_field/extended_text_field.dart';
import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';
import 'package:live_app/utils/log_utils.dart';
import 'package:live_app/widgets/jh_text.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

import '../../utils/color_util.dart';
import 'emojis.dart';
import 'my_span_builder.dart';

enum PanelType {
  none,    // 无面板
  keyboard, // 键盘
  emoji,   // 表情
  at,      // @用户列表
}

class InputWidget extends StatefulWidget {
  const InputWidget({super.key});

  @override
  State<InputWidget> createState() => _InputWidgetState();
}

class _InputWidgetState extends State<InputWidget> with WidgetsBindingObserver {
  FocusNode focusNode = FocusNode();
  late TextEditingController textEditingController;
  PanelType currentPanelType = PanelType.none;
  final GlobalKey<ExtendedTextFieldState> _key = GlobalKey<ExtendedTextFieldState>();
  // 表情面板固定高度
  static const double emojiPanelHeight = 300.0;

  @override
  void initState() {
    textEditingController = TextEditingController();
    // insertText('@www \uC001 ');
    textEditingController.addListener(editorListen);
    focusNode.addListener(focusListener);
    WidgetsBinding.instance.addObserver(this);
    super.initState();
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    textEditingController.dispose();
    textEditingController.removeListener(editorListen);
    focusNode.removeListener(focusListener);
    super.dispose();
  }

  void editorListen() {
    String text = textEditingController.text;

    if (text.isNotEmpty && text.substring(text.length - 1) == "@") {
      // FocusScope.of(context).unfocus();
    }
    setState(() {});
  }

  void focusListener() {
    if (focusNode.hasFocus) {
      setState(() {
        currentPanelType = PanelType.keyboard;
      });
    }
    setState(() {});
  }

  void _switchPanel(PanelType type) {
    // 如果点击当前显示的面板，则关闭它
    if (currentPanelType == type) {
      setState(() {
        currentPanelType = PanelType.none;
      });
      if (type == PanelType.keyboard) {
        focusNode.unfocus();
      }
      return;
    }

    // 切换到新面板
    setState(() {
      currentPanelType = type;
    });

    // 处理键盘焦点
    if (type == PanelType.keyboard) {
      focusNode.requestFocus();
    } else {
      focusNode.unfocus();
    }
  }

  _buildDefaultInput() {
    return Container(
      decoration: const BoxDecoration(
        border: Border(
          top: BorderSide(
            width: 1.0,
            color: Color(0xffeeeeee),
          ),
        ),
      ),
      child: Container(
        // padding: const EdgeInsets.symmetric(horizontal: 10,vertical: 5),
        padding: EdgeInsets.all(10),
        margin: EdgeInsets.fromLTRB(10, 10, 10, 30),
        // margin: const EdgeInsets.symmetric(vertical: 20, horizontal: 10),
        width: MediaQuery.of(context).size.width,
        decoration: BoxDecoration(
          color: const Color(0xfff1f2f3),
          borderRadius: BorderRadius.circular(30.0),
        ),
        child: defaultView(),
      ),
    );
  }

  defaultView() {
    return Row(
      children: [
        Expanded(
            child: InkWell(
          child: JhText.cText('善语结善缘，恶言伤人心', ColorUtil.textLightGrey, 14),
          onTap: () {
            // defaultB = true;
            // setState(() {});
            showBottomDia();
          },
        )),
        InkWell(
          onTap: () {
            debugPrint('add_image');
          },
          child: const Icon(Icons.alternate_email_rounded, color: ColorUtil.grey),
        ),
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 6),
          child: InkWell(
            onTap: () {
              debugPrint('add_image');
            },
            child: const Icon(Icons.emoji_emotions_outlined, color: ColorUtil.grey),
          ),
        ),
        InkWell(
          onTap: () {
            debugPrint('add_image');
          },
          child: const Icon(Icons.add_circle_outline, color: ColorUtil.grey),
        )
      ],
    );
  }

  showBottomDia() async {
    currentPanelType = PanelType.none;
    await showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: ColorUtil.transparent,
      enableDrag: true,
      isDismissible: true,
      useSafeArea: true,
      builder: (context) => StatefulBuilder(
        builder: (BuildContext context, StateSetter setState) {
          // 获取键盘高度
          final keyboardHeight = MediaQuery.of(context).viewInsets.bottom;
          // 计算输入区域高度
          final inputHeight = 140.0; // 输入框和按钮区域的基础高度
          final atHeight = 60.0;
          // 计算表情面板高度
          final emojiHeight = currentPanelType == PanelType.emoji ? emojiPanelHeight : 0.0;
          // 计算总高度
          final bottomSheetHeight = keyboardHeight + inputHeight + atHeight + emojiHeight;
          // 限制最大高度为屏幕高度的60%
          final maxHeight = MediaQuery.of(context).size.height * 0.7;
          final finalHeight = bottomSheetHeight > maxHeight ? maxHeight : bottomSheetHeight;

          LogUtils.e('键盘高度: $keyboardHeight, 输入区域: $inputHeight, 表情面板: $emojiHeight, 总高度: $finalHeight');
          return SizedBox(
            height: finalHeight,
            child: inputView(setState),
          );
        },
      ),
    );
  }

  inputView(setState) {
    return Container(
      decoration: BoxDecoration(
        color: ColorUtil.white,
        borderRadius: BorderRadius.vertical(top: Radius.circular(8)),
      ),
      child: SingleChildScrollView(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            AnimatedOpacity(
              duration: const Duration(milliseconds: 200),
              curve: Curves.easeOut,
              opacity: currentPanelType == PanelType.at ? 1.0 : 0.0,
              child: Container(
                height: currentPanelType == PanelType.at ? 60 : 0,
                padding: const EdgeInsets.symmetric(horizontal: 10),
                margin: EdgeInsets.only(top: 10),
                child: ListView.builder(
                  scrollDirection: Axis.horizontal,
                  itemCount: 16,
                  itemBuilder: (context, index) {
                    return Container(
                      width: 50,
                      padding: const EdgeInsets.symmetric(horizontal: 5),
                      child: InkWell(
                        onTap: () {
                          insertText('@长笛琴人 ', 0);
                        },
                        child: Stack(
                          alignment: Alignment.center,
                          children: [
                            Positioned(
                              top: 0,
                              child: const CircleAvatar(
                                radius: 16,
                                backgroundColor: ColorUtil.white,
                                backgroundImage: NetworkImage(
                                  'https://i2.hdslb.com/bfs/face/de737cd746a96742c07ced6c213aa25cf0429d90.jpg@60w_60h_1c_1s_!web-avatar-comment.webp',
                                ),
                              ),
                            ),
                            Positioned(
                              bottom: 0,
                              child: SizedBox(
                                width: 40,
                                child: JhText.cText(
                                  '长笛琴人',
                                  ColorUtil.black45, 10.0,
                                  maxLines: 1,
                                  overflow: TextOverflow.ellipsis,
                                  textAlign: TextAlign.center,
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                ),
              ),
            ),
            Container(
              padding: const EdgeInsets.all(20),
              margin: const EdgeInsets.all(10),
              width: MediaQuery.of(context).size.width,
              decoration: BoxDecoration(
                color: const Color(0xfff1f2f3),
                borderRadius: BorderRadius.circular(10.0),
              ),
              child: ExtendedTextField(
                key: _key,
                autofocus: true,
                focusNode: focusNode,
                controller: textEditingController,
                maxLines: null,
                minLines: 1,
                textAlign: TextAlign.left,
                strutStyle: const StrutStyle(),
                specialTextSpanBuilder: MySpanBuilder(
                  showAtBackground: false,
                ),
                cursorHeight: 18,
                cursorColor: const Color.fromRGBO(238, 48, 88, 1),
                style: const TextStyle(
                  fontSize: 14,
                  fontFamily: 'Emojifont',
                  height: 1.2,
                  color: ColorUtil.contentText,
                ),
                decoration: InputDecoration(
                  isCollapsed: true,
                  border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12.0),
                    borderSide: BorderSide.none,
                  ),
                  fillColor: const Color.fromRGBO(243, 243, 243, 1),
                  filled: true,
                  hintStyle: const TextStyle(
                    fontSize: 14,
                    height: 1.2,
                    color: ColorUtil.textLightGrey,
                  ),
                  hintText: '善语结善缘，恶言伤人心',
                ),
              ),
            ),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              child: Row(
                children: [
                  _buildIconButton(
                    icon: Icons.alternate_email_rounded,
                    onTap: () {
                      _switchPanel(PanelType.at);
                      // setState(() {});
                    },
                  ),
                  const SizedBox(width: 12),
                  _buildIconButton(
                    icon: Icons.emoji_emotions_outlined,
                    onTap: () {
                      _switchPanel(PanelType.emoji);
                      // setState(() {});
                    },
                  ),
                  const SizedBox(width: 12),
                  _buildIconButton(
                    icon: Icons.add_circle_outline,
                    onTap: () {
                      debugPrint('add_image');
                    },
                  ),
                  const Spacer(),
                  TextButton(
                    onPressed: () {
                      // TODO: 实现发送功能
                    },
                    style: TextButton.styleFrom(
                      backgroundColor: ColorUtil.red,
                      foregroundColor: ColorUtil.white,
                      minimumSize: const Size(60, 32),
                      padding: const EdgeInsets.symmetric(horizontal: 16),
                      textStyle: const TextStyle(
                        fontSize: 14,
                        fontWeight: FontWeight.w500,
                      ),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(16.0),
                      ),
                    ),
                    child: JhText.cText('发送', ColorUtil.white, 14.0, fontWeight: FontWeight.w500),
                  ),
                ],
              ),
            ),
            AnimatedOpacity(
              duration: const Duration(milliseconds: 300),
              curve: Curves.easeInOut,
              opacity: currentPanelType == PanelType.emoji ? 1 : 0,
              child: Container(
                height: currentPanelType == PanelType.emoji ? emojiPanelHeight : 0,
                decoration: const BoxDecoration(
                  color: ColorUtil.white,
                  borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
                ),
                child: buildEmojiGird(),
              ),
            ),
          ],
        ),
      ),
    );
  }

  inputView1() {
    return Container(
      decoration: BoxDecoration(
        color: ColorUtil.white,
        borderRadius: BorderRadius.vertical(top: Radius.circular(8)),
      ),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          AnimatedOpacity(
            duration: const Duration(milliseconds: 200),
            curve: Curves.easeOut,
            opacity: currentPanelType == PanelType.at ? 1.0 : 0.0,
            child: Container(
              height: currentPanelType == PanelType.at ? 60 : 0,
              padding: const EdgeInsets.symmetric(horizontal: 10),
              margin: EdgeInsets.only(top: 10),
              child: ListView.builder(
                scrollDirection: Axis.horizontal,
                itemCount: 16,
                itemBuilder: (context, index) {
                  return Container(
                    width: 50,
                    padding: const EdgeInsets.symmetric(horizontal: 5),
                    child: InkWell(
                      onTap: () {
                        insertText('@长笛琴人 ', 0);
                      },
                      child: Stack(
                        alignment: Alignment.center,
                        children: [
                          Positioned(
                            top: 0,
                            child: const CircleAvatar(
                              radius: 16,
                              backgroundColor: ColorUtil.white,
                              backgroundImage: NetworkImage(
                                'https://i2.hdslb.com/bfs/face/de737cd746a96742c07ced6c213aa25cf0429d90.jpg@60w_60h_1c_1s_!web-avatar-comment.webp',
                              ),
                            ),
                          ),
                          Positioned(
                            bottom: 0,
                            child: SizedBox(
                              width: 40,
                              child: JhText.cText(
                                '长笛琴人',
                                ColorUtil.black45, 10.0,
                                maxLines: 1,
                                overflow: TextOverflow.ellipsis,
                                textAlign: TextAlign.center,
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  );
                },
              ),
            ),
          ),
          Container(
            padding: const EdgeInsets.all(20),
            margin: const EdgeInsets.all(10),
            width: MediaQuery.of(context).size.width,
            decoration: BoxDecoration(
              color: const Color(0xfff1f2f3),
              borderRadius: BorderRadius.circular(10.0),
            ),
            child: ExtendedTextField(
              key: _key,
              autofocus: false,
              focusNode: focusNode,
              controller: textEditingController,
              maxLines: null,
              minLines: 1,
              textAlign: TextAlign.left,
              strutStyle: const StrutStyle(),
              specialTextSpanBuilder: MySpanBuilder(
                showAtBackground: false,
              ),
              cursorHeight: 18,
              cursorColor: const Color.fromRGBO(238, 48, 88, 1),
              style: const TextStyle(
                fontSize: 14,
                fontFamily: 'Emojifont',
                height: 1.2,
                color: ColorUtil.contentText,
              ),
              decoration: InputDecoration(
                isCollapsed: true,
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12.0),
                  borderSide: BorderSide.none,
                ),
                fillColor: const Color.fromRGBO(243, 243, 243, 1),
                filled: true,
                hintStyle: const TextStyle(
                  fontSize: 14,
                  height: 1.2,
                  color: ColorUtil.textLightGrey,
                ),
                hintText: '善语结善缘，恶言伤人心',
              ),
            ),
          ),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            child: Row(
              children: [
                _buildIconButton(
                  icon: Icons.alternate_email_rounded,
                  onTap: () {
                    _switchPanel(PanelType.at);
                    // setState(() {});
                  },
                ),
                const SizedBox(width: 12),
                _buildIconButton(
                  icon: Icons.emoji_emotions_outlined,
                  onTap: () {
                    _switchPanel(PanelType.emoji);
                    // setState(() {});
                  },
                ),
                const SizedBox(width: 12),
                _buildIconButton(
                  icon: Icons.add_circle_outline,
                  onTap: () {
                    debugPrint('add_image');
                  },
                ),
                const Spacer(),
                TextButton(
                  onPressed: () {
                    // TODO: 实现发送功能
                  },
                  style: TextButton.styleFrom(
                    backgroundColor: ColorUtil.red,
                    foregroundColor: ColorUtil.white,
                    minimumSize: const Size(60, 32),
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                    textStyle: const TextStyle(
                      fontSize: 14,
                      fontWeight: FontWeight.w500,
                    ),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(16.0),
                    ),
                  ),
                  child: JhText.cText('发送', ColorUtil.white, 14.0, fontWeight: FontWeight.w500),
                ),
              ],
            ),
          ),
          AnimatedOpacity(
            duration: const Duration(milliseconds: 300),
            curve: Curves.easeInOut,
            opacity: currentPanelType == PanelType.emoji ? 1 : 0,
            child: Container(
              height: currentPanelType == PanelType.emoji ? emojiPanelHeight : 0,
              decoration: const BoxDecoration(
                color: ColorUtil.white,
                borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
              ),
              child: buildEmojiGird(),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildIconButton({required IconData icon, required VoidCallback onTap}) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(20),
      child: Container(
        padding: const EdgeInsets.all(8),
        child: Icon(icon, color: ColorUtil.grey, size: 24),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Column();
  }

  void insertText(String text, int type) {
    final TextEditingValue value = textEditingController.value;
    final int start = value.selection.baseOffset;
    int end = value.selection.extentOffset;
    if (value.selection.isValid) {
      String newText = '';
      if (value.selection.isCollapsed) {
        if (end > 0) {
          newText += value.text.substring(0, end);
        }
        newText += text;
        if (value.text.length > end) {
          newText += value.text.substring(end, value.text.length);
        }
      } else {
        newText = value.text.replaceRange(start, end, text);
        end = start;
      }
      textEditingController.value = value.copyWith(
          text: newText + (type == 0 ? ' ' : ''),
          selection:
              value.selection.copyWith(baseOffset: end + text.length + (type == 0 ? 1 : 0), extentOffset: end + text.length + (type == 0 ? 1 : 0)));
    } else {
      textEditingController.value = TextEditingValue(text: text, selection: TextSelection.fromPosition(TextPosition(offset: text.length)));
    }

    SchedulerBinding.instance.addPostFrameCallback((Duration timeStamp) {
      _key.currentState?.bringIntoView(textEditingController.selection.base);
    });
  }

  Widget buildEmojiGird() {
    return GridView.builder(
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount: 8, crossAxisSpacing: 10.0, mainAxisSpacing: 10.0),
      itemBuilder: (BuildContext context, int index) {
        return GestureDetector(
          behavior: HitTestBehavior.translucent,
          onTap: () {
            insertText(emojis[index], 1);
          },
          child: Text(
            emojis[index],
            style: const TextStyle(fontFamily: 'Emojifont', fontSize: 28),
          ),
        );
      },
      itemCount: emojis.length,
      padding: const EdgeInsets.all(5.0),
    );
  }
}
