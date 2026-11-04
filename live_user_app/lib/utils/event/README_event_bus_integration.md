# BaseController 事件监听功能

`BaseController` 现已集成 EventBus 功能，支持自动监听登录、登出等事件并刷新数据。

## 🚀 功能特性

- ✅ **自动事件监听** - 控制器创建时自动注册事件监听
- ✅ **自动内存管理** - 控制器销毁时自动取消所有订阅
- ✅ **灵活配置** - 可选择监听的事件类型
- ✅ **生命周期安全** - 确保事件处理时控制器仍然有效
- ✅ **默认行为** - 提供合理的默认事件处理逻辑
- ✅ **可扩展** - 子类可以重写事件处理方法

## 📋 支持的事件类型

### 默认监听的事件
- `AppEvents.userLogin` - 用户登录事件
- `AppEvents.userLogout` - 用户登出事件  
- `AppEvents.dataRefresh` - 数据刷新事件

### 可选监听的事件
- `AppEvents.networkChanged` - 网络状态变化事件
- `AppEvents.themeChanged` - 主题切换事件

## 🛠️ 使用方法

### 1. 基本使用（使用默认配置）

```dart
class MyController extends BaseController {
  @override
  Future<void> initData() async {
    // 你的数据加载逻辑
    final data = await api.getData();
    // 处理数据...
  }
}
```

**默认行为：**
- 自动监听登录、登出、数据刷新事件
- 收到事件时自动调用 `refreshData()` 刷新数据
- 控制器销毁时自动清理事件监听

### 2. 自定义监听的事件类型

```dart
class UserSensitiveController extends BaseController {
  /// 只监听用户相关事件
  @override
  List<String> get eventTypesToListen => [
    AppEvents.userLogin,
    AppEvents.userLogout,
  ];

  @override
  Future<void> initData() async {
    // 数据加载逻辑
  }
}
```

### 3. 自定义事件处理逻辑

```dart
class CustomController extends BaseController {
  final RxString currentUser = ''.obs;

  @override
  void onUserLogin(UserEvent event) {
    super.onUserLogin(event); // 调用父类方法（刷新数据）
    
    // 自定义登录处理逻辑
    currentUser.value = event.username ?? '未知用户';
    
    Get.snackbar(
      '欢迎回来',
      '用户 ${event.username} 已登录',
    );
  }

  @override
  void onUserLogout(UserEvent event) {
    super.onUserLogout(event); // 调用父类方法（刷新数据）
    
    // 自定义登出处理逻辑
    currentUser.value = '';
    
    Get.snackbar(
      '已登出',
      '用户已安全登出',
    );
  }
}
```

### 4. 禁用事件监听

```dart
class NoEventController extends BaseController {
  /// 禁用事件监听
  @override
  bool get enableEventListeners => false;

  @override
  Future<void> initData() async {
    // 这个控制器不会监听任何事件
  }
}
```

### 5. 网络状态敏感控制器

```dart
class NetworkController extends BaseController {
  final RxBool isOnline = true.obs;

  /// 监听网络状态变化
  @override
  List<String> get eventTypesToListen => [
    AppEvents.networkChanged,
  ];

  @override
  void onNetworkChanged(NetworkEvent event) {
    super.onNetworkChanged(event);
    
    isOnline.value = event.isConnected;
    
    if (event.isConnected) {
      Get.snackbar('网络已恢复', '正在重新加载数据...');
    } else {
      Get.snackbar('网络已断开', '当前使用离线模式');
    }
  }
}
```

## 🔧 可重写的方法

### 事件处理方法

```dart
/// 用户登录事件处理
void onUserLogin(UserEvent event) {
  // 默认行为：调用 refreshData()
  super.onUserLogin(event);
  // 自定义逻辑...
}

/// 用户登出事件处理
void onUserLogout(UserEvent event) {
  // 默认行为：调用 refreshData()
  super.onUserLogout(event);
  // 自定义逻辑...
}

/// 数据刷新事件处理
void onDataRefresh(SimpleEvent event) {
  // 默认行为：调用 refreshData()
  super.onDataRefresh(event);
  // 自定义逻辑...
}

/// 网络状态变化事件处理
void onNetworkChanged(NetworkEvent event) {
  // 默认行为：网络恢复时调用 refreshData()
  super.onNetworkChanged(event);
  // 自定义逻辑...
}

/// 主题变化事件处理
void onThemeChanged(ThemeEvent event) {
  // 默认行为：调用 update() 更新UI
  super.onThemeChanged(event);
  // 自定义逻辑...
}
```

### 配置方法

```dart
/// 是否启用事件监听（默认：true）
bool get enableEventListeners => true;

/// 需要监听的事件类型列表（默认：登录、登出、数据刷新）
List<String> get eventTypesToListen => [
  AppEvents.userLogin,
  AppEvents.userLogout,
  AppEvents.dataRefresh,
];
```

## 📝 使用场景

### 场景1：用户登录后刷新所有相关页面

```dart
// 在登录成功后发布事件
EventBusExample.emitUserLogin('user123', '张三');

// 所有继承 BaseController 的控制器都会自动刷新数据
```

### 场景2：网络恢复后重新加载数据

```dart
// 监听网络状态变化
class DataController extends BaseController {
  @override
  List<String> get eventTypesToListen => [
    AppEvents.networkChanged,
  ];

  @override
  void onNetworkChanged(NetworkEvent event) {
    if (event.isConnected) {
      // 网络恢复，重新加载数据
      refreshData();
    }
  }
}
```

### 场景3：主题切换后更新UI

```dart
class ThemeController extends BaseController {
  @override
  List<String> get eventTypesToListen => [
    AppEvents.themeChanged,
  ];

  @override
  void onThemeChanged(ThemeEvent event) {
    // 更新主题相关配置
    updateThemeConfig(event.themeMode);
    // 刷新UI
    update();
  }
}
```

## ⚠️ 注意事项

1. **内存管理** - 控制器会自动管理事件订阅，无需手动清理
2. **生命周期检查** - 事件处理前会自动检查控制器是否仍然有效
3. **防抖机制** - 继承自 `BaseController` 的防抖机制仍然有效
4. **错误处理** - 事件处理中的错误会被捕获并显示
5. **性能考虑** - 避免在事件处理中执行耗时操作

## 🔍 调试

### 查看事件监听状态

```dart
// 在控制器中查看当前活跃的订阅数量
print('当前活跃订阅数: ${EventBusUtil().activeSubscriptionCount}');
```

### 手动发布事件进行测试

```dart
// 测试登录事件
EventBusExample.emitUserLogin('test_user', '测试用户');

// 测试数据刷新事件
EventBusExample.emitCustomEvent(AppEvents.dataRefresh);

// 测试网络状态变化
EventBusExample.emitNetworkChanged(true, connectionType: 'wifi');
```

## 📚 完整示例

参考 `example_controller.dart` 文件中的完整示例：

- `ExampleController` - 基本使用示例
- `NetworkSensitiveController` - 网络状态敏感控制器
- `ThemeSensitiveController` - 主题敏感控制器
- `NoEventController` - 禁用事件监听的控制器 