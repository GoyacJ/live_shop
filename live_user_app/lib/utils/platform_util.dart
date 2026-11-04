// 条件导入，根据平台自动选择对应的实现
export 'platform_util_io.dart'
  if (dart.library.html) 'platform_util_web.dart';


/// 是否是生产环境
const bool inProduction = bool.fromEnvironment("dart.vm.product");