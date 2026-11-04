import 'dart:html' as html;

typedef PageVisibilityCallback = void Function();

Future<void> openPayUrl(String url) async {
  html.window.open(url, '_blank');
}

Object addPageVisibilityListener(PageVisibilityCallback callback) {
  final html.EventListener listener = (html.Event event) => callback();
  html.document.addEventListener('visibilitychange', listener);
  return listener;
}

void removePageVisibilityListener(Object listener) {
  html.document.removeEventListener(
    'visibilitychange',
    listener as html.EventListener,
  );
}

bool get pageIsVisible => html.document.hidden == false;
