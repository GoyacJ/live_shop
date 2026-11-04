import 'dart:ui';

import 'package:get/get.dart';
import 'package:live_app/base/controller/base_controller.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:webview_flutter/webview_flutter.dart';

class MyWebViewLogic extends BaseController {
  late final WebViewController webViewController;
  double lineProgress = 0.0;
  String title = '';
  String urlStr = '';
  bool isTitleShow = true;

  @override
  initData() async{
    // TODO: implement initData
    title = Get.arguments['title'];
    urlStr = Get.arguments['urlStr'];
    isTitleShow = Get.arguments['isTitleShow'];
    initWebView();
  }

  initWebView() {
    webViewController = WebViewController()
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setBackgroundColor(const Color(0x00000000))
      ..setNavigationDelegate(
        NavigationDelegate(
          onNavigationRequest: (NavigationRequest request) async {
            if (request.url.startsWith('tel:')) {
              final Uri url = Uri.parse(request.url);
              if (await canLaunchUrl(url)) {
                await launchUrl(url);
              } else {
                print('Could not launch ${request.url}');
              }
              return NavigationDecision.prevent;
            }
            return NavigationDecision.navigate;
          },
          onProgress: (int progress) {
            // Update loading bar.
            lineProgress = progress / 100;
            update();
          },

          ///开始加载新页面时被调用
          onPageStarted: (String url) {},

          ///加载完成页面后被调用
          onPageFinished: (String url) {},

          ///加载资源时发生错误时被调用
          onWebResourceError: (WebResourceError error) {},
        ),
      )
      ..loadRequest(Uri.parse(urlStr));
  }
}
