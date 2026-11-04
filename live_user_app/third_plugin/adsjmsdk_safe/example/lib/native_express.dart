import 'package:flutter/material.dart';
import 'package:adsjmsdk/native_express_ad_view.dart';
import 'package:flutter/foundation.dart';

class NativeExpressPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    var adId = 'sjmad_test005';
    if (defaultTargetPlatform == TargetPlatform.iOS) {
      adId = "SJM_F005";
    }
    return Scaffold(
      appBar: AppBar(),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            ElevatedButton(
                onPressed: () {
                  NativeExpressAdView.enableDebug(true);
                  //showDialog 覆盖广告的情况
                  NativeExpressAdView.updateCoverBounds(true, 0, 0, 350, 300);
                  showDialog(
                      context: context,
                      builder: (BuildContext context) {
                        return GestureDetector(
                          onTap: () {
                            Navigator.pop(context);
                            //设置了isCovered=true，在Dialog消失的时候必需设置成false
                            NativeExpressAdView.updateCoverBounds(
                                false, 0, 0, 0, 0);
                            },
                          child: Container(
                            color: Color.fromRGBO(255, 255, 0, 0.4),
                          ),
                        );
                      });
                },
                child: Text("Dialog")),
            NativeExpressAdView(
              adId: adId,
              width: 350,
              height: 300,
              onSjmAdLoaded: (String id) {
                print("native_express onSjmAdLoad");
              },
              onSjmAdShow: (String id) {
                print("native_express onSjmAdShow");
              },
              onSjmAdClicked: (String id) {
                print("native_express onSjmAdClick");
              },
              onSjmAdClosed: (String id) {
                print("native_express onSjmAdClose");
              },
              onSjmAdError: (String id, int code, String message) {
                print("native_express onError");
              },
            ),
          ],
        ),
      ),
    );
  }
}
