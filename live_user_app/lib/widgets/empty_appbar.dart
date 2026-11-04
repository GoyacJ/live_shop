import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

/// Created by Jcy
/// on 2025/5/14
/// 
class EmptyAppBar extends StatefulWidget implements PreferredSizeWidget{
  Color topBarColor;
  EmptyAppBar({super.key,required this.topBarColor});

  @override
  State<EmptyAppBar> createState() => _EmptyAppBarState();

  @override
  // TODO: implement preferredSize
  Size get preferredSize => const Size(0.0, 0.0);
}

class _EmptyAppBarState extends State<EmptyAppBar> {
  @override
  Widget build(BuildContext context) {
    return Container(
      color: widget.topBarColor,
    );
  }
}