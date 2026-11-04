import UIKit
import Flutter
import LUYSDK
import AppTrackingTransparency

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    override func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        LUYSDKConfig.registerAppId("sjm_ios123")
        GeneratedPluginRegistrant.register(with: self)
        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    }
    
//    //申请获取IDFA权限，根据媒体需要添加
//    override func applicationDidBecomeActive(_ application: UIApplication) {
//        self.requestIDFA()
//    }
//    
//    func requestIDFA() {
//        if #available(iOS 14, *) {
//            ATTrackingManager .requestTrackingAuthorization { ATTrackingManagerAuthorizationStatus in
//                
//            }
//        }
//    }
}
