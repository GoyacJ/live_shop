
import 'package:flutter/cupertino.dart';
import 'package:intl/intl.dart';

class CustomDateUtil {

  /// 获取当前日期月份的第一天
  static DateTime getFirstDayOfMonth(DateTime date) {
    // 获取当前的年份和月份
    int year = date.year;
    int month = date.month;

    // 返回当前月的第一天
    return DateTime(year, month, 1);
  }

  /// 获取当前日期月份的最后一天
  static DateTime getLastDayOfMonth(DateTime date) {
    // 获取当前的年份和月份
    int year = date.year;
    int month = date.month;

    // 如果是12月，则设置为下一年的1月
    if (month == 12) {
      year++;
      month = 1;
    } else {
      month++;
    }

    // 获取下一个月的第一天
    DateTime firstDayOfNextMonth = DateTime(year, month, 1,23,59,59,999,999);
    // 返回下一个月的第一天减去1天，即为当前月的最后一天
    return firstDayOfNextMonth.subtract(const Duration(days: 1));
  }

  ///格式化日期，返回国际化的月份年份
  ///例如：2021-10-01 -> Oct 2021
  static String formatMonthYear(BuildContext context,DateTime date) {
    var string = Localizations.localeOf(context).toString();
    ///MMM表示月份的缩写如Oct,Jan,Feb等，MMMM表示月份全称，yyyy标识年份
    final formatter = DateFormat('MMM yyyy', string);
    return formatter.format(date);
  }

  ///将日期格式化为30 Aug 2023 17:23这种格式
  static String formatDateTimeddMMyyyyHHmm(BuildContext context,DateTime date) {
    var string = Localizations.localeOf(context).toString();
    final formatter = DateFormat('dd MMM yyyy HH:mm', string);
    return formatter.format(date);
  }

  ///通用的日期格式化方法
  static String formatDateCommon(BuildContext context,DateTime date,String format) {
    var string = Localizations.localeOf(context).toString();
    final formatter = DateFormat(format, string);
    return formatter.format(date);
  }

  static Duration parseDuration(String input) {
    final parts = input.split(':');
    if (parts.length != 3) {
      throw FormatException('Invalid Duration format: $input');
    }

    final hours = int.parse(parts[0]);
    final minutes = int.parse(parts[1]);

    // 处理 "SS.microseconds"
    final secParts = parts[2].split('.');
    final seconds = int.parse(secParts[0]);
    final micros = secParts.length > 1 ? int.parse(secParts[1].padRight(6, '0')) : 0;

    return Duration(
      hours: hours,
      minutes: minutes,
      seconds: seconds,
      microseconds: micros,
    );
  }


}
