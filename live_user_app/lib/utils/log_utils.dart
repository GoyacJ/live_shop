import 'package:logger/logger.dart';

class LogUtils {
  static final Logger _logger = Logger(
    printer: PrettyPrinter(
      methodCount: 0, // 显示的方法调用数量
      lineLength: 120, // 每行长度
      colors: true, // 是否使用颜色
      printEmojis: true, // 是否打印表情
      printTime: true, // 是否打印时间
    ),
  );

  // 是否开启调试模式
  static bool _debugMode = true;

  // 初始化日志工具
  static void init({bool debugMode = true}) {
    _debugMode = debugMode;
  }

  // Trace级别日志（替代原来的Verbose），显示所有日志
  static void t(dynamic message, {dynamic error, StackTrace? stackTrace}) {
    if (_debugMode) {
      _logger.t(message, error: error, stackTrace: stackTrace);
    }
  }

  // Debug级别日志，调试信息
  static void d(dynamic message, {dynamic error, StackTrace? stackTrace}) {
    if (_debugMode) {
      _logger.d(message, error: error, stackTrace: stackTrace);
    }
  }

  // Info级别日志，正常信息
  static void i(dynamic message, {dynamic error, StackTrace? stackTrace}) {
    if (_debugMode) {
      _logger.i(message, error: error, stackTrace: stackTrace);
    }
  }

  // Warning级别日志，警告信息
  static void w(dynamic message, {dynamic error, StackTrace? stackTrace}) {
    if (_debugMode) {
      _logger.w(message, error: error, stackTrace: stackTrace);
    }
  }

  // Error级别日志，错误信息
  static void e(dynamic message, {dynamic error, StackTrace? stackTrace}) {
    if (_debugMode) {
      _logger.e(message, error: error, stackTrace: stackTrace);
    }
  }

  // WTF级别日志，严重错误信息
  static void wtf(dynamic message, {dynamic error, StackTrace? stackTrace}) {
    if (_debugMode) {
      _logger.f(message, error: error, stackTrace: stackTrace);
    }
  }
}