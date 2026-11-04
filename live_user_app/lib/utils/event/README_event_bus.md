# EventBusUtil 事件总线工具类

基于 `event_bus_plus: ^0.7.0` 封装的事件总线工具类，用于应用内的事件发布和订阅。

## 功能特性

- ✅ 单例模式，全局统一管理
- ✅ 类型安全的事件订阅
- ✅ 自动订阅管理，防止内存泄漏
- ✅ 支持自定义事件类型
- ✅ 预定义常用事件类型
- ✅ 完整的错误处理

## 快速开始

### 1. 基本使用

```dart
import 'package:your_app/utils/event_bus_util.dart';

// 获取事件总线实例
final eventBus = EventBusUtil();

// 发布事件
eventBus.emit(SimpleEvent('custom_event', data: {'key': 'value'}));

// 订阅事件
String subscriptionId = eventBus.on<SimpleEvent>((event) {
  print('收到事件: ${event.type}');
  print('事件数据: ${event.data}');
});

// 取消订阅
eventBus.cancelSubscription(subscriptionId);
```

### 2. 使用预定义事件

```dart
// 发布用户登录事件
EventBusExample.emitUserLogin('user123', '张三');

// 发布网络状态变化事件
EventBusExample.emitNetworkChanged(true, connectionType: 'wifi');

// 发布主题切换事件
EventBusExample.emitThemeChanged('dark');
```

### 3. 在 Widget 中使用

```dart
class MyWidget extends StatefulWidget {
  @override
  _MyWidgetState createState() => _MyWidgetState();
}

class _MyWidgetState extends State<MyWidget> {
  String? _subscriptionId;

  @override
  void initState() {
    super.initState();
    
    // 监听数据刷新事件
    _subscriptionId = EventBusUtil().on<SimpleEvent>((event) {
      if (event.type == AppEvents.dataRefresh) {
        setState(() {
          // 刷新数据
        });
      }
    });
  }

  @override
  void dispose() {
    // 取消订阅，防止内存泄漏
    if (_subscriptionId != null) {
      EventBusUtil().cancelSubscription(_subscriptionId!);
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Container();
  }
}
```

## 预定义事件类型

### AppEvents 常量

```dart
class AppEvents {
  static const String userLogin = 'user_login';      // 用户登录
  static const String userLogout = 'user_logout';    // 用户登出
  static const String networkChanged = 'network_changed'; // 网络状态变化
  static const String themeChanged = 'theme_changed';     // 主题切换
  static const String languageChanged = 'language_changed'; // 语言切换
  static const String dataRefresh = 'data_refresh';       // 数据刷新
}
```

### 预定义事件类

#### UserEvent - 用户事件
```dart
UserEvent(String type, String userId, {String? username, Map<String, dynamic>? data})
```

#### NetworkEvent - 网络事件
```dart
NetworkEvent(bool isConnected, {String? connectionType})
```

#### ThemeEvent - 主题事件
```dart
ThemeEvent(String themeMode) // themeMode: 'light', 'dark', 'system'
```

#### SimpleEvent - 简单事件
```dart
SimpleEvent(String type, {Map<String, dynamic>? data})
```

## API 参考

### EventBusUtil 类

#### 方法

- `emit(dynamic event)` - 发布事件
- `on<T extends AppEvent>(callback, {onError, cancelOnError})` - 订阅事件
- `cancelSubscription(String subscriptionId)` - 取消指定订阅
- `cancelAllSubscriptions()` - 取消所有订阅
- `destroy()` - 销毁事件总线

#### 属性

- `activeSubscriptionCount` - 当前活跃订阅数量

### EventBusExample 类

提供便捷的静态方法：

- `initEventListeners()` - 初始化事件监听
- `emitUserLogin(userId, username)` - 发布用户登录事件
- `emitUserLogout(userId)` - 发布用户登出事件
- `emitNetworkChanged(isConnected, connectionType)` - 发布网络状态变化事件
- `emitThemeChanged(themeMode)` - 发布主题切换事件
- `emitCustomEvent(eventType, data)` - 发布自定义事件
- `dispose()` - 清理所有事件监听

## 最佳实践

### 1. 内存管理

```dart
// ✅ 正确：在 dispose 中取消订阅
@override
void dispose() {
  if (_subscriptionId != null) {
    EventBusUtil().cancelSubscription(_subscriptionId!);
  }
  super.dispose();
}

// ❌ 错误：忘记取消订阅会导致内存泄漏
```

### 2. 事件类型安全

```dart
// ✅ 正确：使用类型安全的事件订阅
eventBus.on<UserEvent>((event) {
  // event 是 UserEvent 类型，有类型安全
  print(event.userId);
});

// ❌ 错误：使用 dynamic 类型
eventBus.on<dynamic>((event) {
  // event 是 dynamic 类型，没有类型安全
});
```

### 3. 错误处理

```dart
// ✅ 正确：添加错误处理
String subscriptionId = eventBus.on<UserEvent>(
  (event) {
    // 处理事件
  },
  onError: (error) {
    print('事件处理错误: $error');
  },
  cancelOnError: false, // 错误时不取消订阅
);
```

### 4. 应用初始化

```dart
void main() {
  // 在应用启动时初始化事件监听
  EventBusExample.initEventListeners();
  
  runApp(MyApp());
}
```

## 注意事项

1. **内存泄漏**：务必在 Widget 的 `dispose` 方法中取消订阅
2. **事件类型**：建议使用预定义的事件类型，确保类型安全
3. **错误处理**：为重要的事件订阅添加错误处理
4. **性能考虑**：避免在事件处理中执行耗时操作
5. **调试**：使用 `activeSubscriptionCount` 监控订阅数量

## 示例场景

### 场景1：用户登录后刷新多个页面

```dart
// 在登录成功后发布事件
EventBusExample.emitUserLogin('user123', '张三');

// 在需要刷新的页面中监听
eventBus.on<UserEvent>((event) {
  if (event.type == AppEvents.userLogin) {
    // 刷新页面数据
    refreshPageData();
  }
});
```

### 场景2：网络状态变化处理

```dart
// 监听网络状态变化
eventBus.on<NetworkEvent>((event) {
  if (event.isConnected) {
    // 网络恢复，重新加载数据
    loadData();
  } else {
    // 网络断开，显示离线提示
    showOfflineMessage();
  }
});
```

### 场景3：主题切换

```dart
// 发布主题切换事件
EventBusExample.emitThemeChanged('dark');

// 在主题相关的 Widget 中监听
eventBus.on<ThemeEvent>((event) {
  // 更新主题
  updateTheme(event.themeMode);
});
``` 