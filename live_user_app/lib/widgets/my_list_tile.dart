import 'package:flutter/material.dart';

import '../utils/color_util.dart';
import 'jh_text.dart';

class MyListTile extends StatelessWidget {
  const MyListTile({
    super.key,
    this.leading,
    this.title = "",
    this.value = "",
    this.trailing,
    this.onClick,
  });

  final String title;
  final String value;
  final Widget? leading;
  final Widget? trailing;
  final Function()? onClick;

  @override
  Widget build(BuildContext context) {
    List<Widget> rowList = [
      JhText.cText(title, ColorUtil.black, 18.0),
      const SizedBox(width: 20.0),
      Expanded(child: JhText.cText(value, ColorUtil.black, 16.0, textAlign: TextAlign.right)),
    ];

    if (leading != null) {
      rowList.insert(0, Padding(
        padding: const EdgeInsets.only(right: 4.0),
        child: leading,
      ));
    }
    if (trailing != null) {
      rowList.add(const SizedBox(width: 8.0));
      rowList.add(trailing!);
    }

    return onClick==null ? Container(
      padding: const EdgeInsets.symmetric(vertical: 10.0),
      child: Row(
        crossAxisAlignment: trailing!=null ? CrossAxisAlignment.center : CrossAxisAlignment.start,
        children: rowList,
      ),
    ) : Material(
      child: Ink(
        color: ColorUtil.white,
        child: InkWell(
          onTap: onClick,
          splashColor: ColorUtil.grey[200],
          child: Container(
            padding: const EdgeInsets.symmetric(vertical: 12.0, horizontal: 12.0),
            child: Row(
              crossAxisAlignment: trailing!=null ? CrossAxisAlignment.center : CrossAxisAlignment.start,
              children: rowList,
            )
          )
        ),
      )
    );
  }
}
