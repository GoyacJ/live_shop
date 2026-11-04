import 'package:shared_preferences/shared_preferences.dart';

/// SharedPreferences工具类
/// 提供简单的键值对存储功能
class SpUtil {
  static SharedPreferences? _prefs;
  
  /// 初始化SharedPreferences
  static Future<void> init() async {
    _prefs = await SharedPreferences.getInstance();
  }

  /// 检查是否已初始化
  static void _checkInit() {
    assert(_prefs != null, 'SpUtil未初始化，请先调用SpUtil.init()');
  }

  /// 保存数据
  static Future<bool> setData<T>(String key, T value) async {
    _checkInit();
    
    if (value is String) {
      return await _prefs!.setString(key, value);
    } else if (value is int) {
      return await _prefs!.setInt(key, value);
    } else if (value is double) {
      return await _prefs!.setDouble(key, value);
    } else if (value is bool) {
      return await _prefs!.setBool(key, value);
    } else if (value is List<String>) {
      return await _prefs!.setStringList(key, value);
    } else {
      throw ArgumentError('不支持的数据类型: ${value.runtimeType}');
    }
  }

  /// 获取数据
  static T? getData<T>(String key, {T? defaultValue}) {
    _checkInit();
    
    if (T == String) {
      return (_prefs!.getString(key) ?? defaultValue) as T?;
    } else if (T == int) {
      return (_prefs!.getInt(key) ?? defaultValue) as T?;
    } else if (T == double) {
      return (_prefs!.getDouble(key) ?? defaultValue) as T?;
    } else if (T == bool) {
      return (_prefs!.getBool(key) ?? defaultValue) as T?;
    } else if (T == List<String>) {
      return (_prefs!.getStringList(key) ?? defaultValue) as T?;
    } else {
      throw ArgumentError('不支持的数据类型: $T');
    }
  }

  /// 删除指定数据
  static Future<bool> remove(String key) async {
    _checkInit();
    return await _prefs!.remove(key);
  }

  /// 清除所有数据
  static Future<bool> clear() async {
    _checkInit();
    return await _prefs!.clear();
  }

  /// 获取所有键
  static Set<String> getKeys() {
    _checkInit();
    return _prefs!.getKeys();
  }

  /// 判断是否包含某个键
  static bool containsKey(String key) {
    _checkInit();
    return _prefs!.containsKey(key);
  }
}
