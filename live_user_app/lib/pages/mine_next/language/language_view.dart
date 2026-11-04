import 'package:flutter/material.dart';
import 'package:live_app/base/widget/base_stateful_widget.dart';
import 'package:get/get.dart';
import 'package:live_app/widgets/jh_text.dart';

import '../../../utils/color_util.dart';
import 'language_logic.dart';

class LanguagePage extends BaseStatefulWidget<LanguageLogic> {
  const LanguagePage({super.key});

  @override
  String? title() => 'language_title'.tr;

  @override
  Widget buildContent(BuildContext context) {
    return ListView.builder(
      itemCount: controller.supportedLocales.length,
      itemBuilder: (context, index) {
        final locale = controller.supportedLocales[index];
        final isSelected = controller.currentLocale.value == locale;

        return ListTile(
          leading: JhText.cText(
            controller.getLanguageIcon(locale),
            ColorUtil.black, 24.0,
          ),
          title: JhText.cText(controller.getLanguageName(locale).tr, ColorUtil.black, 16.0),
          trailing: isSelected
              ? const Icon(
            Icons.check_circle,
            color: ColorUtil.blue,
          )
              : null,
          selected: isSelected,
          onTap: () => controller.changeLanguage(locale),
        );
      },
    );
  }
}
