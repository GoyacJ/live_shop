import 'package:live_app/base/controller/base_controller.dart';
import 'package:get/get.dart';

import '../../../config/language_config.dart';
import '../../../utils/config_util.dart';

class SettingLogic extends BaseController {
  // 当前选中的语言代码
  final RxString currentLocale = ConfigUtil.instance.locale.obs;

  final RxBool loading = true.obs;

  @override
  Future<void> initData() async {
    loading.value = true;
    // 模拟加载数据
    await Future.delayed(const Duration(seconds: 4), () {
      loading.value = false;
    });
  }

  // 支持的语言列表
  final supportedLocales = LanguageConfig.supportedLocales
      .map((locale) => LanguageConfig.getStringFromLocale(locale))
      .toList();

  // 获取语言显示名称
  String getLanguageName() {
    return LanguageConfig.getLanguageName(currentLocale.value);
  }

  // 获取语言图标
  String getLanguageIcon() {
    return LanguageConfig.getLanguageIcon(currentLocale.value);
  }
}
