import 'package:live_app/base/controller/base_controller.dart';
import 'package:get/get.dart';

import '../../../config/language_config.dart';
import '../../../utils/config_util.dart';

class LanguageLogic extends BaseController {
  // 当前选中的语言代码
  final RxString currentLocale = ConfigUtil.instance.locale.obs;

  // 支持的语言列表
  final supportedLocales = LanguageConfig.supportedLocales
      .map((locale) => LanguageConfig.getStringFromLocale(locale))
      .toList();

  // 切换语言
  Future<void> changeLanguage(String locale) async {
    if (currentLocale.value == locale) return;

    try {
      // 更新配置
      await ConfigUtil.instance.setLocale(locale);
      currentLocale.value = locale;

      // 更新应用语言
      final newLocale = LanguageConfig.getLocaleFromString(locale);
      await Get.updateLocale(newLocale);

      Get.snackbar(
        'success'.tr,
        'language_change_success'.tr,
        snackPosition: SnackPosition.TOP,
      );
    } catch (e) {
      Get.snackbar(
        'error'.tr,
        'language_change_failed'.tr,
        snackPosition: SnackPosition.TOP,
      );
    }
  }

  // 获取语言显示名称
  String getLanguageName(String locale) {
    return LanguageConfig.getLanguageName(locale);
  }

  // 获取语言图标
  String getLanguageIcon(String locale) {
    return LanguageConfig.getLanguageIcon(locale);
  }
}
