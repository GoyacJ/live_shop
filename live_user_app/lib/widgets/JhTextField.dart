import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

import '../utils/key_board_util.dart';
import '../utils/color_util.dart';

/**
 *Created by Jcy
 *on 2023/8/15
 * 输入框
 */
typedef _InputCallBack = void Function(String value);
typedef _InputSubmitted = void Function(String value);

class JhTextField extends StatefulWidget {
  final String? text;
  final String? hintText;
  final Color? hintColor;
  final double hintTextSize;
  final FontWeight hintTextFontWeight;
  final Color? textColor;
  final double textSize;
  final FontWeight textFontWeight;
  final String? labelText; //top提示文字
  final TextEditingController? controller;
  final TextInputType? keyboardType;
  final bool? isOnclick;
  final Color? cursorColor;
  final TextInputAction? textInputAction; //键盘搜索
  final FocusNode? focusNode;
  final bool? isPwd; //是否是密码，默认不是
  final Widget? leftWidget; //左侧widget ，默认隐藏
  final Widget? rightWidget; //右侧widget ，默认隐藏
  final int? maxLines;
  final int? maxLength; //最大长度，默认20
  final bool rightNum; //是否显示右下角计数
  final bool? isShowDeleteBtn; //是否显示右侧删除按钮，默认不显示
  final List<TextInputFormatter>? inputFormatters;
  final _InputCallBack? inputCallBack;
  final _InputSubmitted? inputSubmitted; //输入完成回调
  final String? pwdOpen; //自定义密码图片路径 睁眼
  final String? pwdClose; //自定义密码图片路径 闭眼
  final bool? isDense; //是否紧凑显示，默认false
  final Color? borderSideColor; //设置颜色显示下划线，不设置不显示
  final EdgeInsets? contentPadding; //设置下划线和文字的距离
  final bool? autofocus;
  final TextAlign textAlign;
  final String obscuringCharacter;

  const JhTextField(
      {Key? key,
      this.text = '',
      this.keyboardType = TextInputType.text,
      this.textInputAction = TextInputAction.done,
      this.isOnclick = true,
      this.cursorColor = ColorUtil.cursorColor,
      this.hintColor,
      this.hintTextSize = 28,
      this.hintTextFontWeight = FontWeight.w400,
      this.hintText = '',
      this.textColor = ColorUtil.textPrimary,
      this.textSize = 28,
      this.textFontWeight = FontWeight.w400,
      this.labelText,
      this.controller,
      this.focusNode,
      this.isPwd = false,
      this.leftWidget,
      this.rightWidget,
      this.maxLines = 1,
      this.maxLength = 20000,
      this.rightNum = false,
      this.isShowDeleteBtn = false,
      this.inputFormatters,
      this.inputCallBack,
      this.inputSubmitted,
      this.pwdOpen,
      this.pwdClose,
      this.isDense = false,
      this.borderSideColor,
      this.contentPadding = const EdgeInsets.symmetric(vertical: 5),
      this.autofocus = false,
      this.textAlign = TextAlign.left,
      this.obscuringCharacter = '•'})
      : super(key: key);

  @override
  _JhTextFieldState createState() => _JhTextFieldState();
}

class _JhTextFieldState extends State<JhTextField> {
  TextEditingController? _textController;
  FocusNode? _focusNode;
  bool? _isShowDelete;
  bool? _isHideenPwdBtn; //是否隐藏 右侧密码明文切换按钮 ，密码样式才显示（isPwd =true），
  bool? _pwdShow; //控制密码 明文切换
  Widget? _pwdImg; //自定义密码图片

