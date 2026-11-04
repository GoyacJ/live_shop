// import 'dart:js' as js;
//
// class WeChatPay {
//   static void initSDK(String appId, String timestamp, String nonceStr, String signature) {
//     js.context.callMethod('wx.config', [
//       js.JsObject.jsify({
//         'debug': false, // 生产环境关闭调试
//         'appId': appId,
//         'timestamp': timestamp,
//         'nonceStr': nonceStr,
//         'signature': signature,
//         'jsApiList': ['chooseWXPay'] // 需要使用的JS接口
//       })
//     ]);
//   }
//
//   static Future<void> pay({
//     required String timestamp,
//     required String nonceStr,
//     required String prepayId,
//     required String signType,
//     required String paySign,
//     required Function onSuccess,
//     required Function(String) onFailure,
//   }) async {
//     js.context.callMethod('wx.ready', [
//       js.allowInterop(() {
//         js.context.callMethod('wx.chooseWXPay', [
//           js.JsObject.jsify({
//             'timestamp': timestamp,
//             'nonceStr': nonceStr,
//             'package': 'prepay_id=$prepayId',
//             'signType': signType,
//             'paySign': paySign,
//             'success': js.allowInterop((res) => onSuccess()),
//             'fail': js.allowInterop((res) => onFailure(res.toString())),
//           })
//         ]);
//       })
//     ]);
//   }
// }