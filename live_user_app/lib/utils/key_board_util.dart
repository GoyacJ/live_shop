import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class KeyBoardUtil{

  ///隐藏软键盘
  static void hideSoftKeyBoard(){
    FocusManager.instance.primaryFocus?.unfocus();
  }

  ///仅隐藏软键盘，不丢失焦点
  static void hideSoftKeyBoardOnly(){
    SystemChannels.textInput.invokeMethod('TextInput.hide');
  }


}