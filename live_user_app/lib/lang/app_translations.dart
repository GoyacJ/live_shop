import 'package:get/get.dart';
import '../lang/translations/en_US.dart';
import '../lang/translations/zh_CN.dart';

class AppTranslations extends Translations {
  @override
  Map<String, Map<String, String>> get keys => {
    'en_US': enUS,
    'zh_CN': zhCN,
  };
}
