//
//  SJMSplashAdView.h
//  adsjmsdk
//
//  Created by Allen on 2023/4/24.
//

#import "SJMBaseAdFlutter.h"

NS_ASSUME_NONNULL_BEGIN

@interface SJMSplashAdView : SJMBaseAdFlutter<FlutterPlatformView>

@end

@interface SJMSplashAdFactory : NSObject<FlutterPlatformViewFactory>
- (instancetype)initWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar;

@end

NS_ASSUME_NONNULL_END
