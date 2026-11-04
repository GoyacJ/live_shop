import 'dart:io';
import 'package:cached_network_image/cached_network_image.dart';
import 'package:common_utils/common_utils.dart';
import 'package:flutter/cupertino.dart';
import '../utils/color_util.dart';

///Created by Jcy
///on 2024/12/5
class CacheImage extends StatefulWidget {
  String imageUrl;
  String? errorImage;
  double? width;
  double? height;
  double? radius;
  BoxFit? fit;
  bool isFull;

  CacheImage(
    this.imageUrl, {
    this.errorImage,
    this.width,
    this.height,
    this.radius,
    this.fit = BoxFit.cover,
    this.isFull = false,
    super.key,
  });

  @override
  State<CacheImage> createState() => _CacheImageState();
}

class _CacheImageState extends State<CacheImage> {
  double? width;
  double? height;
  String? errorImage;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    width = widget.width ?? MediaQuery.of(context).size.width;
    height = widget.height;
    errorImage = widget.errorImage ?? 'assets/images/ic_photo_err.png';

    return ClipRRect(
      borderRadius: BorderRadius.circular(widget.radius ?? 0),
      child: ObjectUtil.isNotEmpty(widget.imageUrl)
          ? widget.imageUrl.contains('http')
              ? CachedNetworkImage(
                  width: width,
                  height: height,
                  fit: widget.fit,
                  imageUrl: widget.imageUrl,
        useOldImageOnUrlChange:true,
                  fadeInDuration: const Duration(milliseconds: 300),
                  fadeOutDuration: const Duration(milliseconds: 300),
                  placeholder: (context, url) {
                    return Center(
                      child: Container(
                        height: height,
                        width: width,
                        color: ColorUtil.imageBgColor,
                        // CircularProgressIndicator() 圆环加载框
                        child: const CupertinoActivityIndicator(
                          radius: 10,
                        ),
                      ),
                    );
                  },
                  errorWidget: (context, url, error) {
                    return Container(
                      color: ColorUtil.imageBgColor,
                      width: width,
                      height: height,
                      alignment: Alignment.center,
                      child: Image.asset(
                        errorImage!,
                        width: getWidth(),
                        height: getHeight(),
                      ),
                    );
                  },
                )
              : widget.imageUrl.contains('assets')
                  ? Image.asset(
                      widget.imageUrl,
                      fit: BoxFit.cover,
                      width: width,
                      height: height,
                    )
                  : Image.file(
                      File(widget.imageUrl),
                      fit: BoxFit.cover,
                      width: width,
                      height: height,
                    )
          : Container(
              color: ColorUtil.imageBgColor,
              width: width,
              height: height,
              alignment: Alignment.center,
              child: Image.asset(
                errorImage!,
                width: getWidth(),
                height: getHeight(),
              ),
            ),
    );
  }

  getWidth() {
    if (widget.isFull) {
      return width;
    } else {
      return ((width ?? 460)) / 2;
    }
  }

  getHeight() {
    if (widget.isFull) {
      return height;
    } else {
      return ((height ?? 460)) / 2;
    }
  }
}