  @override
  void initState() {
    // TODO: implement initState
    super.initState();

    _textController = widget.controller ?? TextEditingController();
    _textController!.text = widget.text!;
    _focusNode = widget.focusNode ?? FocusNode();
    _isHideenPwdBtn = !widget.isPwd!;
    _pwdShow = widget.isPwd;

    _isShowDelete = _focusNode!.hasFocus && _textController!.text.isNotEmpty;
    _textController!.addListener(() {
      setState(() {
        _isShowDelete =
            _textController!.text.isNotEmpty && _focusNode!.hasFocus;
      });
    });
    _focusNode!.addListener(() {
      setState(() {
        _isShowDelete =
            _textController!.text.isNotEmpty && _focusNode!.hasFocus;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    if (widget.pwdOpen != null && widget.pwdClose != null) {
      if (widget.pwdOpen!.isNotEmpty && widget.pwdClose!.isNotEmpty) {
        _pwdImg = _pwdShow!
            ? ImageIcon(AssetImage(widget.pwdClose!))
            : ImageIcon(AssetImage(widget.pwdOpen!));
      } else {
        _pwdImg = Icon(_pwdShow! ? Icons.visibility_off : Icons.visibility);
      }
    } else {
      _pwdImg = Icon(_pwdShow! ? Icons.visibility_off : Icons.visibility);
//      _pwdImg = _pwdShow?Image.asset("assets/images/ic_pwd_close.png",width: 18.0,):Image.asset("assets/images/ic_pwd_open.png",width: 18.0,);
//      _pwdImg = _pwdShow?ImageIcon(AssetImage("assets/images/ic_pwd_close.png")):ImageIcon(AssetImage("assets/images/ic_pwd_open.png")) ;
    }

    return Stack(
      alignment: Alignment.centerRight,
      children: [
        Row(
          children: [
            Visibility(
                visible: widget.leftWidget == null ? false : true,
                child: Container(
                  padding: EdgeInsets.only(right: 15.w),
                  child: widget.leftWidget,
                )),
            Expanded(
                child: TextField(
              maxLength: widget.rightNum ? widget.maxLength : null,
              focusNode: _focusNode,
              cursorColor: widget.cursorColor ?? ColorUtil.cursorColor,
              autofocus: widget.autofocus!,
              controller: _textController,
              keyboardType: widget.keyboardType,
              textInputAction: widget.textInputAction,
              enabled: widget.isOnclick,
              maxLines: widget.maxLines,
              textAlign: widget.textAlign,
              style: TextStyle(
                  fontSize: widget.textSize.sp,
                  color: widget.textColor ?? ColorUtil.textPrimary,
                  fontWeight: widget.textFontWeight),
              inputFormatters: widget.inputFormatters ??
                  [LengthLimitingTextInputFormatter(widget.maxLength)],
              decoration: InputDecoration(
                isCollapsed: true,
                counterStyle: TextStyle(fontSize: 28.sp, color: ColorUtil.textGrey),
                contentPadding: widget.contentPadding,
                labelText: widget.labelText,
                hintText: widget.hintText,
                hintStyle: TextStyle(
                  fontSize: widget.hintTextSize.sp,
                  color: widget.hintColor ?? ColorUtil.textHint,
                  fontWeight: widget.hintTextFontWeight,
                ),
                isDense: widget.isDense,
                enabledBorder: UnderlineInputBorder(
                  // 不是焦点的时候颜色
                  borderSide: widget.borderSideColor == null
                      ? BorderSide.none
                      : BorderSide(color: widget.borderSideColor!),
                ),
                focusedBorder: UnderlineInputBorder(
                  // 焦点集中的时候颜色
                  borderSide: widget.borderSideColor == null
                      ? BorderSide.none
                      : BorderSide(color: widget.borderSideColor!),
                ),
              ),
              obscureText: _pwdShow!,
              obscuringCharacter: widget.obscuringCharacter,
              onTapOutside: (e) {
                KeyBoardUtil.hideSoftKeyBoard();
              },
              onChanged: (value) {
                if (widget.inputCallBack != null) {
                  widget.inputCallBack!(value);
                }
              },
              onSubmitted: (value) {
                if (widget.inputSubmitted != null) {
                  widget.inputSubmitted!(value);
                }
              },
            )),
            Visibility(
                visible: widget.rightWidget == null ? false : true,
                child: Container(
                  padding: EdgeInsets.only(left: 15.w),
                  child: widget.rightWidget,
                )),
          ],
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: <Widget>[
            _isShowDelete! && widget.isShowDeleteBtn!
                ? GestureDetector(
                    //删除按钮
                    onTap: () {
                      _textController!.clear();
                      if (widget.inputCallBack != null) {
                        widget.inputCallBack!("");
                      }
                    },
                    child: Container(
                      padding: EdgeInsets.symmetric(horizontal: 5.w),
                      child: Icon(
                        Icons.cancel,
                        size: 22.w,
                        color: ColorUtil.grey400,
                      ),
                    ),
                  )
                : Container(),
            !_isHideenPwdBtn!
                ? GestureDetector(
                    //明文切换按钮
                    onTap: () {
                      setState(() {
                        _pwdShow = !_pwdShow!;
                      });
                    },
                    child: Container(
                      padding: EdgeInsets.only(left: 5.w, right: 15.w),
                      child: _pwdImg,
                    ),
                  )
                : Container()
          ],
        ),
      ],
    );
  }
}
