import 'package:url_launcher/url_launcher.dart';

typedef PageVisibilityCallback = void Function();

Future<void> openPayUrl(String url) async {
  final uri = Uri.parse(url);
  if (!await launchUrl(uri, mode: LaunchMode.externalApplication)) {
    throw StateError('无法打开支付链接');
  }
}

Object? addPageVisibilityListener(PageVisibilityCallback callback) => null;

void removePageVisibilityListener(Object listener) {}

bool get pageIsVisible => true;
