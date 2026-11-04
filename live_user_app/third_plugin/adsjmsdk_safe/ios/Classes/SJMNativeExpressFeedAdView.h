//
//  SJMNativeExpressFeedAdView.h
//  sjmadplugin
//


#import "SJMBaseAdFlutter.h"
#import <Flutter/Flutter.h>
NS_ASSUME_NONNULL_BEGIN

@interface SJMNativeExpressFeedAdView : SJMBaseAdFlutter<FlutterPlatformView>

@end

@interface SJMNativeExpressFeedAdViewFactory : NSObject<FlutterPlatformViewFactory>
- (instancetype)initWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar;

@end

NS_ASSUME_NONNULL_END
