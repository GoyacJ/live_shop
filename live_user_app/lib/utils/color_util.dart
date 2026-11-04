import 'package:flutter/material.dart';

/// 颜色工具类
/// 项目中所有使用的颜色都应该在此定义
/// 避免在代码中硬编码颜色值
class ColorUtil {
  // 基础颜色
  static Color imageBgColor = Colors.grey.shade200;
  static const primaryColor = Color(0xFFB22222);     // 主题色/品牌色
  static const backgroundColor = Color(0xFFE3E3E3);  // 背景色
  static const white = Color(0xFFFFFFFF);           // 白色
  static const black = Color(0xFF000000);           // 黑色
  static const transparent = Colors.transparent;    // 透明色

  // 灰度颜色系列
  static const grey = Colors.grey;                  // 灰色
  static const grey50 = Color(0xFFFAFAFA);          // 灰色 50
  static const grey100 = Color(0xFFF5F5F5);         // 灰色 100
  static const grey200 = Color(0xFFEEEEEE);         // 灰色 200
  static const grey300 = Color(0xFFE0E0E0);         // 灰色 300
  static const grey400 = Color(0xFFBDBDBD);         // 灰色 400
  static const grey500 = Color(0xFF9E9E9E);         // 灰色 500
  static const grey600 = Color(0xFF757575);         // 灰色 600
  static const grey700 = Color(0xFF616161);         // 灰色 700
  static const grey800 = Color(0xFF424242);         // 灰色 800
  static const grey900 = Color(0xFF212121);         // 灰色 900
  
  // 黑色不同透明度
  static const black50 = Color(0x50000000);         // 黑色 50% 透明度
  static const black80 = Color(0x80000000);         // 黑色 80% 透明度
  static const black87 = Color(0xDE000000);         // 黑色 87% 透明度
  static const black54 = Color(0x8A000000);         // 黑色 54% 透明度
  static const black45 = Color(0x73000000);         // 黑色 45% 透明度
  static const black38 = Color(0x61000000);         // 黑色 38% 透明度
  static const black26 = Color(0x42000000);         // 黑色 26% 透明度
  static const black12 = Color(0x1F000000);         // 黑色 12% 透明度
  
  // 主要文本颜色
  static const textPrimary = Color(0xFF2B2B2B);     // 主要文本颜色
  static const textSecondary = Color(0xFF666666);   // 次要文本颜色
  static const textHint = Color(0xFFCCCCCC);        // 提示文本颜色
  static const textGrey = Color(0xFF999999);        // 灰色文本
  static const textDarkGrey = Color(0xFF3D3D3D);    // 深灰色文本
  static const textLightGrey = Color(0xFF888888);   // 浅灰色文本
  static const textPlaceholder = Color(0xFFAEAEB2); // 占位符文本颜色
  
  // 功能颜色
  static const blue = Colors.blue;                  // 蓝色
  static const red = Colors.red;                    // 红色
  static const green = Colors.green;                // 绿色
  static const orange = Colors.orange;              // 橙色
  static const greenAccent = Colors.greenAccent;    // 鲜亮的黄绿色
  static const blueAccent  = Colors.blueAccent;     // 鲜亮的天蓝色
  static const redAccent   = Colors.redAccent;      // 鲜亮的玫红色

  // 特定功能颜色
  static const priceRed = Color(0xFFFF0000);        // 价格红色
  static const searchBg = Color(0xFFF2F2F2);        // 搜索框背景色
  static const dividerColor = Color(0xFFF1F2F3);    // 分割线颜色
  static const cardBg = Color(0xFFF3F3F3);          // 卡片背景色
  static const inputBg = Color(0xFFF4F4F4);         // 输入框背景色
  
  // 渐变色起止点
  static const gradientPink = Color(0xFFFF4BB4);    // 粉色渐变起点
  static const gradientPurple = Color(0xFFCA47D9);  // 紫色渐变终点
  
  // 特殊颜色
  static const cursorColor = Color(0xE5000000);     // 光标颜色
  static const liveBlue = Color(0xFF02E0FD);        // 直播蓝
  static const likeRed1 = Color(0xFFEF6F6F);        // 点赞红色1
  static const likeRed2 = Color(0xFFF03E3E);        // 点赞红色2
  static const successGreen = Color(0xFF07C160);    // 成功绿色
  static const warningYellow = Color(0xFFF7C04A);   // 警告黄色
  static const infoBlue = Color(0xFF02B6FD);        // 信息蓝色
  static const dangerRed = Color(0xFFFF004A);       // 危险红色
  static const followGreen = Color(0xFF6AD192);     // 关注绿色
  static const brightRed = Color(0xFFFC3D42);       // 亮红色
  static const gradientRed1 = Color(0xFFFF474C);    // 渐变红色1
  static const gradientRed2 = Color(0xFFFF5A5D);    // 渐变红色2
  static const scrollBg = Color(0x14FA5741);        // 滚动条背景
  static const tagRed = Color(0xFFFA5741);          // 标签红色
  static const contentText = Color(0xFF333333);     // 内容文本色
  static const followGrey = Color(0xFF707070);      // 关注灰色
  static const systemBlue = Color(0xFF0091EA);      // 系统蓝色

  // 获取主题相关颜色
  static Color getBackgroundColor(BuildContext context) {
    return Theme.of(context).brightness == Brightness.light 
        ? white 
        : black;
  }
  
  // 基于当前背景色获取适当的文本颜色
  static Color getTextColor(BuildContext context) {
    return Theme.of(context).brightness == Brightness.light 
        ? textPrimary 
        : white;
  }
  
  // 获取带透明度的颜色
  static Color getColorWithOpacity(Color color, double opacity) {
    return color.withOpacity(opacity);
  }
  
  // 由十六进制字符串创建颜色
  static Color fromHex(String hexString) {
    final buffer = StringBuffer();
    if (hexString.length == 6 || hexString.length == 7) buffer.write('ff');
    buffer.write(hexString.replaceFirst('#', ''));
    return Color(int.parse(buffer.toString(), radix: 16));
  }
}