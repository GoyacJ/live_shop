# Live App 项目编码规范

## UI 开发规范

### 颜色规范
- 所有颜色必须使用 `ColorUtil` 类中定义的颜色常量
- 禁止在代码中硬编码颜色值，如 `Color(0xFF123456)` 或 `Colors.red`
- 如需新增颜色，应添加到 `ColorUtil` 类中并提供有意义的命名

```dart
// 错误示例
Container(color: Colors.red)
Text("内容", style: TextStyle(color: Color(0xFF333333)))

// 正确示例
Container(color: ColorUtil.primaryColor)
Text("内容", style: TextStyle(color: ColorUtil.textPrimary))
```

### 文本组件规范
- 所有文本组件使用 `JhText.cText()` 方法创建
- 标准参数顺序：文本内容、颜色、字体大小
- 可选参数：fontWeight、maxLines、overflow、height、textAlign

```dart
// 错误示例
Text("标题", style: TextStyle(fontSize: 16.sp, color: ColorUtil.black))

// 正确示例
JhText.cText("标题", ColorUtil.black, 16)
JhText.cText("标题", ColorUtil.black, 16, fontWeight: FontWeight.bold)
```

### 尺寸适配规范
- 所有尺寸必须使用 ScreenUtil 进行适配
- 字体大小：使用 `.sp` 后缀，如 `16.sp`
- 宽度和高度：使用 `.w` 后缀，如 `10.w`、`8.w`
- 内边距和外边距：使用 `.w` 后缀，如 `EdgeInsets.all(16.w)`

```dart
// 错误示例
Container(
  width: 100,
  height: 50,
  margin: EdgeInsets.all(10),
)

// 正确示例
Container(
  width: 100.w,
  height: 50.w,
  margin: EdgeInsets.all(10.w),
)
```

### 布局规范
- 使用语义化命名的方法拆分复杂 UI
- 每个页面应拆分为独立的小部件方法，如 `_buildHeader()`、`_buildList()`
- 避免嵌套过深的 Widget 树，尽量拆分为独立方法

```dart
// 推荐的页面结构
Widget _buildView() {
  return ListView(
    children: [
      _buildHeader(),
      _buildStats(),
      _buildList(),
    ],
  );
}

Widget _buildHeader() { /* ... */ }
Widget _buildStats() { /* ... */ }
Widget _buildList() { /* ... */ }
```

## 状态管理规范

### GetX 规范
- 使用 GetX 进行状态管理和依赖注入
- 控制器变量使用 `.obs` 后缀创建响应式变量
- 使用 `Obx()` 监听响应式变量变化
- 视图中通过 `controller` 访问控制器

```dart
// 控制器中
RxInt count = 0.obs;
RxList<Item> items = <Item>[].obs;

// 视图中
Obx(() => Text(controller.count.toString()))
```

### 网络请求规范
- 所有网络请求通过 `NetUtil` 类进行
- 请求参数使用 Map 类型，使用 `putIfAbsent` 添加参数
- 处理响应时应检查认证状态，使用 `ConfigUtil.isUnauthorized`

```dart
// 请求示例
Map<String, dynamic> params = {};
params.putIfAbsent('current', () => page);
params.putIfAbsent('size', () => 10);
final result = await NetUtil.getUserData(params);

if (ConfigUtil.isUnauthorized(result['code'], result['msg'])) {
  // 处理数据
}
```

## 组件开发示例

以下是推广页面的标准实现示例：

```dart
/// 推广页面开发规范：
/// 1. 所有颜色使用 ColorUtil 类中定义的颜色，禁止硬编码颜色值
/// 2. 所有文本组件使用 JhText.cText() 方法创建
/// 3. 布局尺寸使用 ScreenUtil 进行适配 (例如：16.sp, 10.w, 8.w)
/// 4. 列表数据来源使用 controller.list
/// 5. 选项卡索引使用 controller.tabIndex.value 控制
/// 6. 页面内主要分为：选项卡、统计卡片、推广明细标题和列表内容四部分

// 选项卡实现
Widget _buildTabs() {
  return Obx(() => Row(
    children: [
      Expanded(
        child: GestureDetector(
          onTap: () => controller.setTabIndex(0),
          child: Container(
            height: 60.w,
            decoration: BoxDecoration(
              color: controller.tabIndex.value == 0 ? ColorUtil.primaryColor : ColorUtil.white,
              borderRadius: BorderRadius.circular(4.r),
            ),
            alignment: Alignment.center,
            child: JhText.cText(
              "全部", 
              controller.tabIndex.value == 0 ? ColorUtil.white : ColorUtil.black, 
              16,
              fontWeight: FontWeight.bold,
            ),
          ),
        ),
      ),
      // 其他选项卡...
    ],
  ));
}
```

## 最佳实践

1. **代码组织**
   - 每个页面使用单独的目录，包含视图、逻辑和模型
   - 公共组件放在 widgets 目录下
   - 工具类放在 utils 目录下

2. **注释规范**
   - 为复杂方法添加注释说明功能
   - 每个页面顶部添加开发规范注释
   - 使用 `//` 添加单行注释，`///` 添加文档注释

3. **性能优化**
   - 避免不必要的重建，合理使用 Obx
   - 图片使用适当的缓存策略
   - 列表使用 ListView.builder 而非 ListView

4. **可访问性**
   - 为图片提供描述性 semanticLabel
   - 确保文本有足够的对比度
   - 控件尺寸应足够大以便于触摸操作

## Cursor IDE 使用规则

### 代码提示与自动完成
- 使用 Cursor 的代码提示功能加速开发流程
- 对于 Flutter 组件，使用自动完成后检查并按照项目规范调整参数
- 使用 `Alt+/` (macOS) 或 `Ctrl+/` (Windows/Linux) 触发内联代码建议

### AI 代码生成
- 生成的代码必须遵循项目中已有的代码风格和命名约定
- 使用 `/Generate` 命令时，提供明确的上下文和需求描述
- 生成后的代码必须手动检查并确保符合项目规范（特别是颜色、组件和尺寸适配规范）

### 代码格式化
- 在保存文件前使用 Cursor 的格式化功能（`Shift+Alt+F`）
- 保持一致的缩进风格（项目使用2个空格缩进）
- 遵循 Dart 标准格式化规则，不要自定义格式设置

### 代码导航
- 使用 `Cmd+P` (macOS) 或 `Ctrl+P` (Windows/Linux) 快速打开文件
- 使用 `Cmd+Shift+O` (macOS) 或 `Ctrl+Shift+O` (Windows/Linux) 导航到文件中的符号
- 为提高效率，学习并使用 Cursor 的键盘快捷键

### 扩展使用规范
- 团队成员应使用统一的 Cursor 扩展集
- Flutter 开发必备扩展：Flutter, Dart, Error Lens
- 代码质量扩展：Flutter Lints, Better Comments

### 协作规则
- 使用 Cursor 的协作功能时，明确标记代码更改的目的和原因
- 结对编程会话应记录关键决策和更改理由
- 在进行重大更改前使用 Cursor 的 AI 功能进行代码分析
